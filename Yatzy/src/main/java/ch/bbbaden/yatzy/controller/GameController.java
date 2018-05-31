
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.yatzy.controller;

import ch.bbbaden.yatzy.model.Game;
import ch.bbbaden.yatzy.model.StateManager;
import ch.bbbaden.yatzy.model.hitter.Player;
import ch.bbbaden.yatzy.yatzy.MainApp;
import java.net.URL;
import java.util.HashMap;
import java.util.Observable;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author andre
 */
public class GameController extends Observable implements Initializable {

    @FXML
    Label mouse;
    @FXML
    private AnchorPane pane;
    @FXML
    private GridPane dealerTable;
    @FXML
    private GridPane playerTable;
    @FXML
    private AnchorPane outputPane;
    @FXML
    private GridPane table;
    @FXML
    private GridPane table_b;
    @FXML
    private GridPane lockedTable;
    final private HashMap<String, HashMap<String, Label>> points;
    final private HashMap<String, Label> playerPoints;
    final private HashMap<String, Label> computerPoints;
    private Game game;
    private boolean ready;
    int result = Integer.MAX_VALUE;

    public GameController() {
        this.ready = false;
        this.computerPoints = new HashMap();
        this.playerPoints = new HashMap();
        this.points = new HashMap();
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setBackground(table_b, new Image(getClass().getResourceAsStream("/images/yatzy/greenBackground.png")), 853, 636, false, false, BackgroundRepeat.NO_REPEAT);
        mouse.setPickOnBounds(false);
        mouse.setOpacity(0);
        createPointsTable(playerPoints, playerTable, true);
        createPointsTable(computerPoints, dealerTable, false);
        points.put("player", playerPoints);
        points.put("computer", computerPoints);
        this.addObserver(StateManager.getInstance());
    }
/**
 * Set the tables in an HashMap in a dynamic way. Every Point area has an ID. With those id's a FXML tag is not needed.
 * @param map The HashMap in which the table will be inserted to
 * @param pane The Parent where to search for the Areas
 * @param clickable Set the type of table. True means, that the user can modify the value of the table.
 */
    private void createPointsTable(HashMap<String, Label> map, GridPane pane, boolean clickable) {
        String[] pointList = {"ones", "twos", "threes", "fours", "fives", "sixes", "one_pair", "three_of_a_kind", "four_of_a_kind", "small_straight", "large_straight", "full_house", "chance", "yatzy", "total"};
        for (String str : pointList) {
            boolean notPoints = false;
            if (str.equals("total_upper") || str.equals("total")) {
                notPoints = true;
            }
            if (clickable && !notPoints) {
                pane.lookup("#" + str).setOnMouseClicked(event -> {
                    try {
                        if (StateManager.getInstance().getActualHitter() instanceof Player && !StateManager.getInstance().getActualHitter().startedNow()) {
                            game.setPoints(str, "player");

                            game.concludeRound();
                        }
                    } catch (Exception asd) {
                        System.err.println("game not started");
                    }
                });
            }
            map.put(str, (Label) pane.lookup("#" + str));
            //map.get(str).setText(str);
        }

    }
/**
 * This method is used to show the number of dice upon the game board.
 * @param event FXML managing. It's used to get the mouse location 
 */
    @FXML
    private void moveMouse(MouseEvent event) {
        mouse.setTranslateY(event.getSceneY() - mouse.getHeight() / 2);
        mouse.setTranslateX(event.getSceneX() - mouse.getWidth() / 2);
        if (mouse.getBoundsInParent().intersects(dealerTable.getBoundsInParent()) || mouse.getBoundsInParent().intersects(playerTable.getBoundsInParent()) || mouse.getBoundsInParent().intersects(outputPane.getBoundsInParent())) {
            mouse.setOpacity(0);
            mouse.setTranslateX(1280);
            mouse.setTranslateY(720);
        } else {
            mouse.setOpacity(1);
        }
    }

    private void setBackground(Region a, Image img, int x, int y, boolean contain, boolean cover, BackgroundRepeat repeat) {
        a.setBackground(new Background(
                new BackgroundImage(
                        img, repeat,
                        repeat,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(
                                x, y,
                                false, false, contain, cover
                        )
                )
        ));
    }

    @FXML
    private boolean surrend(MouseEvent event) {
        game = Game.getInstance();
        if (game == null) {
            return true;
        }
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("surrend");
        alert.setHeaderText("If you surrend, the bet you've made will be lost but you will still be able to play again");
        alert.setContentText("Do you really want to surrend?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            mouse.setBackground(Background.EMPTY);
            game.concludeGame();
            return true;
        }
        return false;
    }
/**
 * Start of a new instance of Game. 
 * @return whether the game is a new instance and has been started.
 */
    private boolean startGame() {
        game = Game.createInstance(points, table, lockedTable, result);
        return true;
    }

    @FXML
    private void dice(MouseEvent event) {
        if (!ready) {
            System.out.println("cannot roll");
        } else {
            System.out.println(ready);
            ready = false;
            setChanged();
            notifyObservers();
            mouse.setBackground(Background.EMPTY);
        }
    }

    private int getFreeDices() {
        return Game.getInstance().getFreeDices();
    }
/**
 * elaboration of data to show the number of dices to dice. It's also used to start the game if it hasn't started jet.
 */
    @FXML
    private void reroll(MouseEvent event) {
        if (Game.getInstance() == null) {
            if (!startGame()) {
                return;
            }
        }
        if (StateManager.getInstance().getActualHitter().startedNow()) {
            Game.getInstance().lockedTable.getChildren().clear();
        }
        System.out.println(StateManager.getInstance().getActualHitter());
        if (StateManager.getInstance().getActualHitter() instanceof Player) {
            System.out.println(ready);
            if (StateManager.getInstance().canDice() && ready == false) {
                System.out.println("roll");
                ready = true;
                game.emptyGrid();
                setBackground(mouse, new Image(MainApp.getInstance().getClass().getResourceAsStream("/images/yatzy/" + getFreeDices() + ".gif")), 102, 94, true, true, BackgroundRepeat.NO_REPEAT);
            }

        }
    }

    @FXML
    private void info(MouseEvent event) {
        pane.setOpacity(0.7);
        try {
            ((AnchorPane) MainApp.getStage().getScene().getRoot()).getChildren().add((Parent) FXMLLoader.load(getClass().getResource("/fxml/yatzy/Info.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
