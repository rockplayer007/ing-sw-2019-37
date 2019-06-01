package network.server.socket;

import network.client.ClientInterface;
import network.messages.Message;
import network.messages.clientToServer.ClientToServer;
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

    private Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private boolean clientConnected;
    private ServerSOCKET server;

    private static final Logger logger = Logger.getLogger(ClientSimulator.class.getName());

    /**
     * Constructor where the input and output stream is initialized
     * @param socket
     * @param server
     * @throws IOException
     */
    public ClientSimulator(Socket socket, ServerSOCKET server) throws IOException {
        this.socket = socket;
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
                if(message.getContent() == Message.Content.LOGIN_REQUEST ){
                    ((LoginRequest) message).setClientInterface(this);
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
     * @param message
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

    //TODO set client connection
}
