package controller;

import model.board.Color;
import model.card.Powerup;
import model.exceptions.TimeFinishedException;
import model.gamehandler.Room;
import model.player.ActionState;
import model.player.Player;
import network.messages.Message;
import network.messages.clientToServer.ListResponse;
import network.messages.serverToClient.AnswerRequest;
import network.messages.serverToClient.InfoMessage;
import network.messages.serverToClient.ServerToClient;
import network.messages.serverToClient.TimeoutMessage;
import network.server.Configs;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Allows to manage the turn from the first to the last player
 */
class TurnController {
    private RoomController roomController;
    private Room room;
    private boolean gameFinished;

    private static final int WAITING_TIME = Configs.getInstance().getTurnTime();
    private static final int POWERUP_TIME = Configs.getInstance().getTimeForTagBackRequest();
    private static final int RESPAWN_TIME = Configs.getInstance().getRespawnTime();
    private static final int MINIMUM_PLAYERS = Configs.getInstance().getMinimumPlayers();


    private RoundController roundController;

    private static final Logger logger = Logger.getLogger(TurnController.class.getName());

    /**
     * Constructor that creates local {@link RoundController} and the main {@link RoomController}
     * @param roomController useful to send messages and retrieve other data
     * @param room where the players are playing
     */
    TurnController(RoomController roomController, Room room) {
        this.roomController = roomController;
        this.room = room;
        roundController = new RoundController(roomController);
        gameFinished = false;

        /*
        timer = new CountDown(1*20*1000, () -> {

            System.out.println("time finished");
            roomController.stopWaiting();

        }); //10 seconds

         */
    }

    /**
     * Begins a round starting from the first player and looping until
     * the game is finished or too many players are disconnected
     */
    void startPlayerRound(){

        CountDown timer;
        //need to ckeck with currentPlayer

        while (!gameFinished){
            Player player = room.getCurrentPlayer();
            //in case the timer finished reset the shoot for the next player
            roundController.resetShot();
            timer = new CountDown(WAITING_TIME, () -> {
                roomController.stopWaiting();
                logger.log(Level.INFO, "timer stopped for {0}", player);
            });

            timer.startTimer();
            try {

                if(player.getRoundStatus() == Player.RoundStatus.FIRST_ROUND){

                    firstRound(player, 2);

                    player.setNextRoundstatus();

                    //continue with normal round
                    normalRound(player);

                    //do this only once
                    if(room.getStartingPlayer() == null){
                        room.setStartingPlayer(player);
                    }

                }
                else if (player.getRoundStatus() == Player.RoundStatus.NORMAL_ROUND){
                    normalRound(player);
                }

                timer.cancelTimer();

            } catch (TimeFinishedException e) {

                player.setDisconnected();

                int stillOnline = (int) room.getPlayers().stream().filter(Player::isConnected).count();
                if( stillOnline < MINIMUM_PLAYERS ){
                    disconnectionCheckout(player, true);
                }
                else {
                    disconnectionCheckout(player, false);
                }


            } catch (IllegalStateException e){
                logger.log(Level.INFO, "ooops, timer already stopped, dont worry ");
                //nothing, just continue
            }
            finally {
                //continue as normal
                //after taking the ammoCard and weapons set a new card
                cleanBoard();
            }


            if(room.endTurnControl() ){
                 //when return true means is end of game
                break;
            }
            //when there are less than 3 connected players quit
            if(!room.setNextPlayer()){
                break;
            }

            //check for dead players
            room.getPlayers().stream().filter(y -> !y.isLive()).forEach(x -> {
                try {
                    player.setLive(true);
                    respawn(x);
                } catch (TimeFinishedException e) {
                    disconnectionCheckout(x, false);
                }
            });

        }
    }

    /**
     * Allows to spawn the player at the beginning or after dieing
     * @param currentPlayer the player that will placed on the board
     * @param cards number of cards he can choose from
     * @throws TimeFinishedException when the player finishes time for choosing a card
     */
    private void firstRound(Player currentPlayer, int cards) throws TimeFinishedException {

        List<Powerup> powerup = room.getBoard().getPowerDeck().getCard(cards);
        AnswerRequest message = new AnswerRequest(roomController.everythingToJson(powerup), Message.Content.POWERUP_REQUEST);
        //sends the cards and receives the chosen one
        if(cards > 1) {
            message.setInfo("Take a powerup and hide it! You will be spawned in the discarded one");
        }
        else{
            message.setInfo("Draw this powerup, you will be spawned here!");
        }
        //chosen card is the card to KEEP
        ListResponse chosenCard;
        try {
            chosenCard = (ListResponse) roomController.sendAndReceive(currentPlayer, message);
        } catch (TimeFinishedException e) {
            room.getBoard().getPowerDeck().usedCard(( powerup.get(0)));
            if(cards > 1){
                room.getBoard().getPowerDeck().usedCard(( powerup.get(1)));
            }
            throw new TimeFinishedException();
        }

        Powerup playerCard;
        Powerup spawnCard;
        try{
            //check if the size is not different
            if(cards > 1){
                playerCard = powerup.get(chosenCard.getSelectedItem());
                powerup.remove(playerCard);
                //give the chosen card to the player
                currentPlayer.addPowerup(playerCard);

                //the second card is the spawn card
                spawnCard = powerup.get(0);
            }
            else {
                spawnCard = powerup.get(chosenCard.getSelectedItem());
            }
            room.getBoard().getPowerDeck().usedCard(spawnCard);

        }catch (RuntimeException e){
            logger.log(Level.WARNING, "CHEATER DETECTED: {0}", currentPlayer.getNickname());
            return;
        }

        //put the player on the generation square
        Color spawnColor = Color.valueOf(spawnCard.getAmmo().toString());
        currentPlayer.movePlayer(room.getBoard().getMap().getGenerationPoint(spawnColor));

        roomController.sendUpdate();
    }

