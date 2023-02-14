package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

/* TODO : ALGORITMA DEFENSE BERDASARKAN PEMBOBOTAN */
// TODO : MINIMAL NEMBAK TORPEDO

public class DefenseMode {
    // Attribut
    private Data dataState;
    private GameObject gFox;
    private PlayerAction gFoxAction;
    private List<GameObject> listAncaman, listEnemy; // listAncaman minimal berukuran 1, ListEnemy bisa kosong

    // Constructor
    public DefenseMode(Data dataState, GameObject gFox, PlayerAction gFoxAction, List<GameObject> listAncaman,
            List<GameObject> listEnemy) {
        this.gFox = gFox;
        this.gFoxAction = gFoxAction;
        this.dataState = dataState;
        this.listAncaman = listAncaman;
        this.listEnemy = listEnemy;
    }

    // Method
    // Getter
    public PlayerAction getPlayerActionDefend() {
        return this.gFoxAction;
    }

    // Fungsi utama untuk menangani ancaman
    public void ressolvingTreat() {

        // KAMUS LOKAL
        int escapeHeading, i;
        boolean isSandwich;
        // ALGORITMA
        escapeHeading = 0; // default
        isSandwich = false;
        if (this.listAncaman.size() > 1) { // Jika ancaman lebih dari 1
            if (this.dataState.getNEnemy() >= 1) { // Jika hanya ada 1 enemy, tembak
                System.out.println("DI DEFENSE " + gFox.TorpedoSalvoCount);
                if (gFox.TorpedoSalvoCount > 0 && gFox.getSize() > 10) { // Jika ada salvo charge, tembakkan
                    gFoxAction.action = PlayerActions.FIRETORPEDOES;
                    
                    
                    
                    /* GAK MASUK SINI */
                    System.out.println("MINIMAL MASUK0");
                    
                    
                    
                    
                    gFoxAction.heading = Statistic.getHeadingBetween(gFox, listEnemy.get(0));
                } else { // Jika hanya bisa kabur dari enemy
                    escapeHeading += Statistic.getHeadingBetween(gFox, listEnemy.get(0));
                    for (i = 0; i < this.dataState.getNThreatObject(); i++) { // Hitung heading untuk setiap
                                                                              // ThreatObject
                        escapeHeading += Statistic.getHeadingBetween(gFox, listAncaman.get(i));
                    }
                    if (dataState.isBorderAncaman()) { // Jika Border Masuk ke dalam ancaman
                        escapeHeading += Statistic.getHeadingBetween(gFox,
                                this.dataState.getBorderPosition());
                        escapeHeading /= (this.dataState.getNThreatObject() + this.dataState.getNEnemy() + 1);
                    } else {
                        escapeHeading /= (this.dataState.getNThreatObject() + this.dataState.getNEnemy());
                    }
                    isSandwich = (escapeHeading == 0); // Pengecekan Sandwich
                }
            } else { // Jika terdapat lebih dari satu enemy, atau tak ada enemy
                for (i = 0; i < this.dataState.getNThreatObject(); i++) { // Hitung heading untuk setiap ThreatObject
                    escapeHeading += Statistic.getHeadingBetween(gFox, listAncaman.get(i));
                }
                for (i = 0; i < this.dataState.getNEnemy(); i++) { // Hitung Heading untuk setiap Enemy
                    escapeHeading += Statistic.getHeadingBetween(gFox, listEnemy.get(i));
                }
                if (dataState.isBorderAncaman()) { // Jika border masuk ke dalam ancaman
                    escapeHeading += Statistic.getHeadingBetween(gFox,
                    this.dataState.getBorderPosition());
                    escapeHeading /= (this.dataState.getNThreatObject() + this.dataState.getNEnemy() + 1);
                }else {
                    escapeHeading /= (this.dataState.getNThreatObject() + this.dataState.getNEnemy());
                }
                isSandwich = (escapeHeading == 0); // Pengecekan Sandwich
            }
        } else {
            if (this.dataState.getNEnemy() >= 1) { // Ada satu ancaman yaitu enemy
                if (gFox.TorpedoSalvoCount > 0 && (gFox.getSize() > 10)) { // Jika ada salvo charge, tembakkan
                    gFoxAction.action = PlayerActions.FIRETORPEDOES;
                    
                    
                    
                    
                    
                    
                    /* GAK MASUK SINI */
                    System.out.println("MINIMAL MASUK1");
                    
                    
                    
                    
                    
                    
                    
                    gFoxAction.heading = Statistic.getHeadingBetween(gFox, listEnemy.get(0));
                } else { // Jika hanya bisa kabur dari enemy
                    escapeHeading += Statistic.getHeadingBetween(gFox, listEnemy.get(0));
                    if (dataState.isBorderAncaman()) { // Jika border masuk ke dalam ancaman
                        escapeHeading += Statistic.getHeadingBetween(gFox,
                                this.dataState.getBorderPosition());
                        escapeHeading /= 2;
                    }
                    isSandwich = (escapeHeading == 0); // Pengecekan Sandwich
                }
            }
                
            else if (this.dataState.getNThreatObject() == 1) { // Ancaman satu, tetapi bukan enemy, lari 
                escapeHeading += Statistic.getHeadingBetween(this.gFox, this.listAncaman.get(0));
                if (dataState.isBorderAncaman()) { // Jika border masuk ke dalam ancaman
                    escapeHeading += Statistic.getHeadingBetween(gFox,
                    this.dataState.getBorderPosition());
                    escapeHeading /= 2;
                } 
                isSandwich = (escapeHeading == 0); // Pengecekan Sandwich
            } 
            else if (this.dataState.getNEnemy() > 1){
                for (i = 0; i < this.dataState.getNEnemy(); i++) { // Hitung Heading untuk setiap Enemy
                    escapeHeading += Statistic.getHeadingBetween(gFox, listEnemy.get(i));
                }
                escapeHeading /= this.dataState.getNEnemy();
            } else {
                escapeHeading += Statistic.getHeadingBetween(gFox,
                this.dataState.getBorderPosition());
            }
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
