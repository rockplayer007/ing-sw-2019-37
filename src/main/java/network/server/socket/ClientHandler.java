package network.server.socket;

import network.messages.clientToServer.ClientToServer;
import network.server.MainServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler implements Runnable{

    private Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private boolean clientConnected;
    private MainServer server;

    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());

    public ClientHandler(Socket socket, MainServer server) throws IOException {
        this.socket = socket;
        this.server = server;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.clientConnected = true;
    }


    @Override
    public void run(){
        try {
            while (clientConnected){
                ClientToServer message = (ClientToServer) in.readObject();
                server.handleMessage(message);
            }
        }catch (IOException | ClassNotFoundException e){
            logger.log(Level.WARNING, e.toString(), e);
        }

    }

}
