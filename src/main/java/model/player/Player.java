package model.player;

import model.board.Color;
import model.card.AmmoColor;
import model.board.Square;
import model.card.Weapon;
import model.card.Powerup;
import model.exceptions.*;
import java.util.*;

/**
 *
 */
public class Player {

    private String nickname;
    private Hero hero;
    private Color color;
    private Square position;
    private PlayerBoard playerBoard;
    private Map<AmmoColor,Integer> ammo;
    private List<Weapon> weapons;
    private transient List<Powerup> powerups;
    private ActionState actionStatus;
    private Boolean live;

    public Player(String nickname) {
        this.nickname = nickname;
        ammo = new EnumMap<>(AmmoColor.class);
        for (AmmoColor c : AmmoColor.values()) {
            ammo.put(c, 1);
        }
        powerups=new ArrayList<>();
        weapons=new ArrayList<>();
        playerBoard=new PlayerBoard();
        actionStatus = new TurnActions(this);

    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    private void setPosition(Square position) {
        this.position = position;
    }

    public void setPlayerBoard(PlayerBoard board) {
        this.playerBoard = board;
    }

    public void setActionStatus(ActionState actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getNickname() {
        return nickname;
    }

    public Hero getHero() {
        return hero;
    }

    public Color getColor(){ return color;}

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
        return Collections.unmodifiableList(weapons);
    }

    public ActionState getActionStatus() {
        return actionStatus;
    }

    public void movePlayer(Square square){
        if (!square.equals(getPosition())) {
            this.getPosition().removePlayer(this);
            square.addPlayer(this);
            this.setPosition(square);
        }
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
            throw new AmmoException("error! you haven't enough ammo"+ammoColor);
    }

    public void addPowerup(Powerup powerup){
        powerups.add(powerup);
    }

    public void removePowerup(int i){
        powerups.remove(i);
    }
    public void addWeapon(Weapon weapon){
        weapons.add(weapon);
    }

    public List<Powerup> getPowerups(){
        return this.powerups;
    }

    /**
     * @return true if player has less then 3 weapons
     */
    public Boolean limitWeapon(){
            return (weapons.size()<3);
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
     * @param ammoColor the color of ammo request
     * @return true if play has at least one powerup is same as that request, else no.
     */
    public boolean usePowerupAsAmmo(AmmoColor ammoColor){
        return powerupAsAmmo().contains(ammoColor);
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




}