package model.player;

import model.board.Color;
import model.card.AmmoColor;
import model.board.Square;
import model.card.Weapon;
import model.card.Powerup;
import model.exceptions.*;

import java.io.Serializable;
import java.util.*;

/**
 *
 */
public class Player implements Serializable {

    private String nickname;
    private Heroes hero;
    private transient Square position;
    private PlayerBoard playerBoard;
    private Map<AmmoColor,Integer> ammo;
    private List<Weapon> weapons;
    private transient List<Powerup> powerups;
    private ActionState actionStatus;
    private RoundStatus roundStatus;
    private boolean live;
    private boolean connected;

    public Player(String nickname) {
        this.nickname = nickname;
        ammo = new EnumMap<>(AmmoColor.class);
        for (AmmoColor c : AmmoColor.values()) {
            ammo.put(c, 1);
        }
        powerups=new ArrayList<>();
        weapons=new ArrayList<>();
        playerBoard=new PlayerBoard(this);
        actionStatus = ActionState.TURNACTIONS;
        position = null;
        live = true;
        this.roundStatus = RoundStatus.FIRST_ROUND;
        connected = true;


    }

    private void setPosition(Square position) {
        this.position = position;
    }

    public void setActionStatus(ActionState actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getNickname() {
        return nickname;
    }

    public Heroes getHero() {
        return hero;
    }

    public void setHero(Heroes hero){
        this.hero = hero;
    }

    public Color getColor(){ return hero.getColor();}

    public Square getPosition() {
        return position;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public  Map<AmmoColor,Integer> getAmmo() {
        return ammo;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public ActionState getActionStatus() {
        return actionStatus;
    }

    public RoundStatus getRoundStatus(){
        return roundStatus;
    }

    public void setLive(boolean live) {
        this.live = live;

        if (!live) {
            position.getPlayersOnSquare().remove(this);
            position = null;
            if (actionStatus!=ActionState.FRENETICACTIONS1&&actionStatus!=ActionState.FRENETICACTIONS2)
                actionStatus=ActionState.TURNACTIONS;
        }


    }

    public boolean isLive() {
        return live;
    }

    public void setNextRoundstatus(){

        if(roundStatus == RoundStatus.FIRST_ROUND){
            roundStatus = RoundStatus.NORMAL_ROUND;
        }

    }

    public void setConnected(){
        connected = true;
    }

    public void setDisconnected(){
        connected = false;
    }

    public boolean isConnected(){
        return connected;
    }

    public void movePlayer(Square square){
        if(position == null){
            square.addPlayer(this);
            setPosition(square);
        }
        else if (!square.equals(position)) {
            position.removePlayer(this);
            square.addPlayer(this);
            setPosition(square);
        }
        //else dont move if the player didnt change position
    }

    public void addAmmo(AmmoColor ammoColor){
        if (ammo.get(ammoColor)<3)
            ammo.put(ammoColor, ammo.get(ammoColor)+1);

    }

    /**
     * @param ammoColor color of ammo to be removed (spend).
     * @throws AmmoException if the player hasn't enough ammo
     */
    public void removeAmmo(AmmoColor ammoColor)throws AmmoException{
        if (ammo.get(ammoColor)>0)
            ammo.put(ammoColor, ammo.get(ammoColor)-1);
        else
            throw new AmmoException("you haven't enough ammo"+ammoColor);
    }

    public void addPowerup(Powerup powerup){
        powerups.add(powerup);
    }

    public void removePowerup(Powerup powerup){
        powerups.remove(powerup);
    }

    public void addWeapon(Weapon weapon){
        weapons.add(weapon);
    }

    public List<Powerup> getPowerups(){
        return this.powerups;
    }

    /**
     * @return true if player has more than 3 weapons
     */
    public Boolean limitWeapon(){
            return (weapons.size()>=3);
    }

    /**
     * @return a list of Ammocolor same as that player's powerups color
     */
    public List<AmmoColor> powerupAsAmmo(){
        ArrayList<AmmoColor> bullets=new ArrayList<>();
        powerups.forEach(i->bullets.add(i.getAmmo()));
        return bullets;
    }

    /**
     * @return a map contain all ammos of player
     */
    public Map<AmmoColor,Integer> allAmmo(){
        Map<AmmoColor,Integer> bullets=new EnumMap<>(AmmoColor.class);
        bullets.putAll(ammo);
        powerups.forEach(i->bullets.put(i.getAmmo(), bullets.get(i.getAmmo())+1));
        return bullets;
    }

    /**
     * @param cost is the cost that the player need to be incurred
     * @return true if the player can pay that cost, else no.
     */
    public Boolean enoughAmmos(List<AmmoColor> cost,Boolean allAmmos){
        Map<AmmoColor,Integer> bullets;
        if (cost.isEmpty())
            return true;
        if (allAmmos)
            bullets = allAmmo();
        else
            bullets = new EnumMap<>(ammo);

        for (AmmoColor c:cost){
            if (bullets.get(c)>0)
                bullets.put(c,bullets.get(c)-1);
            else
                return false;
        }
        return true;
    }


    public enum RoundStatus{
    FIRST_ROUND, NORMAL_ROUND;
    }


}