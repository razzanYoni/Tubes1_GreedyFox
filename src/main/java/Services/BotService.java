package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

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
                        dataState.getPlayerObject(), dataState.getPlayerDistance(),
                        dataState.getThreatObjectDistance());
                defenseMode.ressolvingTreat();
                playerAction = defenseMode.getPlayerActionDefend();
            } else {
                /* Attack */
                if (dataState.isFeasibleAttackMode()) {
                    AttackMode attackMode = new AttackMode(dataState, bot, playerAction, gameState);
                    attackMode.resolveAttackMode();
                } else {
                    /* Farming */
                    // System.out.println("FARMINGGG");


                    if ((dataState.getnFoodObject() > 0)) {





                        // DUMMY
                        playerAction.action = PlayerActions.FORWARD;
                        var foodList = gameState.getGameObjects()
                                .stream().filter(item -> item.getGameObjectType() == ObjectTypes.FOOD)
                                .sorted(Comparator
                                        .comparing(item -> Statistic.getDistanceBetween(bot, item)))
                                .collect(Collectors.toList());
                        playerAction.heading = Statistic.getHeadingBetween(bot, foodList.get(0));








//                        FarmingMode foodList = new FarmingMode(dataState, bot, playerAction);
//                        foodList.resolveFarmingFoodAction();
                    } else {
                        playerAction.action = PlayerActions.FORWARD;
                        playerAction.heading = new Random().nextInt(360);
                    }
                }
                /* Jangan Lupa dihapus */
            }
            
            System.out.println("Prey : " + dataState.getNPreyObject());
            System.out.println("Enem : " + dataState.getNEnemy());
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
