package model.board;

import java.util.ArrayList;

public abstract class Square {

    private Color squareColor;
    private int x;

    private int y;
    private ArrayList<Square> nextSquare = new ArrayList<Square>();
    private String type;

    public Square(Color color, String type, int x, int y ){
        this.squareColor = color;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public void addNextSquare(Square next){
        nextSquare.add(next);
    }
    public Color getColor(){
        return squareColor;
    }
    public String getType(){
        return type;
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
}
