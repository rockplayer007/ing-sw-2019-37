package model.card;

import java.io.Serializable;

public abstract class Card {
    private String name;
    private String description;

    public Card (String name, String description){
        this.name=name;
        this.description=description;
    }

    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
}