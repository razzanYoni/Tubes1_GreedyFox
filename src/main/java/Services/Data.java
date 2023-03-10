package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

import java.lang.Math;


public class Data {
    /* Atribute */
    // Game Data
    public GameState gameState;
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
    // TorpedoSalvo Enemy Data
    private List<GameObject> torpedoObject = new ArrayList<GameObject>();
    private Integer nEnemysTorpedo;

    // border Data
    private Position border;

    // Threshold
    private Double thresholdDistanceAncaman;
    private Double thresholdThreatPlayer;
    private int resultanDistanceNonTeleport;

    // Status Data
    private boolean ancamanBorder;
    private boolean outsideBorder;
    private boolean needDefenseMode;
    private boolean feasibleAttackMode;
    private boolean isTorpedoOptimal;
    private boolean torpedoEscapeMode;
    private boolean torpedoShieldMode;
    private boolean superNovaExist;
    private boolean playerAroundCenter;
    private boolean teleportOutsideBorder;
    private boolean teleportOneEnemy;

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
        this.nEnemysTorpedo = 0;
        this.superNovaExist = false;
        this.playerAroundCenter = false;
        this.teleportOutsideBorder = false;
        this.teleportOneEnemy = false;
        setThresholdDistanceAncaman();
        setThresholdDistanceEnemy();
        // Collect Data for other Attributes
        collectingData();
        setIsTorpedoOptimal();
        setTorpedoEscape();
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

    public Integer getNTorpedoesObject(){
        return this.nEnemysTorpedo;
    }

    public List<GameObject> getTorpedoObject() {
        return this.torpedoObject;
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

    public boolean isTorpedoOptimal() {
        return this.isTorpedoOptimal;
    }

    public boolean isTorpedoEscape(){
        return this.torpedoEscapeMode;
    }

    public boolean isTorpedoShield(){
        return this.torpedoShieldMode;
    }

    public boolean isSupernovaExist() {
        return this.superNovaExist; 
    }

    public boolean isTeleportOutsideBorder() {
        return this.teleportOutsideBorder;
    }

    public boolean isOutsideBorder() {
        return this.outsideBorder;
    }

    public boolean isTeleportOneEnemy() {
        return this.teleportOneEnemy;
    }

    /* Setter */

    public void setTorpedoEscape() {
        /* Deskripsi : Menentukan apakah penggunaan shielda ataupun kabur dari torpedo lawan */
        if ((this.nEnemysTorpedo >= 2) && (this.gFox.getSize() >= 40)) {
            /* SHIELD */
            torpedoShieldMode = true;
            torpedoEscapeMode = false;
        } else {
            if(this.nEnemysTorpedo == 0){
                torpedoShieldMode = false;
                torpedoEscapeMode = false;
            } else {
                torpedoShieldMode = false;
                torpedoEscapeMode = true;
            }
        }
    }

    public void setThresholdDistanceAncaman() {
        /* Deskripsi : Mengatur threshold untuk jarak ke object ancaman (gas cloud, asteroid fields, wormhole) */

        Double tempThreshold;
        if (gameState.getWorld().getRadius() >= 1500) {
            tempThreshold = (Double) (gameState.getWorld().getRadius() / 4.5);
        } else if (gameState.getWorld().getRadius() >= 600) {
            tempThreshold = (Double) (gameState.getWorld().getRadius() / 4.25);
        } else {
            tempThreshold = (Double) (gameState.getWorld().getRadius() / 3.5);
        }
        this.thresholdDistanceAncaman = (Double) (this.gFox.getSize() * Math.sqrt(2) + tempThreshold);
    }

    public void setThresholdDistanceEnemy() {
        /* Deskripsi : Mengatur threshold untuk jarak ke player lain */

        Double tempThreshold;
        if (gameState.getWorld().getRadius() >= 1500) {
            tempThreshold = (Double) (gameState.getWorld().getRadius() / 6.25);
        } else if (gameState.getWorld().getRadius() >= 600) {
            tempThreshold = (Double) (gameState.getWorld().getRadius() / 5.5);
        } else {
            tempThreshold = (Double) (gameState.getWorld().getRadius() / 4.5);
        }
        this.thresholdThreatPlayer = (Double) (this.gFox.getSize() * Math.sqrt(2) + tempThreshold);
    }

    public void setIsTorpedoOptimal() {
        /* Deskripsi : Mengatur apakah penggunaan torpedo optimal atau tidak */
        Double tempThreshold;
        if (gameState.getWorld().getRadius() >= 2000) {
            tempThreshold = gameState.getWorld().getRadius() / 3.5;
        } else if (gameState.getWorld().getRadius() >= 1500) {
            tempThreshold = gameState.getWorld().getRadius() / 3.0;
        } else if (gameState.getWorld().getRadius() >= 1000) {
            tempThreshold = gameState.getWorld().getRadius() / 2.0;
        } else if (gameState.getWorld().getRadius() >= 700) {
            tempThreshold = gameState.getWorld().getRadius() / 1.5;
        } else {
            tempThreshold = gameState.getWorld().getRadius() / 1.3;
        }

        if (this.nThreatPlayer > 0
            && ((((this.threatPlayerDistance.get(0) 
                  - (this.threatPlayer.get(0).getSize() * Math.sqrt(2)) 
                  - (this.gFox.getSize() * Math.sqrt(2))) <= (tempThreshold))
            && (this.gFox.getSize() >= 40))
            && (this.gFox.TorpedoSalvoCount > 0)
            // || (this.gFox.getSize() >= 55)
            // && (this.gFox.TorpedoSalvoCount > 0)
            )
        ) {

           if (this.gFox.TorpedoSalvoCount > 0) {

                        isTorpedoOptimal = true;
           } else {
               isTorpedoOptimal = false;
           }

        } else if (this.nPreyObject > 0
                && ((((this.preyObjectDistance.get(0) 
                    - (this.preyObject.get(0).getSize() * Math.sqrt(2)) 
                    - (this.gFox.getSize() * Math.sqrt(2))) <= tempThreshold)
                && (this.gFox.getSize() >= 40))
                && (this.gFox.TorpedoSalvoCount > 0)
                // || (this.gFox.getSize() >= 55)
                // && (this.gFox.TorpedoSalvoCount > 0)
                )
        ) {

           if (this.gFox.TorpedoSalvoCount > 0
                   && this.gFox.getSize() >= 40) {
               
                    isTorpedoOptimal = true;

           } else {
                
                isTorpedoOptimal = false;
           }

        } else /* Tidak ada Enemy */ {isTorpedoOptimal = false;}
    }

    /* Functional */
    private void checkFoodObject(GameObject other) {
        /* Deskripsi : Mengecek apakah ada food atau superfood 
         * F.S. : food dan superfood terurut berdasarkan distance
        */

        Double distance;
        distance = Statistic.getDistanceBetween(this.gFox, other);
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
        } else /* OBJECT ADALAH SUPERFOOD*/ {
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
        }
    }
    
