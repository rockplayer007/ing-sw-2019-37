package model.card;

import controller.MessageHandler;
import controller.ActionHandler;
import model.exceptions.InterruptOperationException;
import model.gamehandler.Room;
import model.board.Color;
import model.board.Square;
import model.exceptions.NotEnoughException;
import model.gamehandler.AttackHandler;
import model.exceptions.NotExecutedException;
import model.player.Player;

import java.util.*;
import model.exceptions.TimeFinishedException;
import java.util.stream.Collectors;

public interface Operation {

    /**
     * @param room the room that this operation is used
     * @throws NotExecutedException when the effect is non execute
     * @throws TimeFinishedException when the timer of players turn is finished during execution of effect
     * @throws InterruptOperationException when the effect is interrupt but that effect worked.
     */
    void execute(Room room)throws NotExecutedException, TimeFinishedException, InterruptOperationException;
}

class InfoClass {
    static final String NOT_POSSIBLE_PLAYERS_CAN_BE_SHOOT = "there are not possible players can be shoot ";
    static final String CHOOSE_SQUARE = "Which square do you want to move the target?";
    static final String CHOOSE_PLAYER = "Which player do you want to attack?";

    private InfoClass() {
        throw new IllegalStateException("Utility class");
    }
}

/**
 * Set the visiblePlayers as possibleTargets in the AttackHandler
 */
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

/**
 * Select "numberTargets" targets form possibleTargets in the AttackHandler, and depend on the attribute "distinctSquare"
 */
class SelectTargets implements Operation{
    private int numberTargets;
    private Boolean distinctSquare;

    SelectTargets(int number,Boolean distinctSquare){
        numberTargets =number;
        this.distinctSquare=distinctSquare;
    }

    @Override
    public void execute(Room room)throws NotExecutedException, TimeFinishedException{
        AttackHandler attackHandler=room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        List<Player> possibleTargets =attackHandler.getPossibleTargets();
        if (possibleTargets.isEmpty())
            throw new NotExecutedException(InfoClass.NOT_POSSIBLE_PLAYERS_CAN_BE_SHOOT);
        List<Player> targets=new ArrayList<>();
        if (distinctSquare) {
            for (int i = 0; i <numberTargets&&!possibleTargets.isEmpty(); i++){
                targets.addAll(Objects.requireNonNull(MessageHandler.choosePlayers(currentPlayer, possibleTargets, 1, room, InfoClass.CHOOSE_PLAYER)));
                Square targetPostion = targets.get(i).getPosition();
                possibleTargets = possibleTargets.stream().filter(x->x.getPosition()!=targetPostion).collect(Collectors.toList());
            }

        }else {
           targets = MessageHandler.choosePlayers(currentPlayer,possibleTargets, numberTargets,room,InfoClass.CHOOSE_PLAYER);
        }
        if (targets != null)
            possibleTargets.removeAll(targets);

        attackHandler.setTargetsToShoot(targets);
    }

}

/**
 * Select "numberTargets" targets form selectedTargets in the AttackHandler
 */
class SelectFromSelectedTargets implements Operation{
    private int numberTargets;
    SelectFromSelectedTargets(int number) {
        numberTargets = number;
    }

    @Override
    public void execute(Room room) throws NotExecutedException, TimeFinishedException{
        AttackHandler attackHandler=room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        List<Player> selectedTargets =attackHandler.getSelectedTargets();
        if (selectedTargets.isEmpty())
            throw new NotExecutedException(InfoClass.NOT_POSSIBLE_PLAYERS_CAN_BE_SHOOT);
        List<Player> targets = MessageHandler.choosePlayers(currentPlayer,selectedTargets, numberTargets,room,InfoClass.CHOOSE_PLAYER);

        if (targets != null)
            selectedTargets.removeAll(targets);

        attackHandler.setTargetsToShoot(targets);

    }
}

/**
 * Give "points" damage to all targets form targetsToShoot in the AttackHandler
 */
class Damage implements Operation{

    private int points;

    Damage(int points){
        this.points = points;
    }

    @Override
    public void execute(Room room){
        AttackHandler attackHandler=room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        List<Player> targets = attackHandler.getTargetsToShoot();
        targets.forEach(x->attackHandler.addDamage(x,points));
        targets.forEach(x->x.getPlayerBoard().addDamage(currentPlayer,points));

    }
}

/**
 * Give "points" marks to all targets form targetsToShoot in the AttackHandler
 */
