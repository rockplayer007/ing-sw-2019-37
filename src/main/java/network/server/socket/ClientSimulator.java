package network.server.socket;

import network.client.ClientInterface;
import network.messages.Message;
import network.messages.clientToServer.ClientToServer;
import network.messages.clientToServer.ConnectionMessage;
import network.messages.clientToServer.LoginRequest;
import network.messages.serverToClient.ServerToClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Virtual view to manage the client from the server
 */
public class ClientSimulator implements Runnable, ClientInterface{

    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private boolean clientConnected;
    private ServerSOCKET server;

    //private static final Logger logger = Logger.getLogger(ClientSimulator.class.getName());

    /**
     * Constructor where the input and output stream is initialized
     * @param socket the socket that has been initialized
     * @param server the server to of the socket
     * @throws IOException when the input and output give problem
     */
    ClientSimulator(Socket socket, ServerSOCKET server) throws IOException {

        this.server = server;

        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.clientConnected = true;

    }

    /**
     * Manages the messages that arrive from the client will be handle here
     */
    @Override
    public void run(){

        try {
            while (clientConnected){
                ClientToServer message = (ClientToServer) in.readObject();
                if(message.getContent() == Message.Content.LOGIN_REQUEST){
                    ((LoginRequest) message).setClientInterface(this);
                }
                else if(message.getContent() == Message.Content.CONNECTION){
                    ((ConnectionMessage) message).setClientInterface(this);
                }
                server.newMessage(message);
            }
        }catch (IOException | ClassNotFoundException e){
            //notify server
            server.disconnectClient(this);
            //logger.log(Level.WARNING, e.toString(), e);
        }
    }

    /**
     * Sends messages to the client
     * @param message the message that has to be sent to the server
     */
    @Override
    public void notifyClient(ServerToClient message)  {
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            server.disconnectClient(this);
            //logger.log(Level.WARNING,"Message not sent", e);
        }
    }

    /**
     * Needed to close the connection when the client is not online anymore
     */
    @Override
    public void closeConnection() {
         clientConnected = false;
    }

}