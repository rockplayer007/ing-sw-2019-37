package network.client.socket;

import network.client.ClientInterface;
import network.messages.clientToServer.ClientToServer;
import network.messages.serverToClient.ServerToClient;
import network.server.ServerInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simulates a server on the client's side
 */
public class ServerSimulator implements ServerInterface {

    private ClientInterface client;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private Thread receiver;

    private static final Logger logger = Logger.getLogger(ServerSimulator.class.getName());

    /**
     * Constructor where the input and output stream is initialized
     * @param client
     * @param serverIp
     * @param port
     * @throws IOException
     */
    public ServerSimulator(ClientInterface client, String serverIp, int port) throws IOException{
        this.client = client;
        Socket connection = new Socket(serverIp, port);
        this.out = new ObjectOutputStream(connection.getOutputStream());
        this.in = new ObjectInputStream(connection.getInputStream());

        receiveMessages();
    }

    /**
     * Creates a thread to receive messages
     */
    public void receiveMessages(){
        receiver = new Thread(
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
                        logger.log(Level.WARNING, "Exception on network:", e);
                        client.notifyClient();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("Wrong deserialization/message: " + e.getMessage());
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
            logger.log(Level.WARNING, "Exception on network, can't send message", e);
        }
    }

    //TODO add an receiver.interrupt()

}
