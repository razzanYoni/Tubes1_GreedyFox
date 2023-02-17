package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

// File : FarmingMode.java
// Untuk memilih apakah memakan food biasa atau super food

public class FarmingMode {
    // Atribut
    private Data gameDataFood;
    private GameObject gfbot;
    // private GameObject foodObj;
    private PlayerAction gfAction;
    
    // Method
    // Contructor
    public FarmingMode(Data gameDataFood, GameObject gfbot, PlayerAction gfAction) {
        this.gameDataFood = gameDataFood;
        this.gfbot = gfbot;
        this.gfAction = gfAction;
        setSuperFoodThreshold(gfbot);
    }

    // Copy Constructor
    public FarmingMode(FarmingMode fm) {
        this.gameDataFood = fm.gameDataFood;
        this.gfbot = fm.gfbot;
        // this.foodObj = fm.foodObj;
        // this.gfAction = fm.gfAction;
    }

    // Membandingkan SuperFood dengan regular food lalu 
    public boolean isBurnerNeeded() {
        if (gameDataFood.getnSuperFoodObject() <= 0) {
            return false;
        } else {
            var rf = gameDataFood.getFoodObjectDistance().get(0);
            var sf = gameDataFood.getSuperFoodObjectDistance().get(0);
            /* superfood sama isburnerneeded */
            return (sf < rf);
        }
    }

    public void resolveFarmingFoodAction() {
        gfAction.action = PlayerActions.FORWARD;
        gfAction.setHeading(Statistic.getHeadingBetween(gfbot ,isBurnerNeeded() ? gameDataFood.getSuperFoodObject().get(0) : gameDataFood.getFoodObject().get(0)));
        if (isBurnerNeeded()) {
            System.out.println("Reach Nearest SuperFood");
        } else {
            System.out.println("Reach Nearest Food");
        }
    }
}