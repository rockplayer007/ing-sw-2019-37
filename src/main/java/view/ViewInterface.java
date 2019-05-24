package view;


import model.board.GameBoard;
import model.board.Square;
import model.card.Powerup;
import model.card.Weapon;
import model.player.ActionOption;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;


/**
 * Gives general methods to use for {@link view.CLI} and for {@link view.GUI}
 */
public interface ViewInterface {

    /**
     * Starts the user interface
     * @throws NotBoundException
     * @throws IOException
     */
    void launch() throws NotBoundException, IOException;

    /**
     * Allows the client to log in with a username
     * @param ask if true asks for the username, if false welcomes the user
     */
    void logIn(boolean ask);

    /**
     * Makes the first logged in player choose the board
     * @param possibleBoards boards to chose from
     */
    void chooseBoard(java.util.Map<Integer,String> possibleBoards);

    /**
     * Notifies the user that the time for his action is finished
     */
    void timeout();


    void updatedBoard(GameBoard board);

    void chooseAction(List<ActionOption> actions);

    void chooseSquare(List<Square> squares);

    void choosePowerup(List<Powerup> powerups, boolean optional);

    void chooseWeapon(List<Weapon> weapons, boolean optional);
}
