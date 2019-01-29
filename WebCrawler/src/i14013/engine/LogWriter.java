package i14013.engine;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

/**
 *
 * @author Gabriella
 */
public class LogWriter {

    private PrintWriter writer;
    private long startTime;

    public LogWriter(String name) {
        this.startTime = System.currentTimeMillis();
        String tStamp = (new Timestamp(startTime)).toString().replace(":", "");
        String logName = name + "_" + tStamp + ".txt";
        try {
            this.writer = new PrintWriter(new FileWriter(logName));
        } catch (IOException ex) {

        }
    }

    public void write(String str) {
        writer.write(str);
        writer.write(System.getProperty("line.separator"));
        writer.flush();
    }

    public void close() {
        long currentTime = System.currentTimeMillis();
        writer.write("Finished on " + new Timestamp(currentTime));
        writer.write(System.getProperty("line.separator"));
        long deltaTime = currentTime - startTime;
        writer.write("Estimated running time: " + (deltaTime)
                + " ms (approx. " + (deltaTime / 1000) + ") seconds");
        writer.close();
    }

}
