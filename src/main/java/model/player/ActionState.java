package model.player;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum  ActionState {

    TURNACTIONS(Arrays.asList(ActionOption.MOVE3, ActionOption.MOVE1_GRAB, ActionOption.SHOOT));
//    ,ADRENALINEACTIONS1,ADRENALINEACTIONS2,FRENETICACTIONS1,FRENETICACTIONS2;

    private List<ActionOption> options;


    ActionState(List<ActionOption> options){
        this.options = options;

    }
    public List<ActionOption> getChoices(Player player){
        if(player.getWeapons().isEmpty()){
            List<ActionOption> noWeaponOption = new ArrayList<>(options);

            //remove all possible shoot options when the player doesnt have weapons
            noWeaponOption.remove(ActionOption.SHOOT);
            noWeaponOption.remove(ActionOption.MOVE1_RELOAD_SHOOT);
            noWeaponOption.remove(ActionOption.MOVE2_RELOAD_SHOOT);
            noWeaponOption.remove(ActionOption.MOVE1_SHOOT);
            return noWeaponOption;
        }
        return options;
    }
    public List<String> getJsonChoices(Player player){
        List<ActionOption> list = getChoices(player);
        List<String> stringed = new ArrayList<>();
        Gson gson = new Gson();
        list.forEach(x -> stringed.add(gson.toJson(x)));
        return stringed;
    }


}



