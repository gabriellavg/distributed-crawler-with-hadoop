package i14013.engine;

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
public class SearchingProcessor {

    public static ArrayList<String> getUrls(String term, int page) throws IOException {

        String[] str = term.toLowerCase().split("\\s+");

        ArrayList<String> res = new ArrayList();

        if (!WebRepository.isRepoEmpty()) {

            Scan scan = new Scan();
            Table t = WebRepository.getTable();
            ResultScanner rs = t.getScanner(scan);

            int count = 0;

            int startNum = ((page - 1) * 10) + 1;
            int endNum = page * 10;

            res.add(page + " " + count);

            for (Result r : rs) {
                String content = Bytes.toString(r.getValue(Bytes.toBytes(WebRepository.getFAMILY1NAME()), Bytes.toBytes("content"))).toLowerCase();
                for (String s : str) {
                    if (content.contains(s)) {
                        count++;
                        if (count >= startNum && count <= endNum) {
                            String url = Bytes.toString(r.getRow());
                            String title = Bytes.toString(r.getValue(Bytes.toBytes(WebRepository.getFAMILY1NAME()), Bytes.toBytes("title")));   
                            res.add(url + "^" + title);
                            break;
                        }
                    }
                }

                rs.close();
                t.close();

            }

            res.set(0, page + "^" + count);

        }
        return res;

    }

    public static String[] getPage(String url) throws IOException {

        String[] res = {"", ""};

        Table table = WebRepository.getTable();

        Scan scan = new Scan();
        scan.setFilter(new RowFilter(CompareFilter.CompareOp.EQUAL,
                new BinaryComparator(Bytes.toBytes(url))));
        ResultScanner resultScanner = table.getScanner(scan);
        Result scannerResult = resultScanner.iterator().next();
        if (scannerResult != null) {
            String title = Bytes.toString(scannerResult.getValue(Bytes.toBytes(WebRepository.getFAMILY1NAME()), Bytes.toBytes("title")));
            String content = Bytes.toString(scannerResult.getValue(Bytes.toBytes(WebRepository.getFAMILY1NAME()), Bytes.toBytes("content")));
            res[0] = title;
            res[1] = content;
        }
        resultScanner.close();
        table.close();

        return res;

    }

}
