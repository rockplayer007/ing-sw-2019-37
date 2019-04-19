package model.player;

import model.board.AmmoSquare;
import model.board.Board;
import model.board.GenerationSquare;
import model.board.Square;

import model.card.*;
import model.gameHandler.Room;


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
    public void run(Room room, int distaceMax) {
        Player player = room.getCurrentPlayer();
        Square destination = chooseSquare(player, distaceMax);
        player.getPosition().removePlayer(player);
        destination.addPlayer(player);
        player.setPosition(destination);
    }

    public Square chooseSquare(Player player, int distanceMax) {
        Set<Square> validPositions = player.getPosition().getValidPosition(distanceMax);
        System.out.println("there are valid points:");
        Square temp;
        validPositions.forEach(i -> System.out.println("X:" + i.getX() + "Y:" + i.getY()));
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Write X that you want to go:");
            int x = sc.nextInt();
            System.out.println("Write Y that you want to go:");
            int y = sc.nextInt();
            for (Square s : validPositions) {
                if (s.isThisSquare(x, y))
                    return s;
            }
            System.out.println("You cant muove this position,please rewrite X and Y");
        }
    }

    /**
     *
     */
    public void shoot() {
        // TODO implement here


    }

    public void grab(Room room) {
        Player player = room.getCurrentPlayer();
        if (!player.getPosition().GetGenerationPoint())
            grabammo(player, ((AmmoSquare) player.getPosition()).getAmmoCard(), room.getBoard());
        else {
            Weapon weapon = ((GenerationSquare) player.getPosition()).getWeapon();
            if (!player.limitWeapon())
                grabWeapon(player, weapon);
            else {
                int i = chooseweapon(player.getWeapons(), "change");
                ((GenerationSquare) player.getPosition()).addWeapon(player.getWeapons().get(i));
                player.getWeapons().remove(i);
                grabWeapon(player, weapon);
            }
        }
    }

    public int chooseweapon(List<Weapon> weapons, String reason) {
        System.out.println("you need choose one Weapon,to " + reason);
        System.out.println("choose one of these:");
        int i=0;
        weapons.forEach(c->System.out.println(weapons.indexOf(c)+1 + ". " + c.getName()));
        System.out.print("Write the number of the card：");
        while (true) {
            Scanner sc = new Scanner(System.in);
            i = sc.nextInt();
            if (i>0||i<weapons.size())
                return i;
            else
                System.out.print("Number writed is not valid!! Please write the number again:");
        }
    }

    /**
     * add WeaponCard in player only if he has less than 3 weapons
     *
     * @param
     */
    public void grabWeapon(Player player, Weapon card) {
        List<Weapon> cards = player.getWeapons();
        if (cards.size() < 3)
            player.addWeapon(card);

    }

    /**
     * grab AmmoCard and add the Ammos and Powerup in player
     */

    public void grabammo(Player player, AmmoCard card, Board board) {

        card.getAmmoList().forEach(player::addAmmo);
        if (card.hasPowerup() && player.getPowerups().size() < 3) {
            player.addPowerup((Powerup) board.getPowerDeck().getCard());
        }
    }


    /**
     *
     */
    public void reload(Room room) {
        List<Weapon> weapons=room.getCurrentPlayer().getWeapons();
        int i = chooseweapon(weapons, "charge");
        weapons.get(i).setCharged(true);
    }





}
