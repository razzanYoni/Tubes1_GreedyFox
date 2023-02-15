package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

// ToDo: 
// State Menyerang (AttackMode.java)
// if  (not inThresHoldAfterBurner)
//          Go to Enemy with teleporter till teleporter distance with enemy <= 10
//       else if   ( inThresHoldAfterBurner)
//          Go to Enemy with afterburner ampe doi tewas
// Use torpedosalvo

public class AttackMode {
    // Atribut
    private Data dataAttack;
    private GameObject gFox;
    private PlayerAction gAction;
    private GameObject prey;
    private GameObject other;
    private final int closePrey = 10;
    private final int closeTele = 20;

    // Method
    // Constructor
    public AttackMode(Data dataAttack, GameObject gFox, PlayerAction gAction) {
        this.dataAttack = dataAttack;
        this.gFox = gFox;
        this.gAction = gAction;
    }
    // Copy Constructor
    public AttackMode(AttackMode am) {
        this.dataAttack = am.dataAttack;
        this.gFox = am.gFox;
        this.gAction = am.gAction;
    }

    // Attack Type
    // size_GF - size_E(nearest object)  > distance*3 + 5(ini superfood)/speed_GF
    private void go() {gAction.action = PlayerActions.FORWARD;}
    private void UseAfterburner() { gAction.action = PlayerActions.STARTAFTERBURNER;}
    private void StopAfterburner() { gAction.action = PlayerActions.STOPAFTERBURNER;}
    private void FireTorpedo() { gAction.action = PlayerActions.FIRETORPEDOES;}
    private void FireSuperNova() { gAction.action = PlayerActions.FIRESUPERNOVA;}
    private void DetonateSuperNova() { gAction.action = PlayerActions.DETONATESUPERNOVA;}
    private void FireTeleport() { gAction.action = PlayerActions.FIRETELEPORT;}
    private void Teleport() { gAction.action = PlayerActions.TELEPORT;}

    public void resolveAttackMode() {
        if (dataAttack.getnPreyObject() > 0) { // harus ada mangsa escape null
            choosePrey();
            gAction.setHeading(Statistic.getHeadingBetween(gFox, prey);
            if (Statistic.getDistanceBetween(gFox, prey) < closePrey) 
            {
                // jika jarak ke mangsa dekat
                UseAfterburner();
                if (!iscloseTraseholdsize())
                {
                    System.out.println("SIUU Torpedo!");
                    FireTorpedo();
                }
            }
            else 
            {
                StopAfterburner();
                if (!dataAttack.isBorderAncaman()) 
                {
                    // cek apakah teleport aman
                    System.out.println("Fire Teleport -> \b ");
                    FireTeleport();
                    System.out.println("Zuoppp Tp");
                    Teleport();
                }
            }
            go();
        }
    }


    // membandingkan ukuran bot Gf dengan musuh saat perkiraan sampai
    public boolean iscloseTraseholdsize() {
        return (gFox.getSize() < prey.getSize());
        // return (gFox.getSize() - prey.getSize() > dataAttack.getPreyObjectDistance().get(0)*3 + 5);
    }

    // method memilih mangsa, apakah pilih yang terdekat dengan player atau terdekat dengan teleporter
    public void choosePrey() { 
        int i;
        boolean found;
        // cari terdekat dengan player (default)
        Double closeTeleport = dataAttack.getPreyObjectDistance().get(0);

        // Mencari yang paling dekat dari tele
        i = 1; found = false;
        while (i < dataAttack.getnPreyObject() && !found) {
            if (dataAttack.getPlayerDistance().get(i) > closeTele - 3 && dataAttack.getPreyObjectDistance().get(i) < closeTele + 3) {
                // choose terdekat dari tele, galat 3, pilih i jika objek ke i lebih dekat dari pada objek ke i-1
                if (Math.abs(closeTele - dataAttack.getPreyObjectDistance().get(i)) < Math.abs(closeTele - closeTeleport)) {
                    closeTeleport = dataAttack.getPreyObjectDistance().get(i);
                }
            }

            found = closeTeleport == closeTele;
            i++;
        }

        // 
        if (dataAttack.getPreyObject().get(i-1).getSize() < gFox.getSize())
        {
            this.prey = dataAttack.getPreyObject().get(i-1);
        }
        else
        {
            if (Math.abs(closeTele - dataAttack.getPreyObjectDistance().get(0)) < Math.abs(closeTele - closeTeleport)) {
                this.prey = dataAttack.getPreyObject.get(0);
            }
            else {
                this.prey = dataAttack.getPlayerObject().get(i-1);
            }
        }
        
    }

}
// FORWARD(1),
// STOP(2),
// STARTAFTERBURNER(3),
// STOPAFTERBURNER(4),
// FIRETORPEDOES(5),
// FIRESUPERNOVA(6),
// DETONATESUPERNOVA(7),
// FIRETELEPORT(8),
// TELEPORT(9),
// ACTIVATESHIELD(10);
