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
     * run actions let current player to changes position
     * @param room room of the game
     * @param  distanceMax Max distance that the player can move
     */
    public void run(Room room, int distanceMax) {
        Player player = room.getCurrentPlayer();
        Square destination = chooseSquare(player, distanceMax);
        player.getPosition().removePlayer(player);
        destination.addPlayer(player);
        player.setPosition(destination);
    }
    /**
     *  general way to let player chooses the Squere that he can go
     * @param player current player
     * @param  distanceMax Max distance that the player can move
     * @return the Square that the player choose to move
     */
    public Square chooseSquare(Player player, int distanceMax) {
        Set<Square> validPositions = player.getPosition().getValidPosition(distanceMax);
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
    public void shoot() {
        // TODO implement here

    }

    /**
     * basic grab method let player to grab all card that they can
     * @param room room of the game
     */
    public void grab(Room room) {
        Player player = room.getCurrentPlayer();
        if (!player.getPosition().GetGenerationPoint())
            grabAmmo(player, ((AmmoSquare) player.getPosition()).getAmmoCard(), room.getBoard());
        else {
            while (true) {
                Weapon weapon = ((GenerationSquare) player.getPosition()).getWeapon();
                List<AmmoColor> cost = weapon.getBuyCost();
                if (player.enoughAmmos(cost)) {
                    if (!player.limitWeapon())
                        grabWeapon(player, weapon);
                    else {
                        int i = chooseCard(player.getWeapons(), "change");
                        ((GenerationSquare) player.getPosition()).addWeapon(player.getWeapons().get(i));
                        player.getWeapons().remove(i);
                        grabWeapon(player, weapon);
                    }
                    break;
                }else {
                    System.out.println("you can't brab this Weapon, because you haven't enough ammo");//
                    if (!choice(1, "choose another Weapon"))
                        break;
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
    public int chooseCard(List<? extends Card> cards, String reason) {
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
    public void grabWeapon(Player player, Weapon card) {
        int c = 0;
        int i;
        ArrayList<AmmoColor> cost = card.getChargeCost();
        if (player.enoughAmmos(cost)) {
            while (cost.stream().distinct().anyMatch(player::usePowerupAsAmmo)) {//if the player has the powerups that can use as ammo
                if (choice(c, "use a weapon as a ammo")) {
                    i = chooseCard(player.getPowerups(), "use as ammo");
                    cost.remove(player.getPowerups().get(i).getAmmoColor());
                    c++;
                } else
                    break;
            }
            cost.forEach(x -> player.removeAmmo(x));
            player.addWeapon(card);
        }
    }

    /**
     * grab AmmoCard and add the Ammos and Powerup in player
     * @param player current player
     * @param  card Ammocard need to analize
     */

    public void grabAmmo(Player player, AmmoCard card, Board board) {
        card.getAmmoList().forEach(player::addAmmo);
        if (card.hasPowerup() && player.getPowerups().size() < 3) {
            player.addPowerup((Powerup) board.getPowerDeck().getCard());
        }
    }


    /**
     * reload the weapon
     * @param room room of the game
     */
    public void reload(Room room) {
        int i;
        int c=0;
        Player player = room.getCurrentPlayer();
        List<Weapon> weapons=player.getWeapons();
        while (true) {
            i = chooseCard(weapons, "charge");
            Weapon weapon = weapons.get(i);
            if (!weapon.getCharged()) {
                ArrayList<AmmoColor> cost = weapon.getChargeCost();
                if (player.enoughAmmos(cost)) {
                    while (cost.stream().distinct().anyMatch(player::usePowerupAsAmmo)) {//if the player has the powerups that can use as ammo
                        if (choice(c,"use a weapon as a ammo")) {
                            i = chooseCard(player.getPowerups(), "use as ammo");
                            cost.remove(player.getPowerups().get(i).getAmmoColor());
                            player.removePowerup(i);
                            c++;
                        } else
                            break;
                    }
                    cost.forEach(x -> player.removeAmmo(x));
                    weapon.setCharged(true);
                }
                break;
            } else {
                System.out.println("it is already charged");//TODO da stampare sul view del player
                if (!choice(1,"choose another Weapon"))
                     break;
            }
        }

    }

    public Boolean choice(int i, String message){
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
