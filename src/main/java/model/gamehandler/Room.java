package model.gamehandler;

import model.board.Board;
import model.board.Square;
import model.player.Player;
import model.exceptions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Room {

    private List<Player> players;
    private Board board;
    private Player currentPlayer;
    private List<Player> visblePlayers;
    private Player selectedTarget;
    private List<Player> targetedList= new ArrayList<>();


    public Room(Board board) {
        this.board=board;
        players=new ArrayList<>();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) throws TooManyPlayerException {
        if(players.size()<5)
            players.add(player);
        else
            throw new TooManyPlayerException("cant add the 6th player");
    }

    public Player getCurrentPlayer() {
        if (currentPlayer==null)
            return getAndSetNextPlayer();
        else
            return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
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

    public Player getAndSetNextPlayer(){
        nextPlayer();
        return currentPlayer;
    }

    public Board getBoard() {
        return board;
    }




















    /**
     * Adds to the target list all the targets that are visible with the weapon
     * @param minDist       minimum distance that must be between player and target
     * @param maxDist       maximum distance that must be between player and target
     */
    public void VisibleTarget(int minDist,int maxDist){
        visblePlayers=new ArrayList<>();
        Square position=currentPlayer.getPosition();
        int dist;
        for(Player player: players){
                dist=Math.abs(currentPlayer.getPosition().getX()+currentPlayer.getPosition().getY()-player.getPosition().getX()-player.getPosition().getY());
                if (position.getColor()== player.getPosition().getColor()&& dist>=minDist && dist <=maxDist)
                    visblePlayers.add(player);
                else {
                    List<Square> neighbourSquare = position.getNeighbourSquare();
                    for (int i = 0; i < neighbourSquare.size(); i++) {
                        if (neighbourSquare.get(i).getColor()==player.getPosition().getColor()&& dist>=minDist && dist <=maxDist)
                                        visblePlayers.add(player);

                    }
                }
        }
    }

    /**
     * Select the target from the visible list
     */
    public void selectTarget(){
        System.out.println("Select player :");
        for (int i=0;i<visblePlayers.size();i++){
            System.out.println(visblePlayers.get(i).getNickname()+ "Code : "+i);
        }
        Scanner reader = new Scanner(System.in);
        int choice =Integer.parseInt(reader.nextLine());
        this.selectedTarget =visblePlayers.get(choice);
    }

    /**
     * Removes the target from the visible list
     */
    public void removeTarget(){
        for (int i=0;i<visblePlayers.size();i++){
            if (visblePlayers.get(i)==selectedTarget)
                        visblePlayers.remove(i);
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
        for (int i=0;i<visblePlayers.size();i++){
            if (visblePlayers.get(i).getPosition()==position)
                selectedTarget=visblePlayers.get(i);
        }
    }



}
