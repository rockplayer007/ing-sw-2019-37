package network.server.rmi;

import network.server.Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerImplementation extends UnicastRemoteObject implements ServerInterface {

    private Server server;

    protected ServerImplementation(Server server) throws RemoteException {
        this.server = server;
    }

    @Override
    public void register(String name) {
        server.addClient(name);
        System.out.println(name + " added to the server");
    }
}
