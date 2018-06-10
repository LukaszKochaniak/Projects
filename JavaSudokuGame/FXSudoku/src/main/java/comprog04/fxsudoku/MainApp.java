package comprog04.fxsudoku;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Scene scene1, scene2;
    private static Stage thestage;
    static Logger log = Logger.getLogger(MainApp.class.getName());

    @Override
    public void start(Stage stage) {

        //for different methods
        thestage = stage;
        thestage.setTitle("Sudoku Application");
        try {
            scene1 = new Scene((Parent) FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml")));
            scene1.getStylesheets().add("/styles/Scene.css");
            scene2 = new Scene((Parent) FXMLLoader.load(getClass().getResource("/fxml/BoardScene.fxml")));
            scene2.getStylesheets().add("/styles/BoardScene.css");

            thestage.setScene(scene1);
            thestage.show();
        } catch (IOException ioex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ioex);
        }
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

}
