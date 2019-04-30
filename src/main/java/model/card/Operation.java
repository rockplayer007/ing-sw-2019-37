package model.card;

import model.board.Color;
import model.board.Square;
import model.gamehandler.Room;
import model.player.ActionHandler;
import model.player.Player;

import java.util.*;
import java.util.stream.Collectors;

public interface Operation {

    void execute(Room room);
}

class VisiblePlayers implements Operation{

    @Override
    public void execute(Room room){
        Player currentPlayer = room.getCurrentPlayer();
        Set<Square> visible = new HashSet<>(currentPlayer.getPosition().visibleSquare(room.getBoard().getMap()));
        ArrayList<Player> visiblePlayers = new ArrayList<>();

        visible.forEach(x-> visiblePlayers.addAll(x.getPlayersOnSquare()));
        visiblePlayers.remove(currentPlayer);

        currentPlayer.setPossibleTargets(visiblePlayers);
    }
}

class SelectTargets implements Operation{
    private int numberTragets;
    Boolean distinctSquare;

    public SelectTargets(int number,Boolean distinctSquare){
        numberTragets=number;
        this.distinctSquare=distinctSquare;
    }

    @Override
    public void execute(Room room){
        Player currentPlayer = room.getCurrentPlayer();
        if (!distinctSquare) {
            //TODO ask player what target he wants
            //available players in currentPlayer.getPossibleTargets
            //to set the currentPlayer.setSelectedTargets AND REMOVE FROM VISIBLE PLAYERS
        }else {
//            TODO the targets choice need be different square
        }
    }

}

class Damage implements Operation{

    private int points;

    public Damage(int points){
        this.points = points;
    }

    @Override
    public void execute(Room room){
        Player currentPlayer = room.getCurrentPlayer();
        List<Player> targets = currentPlayer.getTargetsToShot();
        for(int i = 0; i < points; i++){
            targets.forEach(x->x.getPlayerBoard().addDamage(currentPlayer));
        }
    }
}

class Mark implements Operation{

    private int points;

    public Mark(int points){
        this.points = points;
    }

    @Override
    public void execute(Room room){
        Player currentPlayer = room.getCurrentPlayer();
        List<Player> selectedPlayers = currentPlayer.getSelectedTargets();
        for(int i = 0; i < points; i++){
            selectedPlayers.forEach(x->x.getPlayerBoard().addMark(currentPlayer));
        }

    }
}


class Run implements Operation{
    private int distance;

    public Run(int distance){
        this.distance=distance;
    }

    @Override
    public void execute(Room room){
        Player currentPlayer = room.getCurrentPlayer();
        currentPlayer.movePlayer(ActionHandler.chooseSquare(currentPlayer,currentPlayer.getPosition().getValidPosition(distance)));
    }
}


/**
 * if need to remove the target in base minDistance set "isMaxDistance" as false, and the other way around.
 */
class MinOrMaxDistance implements Operation{
    private int distance;
    private Boolean isMaxDistance;

    public MinOrMaxDistance(int distance,Boolean isMaxDistance){
        this.distance=distance;
        this.isMaxDistance = isMaxDistance;
    }

    @Override
    public void execute(Room room) {
        Player currentPlayer = room.getCurrentPlayer();
        Set<Square> possibleSquares =new HashSet<>(currentPlayer.getPosition().getValidPosition(distance));
        List<Player> possiblePlayers = new ArrayList<>();

        possibleSquares.forEach(x-> possiblePlayers.addAll(x.getPlayersOnSquare()));
        possiblePlayers.remove(currentPlayer);

        if (isMaxDistance)
            currentPlayer.setPossibleTargets(possiblePlayers);
        else
            currentPlayer.getPossibleTargets().removeAll(possiblePlayers);
    }
}

class SameSquare implements Operation{
    @Override
    public void execute(Room room) {
        Player currentPlayer = room.getCurrentPlayer();
        List<Player> possiblePlayers = new ArrayList<>(currentPlayer.getPosition().getPlayersOnSquare());
        currentPlayer.setPossibleTargets(possiblePlayers);
    }
}

/**
 * if yourSquare is false need launch first "VisiblePlayers"
 */
class AddPossibleTargetBeforeMove implements Operation{
    private int distance;
    private Boolean yourSquare;

    public AddPossibleTargetBeforeMove (int distance,Boolean yourSquare){
        this.distance=distance;
        this.yourSquare=yourSquare;
    }

