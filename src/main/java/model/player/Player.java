package model.player;

import model.board.Board;
import model.board.Square;
import model.card.Powerup;
import model.card.AmmoColor;
import model.card.Weapon;

import java.util.*;

/**
 *
 */
public class Player {

    private String nickname;
    private Hero hero;
    private Square position;
    private PlayerBoard board;
    private  Map<AmmoColor,Integer> ammo;
    private ArrayList<Weapon> weapons;
    private ArrayList<Powerup> powerups;
    private Actions actionStatus;

    public Player(String nickname) {
        this.nickname = nickname;
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

    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public Actions getActionStatus() {
        return actionStatus;
    }

    public void addAmmo(AmmoColor ammoColor){
        if (ammo.get(ammoColor)<3)
            ammo.put(ammoColor,ammo.get(ammoColor)+1);
    }
    public void addPowerup(Powerup powerup){
            powerups.add(powerup);
    }
    public void addWeapon(Weapon weapon){
        weapons.add(weapon);
    }
    public void turn() {
        // TODO implement here
    }


}