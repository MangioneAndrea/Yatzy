/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.yatzy.model.hitter;

import ch.bbbaden.yatzy.model.Game;
import ch.bbbaden.yatzy.model.StateManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author andre
 */
public class Player implements Hitter{
    private short actualDice;

    public Player() {
        this.actualDice =0;
    }
    
    @Override
    public Hitter changeHitter() {
        return new Computer();
    }

    @Override
    public void hit() {
            Platform.runLater(new Runnable(){
                @Override
                public void run(){
                    
                    if(actualDice<=3){
                        Game.getInstance().disposeDices();
                        Game.getInstance().updatePoints("player");
                        System.out.println("playerhit");
                    }
                }
            });
    }

    @Override
    public void setPoints() {
    }

    @Override
    public void nextDice() {
        actualDice++;
    }

    @Override
    public boolean hasNextDice() {
        if(actualDice<3)return true;
        return false;
    }

    @Override
    public boolean startedNow() {
        if(actualDice==0)return true;
        return false;
    }

    
}
