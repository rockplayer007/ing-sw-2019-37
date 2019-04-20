package network.server;

import network.server.rmi.ServerRMI;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {

    private List<String> clients = new ArrayList<>();
    private ServerRMI serverRMI;
    private WaitingRoom waitingRoom;

    private Server(){
        this.serverRMI = new ServerRMI(this);
        this.waitingRoom = new WaitingRoom();
    }

    public static void main(String[] args) throws Exception{

        InetAddress inetAddress = InetAddress.getLocalHost();
        System.out.println("Connect your client here: " + inetAddress.getHostAddress());
        System.out.println("Host Name:- " + inetAddress.getHostName());

        try {
            Server server = new Server();
            server.startServer(1099);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }



    }

    private void startServer(int rmiPort) throws RemoteException {
        serverRMI.startServer(rmiPort);
        System.out.println("new rmi server");
    }

    public void addClient(String name){
        clients.add(name);
        waitingRoom.addClient(name);
    }

    public List<String> getClients(){
        return this.clients;
    }
}
