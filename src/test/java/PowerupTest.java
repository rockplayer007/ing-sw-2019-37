import model.card.AmmoColor;
import model.card.Powerup;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PowerupTest {

    private Powerup powerup;
    private AmmoColor ammo;
    @Before
    public void before() {
        ammo = ammo.RED;
        powerup = new Powerup("Mirino","descption",ammo,1);

    }

    @Test
    public void powTest() {
        assertEquals("Mirino", powerup.getname());
        assertEquals("descption", powerup.getDescription());
        assertEquals("RED", powerup.getAmmoColor().name());
    }
}
