package i14013.engine;

import i14013.database.Frontier;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author Gabriella
 */
public class AgentHandler extends Thread {

    private final int assignNumber;
    private final Socket agentSocket;
    private PrintStream printer;
    private BufferedReader reader;

    public AgentHandler(Socket agentSocket, int assignNumber) {
        this.agentSocket = agentSocket;
        this.assignNumber = assignNumber;
    }

    @Override
    public void run() {

        LogWriter log = new LogWriter("AgentHandler #" + assignNumber + " Log");

        try {

            reader = new BufferedReader(new InputStreamReader(agentSocket.getInputStream()));
            printer = new PrintStream(agentSocket.getOutputStream());

            log.write("Connected to agent #" + assignNumber + ". Waiting for Agent message.");

            String fromAgent, task;
            while (true) {

                //terima pesan dari Agent
                fromAgent = reader.readLine();
                log.write("Receiving message from agent: [" + fromAgent + "].");
                if (fromAgent.equals("/READY") || fromAgent.startsWith("/FINISH")) {

                    boolean allProcessed = Frontier.allProcessed();
                    if (!allProcessed) {

                        log.write("Try to get task from frontier.");
                        task = Frontier.getUrl(assignNumber); // isinya <url> <mode> <startdepth> <maxdepth> <root>

                        log.write("Get task: " + task);

                        if (!fromAgent.equals("/READY")) {
                            // /FINISH <url>
                            Frontier.markProcessed(fromAgent.split(" ")[1]); // tandai url sudah diproses

                            printer.println("/TASK " + task);
                            log.write("/TASK " + task);
                        } else {
                            printer.println("/TASK " + task + " " + assignNumber);
                            log.write("/TASK " + task + " " + assignNumber);
                        }

                    } else {
                        if (fromAgent.startsWith("/FINISH")) {
                            Frontier.markProcessed(fromAgent.split(" ")[1]);
                        }
                        log.write("All URL has been processed. Tell agent to disconnect from server");
                        printer.println("/DISCONNECT");
                    }

                } else if (fromAgent.equals("/DISCONNECT")) {
                    log.write("Agent has disconnected from server.");
                    break;
                } else {

                    String[] ntData = fromAgent.split(" "); // isinya /URL <url> <mode> <startdepth> <maxdepth> <root>
                    boolean urlExist = Frontier.urlExist(ntData[1]);
                    log.write("Checking frontier for URL: [" + fromAgent + "] exist? :[" + urlExist + "].");
                    String mode = ntData[2];
                    log.write("Checking mode: " + mode);

                    if (!urlExist) {
                        log.write("Try add URL to frontier.");
                        Frontier.addUrl(ntData[1], Integer.parseInt(ntData[3]), Integer.parseInt(ntData[4]), ntData[5]);
                        log.write("URL: [" + fromAgent + "] added to frontier.");

                    } else {
                        if (mode.equals("recrawl")) {
                            Frontier.markForRecrawl(ntData[1]);
                            log.write("URL marked for recrawl.");
                        } else {
                            log.write("URL: [" + fromAgent + "] not added to frontier.");
                        }
                    }
                }

            }

            reader.close();
            printer.close();
            agentSocket.close();
            log.write("Connection to agent #" + assignNumber + " closed.");

        } catch (IOException ex) {
            System.out.println(ex);
            log.write("Catch exception: [" + ex + "].");
        }

        log.write("--- Finished ---");
        log.close();

    }

}
