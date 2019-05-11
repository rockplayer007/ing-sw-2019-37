package view.CLI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Printer {

    private Thread thread;
    private int choice;

    private static final Logger logger = Logger.getLogger(Printer.class.getName());

    public void displayRequest(List<String> possibilities, Consumer<Integer> selection){
        //bofore asking something else cancel the previous request
        if(thread != null){
            closeRequest();
        }
        thread = new Thread( () ->
        {
            int nRequests = possibilities.size();
            for(int i = 0; i < nRequests; i ++){
                print((i + 1) + ") " + possibilities.get(i));
            }

            print("THRED IS  " + thread.getName());
            print("Choose an option between 1 and " + nRequests  + ":");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            Scanner reader = new Scanner(bufferedReader);
            ///int choice;
            String tmp;
            try {
                do {
                    while (!bufferedReader.ready()) {

                        Thread.sleep(100);
                    }
                    if (reader.hasNextInt()) {
                        choice = reader.nextInt();
                    } else {
                        reader.next();
                        choice = -1;
                    }

                    if(choice < 1 || choice > nRequests){
                        print("Write a valid input:");
                        print("to THREAD   " + thread.getName());
                    }
                }while (choice < 1 || choice > nRequests);
                selection.accept(choice - 1);

            }catch (IOException|InterruptedException e) {
                //dont print anything for cli
            }

        });
        thread.start();
    }


    public void closeRequest(){
        if(!thread.isInterrupted()){
            BufferedReader tmp = new BufferedReader(new InputStreamReader(System.in));
            try {
                if (tmp.ready()) {
                    tmp.readLine();
                }
            } catch (IOException e) {
                //nothing to print for cli
            }
            thread.interrupt();
            print("You finished your time for choosing");
            print("KILLING THREAD  " + thread.getName());
        }

    }

    public void print(String toPrint){
        System.out.println(toPrint);
    }


}
