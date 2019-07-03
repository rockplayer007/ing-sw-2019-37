package model.card;

import controller.RoomController;
import model.exceptions.InterruptOperationException;
import model.exceptions.NotExecutedException;
import model.exceptions.TimeFinishedException;
import model.gamehandler.AttackHandler;
import model.gamehandler.Room;
import model.player.Heroes;
import model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;


//@RunWith(PowerMockRunner.class)
//@PrepareForTest({MessageHandler.class})
public class OperationTest {

    private Room room;



    @BeforeEach
    void before() {

        room= new Room(new RoomController());
        room.setAttackHandler(new AttackHandler());
        room.createMap(0);
        Player player1 = new Player("ciao");
        player1.setHero(Heroes.BANSHEE);
        Player player2 = new Player("hello");
        player2.setHero(Heroes.D_STRUCT_OR);
        Player player3 = new Player("lol");
        player3.setHero(Heroes.DOZER);
        Player player4 = new Player("kek");
        player4.setHero(Heroes.SPROG);
        Player player5 = new Player("rip");
        player5.setHero(Heroes.VIOLET);
        room.setPlayers(Arrays.asList(player1,player2,player3,player4,player5));
        room.setCurrentPlayer(player1);
        player1.movePlayer(room.getBoard().getMap().getSquare(0));
        player2.movePlayer(room.getBoard().getMap().getSquare(2));
        player3.movePlayer(room.getBoard().getMap().getSquare(4));
        player4.movePlayer(room.getBoard().getMap().getSquare(6));
        player5.movePlayer(room.getBoard().getMap().getSquare(0));


//        Printer printer = new Printer(new CLI(new MainClient()));
//        printer.printAllInfo(room.getBoard().getMap(), player1.getPowerups(), room.getBoard().getSkullBoard());
//        printer.printPlayersInfo(room.getBoard().getMap(), player1.getPowerups());
    }

