package model.player;
import model.board.Color;

import java.util.*;

/**
 * 
 */
public class Hero {

    private String name;
    private String description;

    /**
     * constructor
     */
    public Hero(String name,String description) {
        this.name=name;
        this.description=description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

}