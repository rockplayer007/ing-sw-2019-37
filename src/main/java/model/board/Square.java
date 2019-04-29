package model.board;

import model.player.Player;

import java.util.*;

/**
 * Class that defines a single square of the board
 */
public class Square {

    private Color squareColor;
    private int id;
    private int x;
    private int y;
    private List<Square> neighbourSquare = new ArrayList<>();
    private List<Player> playersOnSquare = new ArrayList<>();
    private boolean generationPoint;

    public Square() {    }
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
    public boolean getGenerationPoint(){
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
     * Gives squares that are visible from a given square
     * The rules say that enemies are visible from a square if:
     * "The player is in the same room as you" and
     * "If your square has a door, you can also see any player on
     * any square in the room on the other side of the door"
     * @return a set of squares visible from the current square
     */
    public Set<Square> visibleSquare(Board.BoardMap map){
        Set<Square> positions = new HashSet<>();

        /*
        //adds to the visible positions the squares that are in the same room
        positions.addAll(map.squaresInRoom.get(squareColor));

        neighbourSquare.stream().filter(square -> square.getColor() != squareColor)
                .forEach(x-> positions.addAll(map.squaresInRoom.get(x.squareColor)));
        */
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
        if (getX()==x && getY()==y)
            return true;
        else
            return false;
    }

    public Map<String,Set<Square>> directions(int distance){
        Map<String,Set<Square>> map=new HashMap<>();
        for (Square s:neighbourSquare){
            if (s.getY()<this.getY())
                map.put("Down",this.oneDirection(Direction.DOWN,distance));
            else if (s.getY()>this.getY())
                map.put("Top",this.oneDirection(Direction.TOP,distance));
            else if (s.getX()>this.getX())
                map.put("Right",this.oneDirection(Direction.RIGHT,distance));
            else if (s.getX()<this.getX())
                map.put("Left",this.oneDirection(Direction.LEFT,distance));
        }
        return map;
    }

    public Square getOneofNeighbour(int x,int y){
        Square square=null;
        for (Square s:neighbourSquare){
            if (s.getY()==y&&s.getX()==x)
                square=s;
        }
        return square;
    }

    public Set<Square> oneDirection(Direction direction, int distance){
        Set<Square> set = new HashSet<>();
        if (distance>0) {
            if (direction == Direction.LEFT&&x!=0&&neighbourSquare.stream().anyMatch(s->s.getX()<this.getX()))
                set.addAll(getOneofNeighbour(x-1,y).oneDirection(direction,distance-1));
            else if (direction == Direction.RIGHT&&x!=3&&neighbourSquare.stream().anyMatch(s->s.getX()>this.getX()))
                set.addAll(getOneofNeighbour(x+1,y).oneDirection(direction,distance-1));
            else if (direction == Direction.DOWN&&y!=0&&neighbourSquare.stream().anyMatch(s->s.getY()<this.getY()))
                set.addAll(getOneofNeighbour(x,y-1).oneDirection(direction,distance-1));
            else if (direction == Direction.TOP&&y!=2&&neighbourSquare.stream().anyMatch(s->s.getY()>this.getY()))
                set.addAll(getOneofNeighbour(x,y+1).oneDirection(direction,distance-1));
        }
        set.add(this);
        return set;
    }

    public Map<String,Set<Square>> directionAbsolute(Board.BoardMap boardMap){
        Map<String,Set<Square>> map=new HashMap<>();
        if (x!=0)
            map.put("Left",oneDirectionAbsolute(Direction.LEFT,boardMap));
        if (x!=3)
            map.put("Right",oneDirectionAbsolute(Direction.RIGHT,boardMap));
        if (y!=0)
            map.put("Top",oneDirectionAbsolute(Direction.TOP,boardMap));
        if (y!=2)
            map.put("Down",oneDirectionAbsolute(Direction.DOWN,boardMap));


        return  map;
    }


    public Set<Square> oneDirectionAbsolute(Direction direction,Board.BoardMap boardMap){
        Set<Square> set = new HashSet<>();
        if (direction == Direction.LEFT&x!=0)
            set.addAll(boardMap.getSquare(x-1,y).oneDirectionAbsolute(direction,boardMap));
        else if (direction == Direction.RIGHT&&x!=3)
            set.addAll(boardMap.getSquare(x+1,y).oneDirectionAbsolute(direction,boardMap));
        else if (direction == Direction.DOWN&&y!=0)
            set.addAll(boardMap.getSquare(x,y-1).oneDirectionAbsolute(direction,boardMap));
        else if (direction == Direction.TOP&&y!=2)
            set.addAll(boardMap.getSquare(x,y+1).oneDirectionAbsolute(direction,boardMap));
        set.add(this);
        return set;
    }


}


enum Direction {
    TOP,DOWN,LEFT,RIGHT
}
