package model.board;

import model.player.Player;

import java.io.Serializable;
import java.util.*;

/**
 * Class that defines a single square of the board
 */
public class Square implements Serializable {

    private Color squareColor;
    private int id;
    private int x;
    private int y;
    //needs to be transient otherwhise serialization doesnt work
    private transient List<Square> neighbourSquare = new ArrayList<>();
    private List<Integer> neighbourId = new ArrayList<>();

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
        neighbourId.add(next.getId());
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

    public List<Integer> getNeighbourId() {
        return neighbourId;
    }

    /**
     * Tells if the square is a GenerationSquare or an AmmoSquare
     * @return True if it is a GenerationSquare or false if AmmoSquare
     */
    public boolean isGenerationPoint(){
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

    public int getId() {
        return id;
    }

    /**
     * Given a distance it returns the squares that have
     * this distance from the current square
     * @param maxDistance Distance from the square
     * @return All the squares with that distance
     */
    public Set<Square> getAllPositions(int maxDistance){
        Set<Square> positions = new LinkedHashSet<>();
        if(maxDistance != 0){
            neighbourSquare.forEach(squares-> positions.
                    addAll(squares.getValidPosition(maxDistance-1)));
            //positions.addAll(neighbourSquare);
        }
        else {
            positions.add(this);
        }

        return positions;
    }

    public Set<Square> getValidPosition(int maxDistance){
        Set<Square> all = new LinkedHashSet<>();
        for(int i = 0; i <= maxDistance; i++){
            all.addAll(getAllPositions(i));
        }
        return all;
    }

    public List<Player> getPlayersOnSquare(){
        return playersOnSquare;
    }

    /**
     * Gives squares that are visible from a given square
     * The rules say that enemies are visible from a square if:
     * "The player is in the same room as you" and
     * "If your square has a door, you can also see any player on
     * any square in the room on the other side of the door"
     * @return a set of squares visible from the current square
     */
    public Set<Square> visibleSquare(GameBoard map){
        Set<Square> positions = new LinkedHashSet<>();
        //adds all the squares that are in the same room or in a neighbour room
        neighbourSquare.forEach(square -> positions.addAll(map.getSquaresInRoom().get(square.squareColor)));

        return positions;
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
        return (getX()==x && getY()==y);
    }

    public Map<Direction,Set<Square>> directions(int distance){

        Map<Direction,Set<Square>> allSquares = new EnumMap<>(Direction.class);
        Set<Square> tempSquare;

        for(Direction direction : Direction.values()){
            tempSquare = oneDirection(direction, distance);
            if(!tempSquare.isEmpty()){
                allSquares.put(direction, tempSquare);
            }
        }

        return allSquares;
    }

    public Square getOneOfNeighbour(int x, int y){
        Square square = null;
        for (Square s:neighbourSquare){
            if (s.getY() == y && s.getX() == x)
                square = s;
        }
        return square;
    }

    private Set<Square> oneDirection(Direction direction, int distance){

        Set<Square> squares = new HashSet<>();
        Square currentSquare = this;
        int currentX = x;
        int currentY = y;

        while (distance > 0){

            switch (direction){
                case TOP:
                    currentY--;
                    break;
                case DOWN:
                    currentY++;
                    break;
                case RIGHT:
                    currentX++;
                    break;
                case LEFT:
                    currentX--;
                    break;
            }

            currentSquare = currentSquare.getOneOfNeighbour(currentX, currentY);
            if(currentSquare != null){
                squares.add(currentSquare);
            }
            else {
                break;
            }
            distance--;
        }

        return squares;
    }

    public Map<Direction,Set<Square>> directionAbsolute(GameBoard gameBoard){

        Map<Direction,Set<Square>> allSquares = new EnumMap<>(Direction.class);
        Set<Square> tempSquare;

        for(Direction direction : Direction.values()){
            tempSquare = oneDirectionAbsolute(direction, gameBoard);
            if(!tempSquare.isEmpty()){
                allSquares.put(direction, tempSquare);
            }
        }

        return allSquares;
    }

    private Set<Square> oneDirectionAbsolute(Direction direction, GameBoard gameBoard){


        Set<Square> squares = new HashSet<>();
        Square currentSquare = this;
        int currentX = x;
        int currentY = y;

        while (currentSquare != null){

            switch (direction){
                case TOP:
                    currentY--;
                    break;
                case DOWN:
                    currentY++;
                    break;
                case RIGHT:
                    currentX++;
                    break;
                case LEFT:
                    currentX--;
                    break;
            }

            currentSquare = gameBoard.getSquare(currentX, currentY);
            if(currentSquare != null){
                squares.add(currentSquare);
            }

        }

        return squares;
    }

    @Override
    public String toString () {
        return "X: " + x + " Y: " + y + " Color: " + squareColor;
    }

    public enum Direction {
        //🠘 🠚 🠙 🠛
        //TOP("\uD83E\uDC19"), DOWN("\uD83E\uDC1B"),LEFT("\uD83E\uDC18"),RIGHT("\uD83E\uDC1A");
        TOP, DOWN, LEFT, RIGHT;

        @Override
        public String toString() {
            return super.toString();
        }


    }

}