    /**
     * Round where the player can play his actions
     * @param player current player that is playing
     * @throws TimeFinishedException when the player finishes the time for playing
     */
    private void normalRound(Player player) throws TimeFinishedException{
        //in normal round do this 2 times

        int iterations = 2;

        if(player.getActionStatus() == ActionState.FRENETICACTIONS2){
            iterations = 1;
        }

        for(int i = 0; i < iterations; i++){
            //first ask for powerup

            roundController.powerupController(player);

            //roomController.sendUpdate();
            //ask for action
            roundController.actionController(player);
            roomController.sendUpdate();
            if(roundController.shoot()){
                sendTagBack(player);
            }
        }
        //ask last time
        roundController.powerupController(player);

        //reload
        ActionHandler.reload(player, room);

    }

    private void respawn(Player player) throws TimeFinishedException {
        Powerup chosenCard = room.getBoard().getPowerDeck().getCard(1).get(0);
        //takes one card and gives it to the player
        player.addPowerup(chosenCard);

        List<Powerup> powerup = player.getPowerups();
        AnswerRequest message = new AnswerRequest(
                roomController.everythingToJson(powerup), Message.Content.POWERUP_REQUEST);
        message.setInfo("You got another powerup, discard one where you want to be respawned!");


        try {
            CountDown timer = new CountDown(RESPAWN_TIME, () -> {
                roomController.stopWaiting();
                logger.log(Level.INFO, "timer stopped while respawning for {0}", player);
            });
            timer.startTimer();
            int index =  ((ListResponse) roomController.sendAndReceive(player, message)).getSelectedItem();
            chosenCard = player.getPowerups().get(index);

            timer.cancelTimer();

        } catch (RuntimeException e) {
            //cheater
        }
        finally {
            //put the player on the generation square
            Color spawnColor = Color.valueOf(chosenCard.getAmmo().toString());
            player.movePlayer(room.getBoard().getMap().getGenerationPoint(spawnColor));

            player.removePowerup(chosenCard);
            room.getBoard().getPowerDeck().usedCard(chosenCard);

            roomController.sendUpdate();
        }
    }

    /**
     * Method that allows to ask a player if he wants to use a his TAGBACK GRANADE
     * @param player who has this card
     */
    private void sendTagBack(Player player) {
        String tagback = "TAGBACK GRENADE";

        List<Player> haveTagBack = room.getAttackHandler().getDamaged()
                .keySet().stream().filter(x -> x
                        .getPowerups().stream().anyMatch(y -> y
                                .getName().equals(tagback))).collect(Collectors.toList());


        haveTagBack.remove(player);

        Powerup tempPowerup;
        for(Player attacker : haveTagBack){

            int iterations = (int) attacker.getPowerups().stream()
                    .filter(x -> x.getName().equals(tagback)).count();
            for(int i = 0; i < iterations; i++){

                List<Powerup> attackerPowerups = attacker.getPowerups().stream()
                        .filter(x -> x.getName().equals(tagback)).collect(Collectors.toList());

                AnswerRequest message = new AnswerRequest(room
                        .getRoomController()
                        .everythingToJson(
                                //sends only the tagback cards
                                attackerPowerups),
                        //send message corresponding to the request
                        Message.Content.POWERUP_REQUEST);

                message.setIsOptional();
                message.setInfo("You have a " + tagback +", want to use one? Be fast!");

                //set timer for choosing
                CountDown cd = new CountDown(POWERUP_TIME, () -> {
                    roomController.stopPowerup();
                    roomController.sendMessage(attacker, new InfoMessage("Too slow! Use your " + tagback + " next time"));

                });

                cd.startTimer();

                //when the main player's timer stops before the attacker's
                //all other players can attack as well
                ListResponse chosenCard = room.getRoomController().tagBack(attacker, message);

                try{
                    cd.cancelTimer();
                }catch (IllegalStateException f){
                    logger.log(Level.INFO, "ooops, timer already stopped, dont worry");
                    //nothing, just continue
                }

                if (chosenCard.getSelectedItem() < attackerPowerups.size() && chosenCard.getSelectedItem() >= 0) {
                    //the chosen powerup will be executed
                    tempPowerup = attackerPowerups.get(chosenCard.getSelectedItem());

                }
                else {
                    break;
                }

                attacker.removePowerup(tempPowerup);
                room.getBoard().getPowerDeck().usedCard(tempPowerup);

                player.getPlayerBoard().addMark(attacker, 1);

            }
        }

    }

    /**
     * sets missing ammocards and weapons back on the board and updates the players
     */
    private void cleanBoard(){
        room.getBoard().fillAmmo();
        room.getBoard().fillWeapons();
        roomController.sendUpdate();
    }

    private void disconnectionCheckout(Player player, boolean kick){
        logger.log(Level.INFO,"player: {0} finished his time", player.getNickname());
        //set the player as disconnected
        //player.setDisconnected();

        //send message

        if(kick){
            ServerToClient message = new InfoMessage("You lost time, you lost the game...");
            roomController.sendMessage(room.getCurrentPlayer(), message);
        }
        else {
            roomController.sendMessage(room.getCurrentPlayer(), new TimeoutMessage());
        }

        roomController.sendMessageToAll(new InfoMessage(player.getNickname() + " has disconnected (time exceeded)"));
    }
}
