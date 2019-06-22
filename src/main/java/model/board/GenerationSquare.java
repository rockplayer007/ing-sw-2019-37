package model.board;

import model.card.Weapon;

import java.util.List;

/**
 * A square that has a weapon deck
 */
public class GenerationSquare extends Square {

    private List<Weapon> weaponDeck;

    /**
     * Constructor of the class
     *
     * @param id          Number of the square
     * @param color       Color of the square
     * @param x           x coordinate of the square
     * @param y           y coordinate of the square
     * @param weaponCards List of 3 weapons to be placed
     */
    public GenerationSquare(int id, Color color, int x, int y, List<Weapon> weaponCards) {
        super(id, color, true, x, y);
        this.weaponDeck = weaponCards;
    }

    /**
     * Gives the list of the cards on this square
     *
     * @return List of the Weapons
     */
    public List<Weapon> getWeaponDeck() {
        return weaponDeck;
    }

    /**
     * Adds a weapon to the square
     * @param card the weapon card that has to be added
     */
    void addWeapon(Weapon card) {
        weaponDeck.add(card);
    }

    /**
     * Removes a weapon from the cards in the square
     * @param card the weapon card that has to be removed
     */
    void removeWeapon(Weapon card){
        weaponDeck.remove(card);
    }

}
