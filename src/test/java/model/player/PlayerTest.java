package model.player;

import model.player.Heroes;
import model.player.Player;
import network.client.MainClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {
    private Player player;
    private Player badBoy;
    @BeforeEach
    void before(){
        player = new Player("ciao");
        badBoy = new Player("hah");
    }

    @Test
    void test(){



    }
}
