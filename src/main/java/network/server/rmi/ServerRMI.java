package network.server.rmi;

import network.server.Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerRMI {

    Server server;
    public ServerRMI(Server server){
        this.server = server;
    }

    public void startServer(int port) throws RemoteException {
        System.setProperty("java.rmi.server.hostname", "localhost");
        Registry registry = LocateRegistry.createRegistry(port);
        ServerImplementation serverImplementation = new ServerImplementation(server);

        registry.rebind("Server", serverImplementation);
    }
}
