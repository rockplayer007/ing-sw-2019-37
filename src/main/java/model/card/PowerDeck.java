package model.card;
import java.util.*;

public class PowerDeck extends Deck<Powerup>{


    /**
     * Constructor
     */
    public PowerDeck(){
        createPowerups();
        mixDeck();
    }

    private void createPowerups(){
        String targetingScopeDes ="You may play this card when you are dealing damage to one or more targets. Pay 1 ammo" +
                "cube of any color. Choose 1 of those targets and give it an extra point of damage. Note: You" +
                "cannot use this to do 1 damage to a target that is receiving only marks." ;
        List<Operation> operations = Arrays.asList(new TargetingScope(),new SelectTargets(1, true), new Damage(1));
        Effect targetingScope= new Effect("","",Collections.emptyList(), operations);

        String newtonDes = "You may play this card on your turn before or after any action. Choose any other player's" +
                "figure and move it 1 or 2 squares in one direction. (You can't use this to move a figure" +
                "after it respawns at the end of your turn. That would be too late.)";
        operations = Arrays.asList(new AllPossibleTargets(),new SelectTargets(1, true), new Repel(2));
        Effect newton= new Effect("","",Collections.emptyList(), operations);

        String tagbackGrenadeDes = "You may play this card when you receive damage from a player you can see. Give that player 1 mark.";

        String teleporterDes = "You may play this card on your turn before or after any action. Pick up your figure and" +
                "set it down on any square of the board. (You can't use this after you see where someone " +
                "respawns at the end of your turn. By then it is too late.)";
        operations = Collections.singletonList(new Teleporter());
        Effect teleporter= new Effect("","",Collections.emptyList(), operations);

        for (AmmoColor color:AmmoColor.values()){
            for (int i=0; i<2; i++){
                addCard(new Powerup("TARGETING SCOPE",targetingScopeDes,targetingScope,color));
                addCard(new Powerup("NEWTON",newtonDes,newton,color));
                addCard(new Powerup("TAGBACK GRENADE",tagbackGrenadeDes,null,color));
                addCard(new Powerup("TELEPORTER",teleporterDes,teleporter,color));

            }
        }
    }

}