    private void checkThreatObject(GameObject other) {
        /* Deskripsi : Mengecek apakah ada threat object di sekitar threshold */
        /* F.S : GASCLOUD DAN ASTEROIDFIELD TERURUT MEMBESAR */
        Double distance;
        distance = Statistic.getDistanceBetween(this.gFox, other);

        if (distance < (this.thresholdDistanceAncaman + other.getSize() * Math.sqrt(2))) {
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

    private void checkThreatPlayer(GameObject other){
        /* Deskripsi: Mengecek Apakah ada player di sekitar threshold atau tidak 
         * F.S : threatPlayer atau Prey terurut berdasarkan distance
        */

        Double distance = Statistic.getDistanceBetween(this.gFox, other);
        // Double distanceFromCenter = Statistic.getDistanceBetween(other.getPosition(), gameState.getWorld().centerPoint);

        // if (distanceFromCenter <= ) SUPERNOVA PICKUP
        if (distance < (thresholdThreatPlayer + other.getSize() * Math.sqrt(2))) {
            /* Player terurut berdasarkan distance */
            if ((other.getSize() - this.gFox.getSize()) > 0) {
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
        } else if ((distance < (thresholdThreatPlayer + other.getSize() + (gameState.getWorld().getRadius()/8.0f))) 
                    && ((other.getSize() - gFox.getSize() + 20.0) < 0)) {
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
    }

    private void checkEnemysTorpedo(GameObject other) {
        /* F.S : List Of Enemy's Torpedoes */

        Double distance;
        distance = Statistic.getDistanceBetween(this.gFox, other);

        /* Check Player Action nTorpedoes - torpedoesCount current */

        if (distance < (350 + gFox.getSize() * Math.sqrt(2)) 
        && (((other.currentHeading - Statistic.getHeadingBetween(gFox, other) + 180) % 360) < 15)
        && (((other.currentHeading - Statistic.getHeadingBetween(gFox, other) + 180) % 360) > -15) ) {
            if (other.getGameObjectType() == ObjectTypes.TORPEDOSALVOCOUNT) {
                torpedoObject.add(0,other);
                nEnemysTorpedo++;
            }
        }
    }

    private void checkBorderAncaman() {
        /* Deskripsi : Menentukan Border sebagai ancaman atau tidak 
         * F.S : Jika border termasuk ancaman, maka ancamanBorder = true dan akan men-set border dengan position terdekat oleh GreedyFox
        */
        
        /* Inisialisasi */
        this.ancamanBorder = false;
        this.outsideBorder = false; 
        Position selfPosition = this.gFox.getPosition();
        int xSelf = selfPosition.getX();
        int ySelf = selfPosition.getY();
        Integer radius = this.gameState.getWorld().getRadius();

        /* Menentukan Jarak dari GreedyFox ke Center */
        Double distanceselfCenter = Statistic.getDistanceBetween(selfPosition,
                this.gameState.getWorld().getCenterPoint());

        /* Menentukan Jarak dari GreedyFox ke Border */
        Double distance = radius - distanceselfCenter;

        /* Menentukan Kuadran */
        if ((distance - (gFox.getSize() * (Math.sqrt(2) + 0.2f))) < (this.gameState.getWorld().getRadius()/13.0f) + 30.0) {
            /*Jika Border masuk threshold (dekat) anggap sebagai ancaman*/
             
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

                    this.border = new Position  (
                                                (int) Math.round(radius * Math.cos(theta)),
                                                (int) Math.round(radius * Math.sin(theta))
                                                );
                } else if (ySelf < 0) {
                    Double theta = (Double) Math.atan2(ySelf, xSelf);

                    this.border = new Position  (
                                                (int) Math.round(radius * Math.cos(theta)),
                                                (int) Math.round(radius * Math.sin(theta))
                                                );
                }
            } else if (xSelf < 0) {
                if (ySelf == 0) {
                    this.border = new Position(-radius, 0);
                } else if (ySelf > 0) {
                    Double theta = (Double) Math.atan2(ySelf, xSelf);

                    this.border = new Position  (   
                                                (int) Math.round(radius * Math.cos(theta)),
                                                (int) Math.round(radius * Math.sin(theta))
                                                );
                } else if (ySelf < 0) {
                    Double theta = (Double) Math.atan2(ySelf, xSelf);
                    this.border = new Position  (   
                                                (int) Math.round(radius * Math.cos(theta)),
                                                (int) Math.round(radius * Math.sin(theta))
                                                );
                }
            }
            this.ancamanBorder = true;
            this.outsideBorder = false;
        } else if ((distance - gFox.getSize() - 30) < 0) {
            this.outsideBorder = true;
            this.ancamanBorder = true;
        } else {
            this.ancamanBorder = false;
            this.outsideBorder = false;
        }
    }

    private void collectingData() {
        /* Deskripsi : Memperoleh semua data yang akan digunakan */
        
        // Inisialisasi
        int i;
        List<GameObject> listGameObjects = this.gameState.getGameObjects();
        List<GameObject> listPlayer = this.gameState.getPlayerGameObjects();

        // Collecting Border Threat Data
        checkBorderAncaman();

        // Collecting Object Threat Data
        for (i = 0; i < listGameObjects.size(); i++) {
            if (listGameObjects.get(i).id != this.gFox.id) { // Jika bukan diri sendiri, lakukan pengecekan
                GameObject currentObject= listGameObjects.get(i);
                if ((currentObject.getGameObjectType() == ObjectTypes.FOOD)
                    || (currentObject.getGameObjectType() == ObjectTypes.SUPERFOOD))
                {
                    /* Deskripsi : Mengambil data Food atau SuperFood */
                    checkFoodObject(currentObject);

                } else {
                    if (currentObject.getGameObjectType() == ObjectTypes.GASCLOUD
                        || currentObject.getGameObjectType() == ObjectTypes.ASTEROIDFIELD
                        || currentObject.getGameObjectType() == ObjectTypes.WORMHOLE) 
                        {
                            /* Deskripsi : Mengambil data Gas Cloud, Asteroid Field, dan Worm Hole */
                        checkThreatObject(currentObject);

                    } else {
                        if (currentObject.getGameObjectType() == ObjectTypes.SUPERNOVAPICKUP){
                            superNovaExist = true;
                        } else {
                            if (currentObject.getGameObjectType() == ObjectTypes.TELEPORTER) {
                                Double radiusTeleporter = Statistic.getDistanceBetween(gameState.getWorld().getCenterPoint(), currentObject.getPosition());
                                if (radiusTeleporter > (gameState.getWorld().getRadius() - (gFox.getSize() * Math.sqrt(2)))) {
                                    teleportOutsideBorder = true;
                                } else {
                                    teleportOutsideBorder = false;
                                }
                            } else {
                                checkEnemysTorpedo(currentObject);
                            }
                        }
                    }
                }
            }
        }

        // Collecting Player Threat Data
        Integer nPlayer = listPlayer.size();
        for (i = 0; i < nPlayer; i++) {
            if (listPlayer.get(i).id != this.gFox.id) { // Jika bukan diri sendiri, lakukan pengecekan
                checkThreatPlayer(listPlayer.get(i));
                if ((nPlayer == 2)
                    &&
                    ((nThreatPlayer == 0)
                    || (nPreyObject == 0))) {
                        teleportOneEnemy = true;
                        preyObject.add(0,listPlayer.get(i));
                        preyObjectDistance.add(0, Statistic.getDistanceBetween(gFox, listPlayer.get(i)));
                        nPreyObject++;
                    }
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
}