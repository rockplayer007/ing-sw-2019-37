package network.server;

import controller.RoomController;
import model.player.HeroGenerator;
import network.messages.Message;
import network.messages.serverToClient.ServerToClient;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class allows to store players and start a new match
 */
public class WaitingRoom {

    private static final int STARTING_PLAYERS = 3;
    private static final int MAX_PLAYERS = 5;
    private static final int WAITING_TIME = 15; //seconds

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
        if(waitingClients.size() < MAX_PLAYERS){

            waitingClients.add(p);

            if(waitingClients.size() == STARTING_PLAYERS){
                //start timer
                timer = new Timer();
                startTimer();
            }
            if (waitingClients.size() == MAX_PLAYERS){
                removeDisconnectedClients();
                if (waitingClients.size() >= STARTING_PLAYERS) {
                    //remove timer if the number of players is enough
                    timer.cancel();

                    List<ClientOnServer> newPlayers = new ArrayList<>(waitingClients);
                    waitingClients.clear();

                    Thread playingThread = new Thread(() -> startGame(newPlayers));
                    playingThread.start();
                }


            }
        }
        else {
            //TODO create new queue
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
                int nPlayers = waitingClients.size();

                if(nPlayers >= STARTING_PLAYERS && nPlayers <= MAX_PLAYERS){

                    removeDisconnectedClients();

                    if(waitingClients.size() < STARTING_PLAYERS){
                        startTimer();
                    }
                    else{
                        List<ClientOnServer> newPlayers = new ArrayList<>(waitingClients);
                        waitingClients.clear();

                        startGame(newPlayers);
                    }
                }
                else{
                    // if there are not enough players start the timer again
                    startTimer();
                }
            }

        }, 1*WAITING_TIME*1000);

    }

    /**
     * Creates a new room where the players can play
     */
    private void startGame(List<ClientOnServer> waitingClients){

        RoomController playingRoom = new RoomController();
        List<String> usernames = new ArrayList<>();

        HeroGenerator heroGen = new HeroGenerator();
        for(ClientOnServer waitingClient : waitingClients){

            waitingClient.getPersonalPlayer().setHero(heroGen.getHero());
            playingRoom.addPlayer(waitingClient);
            usernames.add(waitingClient.getUsername());
        }

        String all = waitingClients.stream().map(ClientOnServer::getUsername)
                .collect(Collectors.joining(", "));
        logger.log(Level.INFO, "Game has just started with these players: {0}", all);



        //waitingClients.clear();
        server.setUsernameInRoom(usernames, playingRoom);

        playingRoom.matchSetup();

        //remove controller
        //remove players
        server.removeClientFromServer(usernames);
    }

    private void removeDisconnectedClients(){
        List<ClientOnServer> tempClients = new ArrayList<>(waitingClients);
        for(ClientOnServer client : tempClients){
            //remove player if he disconnects before
            try {
                client.getClientInterface().notifyClient(new ServerToClient(Message.Content.CONNECTION));
                if(!client.getPersonalPlayer().isConnected()){
                    waitingClients.remove(client);
                    server.removeClient(client);
                    logger.log(Level.WARNING, "Player: {0} removed because disconnected before start",
                            client.getUsername());
                }
            } catch (RemoteException e) {

                waitingClients.remove(client);
                server.removeClient(client);
                logger.log(Level.WARNING, "Player: {0} removed because disconnected before start",
                        client.getUsername());
            }

        }
    }
}
