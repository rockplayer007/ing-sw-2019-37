package model.gamehandler;

import model.board.Square;
import model.player.Player;

import java.util.List;

public class AttackHandler {
    private List<Player> PossibleTargets;
    private List<Player> targetsToShot;
    private List<Player> selectedTargets;
    private Square effectSquare;
    public List<Player> getPossibleTargets() {
        return PossibleTargets;
    }

    public void setPossibleTargets(List<Player> targetPlayers) {
        this.PossibleTargets = targetPlayers;
    }

    public List<Player> getTargetsToShot() {
        return targetsToShot;
    }

    public void setTargetsToShot(List<Player> targetsToShot) {
        this.targetsToShot = targetsToShot;
    }

    public List<Player> getSelectedTargets() {
        return selectedTargets;
    }

    public void setSelectedTargets(List<Player> selectedTargets) {
        this.selectedTargets = selectedTargets;
    }

    public Square getEffectSquare() {
        return effectSquare;
    }

    public void setEffectSquare(Square effectSquare) {
        this.effectSquare = effectSquare;
    }
}
