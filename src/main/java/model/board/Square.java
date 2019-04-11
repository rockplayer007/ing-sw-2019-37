package model.board;

import model.player.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Square {

    private Color squareColor;
    private int id;
    private int x;
    private int y;
    private List<Square> neighbourSquare = new ArrayList<>();
    private List<Player> playersOnSquare = new ArrayList<>();
    private boolean generationPoint;

    public Square(int id, Color color, boolean type, int x, int y ){
        this.id = id;
        this.squareColor = color;
        this.generationPoint = type;
        this.x = x;
        this.y = y;
    }

    public void addNextSquare(Square next){
        neighbourSquare.add(next);
    }
    public Color getColor(){
        return squareColor;
    }
    public boolean getType(){
        return generationPoint;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public ArrayList<Square> getValidPosition(int maxDistance){

        //TODO
        return null;//@roland per non lasciare un errore ho messo questo.
    }
    public void addPlayer(Player player){

    }
    public void removePlayer(Player player){

    }


}