    @Override
    public void execute(Room room) {
        Player currentPlayer = room.getCurrentPlayer();
        List<Player> possiblePlayers = new ArrayList<>(room.getPlayers());
        Set<Square> visibleSquare=new HashSet<>(currentPlayer.getPosition().visibleSquare(room.getBoard().getMap()));
        if (!yourSquare){
            possiblePlayers.removeAll(currentPlayer.getPossibleTargets());
            possiblePlayers=possiblePlayers.stream()
                    .filter(player-> player.getPosition().getValidPosition(distance).stream().anyMatch(visibleSquare::contains))
                    .collect(Collectors.toList());
            currentPlayer.getPossibleTargets().addAll(possiblePlayers);
        }else {
            possiblePlayers=possiblePlayers.stream()
                    .filter(player-> player.getPosition().getValidPosition(distance).contains(currentPlayer.getPosition()))
                    .collect(Collectors.toList());
            currentPlayer.setPossibleTargets(possiblePlayers);
        }

    }
}


class  MoveTargetToVisible implements Operation{
    private int distance;

    public MoveTargetToVisible(int distance){
        this.distance = distance;
    }

    @Override
    public void execute(Room room) {
        Player currentPlayer = room.getCurrentPlayer();
        Player target = currentPlayer.getTargetsToShot().get(0); // dovrebbe essere sempre uno solo quando lancia questo operazione
        Set<Square> visibleSquare=currentPlayer.getPosition().visibleSquare(room.getBoard().getMap());
        Set<Square> validSquare;
        validSquare=target.getPosition().getValidPosition(distance).stream().filter(visibleSquare::contains).collect(Collectors.toSet());
        target.movePlayer(ActionHandler.chooseSquare(currentPlayer,validSquare));//TODO DA controllare.
    }
}

class Vortex implements Operation{
    @Override
    public void execute(Room room) {
        Player currentPlayer = room.getCurrentPlayer();
        List<Player> possibleTargets = new ArrayList<>();
        Set<Square> visibleSquare=currentPlayer.getPosition().visibleSquare(room.getBoard().getMap())
                .stream()
                .filter(x->x.getValidPosition(1).stream().anyMatch(p->p.getPlayersOnSquare().size()>0))
                .collect(Collectors.toSet());
        Square vortex=ActionHandler.chooseSquare(currentPlayer,visibleSquare);//TODO DA controllare.
        vortex.getValidPosition(1).forEach(x-> possibleTargets.addAll(x.getPlayersOnSquare()));
        currentPlayer.setPossibleTargets(possibleTargets);
        currentPlayer.setEffectSquare(vortex);
    }
}

class MoveTargetToEffevtSquare implements Operation{
    @Override
    public void execute(Room room) {
        Player currentPlayer = room.getCurrentPlayer();
        currentPlayer.getTargetsToShot().forEach(x->x.movePlayer(currentPlayer.getEffectSquare()));
    }
}

class Furance implements Operation{
    Boolean selectSquare;

    public Furance(Boolean selectSquare){
        this.selectSquare=selectSquare;
    }
    @Override
    public void execute(Room room) {
        Player currentPlayer = room.getCurrentPlayer();
        if (!selectSquare){
            Set<Color> rooms= new HashSet<>();
            currentPlayer.getPosition().getNeighbourSquare().forEach(x->rooms.add(x.getColor()));
            rooms.remove(currentPlayer.getPosition().getColor());
//          TODO far scegliere stanza.

        }else {
            Set<Square> squares=new HashSet<>(currentPlayer.getPosition().getNeighbourSquare());
            squares.remove(currentPlayer.getPosition());
            squares=squares.stream().filter(x->!x.getPlayersOnSquare().isEmpty()).collect(Collectors.toSet());
            currentPlayer.setTargetsToShot(ActionHandler.chooseSquare(currentPlayer,squares).getPlayersOnSquare());//TODO DA controllare.
        }
    }
}


/**
 *  need launch first "VisiblePlayers"
 */
class Heatseekker implements Operation{
    @Override
    public void execute(Room room) {
        Player currentPlayer = room.getCurrentPlayer();
        List<Player> possibleTarets=new ArrayList<>(room.getPlayers());
        possibleTarets.removeAll(currentPlayer.getPossibleTargets());
        currentPlayer.setPossibleTargets(possibleTarets);
    }
}

