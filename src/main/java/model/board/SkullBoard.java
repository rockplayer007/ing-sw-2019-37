package model.board;

import model.player.Player;

import java.util.*;

public class SkullBoard {

    private static int[] pointArray = {8, 6, 4, 2, 1, 1};
    private int numberSkulls;
    private List<Cell> cells;

    public SkullBoard(int numberSkulls){
        this.numberSkulls = numberSkulls;
        cells = new ArrayList<>();
    }

    public void addCell(Cell cell){
        cells.add(cell);
    }

    public int getNumberSkulls() {
        return numberSkulls;
    }

    public List<Cell> getCells() {
        return cells;
    }


    /**
     * this is last liquidation before end game.
     */
     public void liquidation(){
        List<Player> listForLiquidation=listForLiquidation();
        int i=0;
        for (Player p:listForLiquidation){
            p.getPlayerBoard().addPoints(pointArray[i]);
            i++;
        }
    }
    private List<Player> listForLiquidation(){
        Map<Player,Integer> mapofpoints=new LinkedHashMap<>(hitNumberOfPlayers());
        List<Map.Entry<Player,Integer>> sortmap=new ArrayList<>(mapofpoints.entrySet());
        sortmap.sort((Map.Entry<Player, Integer> o1, Map.Entry<Player, Integer> o2)-> o2.getValue()-o1.getValue());
        List<Player> list=new ArrayList<>();
        sortmap.forEach(x->list.add(x.getKey()));
        return list;
    }

    /**
     * @return a Map whit player and his kill point
     */
    private Map<Player,Integer> hitNumberOfPlayers(){
        Map<Player,Integer> map=new LinkedHashMap<>();
        for (Cell c:cells){
            Player kill = c.getKill();
            if (map.containsKey(kill))
                map.put(kill,map.get(kill)+c.getPoint());
            else
                map.put(kill,c.getPoint());
        }

        return map;
    }
}
