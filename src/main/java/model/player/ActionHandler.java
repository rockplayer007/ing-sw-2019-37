package model.player;

import model.board.Board;
import model.board.GenerationSquare;
import model.board.Square;

import model.card.*;


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
     * add WeaponCard in player only if he has less than 3 weapons
     * @param
     */
    public void grab(Player player,Weapon card) {
        List<Weapon>cards=player.getWeapons();
        if(cards.size()<3)
            player.addWeapon(card);
//        else {
//            System.out.println("you need choose one Weapon, to drop and put it in the empty space left by the weapon you are grabbing.");
//            System.out.println("choose one of these:");
//            int i;
//            for (i=0; i<3; i++){
//                System.out.println((i+1)+"."+cards.get(i).getName());
//            }
//            do{
//                System.out.println("Write the number of the card");
//                Scanner sc = new Scanner(System.in);
//                i=sc.nextInt();
//            }while (i>3||i<0);
//            player.getPosition().addwepon(cards.get(i));
//            cards.remove(cards.get(i));
//

        }
    }

    /**
     * grab AmmoCard and add the Ammos and Powerup in player
     */
    public void grab(Player player, AmmoCard card, Board board) {
        card.getAmmoList().forEach(player::addAmmo);
        if (card.hasPowerup() && player.getPowerups().size() < 3) {
            player.addPowerup((Powerup) board.getPowerDeck().getCard());
        }
    }

    /**
     * 
     */
    public void reload(Weapon card ) {
        card.setCharged(true);
    }

}