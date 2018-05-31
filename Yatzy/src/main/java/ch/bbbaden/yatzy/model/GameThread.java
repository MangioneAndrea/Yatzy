/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.yatzy.model;

import java.util.HashMap;
import java.util.concurrent.Callable;
import javafx.scene.control.Label;

/**
 *
 * @author andre
 */
public class GameThread extends Thread {

    final private HashMap<String, HashMap<String, Label>> points;
    private boolean canHit;

    public GameThread(HashMap<String, HashMap<String, Label>> points) {
        super();
        setDaemon(true);
        this.points = points;
        canHit = false;
    }
/**
 * Thread to let the Bot play and control that both hitters play after the rules
 */
    @Override
    public void run() {
        while (Game.getInstance() != null) {
            
            try {
                sleep(100);
                if (canHit) {
                    StateManager.getInstance().getActualHitter().hit();
                    canHit = false;
                }
            } catch (InterruptedException ex) {
                System.out.println("sleep interrupted");
            }
        }
    }

    public void concludeGame() {
        interrupt();
    }

    public void letHit() {
        canHit = true;
    }
}
