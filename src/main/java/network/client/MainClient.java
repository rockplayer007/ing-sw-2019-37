package network.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.board.*;
import model.card.AmmoColor;
import model.card.Effect;
import model.card.Powerup;
import model.card.Weapon;
import model.player.ActionOption;
import model.player.Player;
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.rmi.NotBoundException;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


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
//        System.setProperty("java.rmi.server.hostname","192.168.1.73");
        Scanner reader = new Scanner(System.in);
        System.out.println("CLI or GUI?[C/G]");
        String choice = reader.nextLine().toLowerCase();

        MainClient mainClient = new MainClient();

        if (choice.equals("g")) {
            view = new GUI(mainClient);
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

    public void sendSelectedItem(int card, Message.Content content){
        connection.sendMessage(new ListResponse(username, clientID, card, content));
    }
    public void sendSelectedItem(int card){
        connection.sendMessage(new ListResponse(username, clientID, card, Message.Content.CARD_RESPONSE));
    }

/*
    public void sendSelectedItem(int square){
        connection.sendMessage(new ListResponse(username, clientID, square, Message.Content.SQUARE_RESPONSE));
    }

    public void sendSelectedItem(int effect){
        connection.sendMessage(new ListResponse(username, clientID, effect, Message.Content.EFFECT_RESPOSNSE));
    }

    public void sendSelectedItem(int player){
        connection.sendMessage(new ListResponse(username, clientID, player, Message.Content.PLAYER_RESPONSE));
    }

    public void sendSelectedItem(int direction){
        connection.sendMessage(new ListResponse(username, clientID, direction, Message.Content.DIRECTION_RESPONSE));
    }

    public void sendSelectedItem(int ammo){
        connection.sendMessage(new ListResponse(username, clientID, ammo, Message.Content.AMMO_RESPONSE));
    }

    public void sendSelectedItem(int room){
        connection.sendMessage(new ListResponse(username, clientID, room, Message.Content.ROOM_RESPONSE));
    }

 */





    /**
     * New messages that arrive from the server are managed here
     * @param message
     */
    public void handleMessage(ServerToClient message){

        Type type = new TypeToken<HashMap<String, Integer>>(){}.getType();
        Gson gson = new Gson();
        List<String> stringed;

        switch (message.getContent()){
            case TIMEOUT:
                view.timeout();
                break;
            case LOGIN_RESPONSE:
                clientID = ((LoginResponse) message).getClientID();

                try{
                    String path = "."+ File.separatorChar + "src" + File.separatorChar+
                            "main" + File.separatorChar + "resources" + File.separatorChar + username + "_data.txt";
                    FileWriter fw = new FileWriter(path);
                    fw.write(clientID);
                    fw.close();
                }catch(Exception e){
                    System.out.println("Couldn't write on file");

                }


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
                view.choosePowerup(powerups, ((AnswerRequest) message).isOptional(),
                        ((AnswerRequest) message).getInfo());
                break;

            case WEAPON_REQUEST:
                stringed = ((AnswerRequest) message).getRequests();
                List<Weapon> weapons = new ArrayList<>();

                for(String weapon : stringed){
                    weapons.add( gson.fromJson(weapon, Weapon.class));
                }
                view.chooseWeapon(weapons, ((AnswerRequest) message).isOptional(), ((AnswerRequest) message).getInfo());
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
                view.chooseSquare(squares, ((AnswerRequest) message).getInfo());
                break;
            case EFFECT_REQUEST:
                stringed = ((AnswerRequest) message).getRequests();
                List<Effect> effects = new ArrayList<>();
                stringed.forEach(square -> effects.add(gson.fromJson(square, Effect.class)));
                view.chooseEffect(effects, ((AnswerRequest) message).isOptional());
                break;
            case PLAYER_REQUEST:
                stringed = ((AnswerRequest) message).getRequests();
                List<Player> players = new ArrayList<>();
                stringed.forEach(square -> players.add(gson.fromJson(square, Player.class)));
                view.choosePlayer(players, ((AnswerRequest) message).getInfo());
                break;
            case DIRECTION_REQUEST:
                stringed = ((AnswerRequest) message).getRequests();
                List<Square.Direction> directions = new ArrayList<>();
                stringed.forEach(square -> directions.add(gson.fromJson(square, Square.Direction.class)));
                view.chooseDirection(directions, ((AnswerRequest) message).getInfo());
                break;
            case AMMOCOLOR_REQUEST:
                stringed = ((AnswerRequest) message).getRequests();
                List<AmmoColor> ammoColors = new ArrayList<>();
                stringed.forEach(square -> ammoColors.add(gson.fromJson(square, AmmoColor.class)));
                view.chooseAmmoColor(ammoColors);
                break;
            case ROOM_REQUEST:
                stringed = ((AnswerRequest) message).getRequests();
                List<Color> rooms = new ArrayList<>();
                stringed.forEach(square -> rooms.add(gson.fromJson(square, Color.class)));
                view.chooseRoom(rooms);
                break;
            case ATTACK:

                Player attacker = gson.fromJson(((AttackMessage) message).getAttacker(), Player.class);
                HashMap<String, Integer> sHp = gson.fromJson(((AttackMessage) message).getHp(), type);
                HashMap<String, Integer> sMarks = gson.fromJson(((AttackMessage) message).getMarks(), type);

                Map<Player, Integer> hp = new HashMap<>();
                Map<Player, Integer> marks = new HashMap<>();

                sHp.forEach((x, y) -> hp.put(gson.fromJson(x, Player.class), y));
                sMarks.forEach((x, y) -> marks.put(gson.fromJson(x, Player.class), y));

                view.showAttack(attacker, hp, marks);

                break;
            case INFO:
                view.showInfo(((InfoMessage) message).getInfo());
                break;
            case CONNECTION:
                //if this message arrives, the connection is succesfull
                break;
            case SCORE:

                List<Player> scorePlayers = ((AnswerRequest) message)
                        .getRequests().stream().map(x -> gson.fromJson(x, Player.class))
                        .collect(Collectors.toList());

                view.showScore(scorePlayers);
                /*
                Map<Player, Integer> score = new HashMap<>();
                Map<String, Integer> scoreMessage = ((ScoreMessage) message).getScore();//gson.fromJson(((ScoreMessage) message).getScore(), type);
                scoreMessage.forEach((x, y) -> score.put(gson.fromJson(x, Player.class), y));
                view.showScore(score);

                 */
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
