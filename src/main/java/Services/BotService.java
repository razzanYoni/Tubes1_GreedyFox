package Services;

import Enums.*;
import Models.*;
import Services.Data;
import Services.DefenseMode;

import java.util.*;
import java.util.stream.*;

import javax.sound.midi.Soundbank;

public class BotService {
    private GameObject bot;
    private PlayerAction playerAction;
    private GameState gameState;

    public BotService() {
        this.playerAction = new PlayerAction();
        this.gameState = new GameState();
    }

    public GameObject getBot() {
        return this.bot;
    }

    public void setBot(GameObject bot) {
        this.bot = bot;
    }

    public PlayerAction getPlayerAction() {
        return this.playerAction;
    }

    public void setPlayerAction(PlayerAction playerAction) {
        this.playerAction = playerAction;
    }
    
    public void computeNextPlayerAction(PlayerAction playerAction) {
        // collecting data for state
        if (!gameState.getGameObjects().isEmpty()) {
            Data dataState = new Data(bot, gameState);
            // System.out.println(dataState.getnFoodObject());
            // Determine the state
            
            /* Defense */
            if (dataState.isNeedDefenseMode()) {
                // System.out.println("TERANCAMMM");
                DefenseMode defenseMode = new DefenseMode(dataState, bot, playerAction, dataState.getThreatObject(),
                dataState.getPlayerObject());
                defenseMode.ressolvingTreat();
                playerAction = defenseMode.getPlayerActionDefend();
            } else {
                /* Attack */
                if (dataState.isFeasibleAttackMode() && (bot.TorpedoSalvoCount > 0)) {
                    // System.out.println("MENYERANGGG");
                    
                    
                    
                    /* DUMMY */
                    playerAction.action = PlayerActions.FIRETORPEDOES;
                    playerAction.heading = Statistic.getHeadingBetween(bot, dataState.getPreyObject().get(0));
                    
                    // playerAction.action = PlayerActions.FORWARD;
                    // playerAction.heading = new Random().nextInt(360);
                    
                    

                } else {
                    /* Farming */
                    // System.out.println("FARMINGGG");
                    
                    // DUMMY
                    if ((dataState.getnFoodObject() > 0)) {
                        FarmingMode foodList = new FarmingMode(dataState, bot, playerAction);
                        foodList.resolveFarmingFoodAction();
                    } else {
                        playerAction.heading = new Random().nextInt(360);
                    }
                    playerAction.action = PlayerActions.FORWARD;
                    
                }
                /* Jangan Lupa dihapus */
            }
            // System.out.println("HEHE");
            // System.out.println(dataState.isBorderAncaman());
            // System.out.println("HEHE");
            // System.out.println("Player Heading: " + playerAction.heading);
            // System.out.println("YANG ATAS KITA");
            // System.out.println("Player Action: " + playerAction.action);
            // if (gameState.playerGameObjects.size() > 1) {
            //     System.out.println("Heading Lawan: " + gameState.playerGameObjects.get(0).currentHeading);
            //     System.out.println("Heading Lawan: " + gameState.playerGameObjects.get(1).currentHeading);
            //     System.out.println();
            // }

            // if (dataState.isBorderAncaman()) {
            //     System.out.println(dataState.getBorderPosition().x + " " + dataState.getBorderPosition().y);
            // }


            System.out.println("Prey : " + dataState.getNPreyObject());
            System.out.println("Enem : " + dataState.getNEnemy());
            // if (dataState.getNEnemy() > 0) {
            //     System.out.println(dataState.getPlayerObject().toString());
            // }
            // System.out.println(gameState.world.getRadius());
            System.out.println(bot.TorpedoSalvoCount);

        } 
    }
    
    public GameState getGameState() {
        return this.gameState;
    }
    
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        updateSelfState();
    }

    private void updateSelfState() {
        Optional<GameObject> optionalBot = gameState.getPlayerGameObjects().stream()
                .filter(gameObject -> gameObject.id.equals(bot.id)).findAny();
        optionalBot.ifPresent(bot -> this.bot = bot);
    }
}
