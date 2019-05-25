package network.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.board.*;
import model.card.Effect;
import model.card.Powerup;
import model.card.Weapon;
import model.player.ActionOption;
import network.client.rmi.ConnectionRMI;
import network.client.socket.ConnectionSOCKET;
import network.messages.Message;
import network.messages.clientToServer.ListResponse;
import network.messages.clientToServer.LoginRequest;
import network.messages.serverToClient.*;
import network.server.MainServer;
import view.CLI.CLI;
import view.GUI.GUI;
import view.ViewInterface;

import java.io.IOException;
import java.rmi.NotBoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Allows to create a new Client who can choose GUI or CLI as interface
 * and handles connection and messages
 */
public class MainClient {

    private static String serverIp;
    private ConnectionInterface connection;
    private String username;
    private String clientID = "";
    private ClientInterface clientInterface = null;

    private static ViewInterface view;
    private static boolean socket; //true uses socket false uses rmi

    private static final Logger logger = Logger.getLogger(MainServer.class.getName());

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        System.out.println("CLI or GUI?[C/G]");
        String choice = reader.nextLine().toLowerCase();

        MainClient mainClient = new MainClient();

        if (choice.equals("g")) {
            view = new GUI(mainClient);

        /*
            //usato solo per test
            Map<Integer, String> map = new HashMap<>();
            map.put(1, "ideale per 3/4 giocatori");
            map.put(2, "ideale per 3/4 giocatori");
            map.put(3, "third");
            map.put(0, "zero");
            view.chooseBoard(map);
        */
           // ((GUI) view).map();

        }
        else {
            view = new CLI(mainClient);
        }

        try {
            view.launch();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Unable to connect to server", e);
        }
    }

    /**
     * Connects the client to the server depending on which connection the Client chose
     * @throws NotBoundException
     * @throws IOException
     */
    public void connect() throws  NotBoundException, IOException {

        if(socket){
            connection = new ConnectionSOCKET(this);
        }
        else{
            connection = new ConnectionRMI(this);
        }

    }

    /**
     * Sends the user's username and {@link ClientInterface} in case of RMI connection
     */
    public void sendCredentials(){
        /*
        if (socket) {
            connection.sendMessage(new LoginRequest(username, null, clientID));
        }
        else {
            connection.sendMessage(new LoginRequest(username, clientInterface, clientID));
        }
         */
        connection.sendMessage(new LoginRequest(username, clientInterface, clientID));

    }

    /**
     * Sends a message with the selected board to the server
     * @param board
     */
    public void sendSelectedBoard(int board){
        connection.sendMessage(new ListResponse(username, clientID, board, Message.Content.BOARD_RESPONSE));
    }

    public void sendSelectedCard(int card){
        connection.sendMessage(new ListResponse(username, clientID, card, Message.Content.CARD_RESPONSE));
    }

    public void sendSelectedSquare(int square){
        connection.sendMessage(new ListResponse(username, clientID, square, Message.Content.SQUARE_RESPONSE));
    }


    /**
     * New messages that arrive from the server are managed here
     * @param message
     */
    public void handleMessage(ServerToClient message){

        Gson gson = new Gson();
        List<String> stringed;

        switch (message.getContent()){
            case TIMEOUT:
                view.timeout();
                break;
            case LOGIN_RESPONSE:
                clientID = ((LoginResponse) message).getClientID();
                view.logIn(((LoginResponse) message).getStatus());
                break;
            case BOARD_REQUEST:
                view.chooseBoard(((BoardRequest) message).getBoards());
                break;
            case UPDATE:
                //necessary to deserialize properly also the sub classes
                RuntimeTypeAdapterFactory<Square> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                        .of(Square.class, "Square")
                        .registerSubtype(AmmoSquare.class, "AmmoSquare")
                        .registerSubtype(GenerationSquare.class, "GenerationSquare");

                Gson boardGson = new GsonBuilder()
                        .registerTypeAdapterFactory(runtimeTypeAdapterFactory)
                        .create();

                UpdateMessage update = ((UpdateMessage) message);


                stringed = update.getPlayerPowerups();
                List<Powerup> myPowerups = new ArrayList<>();
                for(String powerup : stringed){
                    myPowerups.add( gson.fromJson(powerup, Powerup.class));
                }

                view.updateAll(boardGson.fromJson(update.getBoard(), GameBoard.class), //board
                        myPowerups, //powerups
                        gson.fromJson(update.getSkullBoard(), SkullBoard.class)); //skullBoard
                break;
            case POWERUP_REQUEST:
                stringed = ((AnswerRequest) message).getRequests();
                List<Powerup> powerups = new ArrayList<>();

                for(String powerup : stringed){
                    powerups.add( gson.fromJson(powerup, Powerup.class));
                }
                view.choosePowerup(powerups, ((AnswerRequest) message).isOptional());
                break;

            case WEAPON_REQUEST:
                stringed = ((AnswerRequest) message).getRequests();
                List<Weapon> weapons = new ArrayList<>();

                for(String weapon : stringed){
                    weapons.add( gson.fromJson(weapon, Weapon.class));
                }
                view.chooseWeapon(weapons, ((AnswerRequest) message).isOptional());
                break;
            case ACTION_REQUEST:

                stringed = ((AnswerRequest) message).getRequests();
                List<ActionOption> actions = new ArrayList<>();
                stringed.forEach(action -> actions.add(gson.fromJson(action, ActionOption.class)));
                view.chooseAction(actions);
                break;

            case SQUARE_REQUEST:
                stringed = ((AnswerRequest) message).getRequests();
                List<Square> squares = new ArrayList<>();
                stringed.forEach(square -> squares.add(gson.fromJson(square, Square.class)));
                view.chooseSquare(squares);
                break;
            case EFFECT_REQUEST:
                stringed = ((AnswerRequest) message).getRequests();
                List<Effect> effects = new ArrayList<>();
                stringed.forEach(square -> effects.add(gson.fromJson(square, Effect.class)));
                view.chooseEffect(effects);
                break;

            default:
                logger.log(Level.WARNING, "Unregistered message");

        }
    }



    public static String getServerIp() {
        return serverIp;
    }
    public static void setServerIp(String serverIp){
        MainClient.serverIp = serverIp;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return username;
    }
    public void setClientInterface(ClientInterface clientInterface){
        this.clientInterface = clientInterface;
    }
    public static void setSocket(Boolean connection){
        socket = connection;
    }
}
