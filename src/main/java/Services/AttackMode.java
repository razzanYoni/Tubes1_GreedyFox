package Services;

import Enums.*;
import Models.*;

import java.sql.SQLOutput;
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
    private GameState gameState;
    private Double closePrey;
    private final int closeTele = 20; // Jangan constant

    // Method
    // Constructor
    public AttackMode(Data dataAttack, GameObject gFox, PlayerAction gAction, GameState gameState) {
        this.dataAttack = dataAttack;
        this.gFox = gFox;
        this.gAction = gAction;
        this.gameState = gameState;
        setClosePrey();
    }
    // Copy Constructor
    public AttackMode(AttackMode am) {
        this.dataAttack = am.dataAttack;
        this.gFox = am.gFox;
        this.gAction = am.gAction;
    }

    // Setter
    public void setClosePrey(){
        this.closePrey = gFox.getSize() * Math.sqrt(2) + 15;
    }

    // Attack Type
    // size_GF - size_E(nearest object)  > distance*3 + 5(ini superfood)/speed_GF
    private void go() {gAction.action = PlayerActions.FORWARD;}
    private void UseAfterburner() { gAction.action = PlayerActions.STARTAFTERBURNER;}
    private void StopAfterburner() { gAction.action = PlayerActions.STOPAFTERBURNER;}
    private void FireTorpedo() { gAction.action = PlayerActions.FIRETORPEDOES;}
    private void FireSuperNova() { gAction.action = PlayerActions.FIRESUPERNOVA;}
    private void DetonateSuperNova() { gAction.action = PlayerActions.DETONATESUPERNOVA;}
    private void FireTeleport() {
        gAction.action = PlayerActions.FIRETELEPORT;
        System.out.println("MASUKKKKKKKKKKKKKKKKKKK");
    }
    private void Teleport() {
        gAction.action = PlayerActions.TELEPORT;
        System.out.printf("KELUARRRRRRRRRRRRRRRRRRRRR");
    }

    public void resolveAttackMode() {
        choosePrey();
        go();
        gAction.setHeading(Statistic.getHeadingBetween(gFox, prey));
        if (Statistic.getDistanceBetween(gFox, prey) < closePrey)
        {
            // jika jarak ke mangsa dekat
            UseAfterburner();
        }
        else
        {
            StopAfterburner();
            if (dataAttack.isTorpedoOptimal())
            {
                System.out.println("SIUU Torpedo!");
                FireTorpedo();
            } else if ((gFox.getSize() - 20) > (prey.getSize() + 10)) {
                gAction.setHeading(Statistic.getHeadingBetween(gFox, gameState.getWorld().getCenterPoint()));
                // cek apakah teleport aman
                System.out.println("Fire Teleport -> \b ");
                if (gFox.TeleporterCount > 0)  {
                    Teleport();
                    System.out.println("Zuoppp Tp");
                } else {
                    FireTeleport();
                    System.out.println("TP PENDAHULUAN");
                }
            }
        }
    }

    // method memilih mangsa, apakah pilih yang terdekat dengan player atau terdekat dengan teleporter
    public void choosePrey() { 
        int i;
        boolean found;
        // cari terdekat dengan player (default)
        Double closeTeleport = dataAttack.getPreyObjectDistance().get(0);

        // Mencari yang paling dekat dari tele


        // ?????????
        i = 1; found = false;
        while (i < dataAttack.getNPreyObject() && !found) {
            if (dataAttack.getPreyObjectDistance().get(i) > closeTele - 3 && dataAttack.getPreyObjectDistance().get(i) < closeTele + 3) {
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
                this.prey = dataAttack.getPreyObject().get(0);
            }
            else {
                    this.prey = dataAttack.getPreyObject().get(i - 1);
            }
        }
        
    }

}