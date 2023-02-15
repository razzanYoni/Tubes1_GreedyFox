package Services;

import Enums.*;
import Models.*;

import java.util.*;

/* TODO : ALGORITMA DEFENSE BERDASARKAN PEMBOBOTAN */
// TODO : MINIMAL NEMBAK TORPEDO

public class DefenseMode {
    // Attribut
    private Data dataState;
    private GameObject gFox;
    private PlayerAction gFoxAction;
    private List<GameObject> listAncaman, listEnemy; // listAncaman minimal berukuran 1, ListEnemy bisa kosong
    private List<Double> threatPlayerDistance, threatObjectDistance;

    // Constructor
    public DefenseMode(Data dataState, GameObject gFox, PlayerAction gFoxAction, List<GameObject> listAncaman,
            List<GameObject> listEnemy, List<Double> threatPlayerDistance, List<Double> threatObjectDistance) {
        this.gFox = gFox;
        this.gFoxAction = gFoxAction;
        this.dataState = dataState;
        this.listAncaman = listAncaman;
        this.listEnemy = listEnemy;
        this.threatObjectDistance = threatObjectDistance;
        this.threatPlayerDistance = threatPlayerDistance;
    }

    // Method
    // Getter
    public PlayerAction getPlayerActionDefend() {
        return this.gFoxAction;
    }

    private int getBobotObject(GameObject object) {
        if (object.getGameObjectType() == ObjectTypes.PLAYER) {
            return 1;
        } else if (object.getGameObjectType() == ObjectTypes.GASCLOUD) {
            return 2;
        } else {
            return 0;
        }
    }

    private int countEscapeHeading() {
        int i;
        double realEscape, faktorDistance, divider, distanceBorder;

        faktorDistance = 0;
        realEscape = 0;
        divider = 0;
        distanceBorder = 0;

        // Mencari faktor bobot distance: distanceMaks
        if (dataState.getNThreatObject() > 0) {
            faktorDistance = threatObjectDistance.get(dataState.getNThreatObject() - 1);
        }
        if (dataState.getNEnemy() > 0) {
            faktorDistance = threatPlayerDistance.get(dataState.getNEnemy() - 1);
        }

        if (dataState.getNThreatObject() > 0 && dataState.getNEnemy() > 0) {
            faktorDistance = Math.max(threatObjectDistance.get(dataState.getNThreatObject() - 1),
                    threatPlayerDistance.get(dataState.getNEnemy() - 1));
        }
        if (dataState.isBorderAncaman()) { // Jika border adalah ancaman, distance-nya dipertimbangkan
            distanceBorder = Statistic.getDistanceBetween(gFox.getPosition(), dataState.getBorderPosition());
            faktorDistance = Math.max(distanceBorder, faktorDistance);
        }

        /*
         * Hitung escapeHeading dengan rumus
         * realEscape = heading1*(faktorDistance/distance1)*bobotObject1 + ... +
         * headingN*(faktorDistance/distanceN)*bobotObjectN
         * bobot Object: PLAYER: 1, GASCLOUD: 4, BORDER: 5
         */
        for (i = 0; i < this.dataState.getNThreatObject(); i++) { // Hitung heading untuk setiap
            // ThreatObject
            realEscape += (Statistic.getHeadingBetween(gFox, listAncaman.get(i))
                    * (faktorDistance / threatObjectDistance.get(i)) * getBobotObject(listAncaman.get(i)));
            divider += (faktorDistance / threatObjectDistance.get(i)) * getBobotObject(listAncaman.get(i));
        }
        for (i = 0; i < this.dataState.getNEnemy(); i++) { // Hitung Heading untuk setiap Enemy
            realEscape += (Statistic.getHeadingBetween(gFox, listEnemy.get(i))
                    * (faktorDistance / threatPlayerDistance.get(i)) * getBobotObject(listEnemy.get(i)));
            divider += (faktorDistance / threatPlayerDistance.get(i)) * getBobotObject(listEnemy.get(i));

        }
        if (dataState.isBorderAncaman()) { // Jika Border Masuk ke dalam ancaman
            realEscape += (Statistic.getHeadingBetween(gFox,
                    this.dataState.getBorderPosition()) * (faktorDistance / distanceBorder) * 3);
            divider += ((faktorDistance / distanceBorder) * 3);
        }

        // Hitung hasil akhir real escape
        realEscape /= divider;
        return (int) realEscape;
    }

    // Fungsi utama untuk menangani ancaman
    public void ressolvingTreat() {

        // KAMUS LOKAL
        int escapeHeading;
        boolean isSandwich;
        // ALGORITMA
        escapeHeading = 0; // default
        isSandwich = false;

        // Jika hanya ada 1 enemy dan memungkinkan tembak
        if (this.dataState.getNEnemy() == 1 && gFox.TorpedoSalvoCount > 0 && gFox.getSize() > 10) {
            gFoxAction.action = PlayerActions.FIRETORPEDOES;
            System.out.println("TEMBAKKK, sisa salvo: " + gFox.TorpedoSalvoCount);
            gFoxAction.heading = Statistic.getHeadingBetween(gFox, listEnemy.get(0));
        } else {
            // Jika tidak memungkinkan menembak atau nEnemy != 1
            escapeHeading = countEscapeHeading();
            isSandwich = (escapeHeading == 0); // Pengecekan Sandwich
        }

        if (isSandwich || escapeHeading == 0) { // Penanganan Kasus Sandwich
            // Belum dihandle
            /*
             * Strategi
             * Cari celah jika sandwich hanya dua ancaman
             * Jika lebih dari dua, go to asteroid fields jika ada
             * Jika asteroid fields tak ada pertimbangkan penggunaan teleporter
             * Jika pilihan antara trobos gas cloud atau enemy, trobos gas cloud
             * Jika hanya ada enemy, bergerak menuju enemy terkecil
             */

            System.out.println("Mencoba Untuk Bertahan Dari Kejammnya Kehidupan");
        } else {
            System.out.println("Mencoba Bertahan Nih");
            gFoxAction.action = PlayerActions.FORWARD;
            gFoxAction.heading = (escapeHeading) % 360; // Opposite Direction dari resultan escape
        }
    }
}
