package view.CLI;

import java.lang.invoke.ConstantCallSite;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class QueryClient {

    private static Thread thread;
    private int choice;

    public QueryClient(){

    }

    public void displayRequest(List<String> possibilities, Consumer<Integer> selection){
        //bofore asking something else cancel the previous request
        if(thread != null){
            closeRequest();
        }
        thread = new Thread( () ->
        {
            int nRequests = possibilities.size();
            for(int i = 0; i < nRequests; i ++){
                System.out.println((i + 1) + ") " + possibilities.get(i));
            }

            System.out.println("Choose an option between 1 and " + nRequests  + ":");
            Scanner reader = new Scanner(System.in);
            ///int choice;
            do {
                choice = reader.nextInt();
                if(choice < 1 || choice > nRequests){
                    System.out.println("Write a valid input:");
                }
            }while (choice < 1 || choice > nRequests);

            selection.accept(choice - 1);

        });
        thread.start();
    }


    public static void closeRequest(){
        thread.interrupt();
        System.out.println("time exceeded");
    }

}
