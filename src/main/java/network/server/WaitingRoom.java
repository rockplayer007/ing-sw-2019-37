package network.server;

import model.board.Board;
import model.gamehandler.Room;
import model.player.Player;
import network.client.Client;
import network.messages.BoardRequest;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class allows to store players and start a new match
 */
public class WaitingRoom {

    private Queue<ClientOnServer> waitingClients;
    private Timer timer;
    private static final Logger logger = Logger.getLogger(WaitingRoom.class.getName());

    public WaitingRoom(){
        waitingClients = new LinkedList<>();
    }

    public synchronized void addClient(ClientOnServer p){
        if(waitingClients.size() < 5){

            waitingClients.add(p);


            if(waitingClients.size() == 3){
                //start timer
                timer = new Timer();
                startTimer();
            }
            if (waitingClients.size() == 5){
                //remove timer
                timer.cancel();
                startGame();
            }
        }
        else {
            logger.log(Level.WARNING, "full room");
        }



    }

    private void startTimer(){
        timer.schedule(new TimerTask() {
            @Override
            public void run(){
                if(waitingClients.size() >=3 && waitingClients.size() <= 5){
                    startGame();
                }
                else{
                    // if there are not enough players start the timer again
                    startTimer();
                }
            }

        }, 1*60*1000);

    }

    private void startGame(){

        Room playingRoom = new Room(new Board());

        for(ClientOnServer waitingClient : waitingClients){
            Player player = new Player(waitingClient.getUsername());
            waitingClient.setPersonalPlayer(player);
            playingRoom.addPlayer(player);

        }

        String all = waitingClients.stream().map(ClientOnServer::getUsername)
                .collect(Collectors.joining(", "));
        logger.log(Level.INFO, "game has just started with these players: {0}", all);

        waitingClients.clear();

        playingRoom.startMatch();

    }
}
