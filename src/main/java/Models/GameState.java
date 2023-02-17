package Models;

import java.util.*;

public class GameState {

    public World world;
    public List<GameObject> gameObjects;
    public List<GameObject> playerGameObjects;
    public static boolean afterBurnerOn = false;
    public static boolean shieldActivated = false;
    public static double shieldTick;

    public GameState() {
        world = new World();
        gameObjects = new ArrayList<GameObject>();
        playerGameObjects = new ArrayList<GameObject>();
    }

    public GameState(World world, List<GameObject> gameObjects, List<GameObject> playerGameObjects) {
        this.world = world;
        this.gameObjects = gameObjects;
        this.playerGameObjects = playerGameObjects;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void setGameObjects(List<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }

    public List<GameObject> getPlayerGameObjects() {
        return playerGameObjects;
    }

    public void setPlayerGameObjects(List<GameObject> playerGameObjects) {
        this.playerGameObjects = playerGameObjects;
    }

    public static boolean isAfterBurnerOn() {
        return afterBurnerOn;
    }

    public static void setAfterBurnerOn() {
        afterBurnerOn = true;
    }

    public static void setAfterBurnerOff() {
        afterBurnerOn = false;
    }

    public boolean isShieldActivated() {
        return shieldActivated;
    }

    public void setShieldActivated() {
        shieldActivated = true;
        shieldTick = 0;
        // shieldComplement = false;
    }

    public static void setShieldDeactivated() {
        shieldTick+= 0.4;
        if (shieldTick == 20) {
            shieldActivated = false;
            shieldTick = 0;
        }
        // shieldComplement = true;
    }
}
