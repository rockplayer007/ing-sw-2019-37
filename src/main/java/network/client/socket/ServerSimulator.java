package network.client.socket;

import network.client.ClientInterface;
import network.messages.Message;
import network.messages.clientToServer.ClientToServer;
import network.messages.serverToClient.ServerToClient;
import network.server.ServerInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simulates a server on the client's side
 */
public class ServerSimulator implements ServerInterface {

    private ClientInterface client;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private static final Logger logger = Logger.getLogger(ServerSimulator.class.getName());

    /**
     * Constructor where the input and output stream is initialized
     * @param client the client that is connecting
     * @param serverIp the ip to connect to the server
     * @param port the port to connect to the server
     * @throws IOException error in connection
     */
    ServerSimulator(ClientInterface client, String serverIp, int port) throws IOException{
        this.client = client;
        Socket connection = new Socket(serverIp, port);
        this.out = new ObjectOutputStream(connection.getOutputStream());
        this.in = new ObjectInputStream(connection.getInputStream());

        receiveMessages();
    }

    /**
     * Creates a thread to receive messages
     */
    private void receiveMessages(){
        //logger.log(Level.WARNING, "Exception on network:", e);
        //shouldn't happen, only with rmi
        //do nothing, let the game continue
        //throw new RuntimeException("Wrong deserialization/message: " + e.getMessage());
        Thread receiver = new Thread(
                () -> {
                    ServerToClient message;
                    try {
                        do {
                            message = ((ServerToClient) in.readObject());
                            if (message != null) {
                                client.notifyClient(message);
                            }
                        } while (message != null);

                    } catch (IOException e) {
                        //logger.log(Level.WARNING, "Exception on network:", e);
                        try {
                            client.notifyClient(new ServerToClient(Message.Content.DISCONNECTION));
                        } catch (RemoteException ex) {
                            logger.log(Level.WARNING, "Shouldn't happen actually... ", e);
                            //shouldn't happen, only with rmi
                        }
                    } catch (ClassNotFoundException e) {
                        //do nothing, let the game continue
                        receiveMessages();
                        //throw new RuntimeException("Wrong deserialization/message: " + e.getMessage());
                    }
                }
        );
        receiver.start();
    }

    /**
     * Sends a message to the {@link network.server.MainServer}
     * through the output stream
     * @param message message that arrives from the {@link network.client.MainClient}
     *                and is sent to the {@link network.server.MainServer}
     */
    @Override
    public synchronized void notifyServer(ClientToServer message) {

        try {
            //TODO check this
            //oos.reset();
            out.writeObject(message);
            //oos.flush();
        } catch (IOException e) {
            //logger.log(Level.WARNING, "Exception on network, can't send message", e);
            try {
                client.notifyClient(new ServerToClient(Message.Content.DISCONNECTION));
            } catch (RemoteException ex) {
                logger.log(Level.WARNING, "Exception on network, weird this shouldn't happen", ex);
                //should not happen, only with rmi
            }
        }
    }

}
