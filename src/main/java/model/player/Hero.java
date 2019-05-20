package model.player;
import model.board.Color;

import java.util.*;

/**
 * 
 */
public class Hero {

    private String name;
    private String description;
    private Color color;

    /**
     * Default constructor
     */
    public Hero(String name,String description, Color color) {
        this.name=name;
        this.description=description;
        this.color = color;
    }


    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }
}