    @Test
    void operations() throws NotExecutedException, TimeFinishedException {
        AttackHandler attackHandler = room.getAttackHandler();

//        PowerMockito.mockStatic(MessageHandler.class);
//        PowerMockito.when(MessageHandler.choosePlayers(room.getCurrentPlayer(),attackHandler.getPossibleTargets(),1,room))
//                .thenReturn(Collections.singletonList(attackHandler.getPossibleTargets().get(0)));

        //VisiblePlayers Test
        VisiblePlayers visiblePlayers = new VisiblePlayers();
        visiblePlayers.execute(room);

        assertSame(attackHandler.getPossibleTargets().size(),3);

        //SelectTarget
//        SelectTargets selectTargets = new SelectTargets(1,false);
//        selectTargets.execute(room);


        room.getAttackHandler().setTargetsToShoot(room.getAttackHandler().getPossibleTargets());
        Damage damage = new Damage(1);
        damage.execute(room);
        assertSame(room.getAttackHandler().getTargetsToShoot().get(0).getPlayerBoard().getHp().size(),1);
        assertSame(room.getAttackHandler().getTargetsToShoot().get(1).getPlayerBoard().getHp().size(),1);
        assertSame(room.getAttackHandler().getTargetsToShoot().get(2).getPlayerBoard().getHp().size(),1);

        Mark mark = new Mark(1);
        mark.execute(room);
        assertSame(room.getAttackHandler().getTargetsToShoot().get(0).getPlayerBoard().getMarks().size(),1);
        assertSame(room.getAttackHandler().getTargetsToShoot().get(1).getPlayerBoard().getMarks().size(),1);
        assertSame(room.getAttackHandler().getTargetsToShoot().get(2).getPlayerBoard().getMarks().size(),1);

        SetTargetToSelected setTargetToSelected = new SetTargetToSelected();
        setTargetToSelected.execute(room);
        assertSame(attackHandler.getPossibleTargets().size(),3);

        MinOrMaxDistance maxDistance = new MinOrMaxDistance(2,true);
        maxDistance.execute(room);
        assertSame(attackHandler.getPossibleTargets().size(),3);
        MinOrMaxDistance minDistance = new MinOrMaxDistance(0,false);
        minDistance.execute(room);
        assertSame(attackHandler.getPossibleTargets().size(),2);

        minDistance = new MinOrMaxDistance(2,false);
        minDistance.execute(room);
        assertSame(attackHandler.getPossibleTargets().size(),0);

        SameSquare sameSquare = new SameSquare();
        sameSquare.execute(room);
        assertSame(attackHandler.getPossibleTargets().get(0).getNickname(),"rip");

        attackHandler.getPossibleTargets().clear();
        AddPossibleTargetBeforeMove addPossibleTargetBeforeMove = new AddPossibleTargetBeforeMove(2,false);
        addPossibleTargetBeforeMove.execute(room);
        assertSame(attackHandler.getPossibleTargets().size(),4);
        attackHandler.getPossibleTargets().clear();
        addPossibleTargetBeforeMove = new AddPossibleTargetBeforeMove(2,true);
        addPossibleTargetBeforeMove.execute(room);
        assertSame(attackHandler.getPossibleTargets().size(),3);

        SetPlayerPositionAsEffectSquare setPlayerPositionAsEffectSquare = new SetPlayerPositionAsEffectSquare();
        setPlayerPositionAsEffectSquare.execute(room);
        assertSame(attackHandler.getEffectSquare(),room.getCurrentPlayer().getPosition());

//        room.getCurrentPlayer().movePlayer(room.getBoard().getMap().getSquare(5));
//        assertSame(room.getCurrentPlayer().getPosition(),room.getBoard().getMap().getSquare(5));
        //set "rip" to move
        room.getAttackHandler().getTargetsToShoot().clear();
        sameSquare.execute(room);
        room.getAttackHandler().setTargetsToShoot(room.getAttackHandler().getPossibleTargets());
        assertSame(attackHandler.getTargetsToShoot().size(),1);
        //set "rip" to move
        attackHandler.getTargetsToShoot().get(0).movePlayer(room.getBoard().getMap().getSquare(5));
        assertSame(attackHandler.getTargetsToShoot().get(0).getPosition(),room.getBoard().getMap().getSquare(5));
        MoveTargetToEffectSquare moveTargetToEffectSquare = new MoveTargetToEffectSquare();
        moveTargetToEffectSquare.execute(room);
        assertSame(attackHandler.getTargetsToShoot().get(0).getPosition(),room.getBoard().getMap().getSquare(0));

        Heatseeker heatseeker = new Heatseeker();
        visiblePlayers.execute(room);
        heatseeker.execute(room);
        assertSame(attackHandler.getPossibleTargets().size(),1);
        assertSame(attackHandler.getPossibleTargets().get(0).getNickname(),"kek");

        SelectAllTarget selectAllTarget = new SelectAllTarget();
        selectAllTarget.execute(room);
        assertSame(attackHandler.getPossibleTargets().size(),0);
        assertSame(attackHandler.getTargetsToShoot().get(0).getNickname(),"kek");

        MoveToTarget moveToTarget = new MoveToTarget();
        moveToTarget.execute(room);
        assertSame(room.getCurrentPlayer().getPosition(),room.getBoard().getMap().getSquare(6));
        room.getCurrentPlayer().movePlayer(room.getBoard().getMap().getSquare(0));

        SetTargetPositionAsEffectSquare setTargetPositionAsEffectSquare = new SetTargetPositionAsEffectSquare();
        setTargetPositionAsEffectSquare.execute(room);
        assertSame(attackHandler.getEffectSquare(),room.getBoard().getMap().getSquare(6));

        TargetOnEffectSquare targetOnEffectSquare = new TargetOnEffectSquare();
        attackHandler.getPossibleTargets().clear();
        try {
            targetOnEffectSquare.execute(room);
        } catch (InterruptOperationException e) {
            //
        }
        assertSame(attackHandler.getPossibleTargets().size(),1);
        assertSame(attackHandler.getPossibleTargets().get(0).getNickname(),"kek");

        ThorTargets thorTargets = new ThorTargets(0);
        attackHandler.setSelectedTargets(attackHandler.getPossibleTargets());
        thorTargets.execute(room);
        assertSame(attackHandler.getPossibleTargets().size(),1);
        assertSame(attackHandler.getPossibleTargets().get(0).getNickname(),"lol");
        thorTargets = new ThorTargets(1);
        attackHandler.getSelectedTargets().addAll(attackHandler.getPossibleTargets());
        thorTargets.execute(room);
        assertSame(attackHandler.getPossibleTargets().size(),0);

        NextSquareInDirection nextSquareInDirection = new NextSquareInDirection();
        sameSquare.execute(room);
        attackHandler.getPossibleTargets().get(0).movePlayer(room.getBoard().getMap().getSquare(1));
        attackHandler.setTargetsToShoot(attackHandler.getPossibleTargets());
        nextSquareInDirection.execute(room);
        assertSame(attackHandler.getEffectSquare(),room.getBoard().getMap().getSquare(2));


    }

    }
