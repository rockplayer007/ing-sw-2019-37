package view;


import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface ViewInterface {

    void launch() throws NotBoundException, IOException;

    void logIn(boolean ask);

    void chooseBoard(java.util.Map<Integer,String> possibleBoards);

}
