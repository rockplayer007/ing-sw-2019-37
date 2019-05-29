package model.gamehandler;

import controller.RoomController;
import controller.RoundController;
import model.board.Board;
import model.board.BoardGenerator;
import model.board.GameBoard;
import model.player.Player;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Room {

    private RoomController roomController;
    private List<Player> players;
    private Board board;
    private BoardGenerator boardGenerator;
    private Player currentPlayer;
    private Player startingPlayer;
    private AttackHandler attackHandler=new AttackHandler();

    private static final Logger logger = Logger.getLogger(Room.class.getName());


    public Room(RoomController roomController) {
        this.roomController = roomController;
        board = new Board();
        boardGenerator = new BoardGenerator(board);
        players = new ArrayList<>();

    }

    public void setNextPlayer() {

        if (players != null) {
            if (currentPlayer == null)
                currentPlayer = players.get(0);
            else if (players.indexOf(currentPlayer) < players.size() - 1)
                currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
            else
                currentPlayer = players.get(0);
        }
    }


    public void createMap(int selection) {
        GameBoard gameBoard = boardGenerator.createMap(selection);
        String description = gameBoard.getDescription();
        board.setMap(gameBoard);

        logger.log(Level.INFO, "selected board is {0}", description);

        //TODO add update all message
    }

    public RoomController getRoomController() {
        return roomController;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public BoardGenerator getBoardGenerator(){
        return boardGenerator;
    }
    public Board getBoard() {
        return board;
    }

    public void setAttackHandler(AttackHandler attackHandler) {
        this.attackHandler = attackHandler;
    }

    public AttackHandler getAttackHandler() {
        return attackHandler;
    }

    public void setStartingPlayer(Player player){
        startingPlayer = player;
    }

    public Player getStartingPlayer() {
        return startingPlayer;
    }

    public void setPlayers(List<Player> player){
        players.addAll(player);
    }


    /**
     * do this every time when the player (currentPlayer) finished his turn.
     */
    public void endTurnControl (){
        List<Player> diedPlayers = players.stream().filter(x->x.getPlayerBoard().getHp().size()>10).collect(Collectors.toList());
        if (!diedPlayers.isEmpty()){
            diedPlayers.forEach(x->x.getPlayerBoard().liquidation());
            if (diedPlayers.size()>1)
                currentPlayer.getPlayerBoard().addPoints(1);
        }
    }

    public void startFrenzy(){
        players.forEach(p->p.getPlayerBoard().setFrenzy(true));
//        TODO da mettere  frenetiche action state a ogni player a seconda le regole.

    }
}
