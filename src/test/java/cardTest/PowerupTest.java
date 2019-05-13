package cardTest;

import model.card.AmmoColor;
import model.card.Powerup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PowerupTest {

    private Powerup powerup;
    private AmmoColor ammo;
    @BeforeEach
    public void before() {
        ammo = ammo.RED;
        powerup = new Powerup("Mirino","descption",ammo,1);

    }

    @Test
    public void powTest() {
        assertEquals("Mirino", powerup.getName());
        assertEquals("descption", powerup.getDescription());
        assertEquals("RED", powerup.getAmmo().name());
    }
}
