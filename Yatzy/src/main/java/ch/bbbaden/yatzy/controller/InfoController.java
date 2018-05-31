/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.yatzy.controller;

import ch.bbbaden.yatzy.yatzy.MainApp;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author andre
 */
public class InfoController implements Initializable {

    @FXML
    private ImageView container;
    int pointer = 1;
    @FXML
    private AnchorPane anchorPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pointer = 1;
    }
/**
 * used to control the info. It tries to show the next tutorial image. If no image is given the whole class will be handled by the garbage collector.
 */
    @FXML
    private void next(MouseEvent event) {
        try {
            pointer++;
            container.setImage(new Image(getClass().getResourceAsStream("/images/yatzy/help/" + pointer + ".gif")));
        } catch (Exception ex) {
            ((AnchorPane) MainApp.getStage().getScene().getRoot()).getChildren().remove(anchorPane);
            ((AnchorPane) MainApp.getStage().getScene().getRoot()).setOpacity(1);
        }
    }

}
