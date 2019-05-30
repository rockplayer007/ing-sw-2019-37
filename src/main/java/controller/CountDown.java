package controller;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class CountDown {

    private Timer timer;
    private int time;
    private Runnable timoutAction;


    public CountDown(int time, Runnable timeoutAction){
        timer = new Timer();
        this.time = time;
        this.timoutAction = timeoutAction;
    }


    public void startTimer(){
        timer.schedule(new TimerTask() {
            @Override
            public void run(){
                timoutAction.run();
            }

        }, time);

    }

    public void cancelTimer(){
        timer.cancel();
        timer.purge();
    }
}



