package model.player;

import model.board.Color;


public enum Heroes {
    D_STRUCT_OR(":D-STRUCT-0R","favorite beverage: 5W-30\n" +
            "hobbies: tennis, bowling, sheet-metal origami\n" +
            "favorite board game: Robo Rally\n" +
            "beloved pet: a cordless drill",Color.YELLOW),
    BANSHEE("BANSHEE","home planet: unknown\n" +
            "guilty pleasure: karaoke\n" +
            "perfect date: a long walk by the ocean – or in the ocean\n" +
            "siblings: 900 sisters all the same age",Color.BLUE),
    DOZER("DOZER","background: paramilitary covert ops\n" +
            "specialty: hurting people\n" +
            "other interests: breaking stuff\n" +
            "testosterone level: high",Color.WHITE),
    VIOLET("VIOLET","profession: shooting instructor\n" +
            "nail polish: always perfect\n" +
            "favorite snack: chips and salsa\n" +
            "favorite weapon: anything that goes Boom!",Color.PURPLE),
    SPROG("SPROG","origins: claims to be from Texas\n" +
            "disposition: surly\n" +
            "turn-ons: crickets, flat rocks, and heat lamps\n" +
            "turn-offs: lotion commercials that say \"dry, scaly skin\" like it's a bad thing", Color.GREEN)
    ;
    private String name;
    private String description;
    private Color color;

    Heroes(String name, String description, Color color) {
        this.name=name;
        this.description=description;
        this.color=color;
    }

    public Color getColor() {
        return color;
    }
    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
