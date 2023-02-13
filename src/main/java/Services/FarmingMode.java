package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;


// File : FarmingMode.java
// Untuk memilih ajpakah memakan food biasa atau super food
public class FarmingMode {
    // Atribut
    private int food_afterburner = 3;
    private Data gameDataFood;
    private GameObject foodObj;
    private PlayerAction gfAction;
    private GameObject gfbot;
    
    // Method
    // True jika jarak player ke food < 3
    public boolean isBurnerNeeded() {
        return gameDataFood.getNearestFood().getGameObjectType() == ObjectTypes.SUPERFOOD && gameDataFood.getNearestFood() <= food_afterburner;
    }

    public void resolveFarmingFoodAction(PlayerAction playerAction) {
        playerAction.action = PlayerActions.FORWARD;
        if (isBurnerNeeded()) {
            playerAction.action = PlayerActions.STARTAFTERBURNER;
        }
        else {
            playerAction.action = PlayerActions.STOPAFTERBURNER;
        }

        playerAction.setHeading(Statistic.getDistanceBetween(gameDataFood.getNearestFood()));

        this.gfAction = playerAction;
    }
}
