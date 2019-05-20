package model.player;

import model.board.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeroGenerator{

    private List<Hero> heroes = new ArrayList<>();

    public HeroGenerator(){
        fillList();
    }

    private void fillList(){
        heroes.add(new Hero(":D-STRUCT-0R", "favorite beverage: 5W-30 hobbies: tennis, bowling, sheet-metal origami favorite board game: Robo Rally beloved pet: a cordless drill",
                Color.YELLOW));

        heroes.add(new Hero("BANSHEE", "home planet: unknown guilty pleasure: karaoke perfect date:  a long walk by the ocean –  or in the ocean siblings: 900 sisters all the same age",
                Color.GREEN));

        heroes.add(new Hero("DOZER", "background: paramilitary covert ops specialty: hurting people other interests: breaking stuff testosterone level: high",
                Color.RED));

        heroes.add(new Hero("VIOLET", "profession: shooting instructor nail polish: always perfect favorite snack: chips and salsa favorite weapon: anything that goes Boom!",
                Color.PURPLE));

        heroes.add(new Hero("SPROG", "origins: claims to be from Texas disposition: surly turn-ons: crickets, flat rocks, and heat lamps turn-offs:  lotion commercials that say \"dry, scaly skin\" like it's a bad thing",
                Color.GREEN));

    }

    public Hero getHero(){

        if(heroes.isEmpty()){
            fillList();
        }
        return heroes.remove(0);
    }


}
