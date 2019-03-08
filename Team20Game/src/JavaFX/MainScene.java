package JavaFX;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import static JavaFX.Login.runLogin;

class MainScene {
    static Scene mainScene;

    static void showMainScene(){
    //loggedInScene
    Image CarreyGif = new Image("/Images/CarreyGif.gif");
    Label loggedin = new Label("You're logged in");
    Button backToStart = new Button("Back to start");
        backToStart.setOnAction(e -> runLogin());
    //layout
    GridPane loggedInLayout = new GridPane();
        loggedInLayout.getColumnConstraints().add(new ColumnConstraints(150)); //Setting columnconstraint for left column
        loggedInLayout.getColumnConstraints().add(new ColumnConstraints(400)); //Setting columnconstraint for second column
        loggedInLayout.add(loggedin, 0, 0);
        loggedInLayout.add(backToStart, 0, 1);
        loggedInLayout.add(new ImageView(CarreyGif), 10, 20);
        mainScene = new Scene(loggedInLayout, 1400, 800);
        Main.window.setScene(mainScene);
    }
}
