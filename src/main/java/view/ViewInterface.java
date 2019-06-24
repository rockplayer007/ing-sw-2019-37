package view;


import model.board.Color;
import model.board.GameBoard;
import model.board.SkullBoard;
import model.board.Square;
import model.card.AmmoColor;
import model.card.Effect;
import model.card.Powerup;
import model.card.Weapon;
import model.player.ActionOption;
import model.player.Player;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.List;
import java.util.Map;


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


    void updateAll(GameBoard board, List<Powerup> myPowerups, SkullBoard skullBoard);

    void chooseAction(List<ActionOption> actions);

    void chooseSquare(List<Square> squares, String info);

    void choosePowerup(List<Powerup> powerups, boolean optional, String info);

    void chooseWeapon(List<Weapon> weapons, boolean optional, String info);

    void chooseEffect(List<Effect> effects, boolean optional);

    void choosePlayer(List<Player> players, String info);

    void chooseDirection(List<Square.Direction> directions, String info);

    void chooseAmmoColor(List<AmmoColor> ammoColors);

    void chooseRoom(List<Color> rooms);

    void showAttack(Player attacker, Map<Player, Integer> hp, Map<Player, Integer> marks);

    void showInfo(String info);

    void showScore(List<Player> score);
}
