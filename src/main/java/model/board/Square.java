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
    private int currentX;
    private int currentY;
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
    void addNextSquare(Square next){
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

    /**
     * Gives the squares that are one position away from this square
     * @return all the close squares
     */
    public List<Square> getNeighbourSquare(){
        return neighbourSquare;
    }

    /**
     * Gives the squares that are one position away from this square
     * @return all the ids of the close squares
     */
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

    /**
     * Gives the id of the current square
     * @return the identification number of the square
     */
    public int getId() {
        return id;
    }

    /**
     * Given a distance it returns the squares that have
     * this distance from the current square
     * @param maxDistance Distance from the square
     * @return All the squares with that distance
     */
    private Set<Square> getAllPositions(int maxDistance){
        Set<Square> positions = new LinkedHashSet<>();
        if(maxDistance != 0){
            neighbourSquare.forEach(squares-> positions.
                    addAll(squares.getValidPosition(maxDistance-1)));
        }
        else {
            positions.add(this);
        }

        return positions;
    }

    /**
     * The positions where the player can move
     * @param maxDistance the distance that the player can move
     * @return a set of squares where the player can move
     */
    public Set<Square> getValidPosition(int maxDistance){
        Set<Square> all = new LinkedHashSet<>();
        for(int i = 0; i <= maxDistance; i++){
            all.addAll(getAllPositions(i));
        }
        return all;
    }

    /**
     * Gives all the players on the square
     * @return a list with all the players
     */
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

    /**
     * Gives the all the squares in a given direction
     * @param distance the maximum distance from this square to direction square
     * @return a map with the direction and the the set of squares
     */
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

    /**
     * Gives a square with given coordinates that is one square away
     * @param x X coordinate of the desired square
     * @param y Y coordinate of the desired square
     * @return the desired square if there is one otherwise null
     */
    public Square getOneOfNeighbour(int x, int y){
        Square square = null;
        for (Square s:neighbourSquare){
            if (s.getY() == y && s.getX() == x)
                square = s;
        }
        return square;
    }

    /**
     * Gives all the squares in a given direction considering walls as limiters
     * @param direction where to look for squares
     * @param distance maximum distance to look for squares
     * @return all the squares if there are any
     */
    private Set<Square> oneDirection(Direction direction, int distance){

        Set<Square> squares = new HashSet<>();
        Square currentSquare = this;
        currentX = x;
        currentY = y;

        while (distance > 0){

            changeDirection(direction);

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

    /**
     * Gives all the squares with directions ignoring walls
     * @param gameBoard the current board where the players are playing
     * @return a map with the directions and the relative squares in that direction
     */
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

    /**
     * Gives all the squares in a given direction ignoring walls
     * @param direction considered from the current square
     * @param gameBoard where the players are playing
     * @return all the squares with the given direction
     */
    private Set<Square> oneDirectionAbsolute(Direction direction, GameBoard gameBoard){

        Set<Square> squares = new HashSet<>();
        Square currentSquare = this;
        currentX = x;
        currentY = y;

        while (currentSquare != null){

            changeDirection(direction);

            currentSquare = gameBoard.getSquare(currentX, currentY);
            if(currentSquare != null){
                squares.add(currentSquare);
            }

        }

        return squares;
    }

    /**
     * Changes the x and y current coordinates
     * @param direction the direction where to go
     */
    private void changeDirection(Direction direction){
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
    }

    /**
     * Allows to define directions that can be chosen
     */
    public enum Direction {
        //🠘 🠚 🠙 🠛
        TOP("\uD83E\uDC19"), DOWN("\uD83E\uDC1B"),LEFT("\uD83E\uDC18"),RIGHT("\uD83E\uDC1A");
        private String info;

        /**
         * Gives the arrow of the direction
         * @param info the arrow of the direction
         */
        Direction(String info){
            this.info = info;
        }

        /**
         * Allows to see the correct arrow
         * @return a string of the arrow
         */
        @Override
        public String toString() {
            return info;
        }


    }

}



