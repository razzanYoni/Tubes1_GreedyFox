package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;
import java.lang.Math;

// TO DO benerin +- di fungsi isBorderAncaman

public class Data {
    /* Atribute */
    // List of Object
    private List<GameObject> threatObject = new ArrayList<GameObject>();
    private List<Double> threatObjectDistance = new ArrayList<Double>();
    private Integer nThreatObject = 0;

    private List<GameObject> threatPlayer = new ArrayList<GameObject>();
    private List<Double> threatPlayerDistance = new ArrayList<Double>();
    private Integer nThreatPlayer = 0;

    private List<GameObject> foodObject = new ArrayList<GameObject>();
    private List<Double> foodObjectDistance = new ArrayList<Double>();
    private Integer nFoodObject = 0;
    
    private List<GameObject> superFoodObject = new ArrayList<GameObject>();
    private List<Double> superFoodObjectDistance = new ArrayList<Double>();
    private Integer nSuperFoodObject = 0;

    private Position Border;


    // Threshold
    private Double thresholdAncaman;
    private int resultanDistanceNonTeleport = 2;

    /* Method */

    /* Getter */
    public List<GameObject> getThreatObject() {
        return threatObject;
    }

    public List<Double> getThreatObjectDistance() {
        return threatObjectDistance;
    }

    public Integer getNThreatObject() {
        return nThreatObject;
    }

    public List<GameObject> getPlayerObject() {
        return threatPlayer;
    }

    public List<Double> getPlayerDistance() {
        return threatPlayerDistance;
    }

    public Integer getNEnemy() {
        return nThreatPlayer;
    }

    public List<GameObject> getFoodObject() {
        return foodObject;
    }

    public List<Double> getFoodObjectDistance() {
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
        this.thresholdAncaman = (Double) (GreedyFox.getSize() * 2.5);
    }

    public void isThreatObject(GameObject self, GameObject other) {
        /* F.S : object atau player yang masuk ke dalam threshold ancaman */
        double distance;
        if (other.getGameObjectType() == ObjectTypes.FOOD) {
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
        }  else if (other.getGameObjectType() == ObjectTypes.SUPERFOOD) {
            distance = Statistic.getDistanceBetween(self, other);
            if (distance < thresholdAncaman) {
                for (int i = 0; i < nFoodObject; i++) {
                    if (distance < this.superFoodObjectDistance.get(i)) {
                        superFoodObject.add(i, other);
                        superFoodObjectDistance.add(i, distance);
                        break;
                    }
                }
                nSuperFoodObject++;
            }
        } else {
            if (other.getGameObjectType() == ObjectTypes.PLAYER) {
                /* Player terurut berdasarkan distance */
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
                    // if (distance < this.threatPlayerDistance.get(i)) {
                    // threatObject.add(i, other);
                    // threatObjectDistance.add(i, distance);
                    // break;
                    // }
                    // }
                    // nThreatObject++;
                    nThreatPlayer++;
                }
            } else if (other.getGameObjectType() == ObjectTypes.GASCLOUD
                    || other.getGameObjectType() == ObjectTypes.ASTEROIDFIELD) {
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

        Double distanceselfCenter = (Double) Math.sqrt(Math.pow(xSelf - gameState.getWorld().getCenterPoint().getX(), 2)
                + Math.pow(ySelf - gameState.getWorld().getCenterPoint().getY(), 2));
        ;
        Double distance = radius - distanceselfCenter;

        /* Menentukan Kuadran */
        if (distance < thresholdAncaman) {
            if (xSelf == 0) {
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
                    Double theta = (Double) Math.atan(ySelf / xSelf);
                    int xBorder = (radius * Math.cos(theta)) - (int) radius * Math.cos(theta) >= 0.5
                            ? (int) (radius * Math.cos(theta)) + 1
                            : (int) (radius * Math.cos(theta));
                    int yBorder = (radius * Math.sin(theta)) - (int) radius * Math.sin(theta) >= 0.5
                            ? (int) (radius * Math.sin(theta)) + 1
                            : (int) (radius * Math.sin(theta));
                    this.Border = new Position(xBorder, yBorder);
                } else if (ySelf < 0) {
                    Double theta = (Double) Math.atan(ySelf / xSelf) + (Double) Math.PI;
                    int xBorder = (radius * Math.cos(theta)) - (int) radius * Math.cos(theta) >= 0.5
                            ? (int) (radius * Math.cos(theta)) + 1
                            : (int) (radius * Math.cos(theta));
                    int yBorder = (radius * Math.sin(theta)) - (int) radius * Math.sin(theta) >= 0.5
                            ? (int) (radius * Math.sin(theta)) + 1
                            : (int) (radius * Math.sin(theta));
                    this.Border = new Position(xBorder, yBorder);
                }
            } else if (xSelf < 0) {
                if (ySelf == 0) {
                    this.Border = new Position(-radius, 0);
                } else if (ySelf > 0) {
                    Double theta = (Double) Math.atan(ySelf / xSelf) + (Double) Math.PI;
                    int xBorder = (radius * Math.cos(theta)) - (int) radius * Math.cos(theta) >= 0.5
                            ? (int) (radius * Math.cos(theta)) + 1
                            : (int) (radius * Math.cos(theta));
                    int yBorder = (radius * Math.sin(theta)) - (int) radius * Math.sin(theta) >= 0.5
                            ? (int) (radius * Math.sin(theta)) + 1
                            : (int) (radius * Math.sin(theta));
                    this.Border = new Position(xBorder, yBorder);
                } else if (ySelf < 0) {
                    Double theta = (Double) Math.atan(ySelf / xSelf) + (Double) Math.PI * 1.5f;
                    int xBorder = (radius * Math.cos(theta)) - (int) radius * Math.cos(theta) >= 0.5
                            ? (int) (radius * Math.cos(theta)) + 1
                            : (int) (radius * Math.cos(theta));
                    int yBorder = (radius * Math.sin(theta)) - (int) radius * Math.sin(theta) >= 0.5
                            ? (int) (radius * Math.sin(theta)) + 1
                            : (int) (radius * Math.sin(theta));
                    this.Border = new Position(xBorder, yBorder);
                }
            }
        } else {
            this.Border = null;
        }
    }
}