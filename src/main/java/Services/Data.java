package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

import javax.sound.midi.Soundbank;

import java.lang.Math;

// TODO : THRESHOLD ATTACK
// TODO : THRESHOLD FARMING

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
    // SuperfoodObject Data
    private List<GameObject> superFoodObject = new ArrayList<GameObject>();
    private List<Double> superFoodObjectDistance = new ArrayList<Double>();
    private Integer nSuperFoodObject;
    // Prey Data
    private List<GameObject> preyObject = new ArrayList<GameObject>();
    private List<Double> preyObjectDistance = new ArrayList<Double>();
    private Integer nPreyObject;

    // border Data
    private Position border;

    // Threshold
    private Double thresholdDistanceAncaman;
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
        this.nSuperFoodObject = 0;
        this.nPreyObject = 0;
        this.needDefenseMode = false;
        this.feasibleAttackMode = false;
        this.resultanDistanceNonTeleport = 2;
        setThresholdDistanceAncaman();
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

    public List<GameObject> getPreyObject() {
        return this.preyObject;
    }

    public List<Double> getPreyObjectDistance() {
        return this.preyObjectDistance;
    }

    public Integer getNPreyObject() {
        return this.nPreyObject;
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

    public List<GameObject> getSuperFoodObject() {
        return superFoodObject;
    }

    public List<Double> getSuperFoodObjectDistance() {
        return superFoodObjectDistance;
    }

    public Integer getnSuperFoodObject() {
        return nSuperFoodObject;
    }

    public Position getBorderPosition() {
        return this.border;
    }

    public Double getThresholdDistanceAncaman() {
        return this.thresholdDistanceAncaman;
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
    public void setThresholdDistanceAncaman() {
        if (this.gFox.getSize() < 25) {
            this.thresholdDistanceAncaman = (Double) (this.gFox.getSize() * 8.0);
        } else {
            this.thresholdDistanceAncaman = (Double) (this.gFox.getSize() * 6.0);
        }
    }

    /* Functional */
    private void collectingData() {
        int i;
        List<GameObject> listGameObjects = this.gameState.getGameObjects();
        List<GameObject> listPlayer = this.gameState.getPlayerGameObjects();
        // Collecting Border Threat Data
        checkBorderAncaman();

        // Collecting Object Threat Data
        for (i = 0; i < listGameObjects.size(); i++) {
            if (listGameObjects.get(i).id != this.gFox.id) { // Jika bukan diri sendiri, lakukan pengecekan
                // System.out.println(listGameObjects.get(i).getGameObjectType());
                checkThreatObject(listGameObjects.get(i));
            }
        }

        for (i = 0; i < listPlayer.size(); i++) {
            if (listPlayer.get(i).id != this.gFox.id) { // Jika bukan diri sendiri, lakukan pengecekan
                checkThreatObject(listPlayer.get(i));
            }
        }

        // Determine the Need for Defense Mode
        if ((this.nThreatObject + this.nThreatPlayer) > 0 || this.ancamanBorder) { // Jika terancam
            this.needDefenseMode = true;
        }

        // Determine the Feasiblity for Attack Mode
        if (!this.needDefenseMode) {
            // Lakukan Pengecekan apakah attack mode feasible, jika iya langsung collect
            // data Prey (Mangsa)
            if (this.nPreyObject > 0) {
                this.feasibleAttackMode = true;
            }
        }

    }

    private void checkThreatObject(GameObject other) {
        /* F.S : object atau player yang masuk ke dalam threshold ancaman */
        Double distance;
        distance = Statistic.getDistanceBetween(this.gFox, other);
        if (distance < this.thresholdDistanceAncaman) {
            if (other.getGameObjectType() == ObjectTypes.FOOD) {
                /* Food terurut berdasarkan distance */
                if (nFoodObject == 0) {
                    foodObject.add(other);
                    foodObjectDistance.add(distance);
                } else {
                    if (this.foodObjectDistance.get(nFoodObject - 1) <= distance) {
                        foodObject.add(other);
                        foodObjectDistance.add(distance);
                    } else {
                        for (int i = 0; i < nFoodObject; i++) {
                            if (distance < this.foodObjectDistance.get(i)) {
                                foodObject.add(i, other);
                                foodObjectDistance.add(i, distance);
                                break;
                            }
                        }
                    }
                }
                nFoodObject++;
            } else if (other.getGameObjectType() == ObjectTypes.SUPERFOOD) {
                /* SuperFood terurut berdasarkan distance */
                if (nSuperFoodObject == 0) {
                    superFoodObject.add(other);
                    superFoodObjectDistance.add(distance);
                } else {
                    if (this.superFoodObjectDistance.get(nSuperFoodObject - 1) <= distance) {
                        superFoodObject.add(other);
                        superFoodObjectDistance.add(distance);
                    } else {
                        for (int i = 0; i < nSuperFoodObject; i++) {
                            if (distance < this.superFoodObjectDistance.get(i)) {
                                superFoodObject.add(i, other);
                                superFoodObjectDistance.add(i, distance);
                                break;
                            }
                        }
                    }
                }
                nSuperFoodObject++;
            } else {
                if (other.getGameObjectType() == ObjectTypes.PLAYER) {
                    /* Player terurut berdasarkan distance */
                    if ((other.getSize() - this.gFox.getSize()) >= 0) {
                        if (nThreatPlayer == 0) {
                            threatPlayer.add(other);
                            threatPlayerDistance.add(distance);
                        } else {
                            if (this.threatPlayerDistance.get(nThreatPlayer - 1) <= distance) {
                                threatPlayer.add(other);
                                threatPlayerDistance.add(distance);
                            } else {
                                for (int i = 0; i < nThreatPlayer; i++) {
                                    if (distance < this.threatPlayerDistance.get(i)) {
                                        threatPlayer.add(i, other);
                                        threatPlayerDistance.add(i, distance);
                                        break;
                                    }
                                }
                            }
                        }
                        this.nThreatPlayer++;
                    } else {
                        if (this.nPreyObject == 0) {
                            preyObject.add(other);
                            preyObjectDistance.add(distance);
                        } else {
                            if (this.preyObjectDistance.get(nPreyObject - 1) <= distance) {
                                preyObject.add(other);
                                preyObjectDistance.add(distance);
                            } else {
                                for (int i = 0; i < nPreyObject; i++) {
                                    if (distance < this.preyObjectDistance.get(i)) {
                                        preyObject.add(i, other);
                                        preyObjectDistance.add(i, distance);
                                        break;
                                    }
                                }
                            }
                        }
                        nPreyObject++;
                    }

                } else if (other.getGameObjectType() == ObjectTypes.GASCLOUD) {
                    // || other.getGameObjectType() == ObjectTypes.ASTEROIDFIELD) {
                    /* Object threat terurut berdasarkan distance */
                    if (nThreatObject == 0) {
                        threatObject.add(other);
                        threatObjectDistance.add(distance);
                    } else {
                        if (this.threatObjectDistance.get(nThreatObject - 1) <= distance) {
                            threatObject.add(other);
                            threatObjectDistance.add(distance);
                        } else {
                            for (int i = 0; i < nThreatObject; i++) {
                                if (distance < this.threatObjectDistance.get(i)) {
                                    threatObject.add(i, other);
                                    threatObjectDistance.add(i, distance);
                                    break;
                                }
                            }
                        }
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

        Double distanceselfCenter = Statistic.getDistanceBetween(selfPosition,
                this.gameState.getWorld().getCenterPoint());

        Double distance = radius - distanceselfCenter;

        /* Menentukan Kuadran */
        if (distance < this.thresholdDistanceAncaman) { // Jika border termasuk ancaman
            if (xSelf == 0) {
                if (ySelf > 0) {
                    this.border = new Position(0, radius);
                } else if (ySelf < 0) {
                    this.border = new Position(0, -radius);
                } else {
                    this.border = new Position(radius, 0);
                }
            } else if (xSelf > 0) {
                if (ySelf == 0) {
                    this.border = new Position(radius, 0);
                } else if (ySelf > 0) {
                    Double theta = (Double) Math.atan2(ySelf, xSelf);
                    this.border = new Position((int) Math.round(radius * Math.cos(theta)),
                            (int) Math.round(radius * Math.sin(theta)));
                } else if (ySelf < 0) {
                    Double theta = (Double) Math.atan2(ySelf, xSelf);
                    this.border = new Position((int) Math.round(radius * Math.cos(theta)),
                            (int) Math.round(radius * Math.sin(theta)));
                }
            } else if (xSelf < 0) {
                if (ySelf == 0) {
                    this.border = new Position(-radius, 0);
                } else if (ySelf > 0) {
                    Double theta = (Double) Math.atan2(ySelf, xSelf);
                    this.border = new Position((int) Math.round(radius * Math.cos(theta)),
                            (int) Math.round(radius * Math.sin(theta)));
                } else if (ySelf < 0) {
                    Double theta = (Double) Math.atan2(ySelf, xSelf);
                    this.border = new Position((int) Math.round(radius * Math.cos(theta)),
                            (int) Math.round(radius * Math.sin(theta)));
                }
            }
            this.ancamanBorder = true;
        } else {
            // Kondisi ketika threshold tidak terpenuhi yaitu jauh
            this.ancamanBorder = false;
        }
    }

}