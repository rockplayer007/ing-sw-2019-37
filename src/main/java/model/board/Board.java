package model.board;

import model.card.*;

/**
 * Class for managing the cards and the boards
 */
public class Board {

    private AmmoDeck ammoDeck;
    private PowerDeck powerDeck;
    private WeaponDeck weaponDeck;

    private BoardMap map;
    private SkullBoard skullBoard;

    public Board(){
        ammoDeck = new AmmoDeck();
        powerDeck = new PowerDeck();
        weaponDeck = new WeaponDeck();

        //map = new BoardMap(this);
        skullBoard = new SkullBoard();
    }

    /**
     * Gives the map of the game where the players are playing
     * @return Map of the game
     */
    public BoardMap getMap(){
        return map;
    }


    public void setMap(BoardMap map) {
        this.map = map;
    }

    /**
     * Gives the current deck of ammo
     * @return The ammo deck
     */
    public AmmoDeck getAmmoDeck() {
        return ammoDeck;
    }

    /**
     * Gives the current deck of powerup cards
     * @return The powerup deck
     */
    public PowerDeck getPowerDeck(){
        return powerDeck;
    }

    /**
     * Gives the current deck of weapon cards
     * @return The weapon deck
     */
    public WeaponDeck getWeaponDeck() {
        return weaponDeck;
    }

    public void setAmmoDeck(AmmoDeck ammoDeck) {
        this.ammoDeck = ammoDeck;
    }

    public void setPowerDeck(PowerDeck powerDeck) {
        this.powerDeck = powerDeck;
    }

    public void setWeaponDeck(WeaponDeck weaponDeck) {
        this.weaponDeck = weaponDeck;
    }


}
