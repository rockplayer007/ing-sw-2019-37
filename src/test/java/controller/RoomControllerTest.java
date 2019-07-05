package controller;

import com.google.gson.Gson;
import model.board.*;
import model.card.AmmoColor;
import model.card.Powerup;
import model.exceptions.TimeFinishedException;
import model.exceptions.TooManyPlayerException;
import model.gamehandler.Room;
import model.player.*;
import network.client.MainClient;
import network.client.rmi.ClientImplementation;
import network.server.ClientOnServer;
import network.server.Configs;
import network.server.MainServer;
import network.server.rmi.ServerImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.CLI.Printer;

import java.io.File;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoomControllerTest {

    private RoomController roomController;

    @BeforeEach
    public void createBoard(){
        roomController = new RoomController();
        //roomController.getRoom().createMap(0);

    }

    @Test
    void roomControllerTest(){
        roomController.setTest();

        for(int i = 0; i < 6; i++){
            try {
                ClientOnServer cs = new ClientOnServer("" + i,
                        new ClientImplementation(new MainClient()), ""+ i);
                cs.getPersonalPlayer().setHero(new HeroGenerator().getHero());
                roomController.addPlayer(cs);
            } catch (TooManyPlayerException e) {
                //
            }
        }

        roomController.matchSetup();
        //roomController.getTurnController().startPlayerRound();





        int i= 0;
        for(Player p : roomController.getRoom().getPlayers()){
            p.addWeapon(roomController.getRoom().getBoard().getWeaponDeck().getCard());
            p.addWeapon(roomController.getRoom().getBoard().getWeaponDeck().getCard());
            p.addPowerup(roomController.getRoom().getBoard().getPowerDeck().getCard());
            p.addPowerup(roomController.getRoom().getBoard().getPowerDeck().getCard());
            for(AmmoColor c : AmmoColor.values()){
                p.addAmmo(c);
                p.addAmmo(c);
            }

            p.movePlayer(roomController.getRoom().getBoard().getMap().getSquare(i++));
            p.setNextRoundstatus();
        }

        roomController.setTestMockMessage(0);
        roomController.getRoom().getCurrentPlayer().setLive(false);
        roomController.getTurnController().startPlayerRound();


        for(Player p : roomController.getRoom().getPlayers()){
            try {
                roomController.getTurnController().normalRound(p);
            } catch (TimeFinishedException e) {

            }
        }

        roomController.setTestMockMessage(0);

        Player p = roomController.getRoom().getCurrentPlayer();
        try {
            roomController.getTurnController().getRoundController().actionController(p);
        } catch (TimeFinishedException e) {

        }
        try {
            roomController.getTurnController().getRoundController().powerupController(p);
        } catch (TimeFinishedException e) {

        }

        for(ActionState action : ActionState.values()){

            p.setActionStatus(action);


            //roomController.setTestMockMessage(x);
            try {
                roomController.getTurnController().getRoundController().actionController(p);
                roomController.getRoom().getBoard().fillAmmo();
                roomController.getRoom().getBoard().fillWeapons();
            } catch (TimeFinishedException e) {

            }

        }

        new Printer().printAllInfo(roomController.getRoom().getBoard().getMap(),
                roomController.getRoom().getCurrentPlayer().getPowerups(),
                roomController.getRoom().getBoard().getSkullBoard(),
                "");


    }


    @Test
    public void toJsonListTest(){
        List<Powerup> powerups = roomController.getRoom().getBoard().getPowerDeck().getCard(2);

        List<String> list = roomController.everythingToJson( powerups);

        Gson gson = new Gson();
        Powerup arrivedCard1 = gson.fromJson(list.get(0), Powerup.class);
        Powerup arrivedCard2 = gson.fromJson(list.get(1), Powerup.class);

        assertEquals(( powerups.get(0)).getAmmo(), (arrivedCard1).getAmmo());

        assertEquals((powerups.get(1)).getAmmo(), (arrivedCard2).getAmmo());

        roomController.getRoom().createMap(3);

        List<GenerationSquare> squares = roomController.getRoom().getBoard().getMap().getGenPoints();
        list = roomController.everythingToJson(squares);


        Square arrivedSquare1 = gson.fromJson(list.get(0), GenerationSquare.class);
        Square arrivedSquare2 = gson.fromJson(list.get(1), GenerationSquare.class);

        assertEquals(( squares.get(0)).getColor(), arrivedSquare1.getColor());

        assertEquals((squares.get(1)).getColor(), arrivedSquare2.getColor());
    }

    @Test
    public void fileTest(){
        Configs configs = Configs.getInstance();
        configs.getTurnTime();
        configs.getMaximumPlayers();
        configs.getMinimumPlayers();
        configs.getSkulls();
        configs.getTimeForTagBackRequest();

    }



}


