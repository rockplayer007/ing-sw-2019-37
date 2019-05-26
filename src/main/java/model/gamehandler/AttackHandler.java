package model.gamehandler;

import model.board.Square;
import model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttackHandler {
    private List<Player> possibleTargets;
    private List<Player> targetsToShot;
    private List<Player> selectedTargets;
    private Square effectSquare;
    private Map<Player,Integer> damaged;

    public AttackHandler() {
        possibleTargets = new ArrayList<>();
        targetsToShot = new ArrayList<>();
        selectedTargets = new ArrayList<>();
        damaged = new HashMap<>();
    }

    public List<Player> getPossibleTargets() {
        return possibleTargets;
    }

    public void setPossibleTargets(List<Player> targetPlayers) {
        this.possibleTargets = targetPlayers;
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

    public Map<Player, Integer> getDamaged() {
        return damaged;
    }

    public void addDamage(Player player,int point){
        if (damaged.containsKey(player))
            damaged.put(player, damaged.get(player)+point);
        else
            damaged.put(player,point);
    }

    public void damage(Player player){
        damaged.forEach((key, value) ->key.getPlayerBoard().addDamage(player,value));

    }
}
