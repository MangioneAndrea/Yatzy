/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.yatzy.model.hitter;

/**
 *
 * @author andre
 */
public interface Hitter {
    public void hit();
    public Hitter changeHitter();
    public void setPoints();
    public void nextDice();
    public boolean hasNextDice();
    public boolean startedNow();
}
