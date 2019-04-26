package model.gamehandler;

import model.board.Board;
import model.board.Square;
import model.player.Player;
import model.exceptions.*;
import network.messages.serverToClient.BoardRequest;
import network.messages.Message;
import network.server.ClientOnServer;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Room {

    private List<Player> players;
    private Map<Player, ClientOnServer> connectionToClient = new HashMap<>();
    private Board board = new Board();
    private Player currentPlayer;
    private Player startingPlayer;
    private List<Player> visiblePlayers;
    private Player selectedTarget;
    private List<Player> targetedList= new ArrayList<>();

    private static final Logger logger = Logger.getLogger(Room.class.getName());


    public Room(Board board) {
        super();
        this.board=board;
        this.players= new ArrayList<>();
    }


    public void addPlayer(Player player) throws TooManyPlayerException {
        if(players.isEmpty()){
            startingPlayer = player;
            currentPlayer = player;
        }
        if(players.size()<5)
            players.add(player);
        else
            throw new TooManyPlayerException("cant add the 6th player");
    }

    //needed for starting a new room from waitingRoom
    public void addPlayer(ClientOnServer client){
        if(players.isEmpty()){
            startingPlayer = client.getPersonalPlayer();
            currentPlayer = client.getPersonalPlayer();
        }
        if(players.size()<5){
            players.add(client.getPersonalPlayer());
            connectionToClient.put(client.getPersonalPlayer(), client);
        }

        else
            throw new TooManyPlayerException("cant add the 6th player");
    }


    public void nextPlayer(){
        if (players!=null) {
            if (currentPlayer == null)
                currentPlayer = players.get(0);
            else if(players.indexOf(currentPlayer)<players.size())
                currentPlayer=players.get(players.indexOf(currentPlayer)+1);
            else
                currentPlayer = players.get(0);
        }
    }

    public void startMatch(){
        //TODO add controller
        //
        Message boardRequest = new BoardRequest(board.getMap().getMaps());
        sendMessage(startingPlayer, boardRequest);
    }

    public void createMap(int selection){
        board.getMap().createMap(selection);
        String description = board.getMap().getMaps().get(selection);

        logger.log(Level.INFO, "selected board is {0}", description);
    }

    //move this somewhere else if needed (better controller probably)
    public void sendMessage(Player player, Message message){
        try{
            connectionToClient.get(player).getClientInterface()
                    .notifyClient(message);
        }catch (RemoteException e){
            logger.log(Level.WARNING, "Connection error", e);
        }

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

    public Board getBoard() {
        return board;
    }

    public Player getStartingPlayer() {
        return startingPlayer;
    }






    /**
     * Adds to the target list all the targets that are visible with the weapon
     * @param minDist       minimum distance that must be between player and target
     * @param maxDist       maximum distance that must be between player and target
     */
    public void VisibleTarget(int minDist,int maxDist){
        visiblePlayers =new ArrayList<>();
        Square position=currentPlayer.getPosition();
        int dist;
        for(Player player: players){
                dist=Math.abs(currentPlayer.getPosition().getX()+currentPlayer.getPosition().getY()-player.getPosition().getX()-player.getPosition().getY());
                if (position.getColor()== player.getPosition().getColor()&& dist>=minDist && dist <=maxDist)
                    visiblePlayers.add(player);
                else {
                    List<Square> neighbourSquare = position.getNeighbourSquare();
                    for (int i = 0; i < neighbourSquare.size(); i++) {
                        if (neighbourSquare.get(i).getColor()==player.getPosition().getColor()&& dist>=minDist && dist <=maxDist)
                                        visiblePlayers.add(player);

                    }
                }
        }
    }

    /**
     * Select the target from the visible list
     */
    public void selectTarget(){
        System.out.println("Select player :");
        for (int i = 0; i< visiblePlayers.size(); i++){
            System.out.println(visiblePlayers.get(i).getNickname()+ "Code : "+i);
        }
        Scanner reader = new Scanner(System.in);
        int choice =Integer.parseInt(reader.nextLine());
        this.selectedTarget = visiblePlayers.get(choice);
    }

    /**
     * Removes the target from the visible list
     */
    public void removeTarget(){
        for (int i = 0; i< visiblePlayers.size(); i++){
            if (visiblePlayers.get(i)==selectedTarget)
                        visiblePlayers.remove(i);
        }
    }

    /**
     * Adds a player already targeted to the targeted list
     */
    public void addTargetedList(){
        targetedList.add(selectedTarget);
    }

    /**
     * Select the target from the targeted list
     */
    public void selectTargeted() {

    System.out.println("Select Targeted player ");
    for (int i=0;i<targetedList.size();i++){
        System.out.println(targetedList.get(i).getNickname() + "Code : "+i);
    }
    Scanner r= new Scanner(System.in);
        int s =Integer.parseInt(r.nextLine());
        this.selectedTarget =targetedList.get(s);
    }

    /**
     * Select another player as a target that is in the same room as the previous target
     */
    public void selectTargetOnSameSquare(){
        Square position = selectedTarget.getPosition();
        removeTarget();
        for (int i = 0; i< visiblePlayers.size(); i++){
            if (visiblePlayers.get(i).getPosition()==position)
                selectedTarget= visiblePlayers.get(i);
        }
    }



}
