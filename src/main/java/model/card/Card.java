package model.card;

public abstract class Card {
    public String name;
    public String description;

    public Card() {
    }
    public Card (String name, String description){
        this.name=name;
        this.description=description;
    }



    public String getname(){
        return name;
    }
    public String getDescription(){
        return description;
    }
}