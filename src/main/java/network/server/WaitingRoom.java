package network.server;

import controller.RoomController;
import model.player.HeroGenerator;
import model.player.Player;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class allows to store players and start a new match
 */
public class WaitingRoom {

    private Queue<ClientOnServer> waitingClients;
    private Timer timer;
    private MainServer server;
    private static final Logger logger = Logger.getLogger(WaitingRoom.class.getName());

    public WaitingRoom(MainServer server){
        waitingClients = new LinkedList<>();
        this.server = server;
    }

    /**
     * Takes a {@link ClientOnServer} and puts him in a waiting queue. A timer starts when there are
     * 3 players. The game starts when there are 5 players.
     * @param p Client to add to the queue
     */
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

    /**
     * Starts a timer when there are enough players and starts a game when the timer finishes
     */
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

        }, 1*5*1000);

    }

    /**
     * Creates a new room where the players can play
     */
    private void startGame(){

        RoomController playingRoom = new RoomController();
        List<String> usernames = new ArrayList<>();

        HeroGenerator heroGen = new HeroGenerator();
        for(ClientOnServer waitingClient : waitingClients){
            Player player = new Player(waitingClient.getUsername(), heroGen.getHero());
            waitingClient.setPersonalPlayer(player);
            playingRoom.addPlayer(waitingClient);
            usernames.add(waitingClient.getUsername());
        }

        String all = waitingClients.stream().map(ClientOnServer::getUsername)
                .collect(Collectors.joining(", "));
        logger.log(Level.INFO, "Game has just started with these players: {0}", all);



        waitingClients.clear();
        server.setUsernameInRoom(usernames, playingRoom);

        playingRoom.matchSetup();

    }
}
