package controller;

import java.util.Timer;

public class GameTimer {

    private Timer timer;


    public GameTimer(){
        timer = new Timer();
    }




    public void cancel(){
        timer.cancel();
    }
}
