package model.card;

public class Weapon extends Card {
    private Boolean charged;


    public Boolean getCharged(){
        return charged;
    }

    public void setCharged(Boolean state){
        charged=state;
    }
}
