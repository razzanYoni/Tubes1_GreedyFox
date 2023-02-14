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
    private void go() {gAction.action = PlayerActions.FORWARD;}
    private void UseAfterburner() { gAction.action = PlayerActions.STARTAFTERBURNER;}
    private void StopAfterburner() { gAction.action = PlayerActions.STOPAFTERBURNER;}
    private void UseTorpedo() { gAction.action = PlayerActions.FIRETORPEDOES;}
    private void UseSuperNova() { gAction.action = PlayerActions.FIRESUPERNOVA;}
    private void DetonateSuperNova() { gAction.action = PlayerActions.DETONATESUPERNOVA;}
    private void FireTeleport() { gAction.action = PlayerActions.FIRETELEPORT;}
    private void Teleport() { gAction.action = PlayerActions.TELEPORT;}

    public void resolveAttackMode() {
        if (isTresholdAfterBurner()) {
            UseAfterburner();
        } else {
            go();
        }
    }

    public boolean isBurnerNeeded(GameObject enemy) {
        boolean flag;
        int i;
        if (dataAttack.getNEnemy() > 0)
        {
            for (i = 0; i < dataAttack.getPrey; i++) // cek setiap musuh
            {
    
            }
            return  Statistic.getDistanceBetween(gFox, enemy) > ;

        }
    }




}
