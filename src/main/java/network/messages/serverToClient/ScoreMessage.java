package network.messages.serverToClient;

import java.util.Map;

public class ScoreMessage extends ServerToClient {

    private Map<String, Integer> score;

    public ScoreMessage(Map<String, Integer> score) {
        super(Content.SCORE);
        this.score = score;
    }

    public Map<String, Integer> getScore() {
        return score;
    }
}
