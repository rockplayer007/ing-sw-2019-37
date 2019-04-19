package model.player;

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
    private Square position;
    private PlayerBoard playerBoard;
    private Map<AmmoColor,Integer> ammos;
    private List<Weapon> weapons;
    private List<Powerup> powerups;
    private Actions actionStatus;

    public Player(String nickname) {
        this.nickname = nickname;
        ammos = new EnumMap<>(AmmoColor.class);
        for (AmmoColor c : AmmoColor.values()) {
            ammos.put(c, 0);
        }

    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public void setPosition(Square position) {
        this.position = position;
    }

    public void setPlayerBoard(PlayerBoard board) {
        this.playerBoard = board;
    }

    public void setActionStatus(Actions actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getNickname() {
        return nickname;
    }

    public Hero getHero() {
        return hero;
    }

    public Square getPosition() {
        return position;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public  Map<AmmoColor,Integer> getAmmos() {
        return ammos;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public Actions getActionStatus() {
        return actionStatus;
    }

    public void addAmmo(AmmoColor ammoColor){
        if (ammos.get(ammoColor)<3)
            ammos.put(ammoColor, ammos.get(ammoColor)+1);

    }

    public void removeAmmo(AmmoColor ammoColor)throws AmmoException{
        if (ammos.get(ammoColor)>0)
            ammos.put(ammoColor, ammos.get(ammoColor)-1);
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

    public Boolean limitWeapon(){
            return (weapons.size()<3);

    }

    public List<AmmoColor> powerupAsAmmo(){
        ArrayList<AmmoColor> bullets=new ArrayList<>();
        powerups.forEach(i->bullets.add(i.getAmmoColor()));
        return bullets;
    }

    public boolean usePowerupAsAmmo(AmmoColor ammoColor){
        return powerupAsAmmo().contains(ammoColor);
    }

    public Map<AmmoColor,Integer> allAmmos(){
        Map<AmmoColor,Integer> bullets=new EnumMap<>(AmmoColor.class);
        bullets.putAll(getAmmos());
        powerups.forEach(i->bullets.put(i.getAmmoColor(), bullets.get(i.getAmmoColor())+1));
        return bullets;
    }

    public Boolean enoughAmmos(List<AmmoColor> cost){
        Map<AmmoColor,Integer> bullets=allAmmos();
        for (AmmoColor c:cost){
            if (bullets.get(c)>0)
                bullets.put(c,bullets.get(c)-1);
            else
                return false;
        }
        return true;
    }


}