package GUI;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static GUI.LoginScreen.Login.runLogin;

/**
 * <h1>Main</h1>
 * This class contains the main method and is used to start the program.
 * @since 10.04.2019
 * @author Team 20
 */

public class Main extends Application {
    public static Stage window;

    /**
     * Main method that launches the program.
     */
    public static void main(String[] args){
        launch(args);
    }

    @Override
    /**
     * Method that launches the login screen.
     */
    public void start(Stage primaryStage){
        window = primaryStage;
        runLogin();
        primaryStage.centerOnScreen();
        window.setTitle("Recess Chess");
        window.getIcons().add(new Image("/Images/icon.png"));
        window.show();
    }
}