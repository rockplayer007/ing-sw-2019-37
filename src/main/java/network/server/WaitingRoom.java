package network.server;

import model.board.Board;
import model.gamehandler.Room;
import model.player.Player;

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
    private Server server;
    private static final Logger logger = Logger.getLogger(WaitingRoom.class.getName());

    public WaitingRoom(Server server){
        waitingClients = new LinkedList<>();
        this.server = server;
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
            playingRoom.addPlayer(waitingClient);

        }

        String all = waitingClients.stream().map(ClientOnServer::getUsername)
                .collect(Collectors.joining(", "));
        logger.log(Level.INFO, "Game has just started with these players: {0}", all);



        waitingClients.clear();

        playingRoom.startMatch();

    }
}
