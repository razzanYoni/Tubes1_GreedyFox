package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

// File : FarmingMode.java
// Untuk memilih apakah memakan food biasa atau super food

// ??? : STOP AFTER BURNER EDGE CASE BELUM TER-HANDLE
// TODO : KASUS GALAU

public class FarmingMode {
    // Atribut
    private Double afterburner_food_threshold;
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
        this.afterburner_food_threshold = fm.afterburner_food_threshold;
        this.gameDataFood = fm.gameDataFood;
        this.gfbot = fm.gfbot;
        // this.foodObj = fm.foodObj;
        // this.gfAction = fm.gfAction;
    }

    // Membandingkan SuperFood dengan regular food lalu 
    public void setSuperFoodThreshold(GameObject obj) {
        afterburner_food_threshold = (Double) (obj.getSize() * 0.02);  // Pake Radius Kah??
    }

    public boolean isBurnerNeeded() {
        if (gameDataFood.getnSuperFoodObject() <= 0) {
            return false;
        } else {
            var rf = gameDataFood.getFoodObjectDistance().get(0);
            var sf = gameDataFood.getSuperFoodObjectDistance().get(0);
            /* superfood sama isburnerneeded*/
            return (rf < (sf + afterburner_food_threshold*0.1));
        }
    }

    public void resolveFarmingFoodAction() {
        gfAction.action = PlayerActions.FORWARD;

        // KALO MAU PAKE HANDLE LAGI KASUS GERAK AFTERBURNER SAMA STOPAFTERBURNER
//        if (isBurnerNeeded()) {
//            gfAction.action = PlayerActions.STARTAFTERBURNER;
//        }
//        else {
//            gfAction.action = PlayerActions.STOPAFTERBURNER;
//        }

        gfAction.setHeading(Statistic.getHeadingBetween(gfbot ,isBurnerNeeded() ? gameDataFood.getSuperFoodObject().get(0) : gameDataFood.getFoodObject().get(0)));
    }
}