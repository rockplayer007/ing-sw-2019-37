package model.card;

import java.util.ArrayList;
import java.util.List;
import model.player.*;
import model.gameHandler.Room;

interface Effect  {
    void applyOn(Player p, Room room);
}

public class Weapon extends Card {
    private Boolean charged;
    private List<AmmoColor> buyCost=new ArrayList<>();
    private List<AmmoColor> chargeCost=new ArrayList<>();
    private List<Effect> effects = new ArrayList<>();
    private List<Effect> alternativeeffects = new ArrayList<>();
    private int minDist;
    private int maxDist;
    private int minMove;
    private int maxMove;
    private boolean sameRoom;
    private boolean seeBefore;
    private boolean seeAfter;

    public Weapon(String name, String description,int minDist, int maxDist, int minMove, int maxMove, boolean seeBefore, boolean seeAfter){
        super(name,description);
        this.minDist=minDist;
        this.maxDist=maxDist;
        this.minMove=minMove;
        this.maxMove=maxMove;
        //this.sameRoom=sameRoom;
        this.seeBefore=seeBefore;
        this.seeAfter=seeAfter;

    //remove ammo red for additional / alternative effects
        Effect removeRed = (p,room) -> {
            p.removeAmmo(AmmoColor.RED);
        };

        //remove ammo yellow for additional / alternative effects
        Effect removeYellow = (p,room) -> {
            p.removeAmmo(AmmoColor.YELLOW);
        };

        //remove ammo blue for additional / alternative effects
        Effect removeBlue = (p,room) -> {
            p.removeAmmo(AmmoColor.BLUE);
        };

         //change the list of visible targets
        Effect visibletarget = (p,room) -> {
           room.VisibleTarget(minDist,maxDist);
        };

         // select target from the list
        Effect selectTarget = (p,room) -> {
            room.selectTarget();
        };

        //damage the selected target
        Effect damage = (p,room) -> {
           // room.giveDamage();
        };

         //mark the selected target
        Effect mark = (p,room) -> {
            // room.giveMark();
        };

        //remove target from the target list
        Effect removeTarget = (p,room) -> {
             room.removeTarget();
        };

        //add target to the list of those who received damage
        Effect addTargted = (p,room) -> {
            room.addTargetedList();
        };

         //select target from the targeted list
        Effect selectTargted = (p,room) -> {
             room.selectTargeted();
        };

         //move player
        Effect movePlayer = (p,room) -> {
            // p.move(minMove,maxMove);
        };

         //move target
        Effect moveTarget = (p,room) -> {
           // room.moveTarget(minMove,maxMove,seeBefore,seeAfter);
        };

         //get list of all targets for attack without visibility
        Effect invisibleTarget = (p,room) -> {
             //room.getInvisibleTarget();
        };
        
        Effect selectTargetOnSameSquare = (p,room) -> {
            room.selectTargetOnSameSquare();
        };



        //only for test purposes then they will be added by reading the weapons file

        if (name=="Whisper"){
            effects.add(visibletarget);
            effects.add(selectTarget);
            effects.add(damage);
            effects.add(damage);
            effects.add(damage);
            effects.add(mark);
        }

        if (name=="Hellion"){
            effects.add(visibletarget);
            effects.add(selectTarget);
            effects.add(damage);
            effects.add(mark);
            effects.add(selectTargetOnSameSquare);
            effects.add(mark);
            effects.add(selectTargetOnSameSquare);
            effects.add(mark);
            effects.add(selectTargetOnSameSquare);
            effects.add(mark);

            alternativeeffects.add(removeRed);
            alternativeeffects.add(visibletarget);
            alternativeeffects.add(selectTarget);
            alternativeeffects.add(damage);
            alternativeeffects.add(mark);
            alternativeeffects.add(mark);
            alternativeeffects.add(selectTargetOnSameSquare);
            alternativeeffects.add(mark);
            alternativeeffects.add(mark);
            alternativeeffects.add(selectTargetOnSameSquare);
            alternativeeffects.add(mark);
            alternativeeffects.add(mark);
            alternativeeffects.add(selectTargetOnSameSquare);
            alternativeeffects.add(mark);
            alternativeeffects.add(mark);
        }


    }



    public Boolean getCharged(){
        return charged;
    }

    public void setCharged(Boolean state){
        charged=state;
    }

    public List<AmmoColor> getBuyCost() {
        return buyCost;
    }

    public ArrayList<AmmoColor> getChargeCost() {
        ArrayList<AmmoColor>cost=new ArrayList<>();
        cost.addAll(chargeCost);
        cost.addAll(buyCost);
        return cost;
    }
    public List<Effect> getEffects(){
        return  effects;
    }
    public List<Effect> getAlternativeEffects(){
        return  alternativeeffects;
    }
}
