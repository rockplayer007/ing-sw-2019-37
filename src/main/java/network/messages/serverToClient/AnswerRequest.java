package network.messages.serverToClient;

import network.messages.Message;

import java.util.List;

public class AnswerRequest extends ServerToClient {

    List<String> requests;
    Boolean isOptional = false; //isOptional = true the player can choose if he wants to use the card or not


    public AnswerRequest(List<String> requests, Content content){
        super(content);
        this.requests = requests;
    }

    public List<String> getRequests() {
        return requests;
    }

    public void setIsOptional(){
        isOptional = true;
    }
    public boolean isOptional(){
        return isOptional;
    }
}
