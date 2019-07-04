package model.card;


import java.io.Serializable;

public abstract class Card implements Serializable {
    private String name;
    private String description;

    /**
     * Default constructor
     */
    public Card(){
        name = "";
        description = "";
    }

    /**
     * @param name name of card
     * @param description description of card
     */
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

    public void setDescription(String description) {
        this.description = description;
    }
}