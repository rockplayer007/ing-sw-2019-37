package model.player;

public enum ActionOption {
    MOVE3("Move up to 3 squares"), MOVE1_GRAB("Move up to 1 square and grab"), SHOOT("shoot"),
    MOVE2_GRAB("Move up to 2 square and grab"), MOVE1_SHOOT("Move up to 1 square and shoot"),
    MOVE1_RELOAD_SHOOT("Move up to 1 square, reload if you want then shoot"), MOVE4("Move up to 4 squares"),
    MOVE2_RELOAD_SHOOT("Move up to 2 square, reload if you want then shoot"), MOVE3_GRAB("Move up to 3 squares and grab"),
    RELOAD("Reload");

    public final String explenation;

    ActionOption(String label) {
        this.explenation = label;
    }



}
