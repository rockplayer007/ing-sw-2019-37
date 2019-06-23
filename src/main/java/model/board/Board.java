package model.board;

import model.card.*;
import network.server.Configs;

/**
 * Class for managing the cards and the boards
 */
public class Board {

    private transient AmmoDeck ammoDeck;
    private transient PowerDeck powerDeck;
    private transient WeaponDeck weaponDeck;

    private GameBoard map;
    private SkullBoard skullBoard;

    /**
     * The constructor creates new decks and a new skull board
     */
    public Board(){
        ammoDeck = new AmmoDeck();
        powerDeck = new PowerDeck();
        weaponDeck = new WeaponDeck();
        skullBoard = new SkullBoard(Configs.getInstance().getSkulls());
    }

    /**
     * Gives the map of the game where the players are playing
     * @return Map of the game
     */
    public GameBoard getMap(){
        return map;
    }

    /**
     * Sets a new board mpa
     * @param map the map where the players will play
     */
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
    WeaponDeck getWeaponDeck() {
        return weaponDeck;
    }

    //not useful for now
    /*
    public void setAmmoDeck(AmmoDeck ammoDeck) {
        this.ammoDeck = ammoDeck;
    }

    public void setPowerDeck(PowerDeck powerDeck) {
        this.powerDeck = powerDeck;
    }

    public void setWeaponDeck(WeaponDeck weaponDeck) {
        this.weaponDeck = weaponDeck;
    }

     */

    /**
     * Gives the current skull board
     * @return the skull board of the players
     */
    public SkullBoard getSkullBoard(){
        return skullBoard;
    }

    /**
     * Puts ammo cards in the squares where there are no cards
     */
    public void fillAmmo(){
        for(Square square : map.allSquares()){
            if(!square.isGenerationPoint() && ((AmmoSquare) square).getAmmoCard() == null){

                ((AmmoSquare) square).setAmmoCard( ammoDeck.getCard());

            }
        }
    }

    /**
     * Puts back weapons where it's needed if it is possible
     */
    public void fillWeapons(){

        for(GenerationSquare square : map.getGenPoints()){
            int nWeapons = square.getWeaponDeck().size();
            if(nWeapons < 3){
                for(int i = nWeapons; i < 3; i ++){
                    Weapon temp = weaponDeck.getCard();
                    if(temp != null){
                        square.addWeapon(temp);
                    }
                    else{
                        return;
                    }

                }
            }
        }
    }

}
