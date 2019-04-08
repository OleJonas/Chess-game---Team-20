package GUI;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import static GUI.LoginScreen.Login.runLogin;

public class Main extends Application {
    public static Stage window;

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