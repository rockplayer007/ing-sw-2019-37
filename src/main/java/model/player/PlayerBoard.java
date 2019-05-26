package model.player;

import java.util.*;

/**
 * 
 */
public class PlayerBoard {
    private  Player player;
    private List<Player> hp;
    private static int[] pointArray = {8, 6, 4, 2, 1, 1};
    private static int[] frenzyPoints = {2, 1, 1, 1};
    private Boolean isFrenzy;
    private int points;
    private int deathTimes;
    private List<Player> marks;

    public PlayerBoard() {
        this.hp=new ArrayList<>(12);
        this.points=0;
        this.deathTimes=0;
    }

    public int getDeathTimes() {
        return deathTimes;
    }

    public void setFrenzy(Boolean frenzy) {
        isFrenzy = frenzy;
    }

    public int getPoints() {
        return points;
    }

    /**
     * Sets the player that applied the damage on the board
     * @param player that give you the damage.
     * @param points how damage give this(player).
     */
    public void addDamage(Player player,int points){
        for (int i=0;i<points;i++) {
            if (hp.size() < 12)
                hp.add(player);
        }
        int c=playerMarks(player);
        if (c!=0) {
            removeMarks(player);
            this.addDamage(player, c);
        }
        if (!isFrenzy){
//            if (hp.size()>5)
//                this.player.setActionStatus(ActionState.ADRENALINEACTIONS2);
//            else if (hp.size()>2)
//                this.player.setActionStatus(ActionState.ADRENALINEACTIONS1);
        }
        //TODO bisogna vedere come funziona con gli pattern observer
    }

    /**
     *  remove all marks of that player
     * @param player that player's marks to remove
     */
    public void removeMarks(Player player){
        int c=playerMarks(player);
        if (c!=0)
            for (int i=0;i<c;i++) {
                marks.remove(player);
            }
    }

    /**
     * @param player the player to count how mark is his.
     * @return  number of mark of that player marked
     */
    public int playerMarks( Player player){
        int i=0;
        for (Player p:marks){
            if (p==player)
                i++;
        }
        return i;
    }

    /**
     * add a mark if "this" has less than 3 mark of that player
     * @param player the player marked "this"
     */
    public void addMark(Player player){
        if (playerMarks(player)<3)
            marks.add(player);
    }

    /**
     *  add the point that "this" has get
     * @param points number of point
     */
    public void addPoints(int points){
        this.points+=points;
    }

    /**
     * @return a List of player that attached "this"  and sorted in base hit numbers
     */
    private List<Player> listForLiquidation(){
        Map<Player,Integer> mapofpoints=new TreeMap<>(hitNumberOfPlayers());
        List<Map.Entry<Player,Integer>> sortmap=new ArrayList<>(mapofpoints.entrySet());
        sortmap.sort((Map.Entry<Player, Integer> o1, Map.Entry<Player, Integer> o2)-> o2.getValue()-o1.getValue());
        List<Player> list=new ArrayList<>();
        sortmap.forEach(x->list.add(x.getKey()));
        return list;
    }

    /**
     * @return a Map whit player and his hit numbers
     */
    public Map<Player,Integer> hitNumberOfPlayers(){
        Map<Player,Integer> map=new HashMap<>();
        for (Player p:hp){
            if (map.containsKey(p))
                map.put(p,map.get(p)+1);
            else
                map.put(p,1);
        }
        return map;
    }

    /**
     * liquidation when the hero dies, and give the point to others player
     */
    public void liquidation(){
        List<Player> listForLiquidation=listForLiquidation();
        int i=0;
        for (Player p:listForLiquidation){
            p.getPlayerBoard().addPoints(isFrenzy ? frenzyPoints[deathTimes+i] : pointArray[deathTimes+i]);
            i++;
        }
        if (hp.get(0)!=null&&!isFrenzy)
            hp.get(0).getPlayerBoard().addPoints(1);

        if (hp.size()==12&&hp.get(11)!=null)
            hp.get(11).getPlayerBoard().addMark(this.player);
        hp=new ArrayList<>();

    }

}