package model.player;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class PlayerTest {
    private Player player;
    private Player badBoy;
    private Player badBoy2;
    @BeforeEach
    void before(){
        player = new Player("ciao");
        player.setHero(Heroes.BANSHEE);
        badBoy = new Player("hah");
        badBoy.setHero(Heroes.D_STRUCT_OR);
        badBoy2 = new Player("xxx");
        badBoy2.setHero(Heroes.DOZER);
    }

    @Test
    void test(){
        player.getPlayerBoard().addDamage(badBoy,5);
        player.getPlayerBoard().addDamage(badBoy2,5);
        assertSame(player.getPlayerBoard().getHp().size(),10);
        player.getPlayerBoard().liquidation();
        assertSame(badBoy.getPlayerBoard().getPoints(),9);
        assertSame(badBoy2.getPlayerBoard().getPoints(),6);



    }
}
