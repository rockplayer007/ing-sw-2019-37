package model.player;

import model.board.Board;
import model.board.Square;
import model.card.Weapon;
import model.card.Card;
import model.card.AmmoCard;


import java.util.*;

/**
 * 
 */
public class ActionHandler {

    /**
     * Default constructor
     */
    public ActionHandler() {
    }


    /**
     * 
     */
    public void run(Player player, Square destination) {
        player.getPosition().removePlayer(player);
        destination.addPlayer(player);
        player.setPosition(destination);

//
//        List<Square> validPositions=this.getPosition().getValidPosition(distanceMax);
//        System.out.println("there are valid points:");
//        Square temp;
//        for(Iterator<Square> iter=validPositions.iterator(); iter.hasNext();){
//            temp=iter.next();
//            System.out.println("X:"+temp.getX()+"Y:"+temp.getY());
//        }
//        Scanner sc = new Scanner(System.in);
//        Boolean muoved=false;
//        while (!muoved){
//            System.out.println("Write X that you want to go:");
//            int x=sc.nextInt();
//            System.out.println("Write Y that you want to go:");
//            int y=sc.nextInt();
//            for(Iterator<Square> iter=validPositions.iterator(); iter.hasNext();){
//                temp=iter.next();
//                if(x==temp.getX()) {
//                    if (y == temp.getY()) {
//                        this.setPosition(temp);
//                        muoved = true;
//                    }
//                }
//            }
//            if (!muoved)
//                System.out.println("You cant muove this position,please rewrite X and Y");
//        }


    }

    /**
     * 
     */
    public void shoot() {
        // TODO implement here


    }

    /**
     * 
     */
    public void grab(Player player, Card card, Board board) {
        if (!player.getPosition().getType()) {
            player.addWeapon((Weapon) card);
        }
        else {
            ((AmmoCard)card).getAmmoList().stream()
                    .forEach(i ->player.addAmmo(i));
            if (((AmmoCard)card).hasPowerup())
                player.addPowerup(board.getPowerdeck.getCard());

        }

    }

    /**
     * 
     */
    public void reload() {
        // TODO implement here


    }

}