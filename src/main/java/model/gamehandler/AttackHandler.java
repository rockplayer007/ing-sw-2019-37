package model.gamehandler;

import model.board.Square;
import model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttackHandler {
    private List<Player> possibleTargets;
    private List<Player> targetsToShoot;
    private List<Player> selectedTargets;
    private Square effectSquare;
    private Map<Player,Integer> damaged;
    private Map<Player,Integer> marked;

    /**
     * Constructor
     */
    public AttackHandler() {
        possibleTargets = new ArrayList<>();
        targetsToShoot = new ArrayList<>();
        selectedTargets = new ArrayList<>();
        damaged = new HashMap<>();
        marked = new HashMap<>();
    }

    public List<Player> getPossibleTargets() {
        return possibleTargets;
    }

    public void setPossibleTargets(List<Player> targetPlayers) {
        this.possibleTargets = targetPlayers;
    }

    public List<Player> getTargetsToShoot() {
        return targetsToShoot;
    }

    public void setTargetsToShoot(List<Player> targetsToShoot) {
        this.targetsToShoot = targetsToShoot;
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

    public Map<Player, Integer> getMarked() {
        return marked;
    }

    /**
     * For record damages
     * @param player player that damaged
     * @param point point that damaged
     */
    public void addDamage(Player player, int point){
        if (damaged.containsKey(player))
            damaged.put(player, damaged.get(player)+point);
        else
            damaged.put(player,point);
    }

    /**
     * For record marks
     * @param player player that marked
     * @param point point that marked
     */
    public void addmark(Player player,int point){
        if (marked.containsKey(player))
            marked.put(player, marked.get(player)+point);
        else
            marked.put(player,point);
    }
}
