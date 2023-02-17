package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

import com.ctc.wstx.shaded.msv_core.reader.trex.classic.DataState;

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
        gameState.setShieldDeactivated();
        if (!gameState.getGameObjects().isEmpty()) {
            Data dataState = new Data(bot, gameState);

            if (dataState.isOutsideBorder()) {
                System.out.println("GO TO INSIDE");
                playerAction.action = PlayerActions.FORWARD;
                playerAction.heading = Statistic.getHeadingBetween(bot, gameState.getWorld().centerPoint);
            } else {
                /* Defense */
                if (dataState.isNeedDefenseMode()) {
                    System.out.println("DEFENSE");
                    DefenseMode defenseMode = new DefenseMode(dataState, bot, playerAction, dataState.getThreatObject(),
                            dataState.getPlayerObject(), dataState.getPlayerDistance(),
                            dataState.getThreatObjectDistance(), gameState);
                    defenseMode.ressolvingTreat();
                    playerAction = defenseMode.getPlayerActionDefend();
                } else {
                    /* Attack */
                    if (dataState.isFeasibleAttackMode()) {
                        System.out.println("ATTACK");
                        AttackMode attackMode = new AttackMode(dataState, bot, playerAction, gameState);
                        attackMode.resolveAttackMode();
                    } else {
                        if (dataState.isSupernovaExist()) {
                            System.out.println("Reach SUPERNOVA");
                            playerAction.action = PlayerActions.FORWARD;
                            playerAction.heading = Statistic.getHeadingBetween(bot, gameState.getWorld().centerPoint);
                        } else {
                            /* Farming */
                            System.out.println("FARMINGGG");
                            if ((dataState.getnFoodObject() > 0)) {
                                FarmingMode foodList = new FarmingMode(dataState, bot, playerAction);
                                foodList.resolveFarmingFoodAction();
                            } else {
                                playerAction.action = PlayerActions.FORWARD;
                                playerAction.heading = new Random().nextInt(360);
                            }
                        }
    
                    }
                }
            }

            System.out.println("Player ACTION: " + playerAction.getAction() + " Player HEADING: " + playerAction.getHeading());
            System.out.println("Prey : " + dataState.getNPreyObject());
            System.out.println("Enemy : " + dataState.getNEnemy());
            System.out.println("TorpedoSalvoCount: " + bot.TorpedoSalvoCount);
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
