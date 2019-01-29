package i14013.engine;

import i14013.database.Frontier;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Gabriella
 */
public class Server {

    private static ServerSocket serverSocket;
    private static Socket agentSocket;
    private static final int PORT = 5056;
    private static ArrayList<AgentHandler> agentHandler = new ArrayList<AgentHandler>();

    public static void main(String[] args) {

        System.out.println("--- Starting server ---");

        LogWriter log = new LogWriter("Server Log");
        log.write("Server started.");

        try {

            Frontier.initFrontier();
            System.out.println("Connected to frontier.");

            serverSocket = new ServerSocket(PORT);

            int connectionCount = 0; //jumlah agent yang terkoneksi saat ini
            while (true) {

                System.out.println("Waiting for connection...");
                agentSocket = serverSocket.accept();
                connectionCount++;
                AgentHandler handler = new AgentHandler(agentSocket, connectionCount);
                agentHandler.add(handler);
                handler.start();
                System.out.println("Connection #" + connectionCount + " accepted.");
                log.write("Connection #" + connectionCount + "accepted. Thread has started.");

            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            log.write("Catch exception: [" + ex + "].");
        }
    }
}
