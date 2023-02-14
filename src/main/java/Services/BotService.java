package Services;

import Enums.*;
import Models.*;
import Services.Data;
import Services.DefenseMode;
import Services.Statistic;

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
        var dataState = new Data(bot, gameState);

        // Determine the state
        if (dataState.isNeedDefenseMode()) {
            System.out.println("TERANCAMMM");
            var defenseMode = new DefenseMode(dataState, bot, playerAction, dataState.getThreatObject(),
                    dataState.getPlayerObject());
            defenseMode.ressolvingTreat();
            playerAction = defenseMode.getPlayerActionDefend();
        } else {
            playerAction.action = PlayerActions.STOP;
            playerAction.heading = new Random().nextInt(360);
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
