package view.GUI;

import model.board.Color;
import model.board.Square;

import javax.swing.*;

public class JMapButton extends JButton {
    private int posx;
    private int posy;
    private Color color;

    JMapButton(int x, int y,Color color){
        this.posx=x;
        this.posy=y;
        this.color=color;
    }

    public int getPosx() {
        return posx;
    }

    public int getPosy() {
        return posy;
    }

    public Color getColor() {
        return color;
    }
}
