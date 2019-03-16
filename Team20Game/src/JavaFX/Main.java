package JavaFX;
import javafx.application.Application;
import javafx.stage.Stage;

import static JavaFX.Login.runLogin;
import static JavaFX.MainScene.showMainScene;

public class Main extends Application {

    static Stage window;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        window = primaryStage;
        //runLogin();
        showMainScene();
        primaryStage.centerOnScreen();
        window.setTitle("Recess Chess");
        window.show();
    }
}
