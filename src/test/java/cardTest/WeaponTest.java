package cardTest;

import controller.RoomController;
import model.card.*;
import model.gamehandler.AttackHandler;
import model.gamehandler.Room;
import model.player.Heroes;
import model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class WeaponTest {

    Room room;
    WeaponDeck weaponDeck;


    @BeforeEach
    public void before() {
        weaponDeck = new WeaponDeck();
        room= new Room(new RoomController());
        room.setAttackHandler(new AttackHandler());
        room.createMap(0);
        Player player1 = new Player("ciao", Heroes.BANSHEE);
        Player player2 = new Player("hello", Heroes.D_STRUCT_OR);
        Player player3 = new Player("lol", Heroes.DOZER);
        Player player4 = new Player("kek", Heroes.SPROG);
        Player player5 = new Player("rip", Heroes.VIOLET);
        room.setPlayers(Arrays.asList(player1,player2,player3,player4,player5));
        room.setCurrentPlayer(player1);
        player1.movePlayer(room.getBoard().getMap().getSquare(1));
        player2.movePlayer(room.getBoard().getMap().getSquare(3));
        player3.movePlayer(room.getBoard().getMap().getSquare(5));
        player4.movePlayer(room.getBoard().getMap().getSquare(7));
        player5.movePlayer(room.getBoard().getMap().getSquare(8));

    }

    @Test
    public void operation1 (){
//        List<Weapon> weapons = new ArrayList<>(weaponDeck.getCardDeck());
//        Weapon weapon = weapons.stream().filter(x->x.getName()=="LOCK RIFLE").collect(Collectors.toList()).get(0);
//        List<Effect> effects = new ArrayList<>(weapon.getEffects().keySet());
//
//        try {
//            effects.get(0).getOperations().get(0).execute(room);
//        } catch (NullTargetsException e) {
//            e.printStackTrace();
//        }
//        room.getAttackHandler().getPossibleTargets().forEach(x->System.out.println(x.getNickname()));


    }

    }
