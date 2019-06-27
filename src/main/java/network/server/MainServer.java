package network.server;

import controller.RoomController;
import network.client.ClientInterface;
import network.messages.Message;
import network.messages.clientToServer.ClientToServer;
import network.messages.clientToServer.ConnectionMessage;
import network.messages.clientToServer.LoginRequest;
import network.messages.serverToClient.InfoMessage;
import network.messages.serverToClient.LoginResponse;
import network.messages.serverToClient.ServerToClient;
import network.server.rmi.ServerRMI;
import network.server.socket.ServerSOCKET;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The main server is being created only once and creates a server for RMI connection
 * and one for socket connections. Then it waits for clients to connect.
 * The message that arrive are transferred to the {@link RoomController} where the user is playing.
 */
public class MainServer {

    //clientID and username
    private Map<String, String> oldClients= new HashMap<>();
    private List<ClientOnServer> allClients = new ArrayList<>();

    private ServerRMI serverRMI;
    private ServerSOCKET serverSocket;
    private WaitingRoom waitingRoom;
    private Map<String, RoomController> usernameInRoom = new HashMap<>();
    private static final Logger logger = Logger.getLogger(MainServer.class.getName());

    /**
     * Constructor that creates RMI and Socket classes
     */
    private MainServer(){
        super();
        this.serverRMI = new ServerRMI(this);
        this.serverSocket = new ServerSOCKET(this);
        this.waitingRoom = new WaitingRoom(this);
    }

    public static void main(String[] args) throws UnknownHostException {

        InetAddress inetAddress = InetAddress.getLocalHost();
        logger.log(Level.INFO, "Connect your client here: {0}\n Host Name: {1}",
                new String[]{inetAddress.getHostAddress(), inetAddress.getHostName()});
        //logger.log(Level.INFO, "Host Name:- {0}", inetAddress.getHostName());

        try {
            MainServer server = new MainServer();
            server.startServer(Configs.getInstance().getRmiPort(), Configs.getInstance().getSocketPort());
        }catch (Exception e){
            logger.log(Level.SEVERE, e.toString(), e);
        }

    }

    /**
     * Starts RMI and Socket servers
     * @param rmiPort
     * @param socketPort
     * @throws IOException
     */
    private void startServer(int rmiPort, int socketPort) throws IOException {
        serverSocket.startServer(socketPort);
        serverSocket.start();
        logger.log(Level.INFO, "new socket server created");

        serverRMI.startServer(rmiPort);

        logger.log(Level.INFO, "new rmi server created");

    }

    /**
     * All messages that arrive from the client are managed here and sent where they need to go
     * @param message
     */
    public void handleMessage(ClientToServer message){
        //verify that the user corresponds with clientID
        if(message.getContent() != Message.Content.CONNECTION){
            logger.log(Level.INFO, "arrived message type: {0}, from player: {1}",
                    new String[]{String.valueOf(message.getContent()), message.getSender()});
        }


        if(message.getContent().equals(Message.Content.LOGIN_REQUEST)){
            addClient((LoginRequest) message);
        }
        else if (message.getContent().equals(Message.Content.CONNECTION)){

            //reconnect player
            allClients.stream()
                    .filter(x -> x.getUsername().equals(message.getSender()))
                    .findFirst().ifPresent(clientOnServer -> {
                clientOnServer.getPersonalPlayer().setConnected();
            });
            try {
                ((ConnectionMessage) message).getClientInterface().notifyClient(new ServerToClient(Message.Content.CONNECTION));
            } catch (RemoteException e) {
                disconnectPlayer(((ConnectionMessage) message).getClientInterface());
            }

        }
        else {
            usernameInRoom.get(message.getSender()).handleMessages(message);
        }

    }

    //TODO
    private boolean checkUser(ClientToServer message){
        //not working when oldClients.get is null

        return message.getSender().equals(oldClients.get(message.getClientID()));
    }

    /**
     * Adds clients to a waiting queue if they never logged in, otherwise reconnects the disconnected clients
     * @param message
     */
    public void addClient(LoginRequest message){
        logger.log(Level.INFO, "{0} wants to login to the server", message.getSender());

        if(oldClients.containsValue(message.getSender())){
            //contains username
            if (oldClients.containsKey(message.getClientID())){
                //client already exists
                //let him continue
                Optional<ClientOnServer> optionalClient = allClients.stream()
                        .filter(x -> x.getUsername().equals(message.getSender()))
                        .findFirst();
                if(optionalClient.isPresent()) {
                    ClientOnServer client = optionalClient.get();
                    client.getPersonalPlayer().setConnected();
                    //setting a new client interface in case the client logs from somewhere else
                    client.setClientInterface(message.getClientInterface());
                    try {
                        client.getClientInterface().notifyClient(new InfoMessage("Welcome back"));
                    } catch (RemoteException e) {
                        logger.log(Level.WARNING, "disconnected in addClient");
                        disconnectPlayer(message.getClientInterface());
                    }
                }

            }
            else{


                //username already exists
                try {
                    message.getClientInterface().notifyClient(new LoginResponse(true, ""));
                }catch (RemoteException e){
                    //logger.log(Level.WARNING, "Player {0} disconnected", e);

                    disconnectPlayer(message.getClientInterface());
                }


            }
        }
        else{
            //username doesnt exist
            //add client to the waiting room
            //gives an UUID to each new client
            String clientID = UUID.randomUUID().toString();
            ClientOnServer newClient = new ClientOnServer(message.getSender(),
                    message.getClientInterface(), clientID);
            allClients.add(newClient);
            oldClients.put(clientID, message.getSender());
            waitingRoom.addClient(newClient);

            try {
                message.getClientInterface().notifyClient(new LoginResponse(false, clientID));
            }catch (RemoteException e){
                disconnectPlayer(message.getClientInterface());
                //logger.log(Level.WARNING, "Player {0} disconnected", message.getSender());

            }
        }
    }

    public void disconnectPlayer(ClientInterface clientInterface){
        for(ClientOnServer client : allClients){

            //check needed because if the player is not in the room already
            if(client.getClientInterface() == clientInterface){
                if(usernameInRoom.containsKey(client.getUsername())){
                    usernameInRoom.get(client.getUsername()).disconnectPlayer(client.getUsername());
                }
                else {
                    client.getPersonalPlayer().setDisconnected();
                    logger.log(Level.WARNING, "Player {0} disconnected", client.getUsername());
                }
                break;
            }
        }
    }

    /**
     * Defines which user is in which room
     * @param usernames
     * @param playingRoom
     */
    public void setUsernameInRoom(List<String> usernames, RoomController playingRoom){
        usernames.forEach(name -> usernameInRoom.put(name, playingRoom));
    }

    public void removeClientFromServer(List<String> usernames){

        usernames.forEach(name -> {
            //remove from room map
            usernameInRoom.remove(name);

            ClientOnServer toRemove = null;

            for(ClientOnServer all : allClients){
                if(all.getUsername().equals(name)){
                    toRemove = all;
                }

            }
            oldClients.remove(toRemove.getClientID());
            //this is for disconnecting the socket client
            try {
                toRemove.getClientInterface().closeConnection();
            } catch (RemoteException e) {
                //dont worry about this
            }
            removeClient(toRemove);
        });

    }

    public void removeClient(ClientOnServer client){
        allClients.remove(client);
        oldClients.remove(client.getClientID());

    }

}
