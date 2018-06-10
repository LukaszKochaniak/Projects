package comprog04.fxsudoku;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FXMLController implements Initializable {

    private enum LEVEL {
        EASY, MEDIUM, HARD;
    };
    @FXML
    private Label welcomeText, levelText, authorTitle, author, chooseLanguage;
    @FXML
    private Button btnPolish, btnEnglish;
    @FXML
    private Button btnEasy, btnMedium, btnHard;

    static Logger log = Logger.getLogger(FXMLController.class.getName());
    private static int currentLevel;
    private ResourceBundle bundle;
//    private Locale[] supportedLocales = {
//        new Locale("pl", "PL"),
//        new Locale("en", "EN")
//    };
    private static Locale locale;

    public static int getLevel() {
        return currentLevel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        locale = new Locale("en", "EN");
        loadView(locale);
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        try {
            Stage stage = (Stage) btnEasy.getScene().getWindow();;
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/BoardScene.fxml"));
            String stylesheet = "/styles/BoardScene.css";
            Scene scene = new Scene(root);
            scene.getStylesheets().add(stylesheet);
            stage.setScene(scene);
            stage.show();

            if (event.getSource() == btnEasy) {
                currentLevel = 0;
            } else if (event.getSource() == btnMedium) {
                currentLevel = 1;
            } else if (event.getSource() == btnHard) {
                currentLevel = 2;
            }

        } catch (IOException ioex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ioex);
        }

        //create a new scene with root and set the stage
    }

    @FXML
    private void changeLanguage(ActionEvent event) {

        if (event.getSource() == btnPolish) {
            locale = new Locale("pl", "PL");
            loadView(locale);
        } else if (event.getSource() == btnEnglish) {
            locale = new Locale("en", "EN");
            loadView(locale);
        }
    }

    private void loadView(Locale locale) {

        bundle = ResourceBundle.getBundle("i18n.authors.MenuBundle", locale);
        authorTitle.setText(bundle.getObject("authorTitle").toString());
        author.setText(bundle.getObject("author").toString());

        bundle = ResourceBundle.getBundle("i18n.MyBundle", locale);
        btnPolish.setText(bundle.getString("polish"));
        btnEnglish.setText(bundle.getString("english"));
        welcomeText.setText(bundle.getString("welcome"));
        levelText.setText(bundle.getString("chooseDifficulty"));
        btnEasy.setText(bundle.getString("easy"));
        btnMedium.setText(bundle.getString("medium"));
        btnHard.setText(bundle.getString("hard"));
        chooseLanguage.setText(bundle.getString("chooseLanguage"));
    }

    public static Locale getLocale() {
        return locale;
    }
}
