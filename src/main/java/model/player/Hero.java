package model.player;
import java.util.*;

/**
 * 
 */
public class Hero {

    private String name;
    private String description;

    /**
     * Default constructor
     */
    public Hero(String name,String description) {
        this.name=name;
        this.description=description;
    }


    public String getName() {
        return name;
    }
}