package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;
import java.lang.Math;

public class Data {
    /* Atribute */
    // Game Data
    private GameState gameState;
    private GameObject gFox;

    // List of Object
    // Threat Object Data
    private List<GameObject> threatObject = new ArrayList<GameObject>();
    private List<Double> threatObjectDistance = new ArrayList<Double>();
    private Integer nThreatObject;
    // Threat Player(Enemy) Data
    private List<GameObject> threatPlayer = new ArrayList<GameObject>();
    private List<Double> threatPlayerDistance = new ArrayList<Double>();
    private Integer nThreatPlayer;
    // FoodObject Data
    private List<GameObject> foodObject = new ArrayList<GameObject>();
    private List<Double> foodObjectDistance = new ArrayList<Double>();
    private Integer nFoodObject;
    // Prey Data
    private List<GameObject> preyObject;
    private List<Double> preyObjectDistance;
    private Integer nPreyObject;

    // border Data
    private Position border;

    // Threshold
    private Double thresholdAncaman;
    private int resultanDistanceNonTeleport;

    // Status Data
    private boolean ancamanBorder;
    private boolean needDefenseMode;
    private boolean feasibleAttackMode;

    /* Constructor */
    public Data(GameObject gFox, GameState gameState) {
        // Passing Data
        this.gFox = gFox;
        this.gameState = gameState;
        // Default
        this.nThreatObject = 0;
        this.nThreatPlayer = 0;
        this.nFoodObject = 0;
        this.nPreyObject = 0;
        this.needDefenseMode = false;
        this.feasibleAttackMode = false;
        this.resultanDistanceNonTeleport = 2;
        setThresholdAncaman();
        // Collect Data for other Attributes
        collectingData();
    }

    /* Method */

    /* Getter */
    public List<GameObject> getThreatObject() {
        return this.threatObject;
    }

    public List<Double> getThreatObjectDistance() {
        return this.threatObjectDistance;
    }

    public Integer getNThreatObject() {
        return this.nThreatObject;
    }

    public List<GameObject> getPlayerObject() {
        return this.threatPlayer;
    }

    public List<Double> getPlayerDistance() {
        return this.threatPlayerDistance;
    }

    public Integer getNEnemy() {
        return this.nThreatPlayer;
    }

    public List<GameObject> getFoodObject() {
        return this.foodObject;
    }

    public List<Double> getFoodObjectDistance() {
        return this.foodObjectDistance;
    }

    public Integer getnFoodObject() {
        return this.nFoodObject;
    }

    public Position getBorderPosition() {
        return this.border;
    }

    public Double getThresholdAncaman() {
        return this.thresholdAncaman;
    }

    public int getResultanDistanceNonTeleport() {
        return this.resultanDistanceNonTeleport;
    }

    public boolean isBorderAncaman() {
        return this.ancamanBorder;
    }

    public boolean isNeedDefenseMode() {
        return this.needDefenseMode;
    }

    public boolean isFeasibleAttackMode() {
        return this.feasibleAttackMode;
    }

    /* Setter */
    public void setThresholdAncaman() {
        // masih coba-coba
        this.thresholdAncaman = (Double) (this.gFox.getSize() * 3.0);
    }

    /* Functional */
    private void collectingData() {
        int i;
        var listGameObjects = this.gameState.getGameObjects();
        // Collecting Border Threat Data
        checkBorderAncaman();

        // Collecting Object Threat Data
        for (i = 0; i < listGameObjects.size(); i++) {
            if (listGameObjects.get(i).id != this.gFox.id) { // Jika bukan diri sendiri, lakukan pengecekan
                checkThreatObject(listGameObjects.get(i));
            }
        }

        // Determine the Need for Defense Mode
        if ((this.nThreatObject + this.nThreatPlayer) > 0) { // Jika terancam
            this.needDefenseMode = true;
        }

        // Determine the Feasiblity for Attack Mode
        if (!this.needDefenseMode) {
            // Lakukan Pengecekan apakah attack mode feasible, jika iya langsung collect
            // data Prey (Mangsa)
        }
    }

