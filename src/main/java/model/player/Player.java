package model.player;

import model.card.AmmoColor;
import model.board.Square;
import model.card.Weapon;
import model.card.Card;
import model.card.Powerup;
import java.util.*;

/**
 *
 */
public class Player {

    private String nickname;
    private Hero hero;
    private Square position;
    private PlayerBoard board;
    private Map<AmmoColor,Integer> ammo;
    private List<Weapon> weapons;
    private List<Powerup> powerups;
    private Actions actionStatus;

    public Player(String nickname) {
        this.nickname = nickname;
        ammo = new HashMap<>();
        for (AmmoColor c : AmmoColor.values()) {
            ammo.put(c, 0);
        }

    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public void setPosition(Square position) {
        this.position = position;
    }

    public void setBoard(PlayerBoard board) {
        this.board = board;
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

    public PlayerBoard getBoard() {
        return board;
    }

    public  Map<AmmoColor,Integer> getAmmo() {
        return ammo;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public Actions getActionStatus() {
        return actionStatus;
    }

    void addAmmo(AmmoColor ammoColor){
        if (ammo.get(ammoColor)<3)
            ammo.put(ammoColor,ammo.get(ammoColor)+1);
    }
    void addPowerup(Powerup powerup){
        powerups.add(powerup);
    }
    void addWeapon(Weapon weapon){
        weapons.add(weapon);
    }

    public List<Powerup> getPowerups(){
        return this.powerups;
    }

    public Boolean limitWeapon(){
        if (weapons.size()<3)
            return false;
        else
            return true;
    }
}