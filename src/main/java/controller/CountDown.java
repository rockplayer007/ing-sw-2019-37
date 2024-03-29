package controller;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Creates a count down timer that can be started and stopped
 * giving a time and a task to execute when it's finished
 */
public class CountDown {

    private Timer timer;
    private int time;
    private Runnable timoutAction;

    /**
     * Constructor of the timer
     * @param time time in seconds of the timer
     * @param timeoutAction action that will be executed at the end of the timer
     */
    public CountDown(int time, Runnable timeoutAction){
        timer = new Timer();
        this.time = time*1000;
        this.timoutAction = timeoutAction;
    }

    /**
     * Starts the count down
     */
    public void startTimer(){
        timer.schedule(new TimerTask() {
            @Override
            public void run(){
                timoutAction.run();
            }

        }, time);

    }

    /**
     * Stops the timer
     */
    public void cancelTimer(){
        try{

            timer.cancel();
            timer.purge();

        }catch (Exception e){
            //nothing, just continue
        }

    }
}



