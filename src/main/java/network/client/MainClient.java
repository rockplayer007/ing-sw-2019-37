package network.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import controller.CountDown;
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
import network.messages.clientToServer.ConnectionMessage;
import network.messages.clientToServer.ListResponse;
import network.messages.clientToServer.LoginRequest;
import network.messages.serverToClient.*;
import network.server.MainServer;
import view.CLI.CLI;
import view.GUI.GUI;
import view.ViewInterface;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.rmi.NotBoundException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private CountDown connectionTimer = new CountDown(PING_TIMER, () -> {});
    private CountDown pingTimer = new CountDown(PING_TIMER, () -> {});
    private static final int PING_TIMER = 5;

    private static ViewInterface view;
    private static boolean socket; //true uses socket false uses rmi

    private boolean online = true;
    private boolean timeout = true;

    private static final Logger logger = Logger.getLogger(MainServer.class.getName());

    public static void main(String[] args) {
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

        view.launch();
    }


    private void ping(){
        pingTimer = new CountDown(PING_TIMER, () -> {

            connection.sendMessage(new ConnectionMessage(username, clientInterface, clientID));
            ping();
        });
        pingTimer.startTimer();

    }
    private void closePing(){

        pingTimer.cancelTimer();
    }

    private void receiveTimer(){
        connectionTimer = new CountDown(PING_TIMER + 5, () -> {
            //logger.log(Level.INFO, "not receiving ping");
            //if nothing comes back
            handleMessage(new ServerToClient(Message.Content.DISCONNECTION));
        });
        connectionTimer.startTimer();
    }
    private void restartTimer(){
        connectionTimer.cancelTimer();
        receiveTimer();
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
        //ping();

    }

    /**
     * Sends the user's username and {@link ClientInterface} in case of RMI connection
     */
    public void sendCredentials(){

        //needed for not asking again
        online = true;
        //just in case
        closePing();
        //start pinging
        ping();
        connection.sendMessage(new LoginRequest(username, clientInterface, readID(username)));

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


    /**
     * New messages that arrive from the server are managed here
     * @param message
     */
    public void handleMessage(ServerToClient message){

        connectionTimer.cancelTimer();
        //logger.log(Level.INFO, "received: " + message.getContent());

        if(message.getContent() !=  Message.Content.DISCONNECTION){
            //logger.log(Level.INFO, "received ping");
            restartTimer();
        }


        Type type = new TypeToken<HashMap<String, Integer>>(){}.getType();
        Gson gson = new Gson();
        List<String> stringed;



        switch (message.getContent()){
            case TIMEOUT:
                if(timeout){
                    timeout = false;
                    view.timeout();
                }

                break;
            case DISCONNECTION:
                //view.showInfo("RECONNECT!!!");
                closePing();
                if(online){
                    online = false;
                    view.disconnection();
                }

                break;
            case LOGIN_RESPONSE:
                clientID = ((LoginResponse) message).getClientID();

                try{
                    String path = "."+ File.separatorChar + "src" + File.separatorChar+
                            "main" + File.separatorChar + "resources" + File.separatorChar + "data.txt";
                    //FileWriter fw = new FileWriter(path);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    //fw.write(dateFormat.format(date) + username + ", " + clientID + ", ");
                    //fw.write(username);
                    if(!((LoginResponse) message).getStatus()) {
                        insertCredentials(username, username + ", " +
                                dateFormat.format(date) + ", " + clientID);
                    }

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
                if((((InfoMessage) message)).getConnection()){
                    online = false;
                    closePing();
                }
                view.showInfo(((InfoMessage) message).getInfo());
                break;
            case CONNECTION:
                //if this message arrives, the connection is succesfull
                break;
            case SCORE:
                online = false;
                closePing();
                connectionTimer.cancelTimer();

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

    private String readID(String username){
        String path = "."+ File.separatorChar + "src" + File.separatorChar+
                "main" + File.separatorChar + "resources" + File.separatorChar + "data.txt";

        List<String> credentials = new ArrayList<>();
        try{
            FileReader temp = new FileReader(path);
            BufferedReader in = new BufferedReader(temp);
            String str;

            while((str = in.readLine()) != null){
                credentials.add(str);
            }
            temp.close();
            in.close();

        }catch (IOException e){
            //fine if there isn't any file
        }

        String id = "";
        for(String info : credentials){
            String name = info.split(", ")[0];
            if(username.equals(name)){
                try {
                    id = info.split(", ")[2];
                } catch (Exception e){
                    //wrong written
                }

            }
        }

        return id;
    }

    public void insertCredentials(String username, String cred) throws IOException {
        String path = "."+ File.separatorChar + "src" + File.separatorChar+
                "main" + File.separatorChar + "resources" + File.separatorChar + "data.txt";
        File yourFile = new File(path);
        yourFile.createNewFile();

        File file = new File(path);

        StringBuilder temp = new StringBuilder();
        Files.lines(file.toPath())
                .filter(line -> !username.contains(line.split(", ")[0]))
                .forEach(x -> temp.append(x).append("\n"));

        temp.append(cred).append("\n");

        FileWriter fw = new FileWriter(path, false);

        fw.write(temp.toString());
        fw.close();

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
    public void resetTimout(){
        timeout = true;
    }
}
