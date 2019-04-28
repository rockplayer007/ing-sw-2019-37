package network.client.socket;

import network.client.ConnectionInterface;
import network.client.MainClient;
import network.messages.clientToServer.ClientToServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionSOCKET implements ConnectionInterface {

    private Socket connection;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private static final Logger logger = Logger.getLogger(ConnectionSOCKET.class.getName());

    public ConnectionSOCKET(String ip) throws IOException {
        this.connection = new Socket(ip, 8000);
        this.out = new ObjectOutputStream(connection.getOutputStream());
        this.in = new ObjectInputStream(connection.getInputStream());
        System.out.println("connected");
    }

    public void sendMessage(ClientToServer message) {

        try {
            out.writeObject(message);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Exception on network", e);
        }


    }
}
