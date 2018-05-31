/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.yatzy.model;

import ch.bbbaden.yatzy.model.hitter.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 *
 * @author andre
 */
public class Game{

    private static Game instance;
    final public HashMap<String, HashMap<String, Label>> points;
    public final GameThread g;
    private final GridPane table;
    public final GridPane lockedTable;
    final private ArrayList<Dice> lockedDices = new ArrayList<>();
    final private int bet;
    private boolean rollable;

    private Game(HashMap<String, HashMap<String, Label>> points, GridPane table, GridPane lockedTable, int bet) {
        super();
        this.table = table;
        this.lockedTable = lockedTable;
        this.bet = bet;
        g = new GameThread(points);
        this.points = points;
        rollable = true;
        for (Map.Entry<String, Label> m : points.get("player").entrySet()) {
            m.getValue().setText("0");
            m.getValue().setDisable(false);
        }
        for (Map.Entry<String, Label> m : points.get("computer").entrySet()) {
            m.getValue().setText("0");
            m.getValue().setDisable(false);
        }
        g.start();
    }
/**
 * It sets a method to call belonging to a parent class.
 * @param c the method to call.
 */
/**
 * method called to end the turn and change the hitter.
 * @param lab label set in which the point will be saved to
 * @param hitter hitter which has just played the round
 */
    public void setPoints(String lab, String hitter) {
        boolean lastRound = true;
        StateManager.getInstance().changeHitter();

        points.get(hitter).get(lab).setDisable(true);

        int total = 0;
        for (Map.Entry<String, Label> m : points.get(hitter).entrySet()) {
            if (m.getKey().equals("total")) {
                continue;
            }
            if (!m.getValue().isDisabled()) {
                m.getValue().setText("0");
                lastRound = false;
            }
            total += Integer.valueOf(m.getValue().getText());
        }
        lastRound = lastRound && StateManager.getInstance().getActualHitter() instanceof Player;
        if (lastRound) {
            endGame();
        }
        points.get(hitter).get("total").setText(String.valueOf(total));
        lockedDices.clear();
        if (hitter.equals("player")) {
            Game.getInstance().g.letHit();
        }
    }
/**
 * All the dices will be put on the table in a random position. This method also prevent to set more than a dice in the same position
 */
    public void disposeDices() {
        emptyGrid();
        int n = getFreeDices();
        HashSet<Integer> a = new HashSet<>();
        int position[] = new int[n];
        while (a.size() < n) {
            int rnd = (int) (Math.random() * 65);
            for (Dice d : lockedDices) {//control if a dice get the same position of an other dice
                if (rnd == d.getX() + d.getY() * 13) {
                    rnd = -1;
                }
            }
            if (rnd == -1) {
                continue;
            }
            a.add(rnd);
        }
        Iterator it = a.iterator();
        for (int i = 0; i < n; i++) {
            position[i] = (int) it.next();
        }
        for (int i = 0; i < n; i++) {
            Dice b = new Dice(Dice.getRandomDice(), position[i] % 13, position[i] / 13);
            table.add(b, position[i] % 13, position[i] / 13);
        }
    }
/**
 * the table will be cleaned out.
 */
    public void emptyGrid() {
        table.getChildren().clear();
    }
/**
 * all the dice container will be cleaned out
 */
    public void concludeRound() {
        emptyGrid();
        lockedTable.getChildren().clear();
    }
/**
 * the instance with all the related items will be destroyed
 */
    public void concludeGame() {
        g.concludeGame();        
        for (Map.Entry<String, Label> m : points.get("player").entrySet()) {
            m.getValue().setText("0");
            m.getValue().setDisable(false);
        }
        concludeRound();
        StateManager.destroyInstance();
        instance = null;
    }
/**
 * Move a dice to the table. The dice will be set as unlocked. More information in class Dice
 * @param dice Dice to edit
 */
    public void putOnTable(Dice dice) {
        lockedDices.remove(dice);
        table.add(dice, dice.getX(), dice.getY());
        lockedTable.getChildren().clear();
        for (int i = 0; i < lockedDices.size(); i++) {
            lockedTable.add(lockedDices.get(i), i + 1, 1);
        }
    }
/**
 * Move a dice off from the table. The dice will be set as locked. More information in class Dice
 * @param dice Dice to edit
 */
    public void takeOffFromTable(Dice dice) {
        lockedDices.add(dice);
        lockedTable.getChildren().clear();
        for (int i = 0; i < lockedDices.size(); i++) {
            lockedTable.add(lockedDices.get(i), i + 1, 1);
        }
    }
/**
 * Method to get the items which can be dice
 * @return the number of the dice which are not locked.
 */
    public int getFreeDices() {
        return 5 - lockedDices.size();
    }

    public static Game getInstance() {
        return instance;
    }

    public static Game createInstance(HashMap<String, HashMap<String, Label>> points, GridPane table, GridPane lockedTable, int bet) {
        if (instance == null) {
            instance = new Game(points, table, lockedTable, bet);
        }
        return instance;
    }
/**
 * Method to show the potential points that can be set
 * @param hitter The hitter which will see the updated points
 */
    public void updatePoints(String hitter) {
        Points.allDices.clear();
        table.getChildren().forEach((d) -> {
            Points.allDices.add((Dice) d);
        });
        lockedTable.getChildren().forEach((d) -> {
            Points.allDices.add((Dice) d);
        });
        for (Map.Entry<String, Label> m : points.get(hitter).entrySet()) {
            if (!m.getValue().isDisabled() && !m.getKey().equals("total")) {
                m.getValue().setText(String.valueOf(Points.calculatePoints(m.getKey())));
            }
        }
    }
/**
 * End the game by playing every round. This is the only method of the class which modifies the Database
 */
    private void endGame() {
        System.out.println("gameEnded");
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Ended");
        int pl = Integer.parseInt(points.get("player").get("total").getText());
        int pc = Integer.parseInt(points.get("computer").get("total").getText());
        if (pl == pc) {
            alert.setHeaderText("It's a tie!");
//            alert.setContentText("The bet of " + bet + "CHF that you've made has returned to your account balance.");
//            Login.getInstance().getLoggedPerson().giveFunds(bet, this);

        } else if (pl > pc) {
            alert.setHeaderText("Congratulations, you've won!");
//            alert.setContentText("You won a total of " + bet * 2 + "CHF including the bet you've made of " + bet + "CHF.");
//            Login.getInstance().getLoggedPerson().giveFunds(bet * 2, this);

        } else {
            alert.setHeaderText("Shame! You've lost");
//            alert.setContentText("The bet of " + bet + "CHF that you've made has been lost.");

        }

        alert.showAndWait();
        concludeGame();
    }
}