    private void checkThreatObject(GameObject other) {
        /* F.S : object atau player yang masuk ke dalam threshold ancaman */
        Double distance;
        if (other.getGameObjectType() == ObjectTypes.FOOD || other.getGameObjectType() == ObjectTypes.SUPERFOOD) {
            distance = Statistic.getDistanceBetween(this.gFox, other);
            if (distance < thresholdAncaman) {
                for (int i = 0; i < nFoodObject; i++) {
                    if (distance < this.foodObjectDistance.get(i)) {
                        foodObject.add(i, other);
                        foodObjectDistance.add(i, distance);
                        break;
                    }
                }
                if (nFoodObject == 0) {
                    foodObject.add(other);
                    foodObjectDistance.add(distance);
                }
                nFoodObject++;
            }
        } else {
            if (other.getGameObjectType() == ObjectTypes.PLAYER) {
                /* Player terurut berdasarkan distance */
                distance = Statistic.getDistanceBetween(this.gFox, other);
                if (distance < this.thresholdAncaman) {
                    for (int i = 0; i < nThreatPlayer; i++) {
                        if (distance < this.threatObjectDistance.get(i)) {
                            threatPlayer.add(i, other);
                            threatPlayerDistance.add(i, distance);
                            break;
                        }
                    }

                    if (nThreatPlayer == 0) {
                        threatPlayer.add(other);
                        threatPlayerDistance.add(distance);
                    }
                    nThreatPlayer++;
                }
            } else if (other.getGameObjectType() == ObjectTypes.GASCLOUD
                    || other.getGameObjectType() == ObjectTypes.ASTEROIDFIELD) {
                /* Object threat terurut berdasarkan distance */
                distance = Statistic.getDistanceBetween(this.gFox, other);
                if (distance < this.thresholdAncaman) {
                    for (int i = 0; i < nThreatObject; i++) {
                        if (distance < this.threatPlayerDistance.get(i)) {
                            threatObject.add(i, other);
                            threatObjectDistance.add(i, distance);
                            break;
                        }
                    }

                    if (nThreatObject == 0) {
                        threatObject.add(other);
                        threatObjectDistance.add(distance);
                    }
                    nThreatObject++;
                }
            }
        }
    }

    private void checkBorderAncaman() {
        this.ancamanBorder = false; // inisialisasi
        /* Menentukan Border sebagai ancaman atau tidak */
        Position selfPosition = this.gFox.getPosition();
        int xSelf = selfPosition.getX();
        int ySelf = selfPosition.getY();
        Integer radius = this.gameState.getWorld().getRadius();

        Double distanceselfCenter = (Double) Math
                .sqrt(Math.pow(xSelf - this.gameState.getWorld().getCenterPoint().getX(), 2)
                        + Math.pow(ySelf - this.gameState.getWorld().getCenterPoint().getY(), 2));
        ;
        Double distance = radius - distanceselfCenter;

        /* Menentukan Kuadran */
        if (distance < thresholdAncaman) { // Jika border termasuk ancaman
            if (xSelf == 0) {
                if (ySelf > 0) {
                    this.border = new Position(0, radius);
                } else if (ySelf < 0) {
                    this.border = new Position(0, -radius);
                } else {
                    this.border = new Position(radius, 0); // random
                }
            } else if (xSelf > 0) {
                if (ySelf == 0) {
                    this.border = new Position(radius, 0);
                } else if (ySelf > 0) {
                    Double theta = (Double) Math.atan(ySelf / xSelf);
                    int xBorder = (radius * Math.cos(theta)) - (int) radius * Math.cos(theta) >= 0.5
                            ? (int) (radius * Math.cos(theta)) + 1
                            : (int) (radius * Math.cos(theta));
                    int yBorder = (radius * Math.sin(theta)) - (int) radius * Math.sin(theta) >= 0.5
                            ? (int) (radius * Math.sin(theta)) + 1
                            : (int) (radius * Math.sin(theta));
                    this.border = new Position(xBorder, yBorder);
                } else if (ySelf < 0) {
                    Double theta = (Double) Math.atan(ySelf / xSelf) + (Double) Math.PI;
                    int xBorder = (radius * Math.cos(theta)) - (int) radius * Math.cos(theta) >= 0.5
                            ? (int) (radius * Math.cos(theta)) + 1
                            : (int) (radius * Math.cos(theta));
                    int yBorder = (radius * Math.sin(theta)) - (int) radius * Math.sin(theta) >= 0.5
                            ? (int) (radius * Math.sin(theta)) + 1
                            : (int) (radius * Math.sin(theta));
                    this.border = new Position(xBorder, yBorder);
                }
            } else if (xSelf < 0) {
                if (ySelf == 0) {
                    this.border = new Position(-radius, 0);
                } else if (ySelf > 0) {
                    Double theta = (Double) Math.atan(ySelf / xSelf) + (Double) Math.PI;
                    int xBorder = (radius * Math.cos(theta)) - (int) radius * Math.cos(theta) >= 0.5
                            ? (int) (radius * Math.cos(theta)) + 1
                            : (int) (radius * Math.cos(theta));
                    int yBorder = (radius * Math.sin(theta)) - (int) radius * Math.sin(theta) >= 0.5
                            ? (int) (radius * Math.sin(theta)) + 1
                            : (int) (radius * Math.sin(theta));
                    this.border = new Position(xBorder, yBorder);
                } else if (ySelf < 0) {
                    Double theta = (Double) Math.atan(ySelf / xSelf) + (Double) Math.PI * 1.5f;
                    int xBorder = (radius * Math.cos(theta)) - (int) radius * Math.cos(theta) >= 0.5
                            ? (int) (radius * Math.cos(theta)) + 1
                            : (int) (radius * Math.cos(theta));
                    int yBorder = (radius * Math.sin(theta)) - (int) radius * Math.sin(theta) >= 0.5
                            ? (int) (radius * Math.sin(theta)) + 1
                            : (int) (radius * Math.sin(theta));
                    this.border = new Position(xBorder, yBorder);
                }
            }
            this.ancamanBorder = true;
        }
    }

}