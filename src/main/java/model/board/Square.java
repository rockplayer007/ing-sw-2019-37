package model.board;

import model.player.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Set<Square> getValidPosition(int maxDistance){
        Set<Square> positions = new HashSet<>();
        if(maxDistance != 0){

            neighbourSquare.forEach((x)-> positions.addAll(x.getValidPosition(maxDistance-1)));

        }
        else {
            positions.add(this);
        }

        return positions;
    }
    public void addPlayer(Player player){
        playersOnSquare.add(player);
    }
    public void removePlayer(Player player){
        playersOnSquare.remove(player);
    }

}
