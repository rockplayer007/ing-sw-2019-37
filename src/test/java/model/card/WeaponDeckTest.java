package model.card;

import model.card.WeaponDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WeaponDeckTest {
    private WeaponDeck weaponDeck;


    @Test
    public void createWeaponDeck(){
        weaponDeck=new WeaponDeck();
        System.out.println(weaponDeck.getCard().getDescription());

    }
}
