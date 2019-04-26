package network.server;

import model.gamehandler.Room;
import network.messages.clientToServer.BoardResponse;
import network.messages.clientToServer.ClientToServer;
import network.messages.clientToServer.LoginRequest;
import network.messages.serverToClient.LoginResponse;
import network.server.rmi.ServerRMI;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    //clientID and username
    private Map<String, String> oldClients= new HashMap<>();

    private ServerRMI serverRMI;
    private WaitingRoom waitingRoom;
    private Map<String, Room> usernameInRoom = new HashMap<>();
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    private Server(){
        super();
        this.serverRMI = new ServerRMI(this);
        this.waitingRoom = new WaitingRoom(this);
    }

    public static void main(String[] args) throws UnknownHostException {

        InetAddress inetAddress = InetAddress.getLocalHost();
        logger.log(Level.INFO, "Connect your client here: {0}", inetAddress.getHostAddress());
        logger.log(Level.INFO, "Host Name:- {0}", inetAddress.getHostName());

        try {
            Server server = new Server();
            server.startServer(1099);
        }catch (Exception e){
            logger.log(Level.SEVERE, e.toString(), e);
        }



    }

    private void startServer(int rmiPort) throws RemoteException {
        serverRMI.startServer(rmiPort);
        logger.log(Level.INFO, "new rmi server created");
        //TODO will add a starting socket server
    }

    public void handleMessage(ClientToServer message){
        //verify that the user corresponds with clientID


        switch (message.getContent()){
            case LOGIN_REQUEST:
                addClient((LoginRequest) message);
                break;
            case BOARD_RESPONSE:
                usernameInRoom.get(message.getSender()).createMap(((BoardResponse) message).getSelectedBoard());
                break;


            default:
                logger.log(Level.WARNING, "Unhandled message");

        }




    }

    //TODO
    private boolean checkUser(ClientToServer message){
        //not working when oldClients.get is null
        return message.getSender().equals(oldClients.get(message.getClientID()));
    }


    public void addClient(LoginRequest message){
        logger.log(Level.INFO, "{0} adding to the server", message.getSender());
        //TODO manage users that were already logged
        //consider a map that tells in which room the user is
        if(oldClients.containsValue(message.getSender())){
            //contains username
            if (oldClients.containsKey(message.getClientID())){
                //client already exists
                //let him continue
            }
            else{

                //username already exists
                try {
                    message.getClientInterface().notifyClient(new LoginResponse(true));
                }catch (RemoteException e){
                    logger.log(Level.WARNING, "Connection error", e);
                }


            }
        }
        else{
            //username doesnt exist
            //add client to the waiting room
            ClientOnServer newClient = new ClientOnServer(message.getSender(), message.getClientInterface(),
                    message.getClientID());
            oldClients.put(message.getClientID(), message.getSender() );
            waitingRoom.addClient(newClient);

            try {
                message.getClientInterface().notifyClient(new LoginResponse(false));
            }catch (RemoteException e){
                logger.log(Level.WARNING, "Connection error", e);
            }
        }
    }

    public void setUsernameInRoom(List<String> usernames, Room playingRoom){
        usernames.forEach(name -> usernameInRoom.put(name, playingRoom));
    }

}
