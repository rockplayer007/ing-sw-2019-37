package model.card;

import controller.MessageHandler;
import controller.ActionHandler;
import model.gamehandler.Room;
import model.board.Color;
import model.board.Square;
import model.exceptions.NotEnoughException;
import model.gamehandler.AttackHandler;
import model.exceptions.NotExecutedException;
import model.player.Player;

import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public interface Operation {

    void execute(Room room)throws NotExecutedException, TimeoutException;
}

class VisiblePlayers implements Operation{

    @Override
    public void execute(Room room){
        AttackHandler attackHandler=room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        Set<Square> visible = new HashSet<>(currentPlayer.getPosition().visibleSquare(room.getBoard().getMap()));
        ArrayList<Player> visiblePlayers = new ArrayList<>();

        visible.forEach(x-> visiblePlayers.addAll(x.getPlayersOnSquare()));
        visiblePlayers.remove(currentPlayer);

        attackHandler.setPossibleTargets(visiblePlayers);
    }
}

class SelectTargets implements Operation{
    private int numberTragets;
    private Boolean distinctSquare;

    SelectTargets(int number,Boolean distinctSquare){
        numberTragets=number;
        this.distinctSquare=distinctSquare;
    }

    @Override
    public void execute(Room room)throws TimeoutException{
        AttackHandler attackHandler=room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        List<Player> possibleTargets =attackHandler.getPossibleTargets();
        List<Player> targets=new ArrayList<>();
        if (distinctSquare) {
            for (int i=0; i< numberTragets;i++){
                targets.addAll(MessageHandler.choosePlayers(currentPlayer,possibleTargets,1,room));
                Square targetPostion = targets.get(i).getPosition();
                possibleTargets = possibleTargets.stream().filter(x->x.getPosition()!=targetPostion).collect(Collectors.toList());
            }

        }else {
           targets = MessageHandler.choosePlayers(currentPlayer,possibleTargets,numberTragets,room);
        }
        // TODO se messaggio da qualche errore come devo gestire cioè quando mi null il targers.
        possibleTargets.removeAll(targets);
        attackHandler.setTargetsToShot(targets);
    }

}

class SelectFromSelectedTargets implements Operation{
    private int numberTragets;
    SelectFromSelectedTargets(int number) {
        numberTragets = number;
    }

    @Override
    public void execute(Room room) throws TimeoutException{
        AttackHandler attackHandler=room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        List<Player> selectedTargets =attackHandler.getSelectedTargets();
        List<Player> targets = MessageHandler.choosePlayers(currentPlayer,selectedTargets,numberTragets,room);
    // TODO se messaggio da qualche errore come devo gestire cioè quando mi null il targers.
        selectedTargets.removeAll(targets);
        attackHandler.setTargetsToShot(targets);

    }
}

class Damage implements Operation{

    private int points;

    Damage(int points){
        this.points = points;
    }

    @Override
    public void execute(Room room){
        AttackHandler attackHandler=room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        List<Player> targets = attackHandler.getTargetsToShot();
        targets.forEach(x->attackHandler.addDamage(x,points));
//        TODO
        targets.forEach(x->x.getPlayerBoard().addDamage(currentPlayer,points));

    }
}

class Mark implements Operation{

    private int points;

    Mark(int points){
        this.points = points;
    }

    @Override
    public void execute(Room room){
        AttackHandler attackHandler = room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        List<Player> targets = attackHandler.getTargetsToShot();
        targets.forEach(x->attackHandler.addmark(x,points));
        targets.forEach(x->x.getPlayerBoard().addMark(currentPlayer,points));


    }
}


class SetTargetToSelected implements Operation{
    @Override
    public void execute(Room room) {
        AttackHandler attackHandler=room.getAttackHandler();
        List<Player> targets =new ArrayList<>(attackHandler.getTargetsToShot());
        List<Player> selectedTargets = attackHandler.getSelectedTargets();

        for (Player p:targets) {
            if (selectedTargets.contains(p))
                selectedTargets.remove(p);
            else
                selectedTargets.add(p);
        }
    }
}


