package network.server;

import model.player.Player;
import network.client.Client;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class allows to store players and start a new match
 */
public class WaitingRoom {

    private Queue<String> waitingClients;
    private Timer timer;

    public WaitingRoom(){
        waitingClients = new LinkedList<>();
    }

    public synchronized void addClient(String p){
        if(waitingClients.size() < 5){

            waitingClients.add(p);

            if(waitingClients.size() == 2){
                //start timer
                timer = new Timer();
                startTimer();
            }
            if (waitingClients.size() == 4){
                //remove timer
                timer.cancel();
                startGame();
            }
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
        System.out.println("game has just started with these players");
        waitingClients.forEach(x-> System.out.println(x));
    }
}
