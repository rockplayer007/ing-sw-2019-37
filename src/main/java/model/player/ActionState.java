package model.player;

import com.google.gson.Gson;
import model.card.Weapon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum  ActionState {

    TURNACTIONS(Arrays.asList(ActionOption.MOVE3, ActionOption.MOVE1_GRAB, ActionOption.SHOOT)),
    ADRENALINEACTIONS1(Arrays.asList(ActionOption.MOVE3, ActionOption.MOVE2_GRAB, ActionOption.SHOOT)),
    ADRENALINEACTIONS2(Arrays.asList(ActionOption.MOVE3, ActionOption.MOVE2_GRAB, ActionOption.MOVE1_SHOOT)),
    FRENETICACTIONS1(Arrays.asList(ActionOption.MOVE1_RELOAD_SHOOT, ActionOption.MOVE4, ActionOption.MOVE2_GRAB)),
    FRENETICACTIONS2(Arrays.asList(ActionOption.MOVE2_RELOAD_SHOOT, ActionOption.MOVE3_GRAB));


    private List<ActionOption> options;


    ActionState(List<ActionOption> options){
        this.options = options;

    }

    /**
     * Get choices that the player can do
     * @param player player that want to use any actions
     * @param dontShoot if the player can do shoot action or no
     * @return the actions that the player can do
     */
    public List<ActionOption> getChoices(Player player, boolean dontShoot){

        List<Weapon> possibleWeapons = player.getWeapons().stream()
                .filter(Weapon::getCharged).collect(Collectors.toList());

        if(possibleWeapons.isEmpty() || dontShoot){
            List<ActionOption> noWeaponOption = new ArrayList<>(options);

            //remove all possible shoot options when the player doesnt have weapons
            noWeaponOption.remove(ActionOption.SHOOT);
            noWeaponOption.remove(ActionOption.MOVE1_SHOOT);
            return noWeaponOption;
        }
        return options;
    }
    public List<String> getJsonChoices(Player player, boolean dontShoot){
        List<ActionOption> list = getChoices(player, dontShoot);
        List<String> stringed = new ArrayList<>();
        Gson gson = new Gson();
        list.forEach(x -> stringed.add(gson.toJson(x)));
        return stringed;
    }


}