class Mark implements Operation{

    private int points;

    Mark(int points){
        this.points = points;
    }

    @Override
    public void execute(Room room){
        AttackHandler attackHandler = room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        List<Player> targets = attackHandler.getTargetsToShoot();
        targets.forEach(x->attackHandler.addmark(x,points));
        targets.forEach(x->x.getPlayerBoard().addMark(currentPlayer,points));


    }
}

/**
 * Set the targetsToShoot as selectedTargets in the AttackHandler
 */
class SetTargetToSelected implements Operation{
    @Override
    public void execute(Room room) {
        AttackHandler attackHandler=room.getAttackHandler();
        List<Player> targets =new ArrayList<>(attackHandler.getTargetsToShoot());
        List<Player> selectedTargets = attackHandler.getSelectedTargets();

        for (Player p:targets) {
            if (selectedTargets.contains(p))
                selectedTargets.remove(p);
            else
                selectedTargets.add(p);
        }
    }
}


/**
 * This operation allow the player move depend by attribute "distance"
 */
class Run implements Operation{
    private int distance;

    Run(int distance){
        this.distance=distance;
    }

    @Override
    public void execute(Room room) throws TimeFinishedException{
        Player currentPlayer = room.getCurrentPlayer();
        currentPlayer.movePlayer(MessageHandler.chooseSquare(currentPlayer,currentPlayer.getPosition().getValidPosition(distance), room,"Which square do you like to move?"));
    }
}


/**
 * This operation allow get and set possibleTargets depend attribute "distance"
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
 * This operation allow get possible targets before move "distance" and depend by attribute "yourSquare"
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
        possiblePlayers.remove(currentPlayer);
        possiblePlayers = possiblePlayers.stream().filter(x->x.getPosition()!=null).collect(Collectors.toList());

        Set<Square> visibleSquare = currentPlayer.getPosition().visibleSquare(room.getBoard().getMap());
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

/**
 * This operation allow to move the targets to visible whit max distance defined by attribute "distance"
 */
class  MoveTargetToVisible implements Operation{
    private int distance;

    MoveTargetToVisible(int distance){
        this.distance = distance;
    }

    @Override
    public void execute(Room room) throws NotExecutedException, TimeFinishedException{
        Player currentPlayer = room.getCurrentPlayer();
        Player target = room.getAttackHandler().getTargetsToShoot().get(0); // dovrebbe essere sempre uno solo quando lancia questo operazione
        if (target == null)
            throw new NotExecutedException(InfoClass.NOT_POSSIBLE_PLAYERS_CAN_BE_SHOOT);
        Set<Square> visibleSquare = currentPlayer.getPosition().visibleSquare(room.getBoard().getMap());
        Set<Square> validSquare;
        validSquare = target.getPosition().getValidPosition(distance).stream().filter(visibleSquare::contains).collect(Collectors.toSet());

        target.movePlayer(MessageHandler.chooseSquare(currentPlayer,validSquare, room,InfoClass.CHOOSE_SQUARE));
    }
}

/**
 * Set player position as effectSquare(attribute of AttackHandler)
 */
class SetPlayerPositionAsEffectSquare implements Operation{
    @Override
    public void execute(Room room) {
        Player currentPlayer = room.getCurrentPlayer();
        room.getAttackHandler().setEffectSquare(currentPlayer.getPosition());
    }
}

/**
 * Select a square to set as effectSquare(attribute of AttackHandler) depend by attribute "zone"
 */
class SelectEffectSquare implements Operation{
    private int zone; // if zone=1 "vortex", if zone=0 "select effect square"

    SelectEffectSquare(int zone){
        this.zone=zone;
    }
    @Override
    public void execute(Room room) throws NotExecutedException, TimeFinishedException{
        AttackHandler attackHandler=room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        List<Player> possibleTargets = new ArrayList<>();
        Set<Square> visibleSquare=currentPlayer.getPosition().visibleSquare(room.getBoard().getMap())
                    .stream()
                    .filter(x -> x.getValidPosition(zone).stream().anyMatch(p -> !p.getPlayersOnSquare().isEmpty()))
                    .collect(Collectors.toSet());
        if (visibleSquare.isEmpty())
            throw new NotExecutedException("there are not possible Square can be choose ");

        Square effectSquare= MessageHandler.chooseSquare(currentPlayer,visibleSquare, room,"Which square do you want to apply this effect?");
        effectSquare.getValidPosition(zone).forEach(x-> possibleTargets.addAll(x.getPlayersOnSquare()));
        possibleTargets.remove(currentPlayer);
        attackHandler.setPossibleTargets(possibleTargets);
        attackHandler.setEffectSquare(effectSquare);
    }
}

