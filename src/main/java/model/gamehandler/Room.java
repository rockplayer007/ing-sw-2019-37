package model.gamehandler;

import controller.RoomController;
import model.board.Board;
import model.board.BoardGenerator;
import model.board.GameBoard;
import model.board.SkullBoard;
import model.player.ActionState;
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
    private AttackHandler attackHandler;
    private int frenzyCounter;

    private static final Logger logger = Logger.getLogger(Room.class.getName());


    public Room(RoomController roomController) {
        this.roomController = roomController;
        board = new Board();
        boardGenerator = new BoardGenerator(board);
        players = new ArrayList<>();
        //needs to be null at the beginning to be set later
        startingPlayer = null;
        attackHandler = new AttackHandler();

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
        frenzyCounter = players.size();
    }


    /**
     * do this every time when the player (currentPlayer) finished his turn.
     * @return true means is end of game.
     */
    public boolean endTurnControl (){
        List<Player> diedPlayers = players.stream().filter(x->x.getPlayerBoard().getHp().size()>10).collect(Collectors.toList());
        SkullBoard skullBoard = board.getSkullBoard();
        if (!diedPlayers.isEmpty()){
            diedPlayers.forEach( x-> {
                skullBoard.addCell(x.getPlayerBoard().liquidation());
                skullBoard.takeOneSkulls();
            });
            if (diedPlayers.size()>1)
                currentPlayer.getPlayerBoard().addPoints(1);
        }

        if (skullBoard.getNumberSkulls()==0)
            startFrenzy();
        ActionState actionState = currentPlayer.getActionStatus();
        if (actionState==ActionState.FRENETICACTIONS1||actionState==ActionState.FRENETICACTIONS2) {
            frenzyCounter--;

            return frenzyCounter==0;
        }
        return false;

    }

    public void startFrenzy(){
        players.forEach(p->p.getPlayerBoard().setFrenzy(true));
        players.forEach(p->p.setActionStatus(ActionState.FRENETICACTIONS2));
        for (int i = players.indexOf(currentPlayer); i< frenzyCounter; i++){
            players.get(i-1).setActionStatus(ActionState.FRENETICACTIONS1);
        }
    }

    public List<Player> endScoreboard(){
        players.forEach(p->p.getPlayerBoard().liquidation());
        return players.stream()
                .sorted(Comparator.comparingInt(p->p.getPlayerBoard().getPoints()))
                .collect(Collectors.toList());
    }


}