class Run implements Operation{
    private int distance;

    Run(int distance){
        this.distance=distance;
    }

    @Override
    public void execute(Room room) throws TimeoutException{
        Player currentPlayer = room.getCurrentPlayer();
        currentPlayer.movePlayer(MessageHandler.chooseSquare(currentPlayer,currentPlayer.getPosition().getValidPosition(distance), room));
        //TODO  se messaggio da qualche errore come devo gestire cioè quando mi null il targers.
    }
}


/**
 * if need to remove the target in base minDistance set "isMaxDistance" as false, and the other way around.
 */
class MinOrMaxDistance implements Operation{
    private int distance;
    private Boolean isMaxDistance;

    MinOrMaxDistance(int distance,Boolean isMaxDistance){
        this.distance=distance;
        this.isMaxDistance = isMaxDistance;
    }

    @Override
    public void execute(Room room) {
        AttackHandler attackHandler=room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        Set<Square> possibleSquares =new HashSet<>(currentPlayer.getPosition().getValidPosition(distance));
        List<Player> possiblePlayers = new ArrayList<>();

        possibleSquares.forEach(x-> possiblePlayers.addAll(x.getPlayersOnSquare()));
        possiblePlayers.remove(currentPlayer);

        if (isMaxDistance)
            attackHandler.setPossibleTargets(possiblePlayers);
        else
            attackHandler.getPossibleTargets().removeAll(possiblePlayers);
    }
}

class SameSquare implements Operation{
    @Override
    public void execute(Room room) {
        Player currentPlayer = room.getCurrentPlayer();
        List<Player> possiblePlayers = new ArrayList<>(currentPlayer.getPosition().getPlayersOnSquare());
        possiblePlayers.remove(currentPlayer);
        room.getAttackHandler().setPossibleTargets(possiblePlayers);
    }
}

/**
 * if yourSquare is false need launch first "VisiblePlayers"
 */
class  AddPossibleTargetBeforeMove implements Operation{
    private int distance;
    private Boolean yourSquare;

    AddPossibleTargetBeforeMove (int distance,Boolean yourSquare){
        this.distance=distance;
        this.yourSquare=yourSquare;
    }

    @Override
    public void execute(Room room) {
        AttackHandler attackHandler = room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        List<Player> possiblePlayers = new ArrayList<>(room.getPlayers());
        Set<Square> visibleSquare=new HashSet<>(currentPlayer.getPosition().visibleSquare(room.getBoard().getMap()));
        if (!yourSquare){
            possiblePlayers.removeAll(attackHandler.getPossibleTargets());
            possiblePlayers=possiblePlayers.stream()
                    .filter(player-> player.getPosition().getValidPosition(distance).stream().anyMatch(visibleSquare::contains))
                    .collect(Collectors.toList());
            attackHandler.getPossibleTargets().addAll(possiblePlayers);
        }else {
            possiblePlayers=possiblePlayers.stream()
                    .filter(player-> player.getPosition().getValidPosition(distance).contains(currentPlayer.getPosition()))
                    .collect(Collectors.toList());
            attackHandler.setPossibleTargets(possiblePlayers);
        }

    }
}


class  MoveTargetToVisible implements Operation{
    private int distance;

    MoveTargetToVisible(int distance){
        this.distance = distance;
    }

    @Override
    public void execute(Room room) throws TimeoutException{
        Player currentPlayer = room.getCurrentPlayer();
        Player target = room.getAttackHandler().getTargetsToShot().get(0); // dovrebbe essere sempre uno solo quando lancia questo operazione
        Set<Square> visibleSquare=currentPlayer.getPosition().visibleSquare(room.getBoard().getMap());
        Set<Square> validSquare;
        validSquare=target.getPosition().getValidPosition(distance).stream().filter(visibleSquare::contains).collect(Collectors.toSet());

        target.movePlayer(MessageHandler.chooseSquare(currentPlayer,validSquare, room));
        //TODO  se messaggio da qualche errore come devo gestire cioè quando mi null il targers.
    }
}


