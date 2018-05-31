package ch.bbbaden.yatzy.yatzy;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {
private static MainApp instance;
private static Stage stage;
    @Override
    public void start(Stage stage) throws Exception {
        MainApp.stage=stage;
        instance=this;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/yatzy/Game.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static MainApp getInstance() {
        return instance;
    }
    public static Stage getStage(){
        return stage;
    }
    
}
