/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.yatzy.model;

import ch.bbbaden.yatzy.model.hitter.Hitter;
import ch.bbbaden.yatzy.model.hitter.Player;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author andre
 */
public class StateManager implements Observer {

    private static StateManager instance;
    private Hitter actualHitter;

    private StateManager() {
        actualHitter = new Player();
    }

    public Hitter getActualHitter() {
        return actualHitter;
    }

    public void changeHitter() {
        actualHitter = actualHitter.changeHitter();
    }
/**
 * method to notify to the game that the user tried to dice. The operation will be handled by the thread GameThread
 */
    @Override
    public void update(Observable o, Object o1) {
        Game.getInstance().g.letHit();
    }

    public boolean canDice() {
        if (actualHitter.hasNextDice()) {
            actualHitter.nextDice();
            return true;
        }
        return false;
    }

    public static StateManager getInstance() {
        if (instance == null) {
            instance = new StateManager();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

}
