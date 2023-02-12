package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;
import java.lang.Math;

public class Data{
    /* Atribute */
    // List of Object
    private List<GameObject> threatObject = new ArrayList<GameObject>();
    private List<Float> threatObjectDistance = new ArrayList<Float>();
    private Integer nThreatObject = 0;

    private List<GameObject> threatPlayer = new ArrayList<GameObject>();
    private List<Float> threatPlayerDistance = new ArrayList<Float>();
    private Integer nThreatPlayer = 0;

    private List<GameObject> foodObject = new ArrayList<GameObject>();
    private List<Float> foodObjectDistance = new ArrayList<Float>();
    private Integer nFoodObject = 0;
    
    private Position Border;

    // Threshold
    private float thresholdAncaman;
    private int resultanDistanceNonTeleport = 2;

    /* Method */

    /* Getter */
    public List<GameObject> getThreatObject() {
        return threatObject;
    }

    public List<Float> getThreatObjectDistance() {
        return threatObjectDistance;
    }

    public Integer getNThreatObject() {
        return nThreatObject;
    }

    public List<GameObject> getPlayerObject() {
        return threatPlayer;
    }

    public List<Float> getPlayerDistance() {
        return threatPlayerDistance;
    }

    public Integer getNEnemy() {
        return nThreatPlayer;
    }

    public List<GameObject> getFoodObject() {
        return foodObject;
    }

    public List<Float> getFoodObjectDistance() {
        return foodObjectDistance;
    }

    public Integer getnFoodObject() {
        return nFoodObject;
    }

    public Position getBorderPosition() {
        return Border;
    }
    
    /* Setter */
    public void setThresholdAncaman(GameObject GreedyFox) {
        // masih coba-coba
        this.thresholdAncaman = (float) (GreedyFox.getSize() * 1.3);
    }

    public void isThreatObject(GameObject self, GameObject other) {
        /* F.S : object atau player yang masuk ke dalam threshold ancaman */
        float distance;
        if (other.getGameObjectType() == ObjectTypes.FOOD || other.getGameObjectType() == ObjectTypes.SUPERFOOD) {
            distance = Statistic.getDistanceBetween(self, other);
            if (distance < thresholdAncaman) {
                for (int i = 0; i < nFoodObject; i++) {
                    if (distance < this.foodObjectDistance.get(i)) {
                        foodObject.add(i, other);
                        foodObjectDistance.add(i, distance);
                        break;
                    }
                }
                nFoodObject++;
            }
        } else {
            if (other.getGameObjectType() == ObjectTypes.PLAYER) {
                /* Player terurut berdasarkan distance*/
                distance = Statistic.getDistanceBetween(self, other);
                if (distance < thresholdAncaman) {
                    for (int i = 0; i < nThreatPlayer; i++) {
                        if (distance < this.threatObjectDistance.get(i)) {
                            threatPlayer.add(i, other);
                            threatPlayerDistance.add(i, distance);
                            break;
                        }
                    }
                    // for (int i = 0; i < nThreatObject; i++) {
                    //     if (distance < this.threatPlayerDistance.get(i)) {
                    //         threatObject.add(i, other);
                    //         threatObjectDistance.add(i, distance);
                    //         break;
                    //     }
                    // }
                    // nThreatObject++;
                    nThreatPlayer++;
                }
            } else if (other.getGameObjectType() == ObjectTypes.GASCLOUD || other.getGameObjectType() == ObjectTypes.ASTEROIDFIELD){
                /* Object threat terurut berdasarkan distance */
                distance = Statistic.getDistanceBetween(self, other);
                if (distance < thresholdAncaman) {
                    for (int i = 0; i < nThreatObject; i++) {
                        if (distance < this.threatPlayerDistance.get(i)) {
                            threatObject.add(i, other);
                            threatObjectDistance.add(i, distance);
                            break;
                        }
                    }
                    nThreatObject++;
                }
            } 
        }
    }

    public void isBorderAncaman(GameObject self, GameState gameState) {
        /* Menentukan Border sebagai ancaman atau tidak */
        Position selfPosition = self.getPosition(); 
        int xSelf = selfPosition.getX(), ySelf = selfPosition.getY();
        Integer radius = gameState.getWorld().getRadius();

        float distanceselfCenter = (float) Math.sqrt(Math.pow(xSelf - gameState.getWorld().getCenterPoint().getX(), 2)
                                    + Math.pow(ySelf - gameState.getWorld().getCenterPoint().getY(), 2));;
        float distance = radius - distanceselfCenter;

        /* Menentukan Kuadran */
        if (distance < thresholdAncaman) {
            if (xSelf == 0 ) {
                if (ySelf > 0) {
                    this.Border = new Position(0, radius);
                } else if (ySelf < 0) {
                    this.Border = new Position(0, -radius);
                } else {
                    this.Border = new Position(radius, 0); // random
                }
            } else if (xSelf > 0) {
                if (ySelf == 0) {
                    this.Border = new Position(radius, 0);
                } else if (ySelf > 0) {
                    float theta = (float) Math.atan(ySelf/xSelf);
                    int xBorder = (radius * Math.cos(theta)) - (int) radius * Math.cos(theta) >= 0.5 ? (int) (radius * Math.cos(theta)) + 1 : (int) (radius * Math.cos(theta));
                    int yBorder = (radius * Math.sin(theta)) - (int) radius * Math.sin(theta) >= 0.5 ? (int) (radius * Math.sin(theta)) + 1 : (int) (radius * Math.sin(theta));
                    this.Border = new Position(xBorder, yBorder);
                } else if (ySelf < 0) {
                    float theta = (float) Math.atan(ySelf/xSelf) + (float) Math.PI;
                    int xBorder = (radius * Math.cos(theta)) - (int) radius * Math.cos(theta) >= 0.5 ? (int) (radius * Math.cos(theta)) + 1 : (int) (radius * Math.cos(theta));
                    int yBorder = (radius * Math.sin(theta)) - (int) radius * Math.sin(theta) >= 0.5 ? (int) (radius * Math.sin(theta)) + 1 : (int) (radius * Math.sin(theta));
                    this.Border = new Position(xBorder, yBorder);
                }
            } else if (xSelf < 0) {
                if (ySelf == 0) {
                    this.Border = new Position(-radius, 0);
                } else if (ySelf > 0) {
                    float theta = (float) Math.atan(ySelf/xSelf) + (float) Math.PI;
                    int xBorder = (radius * Math.cos(theta)) - (int) radius * Math.cos(theta) >= 0.5 ? (int) (radius * Math.cos(theta)) + 1 : (int) (radius * Math.cos(theta));
                    int yBorder = (radius * Math.sin(theta)) - (int) radius * Math.sin(theta) >= 0.5 ? (int) (radius * Math.sin(theta)) + 1 : (int) (radius * Math.sin(theta));
                    this.Border = new Position(xBorder, yBorder);
                } else if (ySelf < 0) {
                    float theta = (float) Math.atan(ySelf/xSelf) + (float) Math.PI * 1.5f;
                    int xBorder = (radius * Math.cos(theta)) - (int) radius * Math.cos(theta) >= 0.5 ? (int) (radius * Math.cos(theta)) + 1 : (int) (radius * Math.cos(theta));
                    int yBorder = (radius * Math.sin(theta)) - (int) radius * Math.sin(theta) >= 0.5 ? (int) (radius * Math.sin(theta)) + 1 : (int) (radius * Math.sin(theta));
                    this.Border = new Position(xBorder, yBorder);
                }
            }
        }
    }
}