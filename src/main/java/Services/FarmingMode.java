package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;


// File : FarmingMode.java
// Untuk memilih ajpakah memakan food biasa atau super food
public class FarmingMode {
    // Atribut
    private Double afterburner_food_threshold;
    private Data gameDataFood;
    private GameObject gfbot;
    private PlayerAction gfAction;
    // private GameObject foodObj;
    
    // Method
    // Contructor
    public FarmingMode(Data gameDataFood, GameObject gfbot, PlayerAction gfAction) {
        this.gameDataFood = gameDataFood;
        this.gfbot = gfbot;
        this.gfAction = gfAction;
        // this.foodObj = foodObj;
    }

    // Copy Constructor
    public FarmingMode(FarmingMode fm) {
        this.afterburner_food_threshold = fm.afterburner_food_threshold;
        this.gameDataFood = fm.gameDataFood;
        this.gfbot = fm.gfbot;
        this.gfAction = fm.gfAction;
        // this.foodObj = fm.foodObj;
    }
    

    // Membandingkan SuperFood dengan regular food lalu 
    public void setFoodThreshold(GameObject obj) {
        afterburner_food_threshold = (Double) (obj.getSize() *0.5);
    }



    public boolean isBurnerNeeded() {
        var rf = gameDataFood.getFoodObjectDistance().get(0);
        var sf = gameDataFood.getSuperFoodObjectDistance().get(0);

        return (rf < (sf + afterburner_food_threshold*0.1));
    }

    public void resolveFarmingFoodAction(PlayerAction playerAction) {
        playerAction.action = PlayerActions.FORWARD;
        if (isBurnerNeeded()) {
            playerAction.action = PlayerActions.STARTAFTERBURNER;
        }
        else {
            playerAction.action = PlayerActions.STOPAFTERBURNER;
        }
        System.out.println("FarmingMode : " + (isBurnerNeeded() ? "Slow" : "FULLGAS"));
        playerAction.setHeading(Statistic.getHeadingBetween(gfbot ,isBurnerNeeded() ? gameDataFood.getSuperFoodObject().get(0) : gameDataFood.getFoodObject().get(0)));
    }
}
