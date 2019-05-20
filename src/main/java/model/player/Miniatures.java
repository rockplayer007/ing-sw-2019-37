package model.player;

import model.board.Color;
import model.card.AmmoColor;
import network.server.ClientOnServer;

public enum Miniatures {
    D_STRUCT_OR(new Hero(":D-STRUCT-0R","favorite beverage: 5W-30\n" +
            "hobbies: tennis, bowling, sheet-metal origami\n" +
            "favorite board game: Robo Rally\n" +
            "beloved pet: a cordless drill"),Color.YELLOW),
    BANSHEE(new Hero("BANSHEE","home planet: unknown\n" +
            "guilty pleasure: karaoke\n" +
            "perfect date: a long walk by the ocean – or in the ocean\n" +
            "siblings: 900 sisters all the same age"),Color.BLUE),
    DOZER(new Hero("DOZER","background: paramilitary covert ops\n" +
            "specialty: hurting people\n" +
            "other interests: breaking stuff\n" +
            "testosterone level: high"),Color.WHITE),
    VIOLET(new Hero("VIOLET","profession: shooting instructor\n" +
            "nail polish: always perfect\n" +
            "favorite snack: chips and salsa\n" +
            "favorite weapon: anything that goes Boom!"),Color.PURPLE),
    SPROG(new Hero("SPROG","origins: claims to be from Texas\n" +
            "disposition: surly\n" +
            "turn-ons: crickets, flat rocks, and heat lamps\n" +
            "turn-offs: lotion commercials that say \"dry, scaly skin\" like it's a bad thing"), Color.GREEN)
    ;
    private Hero hero;
    private Color color;

    Miniatures(Hero hero, Color color) {
        this.hero=hero;
        this.color=color;
    }

    public Color getColor() {
        return color;
    }
}
