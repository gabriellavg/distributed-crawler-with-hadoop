package i14013.database;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author Gabriella
 */
public class WebRepository {

    private final static TableName TABLENAME = TableName.valueOf("webRepo");
    private final static String FAMILY1NAME = "page", FAMILY2NAME = "agent", FAMILY3NAME = "depth";

    private final static TableName IDXNAME = TableName.valueOf("repoIdx");
    private final static String IDXFAMNAME = "idxvalue";

    private static Connection connection;

    public static void addToRepository(String url, String title, String content,
            int agentId, int startDepth, int maxDepth, String root) throws IOException {

        //masukkan url ke table webRepo
        Table tableRepo = connection.getTable(TABLENAME);
        Put pRepo = new Put(Bytes.toBytes(url));
        pRepo.addColumn(Bytes.toBytes(FAMILY1NAME), Bytes.toBytes("title"), Bytes.toBytes(title));
        pRepo.addColumn(Bytes.toBytes(FAMILY1NAME), Bytes.toBytes("content"), Bytes.toBytes(content));
        pRepo.addColumn(Bytes.toBytes(FAMILY2NAME), Bytes.toBytes("id"), Bytes.toBytes(agentId));
        pRepo.addColumn(Bytes.toBytes(FAMILY2NAME), Bytes.toBytes("mode"), Bytes.toBytes("crawl"));
        pRepo.addColumn(Bytes.toBytes(FAMILY3NAME), Bytes.toBytes("start"), Bytes.toBytes(startDepth));
        pRepo.addColumn(Bytes.toBytes(FAMILY3NAME), Bytes.toBytes("max"), Bytes.toBytes(maxDepth));
        pRepo.addColumn(Bytes.toBytes(FAMILY3NAME), Bytes.toBytes("root"), Bytes.toBytes(root));
        tableRepo.put(pRepo);
        tableRepo.close();

        //masukkan ke repoIdx
        Table tableIdx = connection.getTable(IDXNAME);
        Put pIdx = new Put(Bytes.toBytes("id" + (Long.MAX_VALUE - System.currentTimeMillis()) + "_" + url));
        pIdx.addColumn(Bytes.toBytes(IDXFAMNAME), Bytes.toBytes("url"), Bytes.toBytes(url));
        tableIdx.put(pIdx);
        tableIdx.close();

    }

    public static void updateRecrawl(String url, String title, String content, int agentId) throws IOException {

        //masukkan url ke table webRepo
        Table tableRepo = connection.getTable(TABLENAME);
        Put pRepo = new Put(Bytes.toBytes(url));
        pRepo.addColumn(Bytes.toBytes(FAMILY1NAME), Bytes.toBytes("title"), Bytes.toBytes(title));
        pRepo.addColumn(Bytes.toBytes(FAMILY1NAME), Bytes.toBytes("content"), Bytes.toBytes(content));
        pRepo.addColumn(Bytes.toBytes(FAMILY2NAME), Bytes.toBytes("id"), Bytes.toBytes(agentId));
        pRepo.addColumn(Bytes.toBytes(FAMILY2NAME), Bytes.toBytes("mode"), Bytes.toBytes("recrawl"));
        tableRepo.put(pRepo);
        tableRepo.close();

        //update repoIdx
        Table tableIdx = connection.getTable(IDXNAME);
        Scan scan = new Scan();
        scan.setFilter(new SingleColumnValueFilter(Bytes.toBytes(IDXFAMNAME),
                Bytes.toBytes("url"), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(url)));
        ResultScanner resultScanner = tableIdx.getScanner(scan);
        Result res = resultScanner.iterator().next();
        if (res != null) {
            //delete yang lama
            byte[] b = res.getRow();
            Delete d = new Delete(b);
            tableIdx.delete(d);
            //insert yang baru
            Put pIdx = new Put(Bytes.toBytes("id" + (Long.MAX_VALUE - System.currentTimeMillis()) + "_" + url));
            pIdx.addColumn(Bytes.toBytes(IDXFAMNAME), Bytes.toBytes("url"), Bytes.toBytes(url));
            tableIdx.put(pIdx);
            tableIdx.close();
        }

    }

    public static boolean urlExist(String url) throws IOException {
        boolean result = true;
        Table table = connection.getTable(TABLENAME);

        Scan scan = new Scan();
        scan.setFilter(new RowFilter(CompareFilter.CompareOp.EQUAL,
                new BinaryComparator(Bytes.toBytes(url))));
        ResultScanner resultScanner = table.getScanner(scan);
        if (resultScanner.iterator().next() == null) {
            result = false;
        }

        resultScanner.close();

        table.close();
        return result;
    }

    public static void initRepository() throws IOException {

        Configuration con = HBaseConfiguration.create();
        connection = ConnectionFactory.createConnection(con);
        Admin admin = connection.getAdmin();
        if (!admin.tableExists(TABLENAME)) {
            HTableDescriptor tableDescriptor = new HTableDescriptor(TABLENAME);
            tableDescriptor.addFamily(new HColumnDescriptor(FAMILY1NAME));
            tableDescriptor.addFamily(new HColumnDescriptor(FAMILY2NAME));
            tableDescriptor.addFamily(new HColumnDescriptor(FAMILY3NAME));
            admin.createTable(tableDescriptor);
        }
        if (!admin.tableExists(IDXNAME)) {
            HTableDescriptor tableDescriptor = new HTableDescriptor(IDXNAME);
            tableDescriptor.addFamily(new HColumnDescriptor(IDXFAMNAME));
            admin.createTable(tableDescriptor);
        }
        admin.close();
    }

}
