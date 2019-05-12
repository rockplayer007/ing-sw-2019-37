package model.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameBoard {

    private List<GenerationSquare> genPoints = new ArrayList<>();
    private Map<Integer,Square> allSquares = new HashMap<>();
    private Map<Color, ArrayList<Square>> squaresInRoom = new HashMap<>();
    private int id;
    private String description;

    public GameBoard(List<GenerationSquare> genPoints, Map<Integer,
            Square> allSquares, Map<Color, ArrayList<Square>> squaresInRoom, int id, String description){
        this.genPoints = genPoints;
        this.allSquares = allSquares;
        this.squaresInRoom = squaresInRoom;
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Square getGenerationPoint(Color color){
        for(Square gen: genPoints){
            if(color.equals(gen.getColor())){
                return gen;
            }
        }
        return null;
    }

    /**
     * Gives the square with the specified id
     * @param id Id of the square
     * @return The square with that id
     */
    public Square getSquare(int id){
        return allSquares.get(id);
    }

    public Map<Color, ArrayList<Square>> getSquaresInRoom(){
        return squaresInRoom;
    }

    public Square getSquare(int x,int y){

        return  allSquares.entrySet().stream()
                .filter(a -> x == (a.getValue().getX()) && y == (a.getValue().getY()))
                .findAny().map(Map.Entry::getValue)
                .orElse(null);
    }

}
