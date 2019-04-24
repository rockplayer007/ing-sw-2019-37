package model.card;

import model.board.Square;
import model.gamehandler.Room;
import model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface Operation {

    void execute(Room room);
}

class VisiblePlayers implements Operation{

    public void execute(Room room){
        Player currentPlayer = room.getCurrentPlayer();
        Set<Square> visible = currentPlayer.getPosition()
                .visibleSquare(room.getBoard().getMap());
        ArrayList<Player> visiblePlayers = new ArrayList<>();

        visible.forEach(x-> visiblePlayers.addAll(x.getPlayersOnSquare()));
        visiblePlayers.remove(currentPlayer);

        currentPlayer.setVisiblePlayers(visiblePlayers);
    }
}

class SelectTargets implements Operation{

    public void execute(Room room){
        Player currentPlayer = room.getCurrentPlayer();
        //TODO ask player what target he wants
        //available players in currentPlayer.getVisiblePlayers
        //to set the currentPlayer.setSelectedTargets AND REMOVE FROM VISIBLE PLAYERS
    }

}

class Damage implements Operation{

    int points;

    public Damage(int points){
        this.points = points;
    }

    public void execute(Room room){
        Player currentPlayer = room.getCurrentPlayer();
        List<Player> selectedPlayers = currentPlayer.getSelectedTargets();
        for(int i = 0; i < points; i++){
            selectedPlayers.forEach(x->x.getPlayerBoard().addDamage(currentPlayer));
        }

    }
}

class Mark implements Operation{

    int points;

    public Mark(int points){
        this.points = points;
    }

    public void execute(Room room){
        Player currentPlayer = room.getCurrentPlayer();
        List<Player> selectedPlayers = currentPlayer.getSelectedTargets();
        for(int i = 0; i < points; i++){
            selectedPlayers.forEach(x->x.getPlayerBoard().addMark(currentPlayer));
        }

    }
}


//TODO
class MovePlayer implements Operation{

    public void execute(Room room){

    }
}
//TODO
class requiredDistance implements Operation{
    @Override
    public void execute(Room room) {

    }
}


