package Models;

import java.util.*;

public class GameStateDto {

  private World world;
  private Map<String, List<Integer>> gameObjects;
  private Map<String, List<Integer>> playerObjects;

  public Models.World getWorld() {
    return this.world;
  }

  public void setWorld(Models.World world) {
    this.world = world;
  }

  public Map<String, List<Integer>> getGameObjects() {
    return this.gameObjects;
  }

  public void setGameObjects(Map<String, List<Integer>> gameObjects) {
    this.gameObjects = gameObjects;
  }

  public Map<String, List<Integer>> getPlayerObjects() {
    return this.playerObjects;
  }

  public void setPlayerObjects(Map<String, List<Integer>> playerObjects) {
    this.playerObjects = playerObjects;
  }
}
