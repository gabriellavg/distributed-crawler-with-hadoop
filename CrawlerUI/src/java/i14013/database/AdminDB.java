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
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author Gabriella
 */
public class AdminDB {

    private static final TableName TABLENAME = TableName.valueOf("crawlerAdmin");
    private static final String FAMILYNAME = "admin";

    private static Connection connection;

    public static void initAdminDB() throws IOException {
        Configuration con = HBaseConfiguration.create();
        connection = ConnectionFactory.createConnection(con);
        Admin admin = connection.getAdmin();
        if (!admin.tableExists(TABLENAME)) {
            HTableDescriptor tableDescriptor = new HTableDescriptor(TABLENAME);
            tableDescriptor.addFamily(new HColumnDescriptor(FAMILYNAME));
            admin.createTable(tableDescriptor);
        }
        admin.close();
    }

    public static void addAdmin(String username, String password) throws IOException {
        Table table = connection.getTable(TABLENAME);
        Put newAdmin = new Put(Bytes.toBytes(username));
        newAdmin.addColumn(Bytes.toBytes(FAMILYNAME), Bytes.toBytes("pwd"),
                Bytes.toBytes(password));
        table.put(newAdmin);
        table.close();
    }

    public static boolean isUsernameExist(String username) throws IOException {
        boolean result = false;

        Table table = connection.getTable(TABLENAME);

        Scan scan = new Scan();
        FilterList filterList = new FilterList();
        filterList.addFilter(new RowFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(username))));
        scan.setFilter(filterList);

        ResultScanner resultScanner = table.getScanner(scan);

        if (resultScanner.iterator().next() != null) {
            result = true;
        }
        resultScanner.close();
        table.close();

        return result;
    }

    public static boolean isCorrectPassword(String username, String password) throws IOException {
        boolean result = false;

        Table table = connection.getTable(TABLENAME);

        Scan scan = new Scan();
        FilterList filterList = new FilterList();
        filterList.addFilter(new RowFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(username))));
        scan.setFilter(filterList);
        ResultScanner resultScanner = table.getScanner(scan);

        Result scannerResult = resultScanner.next();
        byte[] valuePassword = scannerResult.getValue(Bytes.toBytes(FAMILYNAME), Bytes.toBytes("pwd"));

        String dbPassword = Bytes.toString(valuePassword);
        if (password.equals(dbPassword)) {
            result = true;
        }

        resultScanner.close();
        table.close();

        return result;
    }

}
