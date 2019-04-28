package network.server.socket;

import network.server.MainServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


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

    public void startServer(int port) throws IOException {
        connectionSocket = new ServerSocket(port);
        logger.log(Level.INFO, "Listening on port {0}", port);
    }

    @Override
    public void run(){
        while (keepWakeup) {
            try{
                Socket clientSocket = connectionSocket.accept();
                logger.log(Level.INFO, "New connection with client {0}", clientSocket.getRemoteSocketAddress());
                pool.submit(new ClientHandler(clientSocket, server));
            }catch (IOException e){
                logger.log(Level.WARNING, e.toString(), e);
            }

        }
    }


    public void closeServerSocket() throws IOException {
        connectionSocket.close();
        pool.shutdown();
        keepWakeup = false;
    }

}
