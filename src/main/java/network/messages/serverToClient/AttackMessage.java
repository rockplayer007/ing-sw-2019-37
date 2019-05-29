package network.messages.serverToClient;

import model.player.Player;

import java.util.List;
import java.util.Map;

public class AttackMessage extends ServerToClient{

    Player attacker;
    Map<Player, Integer> hp;
    Map<Player, Integer> marks;

    public AttackMessage(Player attacker, Map<Player, Integer> hp, Map<Player, Integer> marks){
        super(Content.ATTACK);
        this.attacker = attacker;
        this.hp = hp;
        this.marks = marks;
    }

    public Player getAttacker() {
        return attacker;
    }

    public Map<Player, Integer> getHp() {
        return hp;
    }

    public Map<Player, Integer> getMarks() {
        return marks;
    }
}
