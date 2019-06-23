package model.player;


import model.board.Cell;
import model.board.Color;

import java.io.Serializable;
import java.util.*;


/**
 * 
 */
public class PlayerBoard implements Serializable{

    private transient Player player;
    private transient List<Player> hp;
    private static final int DEADPOINT = 11;
    private static final int OVERKILL = 11;
    private List<Color> hpColor;
    private static int[] pointArray = {8, 6, 4, 2, 1, 1};
    private static int[] frenzyPoints = {2, 1, 1, 1};
    private boolean isFrenzy;
    private int points;
    private int deathTimes;
    private transient List<Player> marks;
    private List<Color> marksColor;


    public PlayerBoard(Player player) {
        this.player = player;
        hp = new ArrayList<>(12);
        hpColor = new ArrayList<>(12);
        points=0;
        deathTimes=0;
        marks = new ArrayList<>();
        marksColor = new ArrayList<>();
        isFrenzy = false;


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
        if (points==0)
            return;
        for (int i=0;i<points;i++) {
            if (hp.size() < 12) {
                hp.add(player);
                hpColor.add(player.getColor());
            }
        }
        int c=playerMarks(player);
        if (c!=0) {
            removeMarks(player);
            this.addDamage(player, c);
        }
        if (!isFrenzy){
            if (hp.size()>5)
                this.player.setActionStatus(ActionState.ADRENALINEACTIONS2);
            else if (hp.size()>2)
                this.player.setActionStatus(ActionState.ADRENALINEACTIONS1);
        }
    }

    /**
     * add a mark if "this" has less than 3 mark of that player
     * @param player the player marked "this"
     */
    public void addMark(Player player,int points){
        for (int i=0; i<points; i++) {
            if (playerMarks(player) < 3) {
                marks.add(player);
                marksColor.add(player.getColor());
            }else
                break;
        }
    }

    /**
     *  remove all marks of that player
     * @param player that player's marks to remove
     */
    private void removeMarks(Player player){
        int c=playerMarks(player);
        if (c!=0)
            for (int i=0;i<c;i++) {
                marks.remove(player);
                marksColor.remove(player.getColor());
            }
    }

    /**
     * @param player the player to count how mark is his.
     * @return  number of mark of that player marked
     */
    private int playerMarks( Player player){
        int i=0;
        for (Player p:marks){
            if (p==player)
                i++;
        }
        return i;
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
        Map<Player,Integer> mapofpoints=new LinkedHashMap<>(hitNumberOfPlayers());
        List<Map.Entry<Player,Integer>> sortmap=new ArrayList<>(mapofpoints.entrySet());
        sortmap.sort((Map.Entry<Player, Integer> o1, Map.Entry<Player, Integer> o2)-> o2.getValue()-o1.getValue());
        List<Player> list=new ArrayList<>();
        sortmap.forEach(x->list.add(x.getKey()));
        return list;
    }

    /**
     * @return a Map whit player and his hit numbers
     */
    private Map<Player,Integer> hitNumberOfPlayers(){
        Map<Player,Integer> map=new LinkedHashMap<>();
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
     * @return when the player is died return Cell that need add to SkullBoard, else return null.
     */
    public Cell liquidation(){
        List<Player> listForLiquidation = listForLiquidation();
        Cell cell = new Cell();
        int i=0;
        if (listForLiquidation.isEmpty())
            return cell;
        for (Player p:listForLiquidation){
            p.getPlayerBoard().addPoints(isFrenzy ? frenzyPoints[deathTimes+i] : pointArray[deathTimes+i]);
            i++;
        }
        if (hp.get(0)!=null&&!isFrenzy)
            hp.get(0).getPlayerBoard().addPoints(1);



        if (hp.size() > DEADPOINT-1) {
            Player player1 = hp.get(deathTimes-1);
            cell.setKill(player1);
            player.setLive(false);
            deathTimes++;
        }

        if (hp.size() == OVERKILL) {
            hp.get(OVERKILL -1).getPlayerBoard().addMark(player,1);
            cell.setOverKill();
        }

        hp = new ArrayList<>();
        hpColor = new ArrayList<>();

        return cell;

    }

    public List<Player> getHp() {
        return hp;
    }

    public List<Color> getHpColor() {
        return hpColor;
    }

    public List<Color> getMarksColor() {
        return marksColor;
    }

    public List<Player> getMarks() {
        return marks;
    }

    public int getDeathTimes() {
        return deathTimes;
    }
}