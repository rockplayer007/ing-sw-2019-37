package model.board;

import model.player.Player;

import java.io.Serializable;

public class Cell implements Serializable {
    private transient Player kill ;
    private Color killColor;
    private int point;


    public Cell(){
        killColor = null;
        kill = null;
        point = 0;
        }

    public void setKill(Player kill) {
        this.kill = kill;
        killColor = kill.getColor();
        point = 1;
    }

    public void setOverKill() {
        point = 2;
    }

    public Player getKill() {
        return kill;
    }

    public Color getKillColor() {
        return killColor;
    }

    public int getPoint() {
        return point;
    }
}
