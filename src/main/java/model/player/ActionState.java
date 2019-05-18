package model.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActionState {

    private List<String> options;
    private Player player;
    //TURNACTIONS ,ADRENALINEACTIONS1,ADRENALINEACTIONS2,FRENETICACTIONS1,FRENETICACTIONS2

    public ActionState(ActionOption x, ActionOption y, ActionOption z, Player player){
        options = Arrays.asList(x.toString(), y.toString(), z.toString());
        this.player = player;
    }
    public List<String> getChoices(){
        if(player.getWeapons().isEmpty()){
            List<String> noWeaponOption = new ArrayList<>();
            noWeaponOption.addAll(options);
            //remove all possible shoot options
            noWeaponOption.remove(ActionOption.SHOOT.toString());
            return noWeaponOption;
        }
        return options;
    }

}

class TurnActions extends ActionState{
    public TurnActions(Player player){
        super(ActionOption.MOVE3, ActionOption.MOVE_GRAB, ActionOption.SHOOT, player);
    }
}


