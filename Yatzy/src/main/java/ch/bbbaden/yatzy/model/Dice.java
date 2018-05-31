/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.yatzy.model;

import ch.bbbaden.yatzy.model.hitter.Computer;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

/**
 *
 * @author andre
 */
public class Dice extends Label {

    private boolean blocked;
    private int x, y;
    private final int num;

    public int getNum() {
        return num;
    }
/**
 * Extention of a Label which shows dices as standard image
 * @param num the number to show on the image.
 * @param x the hypothetical x position of the dice 
 * @param y the hypothetical y position of the dice 
 */
    public Dice(int num, int x, int y) {
        super();
        blocked = false;
        this.num = num;
        this.x = x;
        this.y = y;
        setPrefSize(50, 50);
        setBackground(new Background(
                new BackgroundImage(
                        new Image(getClass().getResourceAsStream("/images/yatzy/" + num + ".png")), BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(
                                50, 50,
                                false, false, true, true
                        )
                )
        ));
        this.setOnMouseClicked(event -> {
            changeState();
        });
    }
/**
 * invert the state of the dice. The state is used to control the usage of the dice and let it change.
 */
    private void changeState() {
        if (StateManager.getInstance().getActualHitter().startedNow() || StateManager.getInstance().getActualHitter() instanceof Computer) {
            return;
        }
        if (blocked) {
            unlock();
        } else {
            lock();
        }
    }
    public void lock() {
        System.out.println("unblocked -->" + num);
        if (!blocked) {
            Game.getInstance().takeOffFromTable(this);
        }
        blocked = true;
    }

    public void unlock() {
        System.out.println("blocked -->" + num);
        if (blocked) {
            Game.getInstance().putOnTable(this);
        }
        blocked = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isLocked() {
        return blocked;
    }
    /**
     * Static method to get a random number which is plausible and can be used as parameter to create a new Dice
     * @return a Random number between 1 and 6
     */
    public static int getRandomDice() {
        return ((int) (Math.random() * 6)) + 1;
    }
}
