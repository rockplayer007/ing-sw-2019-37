package network.client.socket;

import network.client.ClientInterface;
import network.messages.clientToServer.ClientToServer;
import network.messages.serverToClient.ServerToClient;
import network.server.socket.ServerInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerSimulator implements ServerInterface {

    private ClientInterface client;
    private Socket connection;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    Thread receiver;

    private static final Logger logger = Logger.getLogger(ServerSimulator.class.getName());

    public ServerSimulator(ClientInterface client, String serverIp, int port) throws IOException{
        this.client = client;
        connection = new Socket(serverIp, port);
        this.out = new ObjectOutputStream(connection.getOutputStream());
        this.in = new ObjectInputStream(connection.getInputStream());

        //TODO if it doesnt connect set a flag to retry the connection

        receiveMessages();
    }

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
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("Wrong deserialization/message: " + e.getMessage());
                    }
                }
        );
        receiver.start();
    }

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
