package model.player;

import model.board.Square;
import java.util.*;

/**
 * 
 */
public class Run implements Command {

    /**
     * Default constructor
     */
    public Run() {
    }

    @Override
    public void execute(Player player, Object o) {
        Square destination=(Square)o;
        player.setPosition(destination);

        //seguenti codice e' integrato un parte codice di Control. serve magari in futuro nel Control.

//        int distanceMax = (int)o;
//        ArrayList<Square> validPositions=player.getPosition().getValidPosition(distanceMax);//ToDO//need a method in map that return valid positions can go to in base distanceMax
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
//                        player.setPosition(temp);
//                        muoved = true;
//                    }
//                }
//            }
//            if (!muoved)
//                System.out.println("You cant muove this position,please rewrite X and Y");
//        }

    }
}