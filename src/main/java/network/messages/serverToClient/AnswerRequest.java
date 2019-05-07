package network.messages.serverToClient;

import network.messages.Message;

import java.util.List;

public class AnswerRequest extends ServerToClient {

    List<String> requests;

    public AnswerRequest(List<String> requests){
        super(Message.Content.BOARD_REQUEST);
        this.requests = requests;
    }

    public List<String> getRequests() {
        return requests;
    }
}
