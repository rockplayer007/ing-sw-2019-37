package model.board;

import model.player.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class that defines a single square of the board
 */
public abstract class Square {

    private Color squareColor;
    private int id;
    private int x;
    private int y;
    private List<Square> neighbourSquare = new ArrayList<>();
    private List<Player> playersOnSquare = new ArrayList<>();
    private boolean generationPoint;

    /**
     * Constructor of the class
     * @param id Number of the square
     * @param color Color of the square
     * @param type enerationPoint true AmmoSquare false
     * @param x x coordinate of the square
     * @param y y coordinate of the square
     */
    public Square(int id, Color color, boolean type, int x, int y ){
        this.id = id;
        this.squareColor = color;
        this.generationPoint = type;
        this.x = x;
        this.y = y;
    }

    /**
     * Adds a square that has distance 1 from the current square
     * @param next Near square
     */
    public void addNextSquare(Square next){
        neighbourSquare.add(next);
    }

    /**
     * Gives the color of the square
     * @return Color of the square
     */
    public Color getColor(){
        return squareColor;
    }

    public List<Square> getNeighbourSquare(){
        return neighbourSquare;
    }

    /**
     * Tells if the square is a GenerationSquare or an AmmoSquare
     * @return True if it is a GenerationSquare or false if AmmoSquare
     */
    public boolean GetGenerationPoint(){
        return generationPoint;
    }

    /**
     * Gives x coordinate of the square
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gives y coordinate of the square
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Given a distance it returns the squares that have
     * this distance from the current square
     * @param maxDistance Distance from the square
     * @return All the squares with that distance
     */
    public Set<Square> getValidPosition(int maxDistance){
        Set<Square> positions = new HashSet<>();
        if(maxDistance != 0){
            neighbourSquare.forEach(squares-> positions.
                    addAll(squares.getValidPosition(maxDistance-1)));
        }
        else {
            positions.add(this);
        }

        return positions;
    }

    public List<Player> getPlayersOnSquare(){
        return playersOnSquare;
    }

    /**
     * Gives squares that are visible from the current square
     * @return squares visible from the current square
     */
    public Set<Square> visibleSquare(){
        //TODO
        return null;
    }
    /**
     * Places a player to the current square
     * @param player Player that has to be placed
     */
    public void addPlayer(Player player){
        playersOnSquare.add(player);
    }

    /**
     * Removes a player from the current square
     * @param player Player that has to be removed
     */
    public void removePlayer(Player player){
        playersOnSquare.remove(player);
    }

    public Boolean isThisSquare(int x,int y){
        if (getX()==x && getY()==y)
            return true;
        else
            return false;
    }

}
