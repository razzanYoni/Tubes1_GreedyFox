package Models;

import Enums.*;
import java.util.*;

public class PlayerAction {

  public UUID playerId;
  public PlayerActions action;
  public int heading;

  public UUID getPlayerId() {
    return this.playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public PlayerActions getAction() {
    return this.action;
  }

  public void setAction(PlayerActions action) {
    this.action = action;
  }

  public int getHeading() {
    return this.heading;
  }

  public void setHeading(int heading) {
    this.heading = heading;
  }
}
