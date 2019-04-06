package model.card;

import java.util.ArrayList;

public class PowerDeck {

    private ArrayList<Powerup> powerups;

    public PowerDeck(){
        powerups = new ArrayList<Powerup>();
        ArrayList<String> na= new ArrayList<String>();
        na.add("Mirino");
        na.add("Raggio cinetico");
        na.add("Granata Venom");
        na.add("Teletrasporto");
        String des = "...";
        for(int i=0;i<2;i++) {
            for (int j = 0; j <na.size(); j++) {
                powerups.add(new Powerup(na.get(j), des, AmmoColor.red, j + 1));
                powerups.add(new Powerup(na.get(j), des, AmmoColor.blue, j + 1));
                powerups.add(new Powerup(na.get(j), des, AmmoColor.yellow, j + 1));
            }
        }
    }
        public Powerup getPowerup () { //ritorna una carta Powerup dal mazzo e la rimuove
            Powerup powerup;
            powerup = powerups.get(0);
            powerups.remove(0);
            return powerup;
        }



}
