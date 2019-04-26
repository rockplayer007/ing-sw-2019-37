package network.server;

import network.messages.LoginRequest;
import network.messages.LoginResponse;
import network.messages.Message;
import network.server.rmi.ServerRMI;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private Map<String, ClientOnServer> oldClients= new HashMap<>();

    private ServerRMI serverRMI;
    private WaitingRoom waitingRoom;
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

    public void handleMessage(Message message){
        switch (message.getContent()){
            case LOGIN_REQUEST:
                addClient((LoginRequest) message);
                break;
            default:
                logger.log(Level.WARNING, "Unhandled message");

        }

    }

    public void addClient(LoginRequest message){
        logger.log(Level.INFO, "{0} adding to the server", message.getSender());
        //TODO manage users that were already logged
        //consider a map that tells in which room the user is
        if(oldClients.containsKey(message.getSender())){
            //contains username
            System.out.println("here");


            if (oldClients.get(message.getSender()).getClientID().equals(message.getClientID())){
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
            oldClients.put(message.getSender(), newClient);
            waitingRoom.addClient(newClient);

            try {
                message.getClientInterface().notifyClient(new LoginResponse(false));
            }catch (RemoteException e){
                logger.log(Level.WARNING, "Connection error", e);
            }
        }
    }



}
