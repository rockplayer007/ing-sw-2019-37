package model.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeroGenerator{

    private List<Heroes> heroes = new ArrayList<>();

    public HeroGenerator(){
        fillList();
    }

    private void fillList(){
            heroes.addAll(Arrays.asList(Heroes.values()));
    }

    public Heroes getHero(){

        if(heroes.isEmpty()){
            fillList();
        }
        return heroes.remove(0);
    }

}
