package i14013.engine;

import i14013.database.WebRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Gabriella
 */
public class Agent {

    private static Socket socket;

    private static BufferedReader reader;
    private static PrintStream printer;
    private static LogWriter log;

    private static int id;

    public static void main(String[] args) {

        try {

            int port = Integer.parseInt(args[0]);
            String host = args[1];

            socket = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printer = new PrintStream(socket.getOutputStream());

            System.out.println("--- Starting agent [host: " + host + "] [port: "
                    + port + "] ---\nTelling server this agent is ready...");

            printer.println("/READY");
            String fromServer;

            boolean hasAssigned = false;
            while (true) {
                fromServer = reader.readLine();
                System.out.println("\nMESSAGE FROM SERVER: " + fromServer + "\n");
                if (!fromServer.equals("/DISCONNECT")) {

                    String taskInfo[] = fromServer.split(" ");
                    //hasil split: [0]/TASK [1]url [2]mode [3]startdepth [4]maxdepth [5]root
                    CrawlingTask task = new CrawlingTask(taskInfo[1], taskInfo[2], Integer.parseInt(taskInfo[3]),
                            Integer.parseInt(taskInfo[4]), taskInfo[5]);

                    if (!hasAssigned) {
                        id = Integer.parseInt(taskInfo[6]);
                        System.out.println("--- CONNECTED AS AGENT #" + id + " ---");
                        log = new LogWriter("Agent #" + id + " Log");
                        hasAssigned = true;
                        WebRepository.initRepository();
                        System.out.println("Connected to Repository.");
                        log.write("Connected to repository.");
                    }

                    System.out.println("Receive URL from server: [" + task.getUrl() + "]");
                    log.write("Receive URL from server: [" + task.getUrl() + "]");

                    System.out.println("Begin crawling...");
                    log.write("Begin crawling.");

                    crawl(task);

                    System.out.println("--- Finish crawling URL: [" + task.getUrl() + "] ---");
                    log.write("--- Finish crawling URL: [" + task.getUrl() + "] ---");
                    printer.println("/FINISH " + task.getUrl());
                    log.write("/FINISH");

                } else {
                    break;
                }

            }

            System.out.println("All URL has been processed. Disconnect from server.");
            printer.println("/DISCONNECT");

            reader.close();
            printer.close();
            socket.close();

            if (hasAssigned) {
                log.write("All URL has been processed. Disconnect from server.");
                log.write("Connection closed.");
                log.close();
            }

        } catch (IOException ex) {
            System.out.println(ex);
        }

    }

    private static void crawl(CrawlingTask task) {

        String url = task.getUrl();
        int depth = task.getStartDepth();
        int max = task.getMaxDepth();

        System.out.println("--------------------------------------\nDepth "
                + depth + "/" + max + " | " + url
                + "\n--------------------------------------\n");

        try {

            if (!WebRepository.urlExist(url) || (WebRepository.urlExist(url) && task.getMode().equals("recrawl"))) {

                System.out.println("Processing URL...");

                Document doc = Jsoup.connect(url).validateTLSCertificates(false).get();

                String title = doc.title();
                String content = Jsoup.parse(doc.body().text()).text();

                System.out.println("Saving title, content, url, and agent ID to Web Repository...");

                if (!WebRepository.urlExist(url)) {

                    // simpan ke web repository dengan format
                    // <url> <title> <content> <agentid> <mode> <startdepth> <maxdepth> <root>
                    WebRepository.addToRepository(url, title, content, id, depth, max, task.getRoot());

                } else {
                    // task.getMode().equals("recrawl") && WebRepository.urlExist(url)
                    WebRepository.updateRecrawl(url, title, content, id);
                }

                System.out.println("Saved to Repository");

                System.out.println("Extracting webpage links...");
                Elements docURLs = doc.select("a[href]");
                int next = depth + 1;

                if (next < max) {

                    for (Element docURL : docURLs) {

                        String link = docURL.attr("abs:href");
                        if (link.contains(" ")) {
                            link = link.replace(" ", "%20");
                        }
                        System.out.println("EXTRACTED " + link);
                        log.write("Extracted: " + link);
                        String linkInfo = UrlChecker.getUrlInfo(link);
                        System.out.println(linkInfo);
                        if (linkInfo.equals("Valid URL")) {
                            log.write("Valid: " + link);
                            // kirim ke server untuk dimasukkan ke frontier
                            // /URL <url> <mode> <startdepth> <maxdepth> <root>
                            String toServer = "/URL " + link + " " + task.getMode() + " "
                                    + next + " " + max + " " + task.getRoot();
                            printer.println(toServer);
                            System.out.println("Sending url to server");
                            log.write(toServer);
                        } else if (linkInfo.equals("URL is a reference")) {
                            System.out.println("Removing the reference...");
                            String notRef = link.split("#")[0];
                            log.write("Reference removed.");
                            String toServer = "/URL " + notRef + " " + task.getMode() + " "
                                    + next + " " + max + " " + task.getRoot();
                            printer.println(toServer);
                            System.out.println("Sending url to server");
                            log.write(toServer);
                        } else {
                            System.out.println(link + " " + linkInfo);
                            log.write(link + " " + linkInfo);
                        }
                    }

                } else {
                    System.out.println("Increment depth = " + next + ", maximum = " + max + ". No need to extract URL.");
                }

            }

        } catch (Exception | Error e) {
            System.out.println(e.getMessage());
            log.write(e.getMessage());
        }

    }

}
