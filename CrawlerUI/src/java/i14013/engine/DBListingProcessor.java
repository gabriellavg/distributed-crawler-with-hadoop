package i14013.engine;

import i14013.database.Frontier;
import i14013.database.WebRepository;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author Gabriella
 */
public class DBListingProcessor {

    public static ArrayList<String> getFrontierData(int page) throws IOException {

        ArrayList<String> res = new ArrayList();

        if (!Frontier.isEmpty()) {

            Table table = Frontier.getTable();

            Scan scan = new Scan();
            scan.setReversed(true);
            ResultScanner scanner = table.getScanner(scan);

            int count = 0;

            int startNum = ((page - 1) * 10) + 1;
            int endNum = page * 10;
            res.add(page + " " + count);

            for (Result scannerResult : scanner) {
                count++;
                if (count >= startNum && count <= endNum) {
                    String url = Bytes.toString(scannerResult.getValue(Bytes.toBytes(Frontier.getFAMILY1NAME()), Bytes.toBytes("url")));
                    String mode = Bytes.toString(scannerResult.getValue(Bytes.toBytes(Frontier.getFAMILY1NAME()), Bytes.toBytes("mode")));
                    int status = Bytes.toInt(scannerResult.getValue(Bytes.toBytes(Frontier.getFAMILY1NAME()), Bytes.toBytes("status")));
                    int procby = Bytes.toInt(scannerResult.getValue(Bytes.toBytes(Frontier.getFAMILY1NAME()), Bytes.toBytes("procby")));
                    int start = Bytes.toInt(scannerResult.getValue(Bytes.toBytes(Frontier.getFAMILY2NAME()), Bytes.toBytes("start")));
                    int max = Bytes.toInt(scannerResult.getValue(Bytes.toBytes(Frontier.getFAMILY2NAME()), Bytes.toBytes("max")));
                    String root = Bytes.toString(scannerResult.getValue(Bytes.toBytes(Frontier.getFAMILY2NAME()), Bytes.toBytes("root")));
                    Long timestamp = scannerResult.rawCells()[0].getTimestamp();

                    String tr = url + " " + mode + " " + status + " " + procby + " " + start + " " + max + " " + root + " " + timestamp;
                    res.add(tr);
                }
            }

            res.set(0, page + " " + count);

            scanner.close();
            table.close();

        }

        return res;
    }

    public static ArrayList<String> getRepositoryData(int page) throws IOException {

        ArrayList<String> res = new ArrayList();

        if (!WebRepository.isRepoEmpty()) {

            Table table = WebRepository.getRepoIdx();

            Scan scan = new Scan();
            ResultScanner scanner = table.getScanner(scan);

            int count = 0;

            int startNum = ((page - 1) * 10) + 1;
            int endNum = page * 10;

            res.add(page + " " + count);

            for (Result result : scanner) {

                count++;
                if (count >= startNum && count <= endNum) {
                    byte[] b = result.getValue(Bytes.toBytes(WebRepository.getIDXFAMNAME()), Bytes.toBytes("url"));
                    Table t = WebRepository.getTable();

                    Scan scanR = new Scan();
                    scanR.setFilter(new RowFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(b)));
                    ResultScanner resultScanner = t.getScanner(scanR);

                    Result scannerResult = resultScanner.iterator().next();
                    if (scannerResult != null) {
                        String url = Bytes.toString(b);
                        int agentId = Bytes.toInt(scannerResult.getValue(Bytes.toBytes(WebRepository.getFAMILY2NAME()), Bytes.toBytes("id")));
                        String mode = Bytes.toString(scannerResult.getValue(Bytes.toBytes(WebRepository.getFAMILY2NAME()), Bytes.toBytes("mode")));
                        int start = Bytes.toInt(scannerResult.getValue(Bytes.toBytes(WebRepository.getFAMILY3NAME()), Bytes.toBytes("start")));
                        int max = Bytes.toInt(scannerResult.getValue(Bytes.toBytes(WebRepository.getFAMILY3NAME()), Bytes.toBytes("max")));
                        String root = Bytes.toString(scannerResult.getValue(Bytes.toBytes(WebRepository.getFAMILY3NAME()), Bytes.toBytes("root")));
                        Long timestamp = scannerResult.rawCells()[0].getTimestamp();

                        String tr = url + " " + agentId + " " + mode + " " + start + " " + max + " " + root + " " + timestamp;

                        res.add(tr);
                    }
                    resultScanner.close();
                }

            }

            res.set(0, page + " " + count);

            scanner.close();
            table.close();

        }

        return res;
    }

}
