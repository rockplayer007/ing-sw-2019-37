package network.messages.serverToClient;

import model.player.Player;

import java.util.List;
import java.util.Map;

public class AttackMessage extends ServerToClient{

    private String attacker;
    private String hp;
    private String marks;

        public AttackMessage(String attacker, String hp, String marks){
        super(Content.ATTACK);
        this.attacker = attacker;
        this.hp = hp;
        this.marks = marks;
    }

    public String getAttacker() {
        return attacker;
    }

    public String getHp() {
        return hp;
    }

    public String getMarks() {
        return marks;
    }
}
