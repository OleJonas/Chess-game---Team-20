package JavaFX;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;

import static JavaFX.LoginScreen.Login.runLogin;

/**
 * <h1>Main</h1>
 * This class contains the main method and is used to start the program.
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
    public void start(Stage primaryStage){
        window = primaryStage;
        runLogin();
        primaryStage.centerOnScreen();
        window.setTitle("Recess Chess");
        window.getIcons().add(new Image("/Images/icon.png"));
        window.show();
    }
}