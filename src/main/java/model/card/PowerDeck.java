package model.card;

import java.util.ArrayList;

public class PowerDeck implements Deck{

    private ArrayList<Powerup> powerups;

    public PowerDeck(){}

    public void CreateDeck(){
        powerups = new ArrayList<Powerup>();
        ArrayList<String> na= new ArrayList<String>();
        na.add("Mirino");
        na.add("Raggio cinetico");
        na.add("Granata Venom");
        na.add("Teletrasporto");
        String des = "...";
        for(int i=0;i<2;i++) {
            for (int j = 0; j < na.size(); j++) {
                powerups.add(new Powerup(na.get(j), des, AmmoColor.RED, j + 1));
                powerups.add(new Powerup(na.get(j), des, AmmoColor.BLUE, j + 1));
                powerups.add(new Powerup(na.get(j), des, AmmoColor.YELLOW, j + 1));
            }
        }
    }
        public Powerup getPowerup () {
            Powerup powerup;
            powerup = powerups.get(0);
            powerups.remove(0);
            return powerup;
        }

        public Card getCard(){ //returns a Powerup from the deck and removes it
            Powerup powerup;
            powerup = powerups.get(0);
            powerups.remove(0);
            return powerup;
        }



}
