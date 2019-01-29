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
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author Gabriella
 */
public class Frontier {

    private static final TableName TABLENAME = TableName.valueOf("frontier");
    private static final String FAMILY1NAME = "crawling", FAMILY2NAME = "depth";
    private static Connection connection;

    public static void addUrl(String url, int startDepth, int maxDepth, String root) throws IOException {

        Table table = connection.getTable(TABLENAME);

        Put row = new Put(Bytes.toBytes("id" + System.currentTimeMillis() + "_" + url));
        row.addColumn(Bytes.toBytes(FAMILY1NAME), Bytes.toBytes("url"), Bytes.toBytes(url));
        row.addColumn(Bytes.toBytes(FAMILY1NAME), Bytes.toBytes("mode"), Bytes.toBytes("crawl"));
        row.addColumn(Bytes.toBytes(FAMILY1NAME), Bytes.toBytes("status"), Bytes.toBytes(-1)); // belum diproses
        row.addColumn(Bytes.toBytes(FAMILY1NAME), Bytes.toBytes("procby"), Bytes.toBytes(0)); // belum ada yang memproses
        row.addColumn(Bytes.toBytes(FAMILY2NAME), Bytes.toBytes("start"), Bytes.toBytes(startDepth));
        row.addColumn(Bytes.toBytes(FAMILY2NAME), Bytes.toBytes("max"), Bytes.toBytes(maxDepth));
        row.addColumn(Bytes.toBytes(FAMILY2NAME), Bytes.toBytes("root"), Bytes.toBytes(root));
        table.put(row);

        table.close();
    }

    public static void markForRecrawl(String url) throws IOException {

        Table table = connection.getTable(TABLENAME);
        Scan scan = new Scan();
        FilterList filterList = new FilterList();
        filterList.addFilter(new SingleColumnValueFilter(Bytes.toBytes(FAMILY1NAME),
                Bytes.toBytes("url"), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(url)));

        scan.setFilter(filterList);
        ResultScanner resultScanner = table.getScanner(scan);
        Result res = resultScanner.iterator().next();

        if (res != null) {
            byte[] b = res.getRow();
            Put p = new Put(b);
            p.addColumn(Bytes.toBytes(FAMILY1NAME), Bytes.toBytes("mode"), Bytes.toBytes("recrawl"));
            p.addColumn(Bytes.toBytes(FAMILY1NAME), Bytes.toBytes("status"), Bytes.toBytes(-1)); // blm diproses
            p.addColumn(Bytes.toBytes(FAMILY1NAME), Bytes.toBytes("procby"), Bytes.toBytes(0)); // blm ada yg ngeproses
            table.put(p);
        }

        resultScanner.close();
        table.close();

    }

    public static boolean urlExist(String url) throws IOException {
        boolean result = true;

        Table table = connection.getTable(TABLENAME);
        Scan scan = new Scan();
        scan.setFilter(new SingleColumnValueFilter(Bytes.toBytes(FAMILY1NAME),
                Bytes.toBytes("url"), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(url)));
        ResultScanner resultScanner = table.getScanner(scan);

        if (resultScanner.iterator().next() == null) {
            result = false;
        }

        resultScanner.close();
        table.close();

        return result;
    }

    public static boolean allProcessed() throws IOException {
        boolean result = true;

        Table table = connection.getTable(TABLENAME);

        Scan scan = new Scan();
        FilterList filterList = new FilterList();
        scan.setFilter(new SingleColumnValueFilter(Bytes.toBytes(FAMILY1NAME),
                Bytes.toBytes("status"), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(-1)));
        ResultScanner resultScanner = table.getScanner(scan);

        if (resultScanner.iterator().next() != null) {
            result = false;
        }

        resultScanner.close();
        table.close();

        return result;
    }

    public static String getUrl(int agentId) throws IOException {
        String result = "";
        Table table = connection.getTable(TABLENAME);

        Scan scan = new Scan();
        scan.setFilter(new SingleColumnValueFilter(Bytes.toBytes(FAMILY1NAME),
                Bytes.toBytes("status"), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(-1)));
        ResultScanner resultScanner = table.getScanner(scan);

        Result scannerResult = resultScanner.iterator().next();
        if (scannerResult != null) {

            String url = Bytes.toString(scannerResult.getValue(Bytes.toBytes(FAMILY1NAME), Bytes.toBytes("url")));
            String mode = Bytes.toString(scannerResult.getValue(Bytes.toBytes(FAMILY1NAME), Bytes.toBytes("mode")));
            int start = Bytes.toInt(scannerResult.getValue(Bytes.toBytes(FAMILY2NAME), Bytes.toBytes("start")));
            int max = Bytes.toInt(scannerResult.getValue(Bytes.toBytes(FAMILY2NAME), Bytes.toBytes("max")));
            String root = Bytes.toString(scannerResult.getValue(Bytes.toBytes(FAMILY2NAME), Bytes.toBytes("root")));

            result = url + " " + mode + " " + start + " " + max + " " + root;

            Put row = new Put(scannerResult.getRow());
            row.addColumn(Bytes.toBytes(FAMILY1NAME), Bytes.toBytes("status"), Bytes.toBytes(0)); //0 = diproses
            row.addColumn(Bytes.toBytes(FAMILY1NAME), Bytes.toBytes("procby"), Bytes.toBytes(agentId)); // sesuai yang memproses
            table.put(row);

            resultScanner.close();
        }
        table.close();
        return result;
    }

    public static void markProcessed(String url) throws IOException {

        Table table = connection.getTable(TABLENAME);

        Scan scan = new Scan();
        FilterList filterList = new FilterList();
        scan.setFilter(new SingleColumnValueFilter(Bytes.toBytes(FAMILY1NAME),
                Bytes.toBytes("status"), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(0)));
        scan.setFilter(new SingleColumnValueFilter(Bytes.toBytes(FAMILY1NAME),
                Bytes.toBytes("url"), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(url)));

        ResultScanner resultScanner = table.getScanner(scan);

        Result scannerResult = resultScanner.iterator().next();

        if (scannerResult != null) {

            Put row = new Put(Bytes.toBytes(Bytes.toString(scannerResult.getRow())));
            row.addColumn(Bytes.toBytes(FAMILY1NAME), Bytes.toBytes("status"), Bytes.toBytes(1)); //1 sudah di proses
            table.put(row);

        }

        resultScanner.close();
        table.close();

    }

    public static void initFrontier() throws IOException {
        Configuration con = HBaseConfiguration.create();
        connection = ConnectionFactory.createConnection(con);

        Admin admin = connection.getAdmin();
        if (!admin.tableExists(TABLENAME)) {
            HTableDescriptor tableDescriptor = new HTableDescriptor(TABLENAME);
            tableDescriptor.addFamily(new HColumnDescriptor(FAMILY1NAME));
            tableDescriptor.addFamily(new HColumnDescriptor(FAMILY2NAME));
            admin.createTable(tableDescriptor);
        }
        admin.close();
    }

}