/**
 * Move all targetToShoot to effectSquare(a attribute of AttackHandler)
 */
class MoveTargetToEffectSquare implements Operation{
    @Override
    public void execute(Room room) {
        AttackHandler attackHandler=room.getAttackHandler();
        attackHandler.getTargetsToShoot().forEach(x->x.movePlayer(attackHandler.getEffectSquare()));
    }
}

/**
 * A operation for weapon Furance
 */
class Furance implements Operation{
    private Boolean selectSquare;

    Furance(Boolean selectSquare){
        this.selectSquare=selectSquare;
    }
    @Override
    public void execute(Room room) throws NotExecutedException, TimeFinishedException{
        AttackHandler attackHandler=room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        if (!selectSquare){
            Set<Color> rooms= new HashSet<>();
            List<Player> targets = new ArrayList<>();
            currentPlayer.getPosition().getNeighbourSquare().forEach(x->rooms.add(x.getColor()));
            rooms.remove(currentPlayer.getPosition().getColor());
            if (rooms.isEmpty())
                throw new NotExecutedException("there are not possible room can be choose ");
            Color color = MessageHandler.chooseRoom(currentPlayer,new ArrayList<>(rooms),room);
            room.getBoard().getMap().getSquaresInRoom().get(color).forEach(x-> targets.addAll(x.getPlayersOnSquare()));
            attackHandler.setTargetsToShoot(targets);
        }else {
            Set<Square> squares=new HashSet<>(currentPlayer.getPosition().getNeighbourSquare());
            squares.remove(currentPlayer.getPosition());
            squares=squares.stream().filter(x->!x.getPlayersOnSquare().isEmpty()).collect(Collectors.toSet());
            if (squares.isEmpty())
                throw new NotExecutedException(InfoClass.NOT_POSSIBLE_PLAYERS_CAN_BE_SHOOT);
            attackHandler.setTargetsToShoot(MessageHandler.chooseSquare(currentPlayer,squares, room,InfoClass.CHOOSE_PLAYER).getPlayersOnSquare());
        }
    }
}


/**
 *  A operation for weapon Heatseeker, for get the player that can not see on the map and set they as possibleTargets
 *  need launch first "VisiblePlayers"
 */
class Heatseeker implements Operation{
    @Override
    public void execute(Room room) {
        AttackHandler attackHandler=room.getAttackHandler();
        List<Player> possibleTargets=new ArrayList<>(room.getPlayers());
        possibleTargets = possibleTargets.stream().filter(x->x.getPosition()!=null).collect(Collectors.toList());
        possibleTargets.removeAll(attackHandler.getPossibleTargets());
        possibleTargets.remove(room.getCurrentPlayer());
        attackHandler.setPossibleTargets(possibleTargets);
    }
}

/**
 * set PossibleTargets find on one direction depend by attributes "distance" and "penetrate"
 */
class DirectionTargets implements Operation{
    private int distance;
    private boolean penetrate;

