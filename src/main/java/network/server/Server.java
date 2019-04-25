package network.server;

import network.messages.LoginRequest;
import network.messages.Message;
import network.server.rmi.ServerRMI;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private List<ClientOnServer> clients = new ArrayList<>();
    private ServerRMI serverRMI;
    private WaitingRoom waitingRoom;
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    private Server(){
        this.serverRMI = new ServerRMI(this);
        this.waitingRoom = new WaitingRoom();
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

        }

    }

    public void addClient(LoginRequest message){
        logger.log(Level.INFO, "{0} adding to the server", message.getSender());

        //TODO manage users that were already logged
        //check that it is the right user looking at the clientID
        //consider a map that tells in which room the user is

        ClientOnServer newClient = new ClientOnServer(message.getSender(), message.getClientInterface());
        clients.add(newClient);
        waitingRoom.addClient(newClient);
    }

    public List<ClientOnServer> getClients(){
        return this.clients;
    }
}
