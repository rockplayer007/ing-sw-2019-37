package model.player;

import model.board.AmmoSquare;
import model.board.Board;
import model.board.GenerationSquare;
import model.board.Square;
import model.card.*;

import model.exceptions.NotEnoughException;


import java.util.*;
import java.util.stream.Collectors;

/**
 * contain the min actions t
 */
public class ActionHandler {

    private ActionHandler(){
    throw new IllegalStateException("Utility class");
    }



    /**
     * run actions let current player to changes position
     * @param player that do this action.
     * @param  distanceMax Max distance that the player can move
     */
    public static void run( Player player, int distanceMax) {
        Set<Square> validPositions = player.getPosition().getValidPosition(distanceMax);
        Square destination = chooseSquare(player, validPositions);
        player.movePlayer(destination);
    }
    /**
     *  general way to let player chooses the Squere that he can go
     * @param player current player
     * @param  validPositions all ssquare that you can choose.
     * @return the Square that the player choose to move
     */
    public static Square chooseSquare(Player player,Set<Square> validPositions) {
        System.out.println("there are valid points:");
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
     * @param
     * @param
     * @return
     */
    public static void shoot() {
        // TODO implement here

    }

    /**
     * basic grab method let player to grab all card that they can
     * @param player that do this action.
     * @param board that the player play.
     */
    public static void grab( Player player,Board board) {
        if (!player.getPosition().isGenerationPoint())
            grabAmmo(player,((AmmoSquare) player.getPosition()).getAmmoCard(),board);
        else {
            List<Weapon> weapons=((GenerationSquare) player.getPosition()).getWeaponDeck().stream().
                    filter(i->player.enoughAmmos(i.getBuyCost(),true))
                    .collect(Collectors.toList());
            while (!weapons.isEmpty()){
                Weapon weapon = weapons.get(chooseCard(weapons,"to grab"));
                if (player.limitWeapon()) {
                    int i = chooseCard(player.getWeapons(), "change");
                    ((GenerationSquare) player.getPosition()).addWeapon(player.getWeapons().get(i));
                    player.getWeapons().remove(i);
                }
                try {
                    grabWeapon(player, weapon);
                    break;
                } catch (NotEnoughException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * general way to let player chooses the Squere that he can go
     * @param cards cards that need to choose
     * @param reason why go to this choose
     * @return position of card choose in the List
     */
    public static int chooseCard(List<? extends Card> cards, String reason) {
        System.out.println("you need choose one card,to " + reason);
        System.out.println("choose one of these:");
        int i;
        cards.forEach(c->System.out.println(cards.indexOf(c)+1 + ". " + c.getName()));
        System.out.print("Write the number of the card：");
        while (true) {
            Scanner sc = new Scanner(System.in);
            i = sc.nextInt();
            if (i>0||i<cards.size())
                return i;
            else
                System.out.print("Number writed is not valid!! Please write the number again:");//TODO da stampare sul view del player
        }
    }

    /**
     * add WeaponCard in player only if he has less than 3 weapons
     * @param player current player
     * @param  card weapon need to add in the player's hand
     */
    private static void grabWeapon(Player player, Weapon card) throws NotEnoughException{
        List<AmmoColor> cost = card.getChargeCost();
        decuction(player,cost);
        player.addWeapon(card);
    }

    public static void decuction(Player player,List<AmmoColor> cost) throws NotEnoughException {
        int i;
        int c=0;
        List<Powerup> temp = new ArrayList<>();
        List<Powerup> powerups = player.getPowerups();
        if (player.enoughAmmos(cost, true)) {
            while (cost.stream().distinct().anyMatch(player::usePowerupAsAmmo)) {//if the player has the powerups that can use as ammo
                if (choice(c, "use a powerup as a ammo")) {
                    i = chooseCard(powerups, "use as ammo");
                    cost.remove(powerups.get(i).getAmmo());
                    temp.add(powerups.get(i));
                    c++;
                } else
                    break;
            }
            if (player.enoughAmmos(cost, false)) {
                temp.forEach(powerups::remove);
                cost.forEach(player::removeAmmo);
            } else
                throw new NotEnoughException("haven't enough ammo.");

        }
    }

    /**
     * grab AmmoCard and add the Ammos and Powerup in player
     * @param player current player
     * @param  card Ammocard need to analize
     */

    public static void grabAmmo(Player player, AmmoCard card, Board board) {
        card.getAmmoList().forEach(player::addAmmo);
        if (card.hasPowerup() && player.getPowerups().size() < 3) {
            player.addPowerup((Powerup) board.getPowerDeck().getCard());
        }
    }


    /**
     * reload the weapon
     * @param player that do this action
     */
    public static void reload(Player player) {
        List<Weapon> weapons=player.getWeapons().stream().filter(x->!x.getCharged()).collect(Collectors.toList());
        while (!weapons.isEmpty()) {
            Weapon weapon = weapons.get(chooseCard(weapons, "charge"));
                List<AmmoColor> cost = weapon.getChargeCost();
                try {
                    decuction(player,cost);
                    weapon.setCharged(true);
                    break;
                } catch (NotEnoughException e) {
                    e.printStackTrace();
                }
        }

    }
    /**
     * general way to let player chooses the yes or no
     * @param i for in the case need a loop of request
     * @param message is the target of question
     * @return a Boonlean in base yes or no, chosen by player
     */
    public static Boolean choice(int i, String message){
        if (i==0)
            System.out.println("You can still "+message+",do you want?");
        else
            System.out.println("You can "+message+",do you want?");
        System.out.println("presse: [y/n] ");
        while (true) {
            Scanner sc = new Scanner(System.in);
            String choice=sc.next();
            if (choice.equalsIgnoreCase("y")) {
                return true;
            } else {
                if(choice.equalsIgnoreCase("n")) {
                    return false;
                }else {
                    System.out.println("wrong choice, please chooce again");
                }
            }
        }
    }






}
