package comprog04.fxsudoku;

import com.mycompany.sudoku.BacktrackingSudokuSolver;
import com.mycompany.sudoku.SudokuBoard;
import com.mycompany.sudoku.Difficulty;
import com.mycompany.sudoku.daos.JdbcSudokuBoardDao;
import com.mycompany.sudoku.exceptions.ApplicationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FXMLBoardController implements Initializable {

    @FXML
    Button button_one, button_two, button_three, button_four, button_five, button_six, button_seven, button_eight, button_nine;
    @FXML
    Button btnBack, btnDifficulty, btnLoad, btnSave, btnCorrectness, btnAutofill;
    @FXML
    Canvas canvas;

    SudokuBoard board;
    Locale locale;
    private ResourceBundle bundle;
    int player_selected_row, player_selected_col;
    
    static Logger log = Logger.getLogger(FXMLBoardController.class.getName());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setButtonFocus();

        locale = FXMLController.getLocale();

        board = new SudokuBoard();
        BacktrackingSudokuSolver solver = new BacktrackingSudokuSolver();
        solver.solve(board);
        Difficulty difficulty;
        switch (FXMLController.getLevel()) {
            case 0:
                difficulty = new Difficulty(Difficulty.Levels.Easy);
                difficulty.applyTo(board);
                break;
            case 1:
                difficulty = new Difficulty(Difficulty.Levels.Medium);
                difficulty.applyTo(board);
                break;
            case 2:
                difficulty = new Difficulty(Difficulty.Levels.Hard);
                difficulty.applyTo(board);
                break;
            default:
                break;
        }
        //Get graphics context from canvas
        GraphicsContext context = canvas.getGraphicsContext2D();
        //Call drawOnCanvas method, with the context we have gotten from the canvas
        drawOnCanvas(context);
    }

    @FXML
    private void backButtonAction(ActionEvent event) {

        Stage stage = (Stage) btnBack.getScene().getWindow();
        try{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
        String stylesheet = "/styles/Scene.css";

        //create a new scene with root and set the stage
        Scene scene = new Scene(root);
        scene.getStylesheets().add(stylesheet);
        stage.setScene(scene);
        stage.show();
        } catch(IOException ioex) {
            Logger.getLogger(FXMLBoardController.class.getName()).log(Level.SEVERE, null, ioex);
        }

    }

    @FXML
    private void difficultyButtonAction(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("difficulty"));
        alert.setHeaderText(bundle.getString("whichDifficulty"));
        alert.setContentText(bundle.getString("chooseDifficulty"));

        ButtonType buttonTypeOne = new ButtonType(bundle.getString("easy"));
        ButtonType buttonTypeTwo = new ButtonType(bundle.getString("medium"));
        ButtonType buttonTypeThree = new ButtonType(bundle.getString("hard"));
        ButtonType buttonTypeCancel = new ButtonType(bundle.getString("cancel"), ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != buttonTypeCancel) {
            BacktrackingSudokuSolver solver = new BacktrackingSudokuSolver();
            Difficulty difficulty;
            solver.solve(board);
            if (result.get() == buttonTypeOne) {
                difficulty = new Difficulty(Difficulty.Levels.Easy);
                difficulty.applyTo(board);
            } else if (result.get() == buttonTypeTwo) {
                difficulty = new Difficulty(Difficulty.Levels.Medium);
                difficulty.applyTo(board);
            } else if (result.get() == buttonTypeThree) {
                difficulty = new Difficulty(Difficulty.Levels.Hard);
                difficulty.applyTo(board);
            }
        }
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    @FXML
    private void loadButtonAction(ActionEvent event) {
        Stage stage = (Stage) btnLoad.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle(bundle.getString("load"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All", "*.*"),
                new FileChooser.ExtensionFilter("Data", "*.data"),
                new FileChooser.ExtensionFilter("Text files", "*.txt")
        );

        board = board.load(fileChooser.showOpenDialog(stage).getName());
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    @FXML
    private void saveButtonAction(ActionEvent event) {
        Stage stage = (Stage) btnLoad.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle(bundle.getString("save"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All", "*.*"),
                new FileChooser.ExtensionFilter("Data", "*.data"),
                new FileChooser.ExtensionFilter("Text files", "*.txt")
        );

        board.save(fileChooser.showSaveDialog(stage).getName());
    }

    @FXML
    private void correctnessButtonAction(ActionEvent event) {
        if (board.checkFill()) {
            showMessage(bundle.getString("success"));
        } else {
            showMessage(bundle.getString("fail"));
        }
    }

    @FXML
    private void autofillButtonAction(ActionEvent event) {
        BacktrackingSudokuSolver solver = new BacktrackingSudokuSolver();
        solver.solve(board);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void drawOnCanvas(GraphicsContext context) {

        context.clearRect(0, 0, 454, 454);

        loadView(locale);

        // draw white rounded rectangles for our board
        {
            int position_y = -50;
            for (int row = 0; row < 9; row++) {
                position_y += 50;
                if (row != 0 & row % 3 == 0) {
                    position_y += 2;
                }
                int position_x = -50;
                for (int col = 0; col < 9; col++) {
                    position_x += 50;
                    if (col != 0 & col % 3 == 0) {
                        position_x += 2;
                    }
                    int width = 46;
                    context.setFill(Color.WHITE);
                    context.fillRoundRect(position_x, position_y, width, width, 10, 10);
                }
            }
        }

        // draw highlight around selected cell
        context.setStroke(Color.RED);
        context.setLineWidth(5);
        context.strokeRoundRect(player_selected_col * 50, player_selected_row * 50, 46, 46, 10, 10);

        //draw lines on the table 
        for (int row = 0; row <= 8; row++) {
            context.setStroke(Color.BLACK);
            context.setLineWidth(4);
            if (row != 0 & row % 3 == 0) {
                context.strokeLine(row * 50, 0, row * 50, 454);
                context.strokeLine(0, row * 50, 454, row * 50);
            }
        }

        // draw the initial numbers from our GameBoard instance
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int position_y = row * 50 + 30;
                int position_x = col * 50 + 20;
                if (board.at(col, row).isInitialized()) {
                    context.setFill(Color.BLACK);
                } else {
                    context.setFill(Color.LIGHTSEAGREEN);
                }
                context.setFont(new Font(20));
                if (board.get(col, row) != 0) {
                    context.fillText(board.get(col, row) + "", position_x, position_y);
                }
            }
        }
    }

    public void showMessage(String message) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        // clear the canvas
        context.clearRect(0, 0, 450, 450);
        // set the fill color to green
        context.setFill(Color.GREEN);
        // set the font to 36pt
        context.setFont(new Font(36));
        // display SUCCESS text on the screen
        context.fillText(message, 150, 250);
    }

    public void canvasMouseClicked() {
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                // TODO Auto-generated method stub
                int mouse_x = (int) event.getX();
                int mouse_y = (int) event.getY();

                // convert the mouseX and mouseY into rows and cols
                player_selected_row = (int) (mouse_y / 50);
                player_selected_col = (int) (mouse_x / 50);

                //get the canvas graphics context and redraw
                drawOnCanvas(canvas.getGraphicsContext2D());
            }
        });
    }

    private void loadView(Locale locale) {
        
        bundle = ResourceBundle.getBundle("i18n.MyBundle", locale);
        btnBack.setText(bundle.getString("back"));
        btnDifficulty.setText(bundle.getString("difficulty"));
        btnLoad.setText(bundle.getString("load"));
        btnSave.setText(bundle.getString("save"));
        btnCorrectness.setText(bundle.getString("correctness"));
        btnAutofill.setText(bundle.getString("autofill"));

    }

    public void canvasKeyPressed() {
        canvas.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                    case W:
                        if (player_selected_row > 0) {
                            player_selected_row -= 1;
                        }
                        break;
                    case DOWN:
                    case S:
                        if (player_selected_row < 8) {
                            player_selected_row += 1;
                        }
                        break;
                    case LEFT:
                    case A:
                        if (player_selected_col > 0) {
                            player_selected_col -= 1;
                        }
                        break;
                    case RIGHT:
                    case D:
                        if (player_selected_col < 8) {
                            player_selected_col += 1;
                        }
                        break;
                    case DIGIT1:
                        if (!board.at(player_selected_col, player_selected_row).isInitialized()) {
                            board.set(player_selected_col, player_selected_row, 1);
                        }
                        break;
                    case DIGIT2:
                        if (!board.at(player_selected_col, player_selected_row).isInitialized()) {
                            board.set(player_selected_col, player_selected_row, 2);
                        }
                        break;
                    case DIGIT3:
                        if (!board.at(player_selected_col, player_selected_row).isInitialized()) {
                            board.set(player_selected_col, player_selected_row, 3);
                        }
                        break;
                    case DIGIT4:
                        if (!board.at(player_selected_col, player_selected_row).isInitialized()) {
                            board.set(player_selected_col, player_selected_row, 4);
                        }
                        break;
                    case DIGIT5:
                        if (!board.at(player_selected_col, player_selected_row).isInitialized()) {
                            board.set(player_selected_col, player_selected_row, 5);
                        }
                        break;
                    case DIGIT6:
                        if (!board.at(player_selected_col, player_selected_row).isInitialized()) {
                            board.set(player_selected_col, player_selected_row, 6);
                        }
                        break;
                    case DIGIT7:
                        if (!board.at(player_selected_col, player_selected_row).isInitialized()) {
                            board.set(player_selected_col, player_selected_row, 7);
                        }
                        break;
                    case DIGIT8:
                        if (!board.at(player_selected_col, player_selected_row).isInitialized()) {
                            board.set(player_selected_col, player_selected_row, 8);
                        }
                        break;
                    case DIGIT9:
                        if (!board.at(player_selected_col, player_selected_row).isInitialized()) {
                            board.set(player_selected_col, player_selected_row, 9);
                        }
                        break;
                    case ENTER:
                        if (board.checkFill() == true) {
                            showMessage("SUCCESS!");
                        } else {
                            showMessage("FAIL!");
                        }
                        break;
                }
                drawOnCanvas(canvas.getGraphicsContext2D());
            }
        });
    }

    public void buttonOnePressed() {
        if (!board.at(player_selected_col, player_selected_row).isInitialized()) {
            board.set(player_selected_col, player_selected_row, 1);
        }
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonTwoPressed() {
        if (!board.at(player_selected_col, player_selected_row).isInitialized()) {
            board.set(player_selected_col, player_selected_row, 2);
        }
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonThreePressed() {
        if (!board.at(player_selected_col, player_selected_row).isInitialized()) {
            board.set(player_selected_col, player_selected_row, 3);
        }
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonFourPressed() {
        if (!board.at(player_selected_col, player_selected_row).isInitialized()) {
            board.set(player_selected_col, player_selected_row, 4);
        }
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonFivePressed() {
        if (!board.at(player_selected_col, player_selected_row).isInitialized()) {
            board.set(player_selected_col, player_selected_row, 5);
        }
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonSixPressed() {
        if (!board.at(player_selected_col, player_selected_row).isInitialized()) {
            board.set(player_selected_col, player_selected_row, 6);
        }
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonSevenPressed() {
        if (!board.at(player_selected_col, player_selected_row).isInitialized()) {
            board.set(player_selected_col, player_selected_row, 7);
        }
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonEightPressed() {
        if (!board.at(player_selected_col, player_selected_row).isInitialized()) {
            board.set(player_selected_col, player_selected_row, 8);
        }
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonNinePressed() {
        if (!board.at(player_selected_col, player_selected_row).isInitialized()) {
            board.set(player_selected_col, player_selected_row, 9);
        }
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void setButtonFocus() {

        button_one.setFocusTraversable(false);
        button_two.setFocusTraversable(false);
        button_three.setFocusTraversable(false);
        button_four.setFocusTraversable(false);
        button_five.setFocusTraversable(false);
        button_six.setFocusTraversable(false);
        button_seven.setFocusTraversable(false);
        button_eight.setFocusTraversable(false);
        button_nine.setFocusTraversable(false);

        btnBack.setFocusTraversable(false);
        btnDifficulty.setFocusTraversable(false);
        btnSave.setFocusTraversable(false);
        btnLoad.setFocusTraversable(false);
        btnCorrectness.setFocusTraversable(false);
        btnAutofill.setFocusTraversable(false);

        canvas.setFocusTraversable(true);
    }
}