class SetPlayerPositionAsEffectSquare implements Operation{
    @Override
    public void execute(Room room) {
        Player currentPlayer = room.getCurrentPlayer();
        room.getAttackHandler().setEffectSquare(currentPlayer.getPosition());
    }
}


class SelectEffectSquare implements Operation{
    private int zone; // if zone=1 "vortex", if zone=0 "select effect square"

    SelectEffectSquare(int zone){
        this.zone=zone;
    }
    @Override
    public void execute(Room room) throws TimeoutException{
        AttackHandler attackHandler=room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        List<Player> possibleTargets = new ArrayList<>();
        Set<Square> visibleSquare=currentPlayer.getPosition().visibleSquare(room.getBoard().getMap())
                .stream()
                .filter(x->x.getValidPosition(zone).stream().anyMatch(p->!p.getPlayersOnSquare().isEmpty()))
                .collect(Collectors.toSet());

        Square vortex= MessageHandler.chooseSquare(currentPlayer,visibleSquare, room);
        //TODO  se messaggio da qualche errore come devo gestire cioè quando mi null il targers.
        vortex.getValidPosition(1).forEach(x-> possibleTargets.addAll(x.getPlayersOnSquare()));
        attackHandler.setPossibleTargets(possibleTargets);
        attackHandler.setEffectSquare(vortex);
    }
}

class MoveTargetToEffevtSquare implements Operation{
    @Override
    public void execute(Room room) {
        AttackHandler attackHandler=room.getAttackHandler();
        attackHandler.getTargetsToShot().forEach(x->x.movePlayer(attackHandler.getEffectSquare()));
    }
}

class Furance implements Operation{
    private Boolean selectSquare;

    Furance(Boolean selectSquare){
        this.selectSquare=selectSquare;
    }
    @Override
    public void execute(Room room) throws TimeoutException{
        AttackHandler attackHandler=room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        if (!selectSquare){
            Set<Color> rooms= new HashSet<>();
            List<Player> targets = new ArrayList<>();
            currentPlayer.getPosition().getNeighbourSquare().forEach(x->rooms.add(x.getColor()));
            rooms.remove(currentPlayer.getPosition().getColor());

            Color color = MessageHandler.chooseRoom(currentPlayer,new ArrayList<>(rooms),room);
            //TODO  se messaggio da qualche errore come devo gestire cioè quando mi null il targers.
            room.getBoard().getMap().getSquaresInRoom().get(color).forEach(x-> targets.addAll(x.getPlayersOnSquare()));
            attackHandler.setTargetsToShot(targets);
        }else {
            Set<Square> squares=new HashSet<>(currentPlayer.getPosition().getNeighbourSquare());
            squares.remove(currentPlayer.getPosition());
            squares=squares.stream().filter(x->!x.getPlayersOnSquare().isEmpty()).collect(Collectors.toSet());
            attackHandler.setTargetsToShot(MessageHandler.chooseSquare(currentPlayer,squares, room).getPlayersOnSquare());
            //TODO  se messaggio da qualche errore come devo gestire cioè quando mi null il targers.
        }
    }
}


/**
 *  need launch first "VisiblePlayers"
 */
class Heatseekker implements Operation{
    @Override
    public void execute(Room room) {
        AttackHandler attackHandler=room.getAttackHandler();
        List<Player> possibleTarets=new ArrayList<>(room.getPlayers());
        possibleTarets.removeAll(attackHandler.getPossibleTargets());
        attackHandler.setPossibleTargets(possibleTarets);
    }
}

/**
 *
 */
class DirectionTargets implements Operation{
    private int distance;
    private boolean penetrate;

