package model.board;

import model.card.*;

/**
 * Class for managing the cards and the boards
 */
public class Board {

    private transient AmmoDeck ammoDeck;
    private transient PowerDeck powerDeck;
    private transient WeaponDeck weaponDeck;

    private GameBoard map;
    private SkullBoard skullBoard;

    public Board(){
        ammoDeck = new AmmoDeck();
        powerDeck = new PowerDeck();
        weaponDeck = new WeaponDeck();

        //TODO fare l'utente a scegliere quandi skull mette.
        skullBoard = new SkullBoard(5);
    }

    /**
     * Gives the map of the game where the players are playing
     * @return Map of the game
     */
    public GameBoard getMap(){
        return map;
    }


    public void setMap(GameBoard map) {
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

    public SkullBoard getSkullBoard(){
        return skullBoard;
    }

    public void fillAmmo(){
        for(Square square : map.allSquares()){
            if(!square.isGenerationPoint()){
                if(((AmmoSquare) square).getAmmoCard() == null){
                    ((AmmoSquare) square).setAmmoCard( ammoDeck.getCard());
                }
            }
        }
    }

}
