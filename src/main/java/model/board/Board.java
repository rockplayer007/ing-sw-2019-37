package model.board;

import model.card.AmmoDeck;
import model.card.PowerDeck;

import java.util.ArrayList;

public class Board {

    private AmmoDeck ammoDeck = new AmmoDeck();
    private PowerDeck powerDeck = new PowerDeck();

    private BoardMap map = new BoardMap();

    public Board(){

    }


    private class BoardMap {
        ArrayList<GenerationSquare> genPoints;

        BoardMap(){
            createMap();
        }

        private ArrayList<GenerationSquare> createMap(){
            ArrayList<GenerationSquare> 
            return ;
        }
        public Square giveGenerationPoint(Color color){

        }
    }



}