class Flamethorwer implements Operation{
    private int distance;

    public Flamethorwer(int distance){
        this.distance=distance;
    }
    @Override
    public void execute(Room room) {
        Player currentPlayer = room.getCurrentPlayer();
        Map<String,Set<Square>> map= currentPlayer.getPosition().directions(distance);
        Set<String> direction=map.keySet();
        //Todo da vedere con messagio per fare la scelta;
        String choise="";

        Set<Square> squares=map.get(choise);
        squares.remove(currentPlayer.getPosition());
        List<Player> possibleTargets=new ArrayList<>();
        squares.forEach(x->possibleTargets.addAll(x.getPlayersOnSquare()));
        currentPlayer.setPossibleTargets(possibleTargets);
    }
}


class SelectAllTarget implements Operation{
    @Override
    public void execute(Room room) {
        Player currentPlayer = room.getCurrentPlayer();
        currentPlayer.setTargetsToShot(currentPlayer.getPossibleTargets());
        currentPlayer.setPossibleTargets(new ArrayList<>());
    }
}

class MoveToTarget implements Operation{
    @Override
    public void execute(Room room) {
        Player currentPlayer = room.getCurrentPlayer();
        currentPlayer.movePlayer(currentPlayer.getTargetsToShot().get(0).getPosition());
    }
}

class MoveTarget implements Operation{
    private int distance;
    public MoveTarget(int distance){
        this.distance=distance;
    }
    @Override
    public void execute(Room room) {
        Player currentPlayer = room.getCurrentPlayer();
        Player target = currentPlayer.getTargetsToShot().get(0); // dovrebbe essere sempre uno solo quando lancia questo operazione
        Set<Square> validSquare;
        validSquare=target.getPosition().getValidPosition(distance);
        target.movePlayer(ActionHandler.chooseSquare(currentPlayer,validSquare));//TODO DA controllare.
    }
}

class SetTargetPositionAsEffectSquare implements Operation{
    @Override
    public void execute(Room room) {
        Player currentPlayer = room.getCurrentPlayer();
        Player target = currentPlayer.getTargetsToShot().get(0);
        currentPlayer.setEffectSquare(target.getPosition());
    }
}

class TargetonEffectSquare implements Operation{
    @Override
    public void execute(Room room) {
        Player currentPlayer = room.getCurrentPlayer();
        List<Player> targets=new ArrayList<>(currentPlayer.getEffectSquare().getPlayersOnSquare());
        targets.addAll(currentPlayer.getSelectedTargets());
        targets=targets.stream().distinct().collect(Collectors.toList());
        currentPlayer.setTargetsToShot(targets);
    }
}

class RaingunTrgets implements Operation{
    int index;
    public RaingunTrgets(int index){
        this.index=index;
    }
    @Override
    public void execute(Room room) {
        Player currentPlayer = room.getCurrentPlayer();
        Set<Square> visible = new HashSet<>(currentPlayer.getSelectedTargets().get(index).getPosition().visibleSquare(room.getBoard().getMap()));
        ArrayList<Player> visiblePlayers = new ArrayList<>();

        visible.forEach(x-> visiblePlayers.addAll(x.getPlayersOnSquare()));
        visiblePlayers.remove(currentPlayer);
        visiblePlayers.removeAll(currentPlayer.getSelectedTargets());

        currentPlayer.setPossibleTargets(visiblePlayers);
    }
}

class NextSquareInDirection implements Operation{
    @Override
    public void execute(Room room) {
        Player currentPlayer = room.getCurrentPlayer();
        Square targetPosition=currentPlayer.getSelectedTargets().get(0).getPosition();
        Square currentPlayerPosiction=currentPlayer.getPosition();
        int diffx=currentPlayerPosiction.getX()-targetPosition.getX();
        int diffy=currentPlayerPosiction.getY()-targetPosition.getY();
        Square nextSquare=null;
        if (diffx==0){
            nextSquare=targetPosition.getOneofNeighbour(targetPosition.getX(),targetPosition.getY()-diffy);
        }else if (diffy==0){
            nextSquare=targetPosition.getOneofNeighbour(targetPosition.getX()-diffx,targetPosition.getY());
        }
        if (nextSquare!=null)
            currentPlayer.setEffectSquare(nextSquare);
        else
            System.out.println("haven't next square");//TODO da controllare.

    }
}


//边走边打，。