    DirectionTargets(int distance, Boolean penetrate){
        this.distance=distance;
        this.penetrate=penetrate;
    }
    @Override
    public void execute(Room room) throws TimeoutException{
        AttackHandler attackHandler=room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        Map<Square.Direction,Set<Square>> map;

        if (!penetrate)
            map = currentPlayer.getPosition().directions(distance);
        else
            map= currentPlayer.getPosition().directionAbsolute(room.getBoard().getMap());

        map=map.entrySet().stream()
                .filter(x -> x.getValue().stream().anyMatch(s -> s.getPlayersOnSquare().isEmpty()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Square.Direction choice = MessageHandler.chooseDirection(currentPlayer,new ArrayList<>(map.keySet()),room);
        //TODO  se messaggio da qualche errore come devo gestire cioè quando mi null il targers.

        Set<Square> squares=map.get(choice);
        List<Player> possibleTargets=new ArrayList<>();
        squares.forEach(x->possibleTargets.addAll(x.getPlayersOnSquare()));
        attackHandler.setPossibleTargets(possibleTargets);
    }
}




class SelectAllTarget implements Operation{
    @Override
    public void execute(Room room) {
        AttackHandler attackHandler=room.getAttackHandler();
        attackHandler.setTargetsToShot(new ArrayList<>(attackHandler.getPossibleTargets()));
        attackHandler.setPossibleTargets(new ArrayList<>());
    }
}

class MoveToTarget implements Operation{
    @Override
    public void execute(Room room) {
        Player currentPlayer = room.getCurrentPlayer();
        currentPlayer.movePlayer(room.getAttackHandler().getTargetsToShot().get(0).getPosition());
    }
}

class MoveTarget implements Operation{
    private int distance;
    MoveTarget(int distance){
        this.distance=distance;
    }
    @Override
    public void execute(Room room) throws TimeoutException{
        Player currentPlayer = room.getCurrentPlayer();
        Player target = room.getAttackHandler().getTargetsToShot().get(0); // dovrebbe essere sempre uno solo quando lancia questo operazione
        Set<Square> validSquare;
        validSquare=target.getPosition().getValidPosition(distance);
        target.movePlayer(MessageHandler.chooseSquare(currentPlayer,validSquare, room));
        //TODO  se messaggio da qualche errore come devo gestire cioè quando mi null il targers.
    }
}

class SetTargetPositionAsEffectSquare implements Operation{
    @Override
    public void execute(Room room) {
        AttackHandler attackHandler=room.getAttackHandler();
        Player target = attackHandler.getTargetsToShot().get(0);
        attackHandler.setEffectSquare(target.getPosition());
    }
}

class TargetOnEffectSquare implements Operation{
    @Override
    public void execute(Room room) {
        AttackHandler attackHandler=room.getAttackHandler();
        List<Player> targets=new ArrayList<>(attackHandler.getEffectSquare().getPlayersOnSquare());
        targets.addAll(attackHandler.getSelectedTargets());
        targets=targets.stream().distinct().collect(Collectors.toList());
        attackHandler.setPossibleTargets(targets);
    }
}


class ThorTargets implements Operation{
    private int index;
    ThorTargets(int index){
        this.index=index;
    }
    @Override
    public void execute(Room room) {
        AttackHandler attackHandler=room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        Set<Square> visible = new HashSet<>(attackHandler.getSelectedTargets().get(index).getPosition().visibleSquare(room.getBoard().getMap()));
        ArrayList<Player> visiblePlayers = new ArrayList<>();

        visible.forEach(x-> visiblePlayers.addAll(x.getPlayersOnSquare()));
        visiblePlayers.remove(currentPlayer);
        visiblePlayers.removeAll(attackHandler.getSelectedTargets());

        attackHandler.setPossibleTargets(visiblePlayers);
    }
}

class NextSquareInDirection implements Operation {
    @Override
    public void execute(Room room) {
        AttackHandler attackHandler = room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        Square targetPosition = attackHandler.getSelectedTargets().get(0).getPosition();
        Square currentPlayerPosition = currentPlayer.getPosition();
        int diffx = currentPlayerPosition.getX() - targetPosition.getX();
        int diffy = currentPlayerPosition.getY() - targetPosition.getY();
        Square nextSquare = null;
        if (diffx == 0) {
            nextSquare = targetPosition.getOneOfNeighbour(targetPosition.getX(), targetPosition.getY() - diffy);
        } else if (diffy == 0) {
            nextSquare = targetPosition.getOneOfNeighbour(targetPosition.getX() - diffx, targetPosition.getY());
        }
        attackHandler.setEffectSquare(nextSquare);


    }
}

class Flamethorwer implements Operation {
    @Override
    public void execute(Room room) throws NotExecutedException,TimeoutException {
        AttackHandler attackHandler = room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        Map<Square.Direction, Set<Square>> map = currentPlayer.getPosition().directions(2)
                .entrySet().stream()
                .filter(x -> x.getValue().stream().anyMatch(s -> s.getPlayersOnSquare().isEmpty()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (map.size() == 0)
            throw new NotExecutedException("haven't targets can be shot");

        Square.Direction choice = MessageHandler.chooseDirection(currentPlayer,new ArrayList<>(map.keySet()),room);
        //TODO  se messaggio da qualche errore come devo gestire cioè quando mi null il targers.

        List<Square> squares = new ArrayList<>(map.get(choice));
        squares.remove(currentPlayer.getPosition());
        for (Square s:map.get(choice)){
            if (currentPlayer.getPosition().getNeighbourSquare().contains(s)) {
                attackHandler.setPossibleTargets(squares.get(0).getPlayersOnSquare());
                new SelectAllTarget().execute(room);
                new Damage(2).execute(room);
            }
            else {
                attackHandler.setPossibleTargets(squares.get(1).getPlayersOnSquare());
                new SelectAllTarget().execute(room);
                new Damage(1).execute(room);
            }
        }
    }
}

class Repel implements Operation{
    private int distance;
    Repel (int distance){
        this.distance=distance;
    }
    @Override
    public void execute(Room room) throws TimeoutException{
        AttackHandler attackHandler = room.getAttackHandler();
        Player target = attackHandler.getTargetsToShot().get(0);
        Set<Square> validPosition = new HashSet<>();
        target.getPosition().directions(distance).forEach((key,value)->validPosition.addAll(value));
        target.movePlayer(MessageHandler.chooseSquare(room.getCurrentPlayer(),validPosition, room));
        //TODO  se messaggio da qualche errore come devo gestire cioè quando mi null il targers.
    }
}
 class AllPossibleTargets implements Operation{
     @Override
     public void execute(Room room) {
         List<Player> players = new ArrayList<>(room.getPlayers());
         players.remove(room.getCurrentPlayer());
         players = players.stream().filter(x->x.getPosition()!=null).collect(Collectors.toList());
         room.getAttackHandler().setPossibleTargets(players);
     }
 }

class TargetingScope implements Operation{
    @Override
    public void execute(Room room) throws NotExecutedException,TimeoutException {
        AttackHandler attackHandler = room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        List<AmmoColor> ammoColors = currentPlayer.allAmmo().entrySet().stream().filter(x->x.getValue()>0).map(Map.Entry::getKey).collect(Collectors.toList());
        AmmoColor ammoColor = MessageHandler.chooseAmmoColor(currentPlayer,ammoColors,room);
        //TODO  se messaggio da qualche errore come devo gestire cioè quando mi null il targers.
        try {
            ActionHandler.payment(currentPlayer,Collections.singletonList(ammoColor),room);
        } catch (NotEnoughException e) {
            throw new NotExecutedException(e.getMessage());
        }
        attackHandler.setPossibleTargets(new ArrayList<>(attackHandler.getDamaged().keySet()));
    }
}


class Teleporter implements Operation{
    @Override
    public void execute(Room room) throws TimeoutException{
        Player currentPlayer = room.getCurrentPlayer();
        currentPlayer.movePlayer(MessageHandler.chooseSquare(currentPlayer,room.getBoard().getMap().allSquares(), room));
        //TODO  se messaggio da qualche errore come devo gestire cioè quando mi null il targers.

    }
}