    DirectionTargets(int distance, Boolean penetrate){
        this.distance=distance;
        this.penetrate=penetrate;
    }
    @Override
    public void execute(Room room) throws NotExecutedException, TimeFinishedException{
        AttackHandler attackHandler=room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        Map<Square.Direction,Set<Square>> map;

        if (!penetrate)
            map = currentPlayer.getPosition().directions(distance);
        else {
            map = currentPlayer.getPosition().directionAbsolute(room.getBoard().getMap());
            map.forEach((Square.Direction d,Set<Square> s)->s.add(currentPlayer.getPosition()));
        }

        map=map.entrySet().stream()
                .filter(x -> x.getValue().stream().anyMatch(s ->(!s.getPlayersOnSquare().isEmpty())&&s.getPlayersOnSquare().stream().anyMatch(p->p!=currentPlayer)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (map.isEmpty())
            throw new NotExecutedException(InfoClass.NOT_POSSIBLE_PLAYERS_CAN_BE_SHOOT);

        Square.Direction choice = MessageHandler.chooseDirection(currentPlayer,new ArrayList<>(map.keySet()),room,"Which direction do you want to attack?");

        Set<Square> squares=map.get(choice);
        List<Player> possibleTargets=new ArrayList<>();
        squares.forEach(x->possibleTargets.addAll(x.getPlayersOnSquare()));
        possibleTargets.remove(currentPlayer);
        attackHandler.setPossibleTargets(possibleTargets);
    }
}


/**
 * set all player in the possibleTarget as targetsToShoot
 */
class SelectAllTarget implements Operation{
    @Override
    public void execute(Room room) throws NotExecutedException {
        AttackHandler attackHandler=room.getAttackHandler();
        List<Player> possibleTargets = attackHandler.getPossibleTargets();
        if (possibleTargets.isEmpty())
            throw new NotExecutedException(InfoClass.NOT_POSSIBLE_PLAYERS_CAN_BE_SHOOT);
        attackHandler.setTargetsToShoot(new ArrayList<>(possibleTargets));
        attackHandler.setPossibleTargets(new ArrayList<>());
    }
}

/**
 * Move currentPlayer to target's position
 */
class MoveToTarget implements Operation{
    @Override
    public void execute(Room room) {
        Player currentPlayer = room.getCurrentPlayer();
        currentPlayer.movePlayer(room.getAttackHandler().getTargetsToShoot().get(0).getPosition());
    }
}

/**
 * Move the target depend by attributes "distance" and "penetrate"
 */
class MoveTarget implements Operation{
    private int distance;
    MoveTarget(int distance){
        this.distance=distance;
    }
    @Override
    public void execute(Room room) throws NotExecutedException, TimeFinishedException{
        Player currentPlayer = room.getCurrentPlayer();
        Player target = room.getAttackHandler().getTargetsToShoot().get(0); // dovrebbe essere sempre uno solo quando lancia questo operazione
        if (target == null)
            throw new NotExecutedException("there are not possible players can be move ");
        Set<Square> validSquare;
        validSquare=target.getPosition().getValidPosition(distance);
        target.movePlayer(MessageHandler.chooseSquare(currentPlayer,validSquare, room,InfoClass.CHOOSE_SQUARE));
    }
}

class SetTargetPositionAsEffectSquare implements Operation{
    @Override
    public void execute(Room room)throws NotExecutedException {
        AttackHandler attackHandler=room.getAttackHandler();
        Player target = attackHandler.getTargetsToShoot().get(0);
        if (target == null)
            throw new NotExecutedException("there are not target choice");
        attackHandler.setEffectSquare(target.getPosition());
    }
}

/**
 * Set all player on the effectSquare as possibleTargets
 */
class TargetOnEffectSquare implements Operation{
    @Override
    public void execute(Room room) throws InterruptOperationException{
        AttackHandler attackHandler=room.getAttackHandler();
        if (attackHandler.getEffectSquare()==null)
            throw new InterruptOperationException("You are end of map have not next square");
        List<Player> targets = new ArrayList<>(attackHandler.getEffectSquare().getPlayersOnSquare());
        attackHandler.setPossibleTargets(targets);
    }
}


/**
 * A operation for weapon T.H.O.R
 */
class ThorTargets implements Operation{
    private int index;
    ThorTargets(int index){
        this.index=index;
    }
    @Override
    public void execute(Room room) throws NotExecutedException {
        AttackHandler attackHandler=room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        if (index>attackHandler.getSelectedTargets().size()-1)
            throw new NotExecutedException("You can not use this effect");
        Set<Square> visible = new HashSet<>(attackHandler.getSelectedTargets().get(index).getPosition().visibleSquare(room.getBoard().getMap()));
        ArrayList<Player> visiblePlayers = new ArrayList<>();

        visible.forEach(x-> visiblePlayers.addAll(x.getPlayersOnSquare()));
        visiblePlayers.remove(currentPlayer);
        visiblePlayers.removeAll(attackHandler.getSelectedTargets());

        attackHandler.setPossibleTargets(visiblePlayers);
    }
}

/**
 * set next square in direction currentPlayer to target as effectSquare
 */
class NextSquareInDirection implements Operation {
    @Override
    public void execute(Room room){
        AttackHandler attackHandler = room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        Square targetPosition = attackHandler.getTargetsToShoot().get(0).getPosition();
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

/**
 * A operation for weapon Flamenthorwer
 */
class Flamethorwer implements Operation {
    @Override
    public void execute(Room room) throws NotExecutedException,TimeFinishedException {
        AttackHandler attackHandler = room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        Map<Square.Direction, Set<Square>> map = currentPlayer.getPosition().directions(2)
                .entrySet().stream()
                .filter(x -> x.getValue().stream().anyMatch(s ->(!s.getPlayersOnSquare().isEmpty())&&s.getPlayersOnSquare().stream().anyMatch(p->p!=currentPlayer)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (map.size() == 0)
            throw new NotExecutedException("haven't targets can be shot");

        Square.Direction choice = MessageHandler.chooseDirection(currentPlayer,new ArrayList<>(map.keySet()),room,"Which direction do you want to attack?");

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

/**
 * A operation whit effect repel in a direction
 */
class Repel implements Operation{
    private int distance;
    Repel (int distance){
        this.distance=distance;
    }
    @Override
    public void execute(Room room) throws NotExecutedException, TimeFinishedException{
        AttackHandler attackHandler = room.getAttackHandler();
        Player target = attackHandler.getTargetsToShoot().get(0);
        if (target == null)
            throw new NotExecutedException("there are not possible players can be move ");
        Set<Square> validPosition = new HashSet<>();
        target.getPosition().directions(distance).forEach((key,value)->validPosition.addAll(value));
        validPosition.add(target.getPosition());
        target.movePlayer(MessageHandler.chooseSquare(room.getCurrentPlayer(),validPosition, room,InfoClass.CHOOSE_SQUARE));
    }
}

/**
 * set all players can find on the map as possiblePlayer
 */
 class AllPossibleTargets implements Operation{
     @Override
     public void execute(Room room) {
         List<Player> players = new ArrayList<>(room.getPlayers());
         players.remove(room.getCurrentPlayer());
         players = players.stream().filter(x->x.getPosition()!=null).collect(Collectors.toList());
         room.getAttackHandler().setPossibleTargets(players);
     }
 }

/**
 * a operation for powerup TargetingScope
 */
class TargetingScope implements Operation{
    @Override
    public void execute(Room room) throws NotExecutedException,TimeFinishedException {
        AttackHandler attackHandler = room.getAttackHandler();
        Player currentPlayer = room.getCurrentPlayer();
        List<AmmoColor> ammoColors = currentPlayer.allAmmo().entrySet().stream().filter(x->x.getValue()>0).map(Map.Entry::getKey).collect(Collectors.toList());
        Set<Player> possibleTargets = new HashSet<>(attackHandler.getDamaged().keySet());
        possibleTargets.addAll(attackHandler.getMarked().keySet());
        if (possibleTargets.isEmpty())
            throw new  NotExecutedException(InfoClass.NOT_POSSIBLE_PLAYERS_CAN_BE_SHOOT);
        if (ammoColors.isEmpty())
            throw new NotExecutedException("You have not enough ammo to pay");
        AmmoColor ammoColor = MessageHandler.chooseAmmoColor(currentPlayer,ammoColors,room);
        try {
            ActionHandler.payment(currentPlayer,Collections.singletonList(ammoColor),room);
        } catch (NotEnoughException e) {
            throw new NotExecutedException(e.getMessage());
        }

        attackHandler.setPossibleTargets(new ArrayList<>(possibleTargets));
    }
}


/**
 * Move the currentPlayer where he want
 */
class Teleporter implements Operation{
    @Override
    public void execute(Room room) throws TimeFinishedException{
        Player currentPlayer = room.getCurrentPlayer();
        currentPlayer.movePlayer(MessageHandler.chooseSquare(currentPlayer,room.getBoard().getMap().allSquares(), room,"Which square do you like to move?"));

    }
}

/**
 * Do a update for view
 */
class Update implements Operation{

    @Override
    public void execute(Room room){
        room.getRoomController().sendUpdate();
    }
}


/**
 * Launch InterruptOperationException
 */
class InterruptControll implements Operation{
    @Override
    public void execute(Room room) throws InterruptOperationException {
        if(room.getAttackHandler().getPossibleTargets().isEmpty())
            throw new InterruptOperationException("effect finished because there are not other possible players can be shoot ");
    }
}

/**
 *  add all player in the selectedTarget to possibleTargets
 */
class AddselectedTargetToPossibleTarget implements Operation{
    @Override
    public void execute(Room room) {
        AttackHandler attackHandler = room.getAttackHandler();
        attackHandler.getSelectedTargets().stream().filter(p->!attackHandler.getPossibleTargets().contains(p)).forEach(x->attackHandler.getPossibleTargets().add(x));
    }
}