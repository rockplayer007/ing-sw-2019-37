package model.board;

import model.player.Player;

import java.io.Serializable;
import java.util.*;

/**
 * Class for managing the generated board where the players will play
 */
public class GameBoard implements Serializable {

    private List<GenerationSquare> genPoints;
    private Map<Integer,Square> allSquares;
    private Map<Color, ArrayList<Square>> squaresInRoom;
    private int id;
    private String description;

    /**
     * Constructor will keep track of all the squares, description and the id
     * @param genPoints the three generation points
     * @param allSquares the squares in the board
     * @param squaresInRoom a set of squares in one room
     * @param id the identification number of the board
     * @param description description of the board
     */
    public GameBoard(List<GenerationSquare> genPoints, Map<Integer,
            Square> allSquares, Map<Color, ArrayList<Square>> squaresInRoom, int id, String description){
        this.genPoints = genPoints;
        this.allSquares = allSquares;
        this.squaresInRoom = squaresInRoom;
        this.id = id;
        this.description = description;
    }

    /**
     * Gives the identification of the board
     * @return the id of the board
     */
    public int getId() {
        return id;
    }

    /**
     * Gives the description of the board
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gives the generation point with a given color
     * @param color the color of the square to get the generation point
     * @return the required generation point
     */
    public Square getGenerationPoint(Color color){
        for(Square gen: genPoints){
            if(color.equals(gen.getColor())){
                return gen;
            }
        }
        return null;
    }

    /**
     * All the generation points of the board
     * @return 3 squares
     */
    public List<GenerationSquare> getGenPoints() {
        return genPoints;
    }

    /**
     * Gives the square with the specified id
     * @param id Id of the square
     * @return The square with that id
     */
    public Square getSquare(int id){
        if(id < 0 || id > allSquares.size() - 1){
            return null;
        }
        else {
            return allSquares.get(id);
        }

    }

    /**
     * gives all the squares in all the rooms
     * @return a map with the color of the room and the squares in that room
     */
    public Map<Color, ArrayList<Square>> getSquaresInRoom(){
        return squaresInRoom;
    }

    /**
     * Gives a square given two coordinates
     * @param x coordinate X of the square
     * @param y coordinate Y of the square
     * @return the required square
     */
    public Square getSquare(int x,int y){

        return  allSquares.entrySet().stream()
                .filter(a -> x == (a.getValue().getX()) && y == (a.getValue().getY()))
                .findAny().map(Map.Entry::getValue)
                .orElse(null);
    }

    /**
     * @return a list of all square in the Map
     */
    public Set<Square> allSquares(){
        return new LinkedHashSet<>(allSquares.values());
    }

    /**
     * Gives all the players that are on the playing board
     * @return a list of the players
     */
    public List<Player> getPlayersOnMap(){
        List<Player> allPlayers = new ArrayList<>();
        allSquares.values().forEach(x -> allPlayers.addAll(x.getPlayersOnSquare()));
        return allPlayers;
    }

}
