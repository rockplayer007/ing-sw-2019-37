package network.server.socket;

import network.client.ClientInterface;
import network.messages.clientToServer.ClientToServer;
import network.server.MainServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Allows to create a thread that manages new connections with the clients
 */
public class ServerSOCKET extends Thread{

    private MainServer server;
    private final ExecutorService pool;
    private ServerSocket connectionSocket;
    private boolean keepWakeup;
    private static final Logger logger = Logger.getLogger(ServerSOCKET.class.getName());

    public ServerSOCKET(MainServer server){
        this.server = server;
        keepWakeup = true;

        pool = Executors.newCachedThreadPool();
    }

    /**
     * Creates a new socket on a specified port
     * @param port the port where to connect the socket
     * @throws IOException thrown when there is an error in the connection
     */
    public void startServer(int port) throws IOException {
        connectionSocket = new ServerSocket(port);
        logger.log(Level.INFO, "Listening on port {0}", port);
    }

    /**
     * Every time a new connection with a new Client is established a new
     * thread is created to manage it
     */
    @Override
    public void run(){
        while (keepWakeup) {
            try{
                Socket clientSocket = connectionSocket.accept();
                logger.log(Level.INFO, "New connection with client {0}", clientSocket.getRemoteSocketAddress());
                pool.submit(new ClientSimulator(clientSocket, this));
            }catch (IOException e){
                logger.log(Level.WARNING, e.toString(), e);
            }

        }
    }

    /**
     * Sends a new arrived message to the {@link MainServer}
     * @param message the message to deliver to the server
     */
    void newMessage(ClientToServer message){
        server.handleMessage(message);
    }

    /**
     * Needed to tell the server to disconnect the client
     * @param client the client that needs to be disconnected
     */
    void disconnectClient(ClientInterface client){
        server.disconnectPlayer(client);
    }

    /*
    public void closeServerSocket() throws IOException {
        connectionSocket.close();
        pool.shutdown();
        keepWakeup = false;
    }

     */

}
