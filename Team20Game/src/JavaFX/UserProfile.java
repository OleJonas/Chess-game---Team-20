package JavaFX;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static JavaFX.Login.USERNAME;
import static JavaFX.Login.runLogin;
import static JavaFX.MainScene.showMainScene;

@SuppressWarnings("Duplicates")
class UserProfile{
    static Scene userProfileScene;

    static void showUserProfileScene(){
        Image CarreyGif = new Image("/Images/CarreyGif.gif");
        String userTitle = "User: " + USERNAME;
        Label recessChess = new Label(userTitle);
        recessChess.setFont(Font.font("Calibri", 40));
        recessChess.setStyle("-fx-font-weight: bold");
        recessChess.setTextFill(Color.WHITE);

        Button backToStart = new Button("Back");
        backToStart.setOnAction(e -> showMainScene());

        //layout
        GridPane mainScreen = new GridPane();
        mainScreen.setPadding(new Insets(20, 20, 20, 20));
        mainScreen.setHgap(20);
        mainScreen.setVgap(12);
        mainScreen.add(backToStart, 0, 0);
        mainScreen.setHalignment(backToStart, HPos.CENTER);
        mainScreen.add(recessChess, 1, 1, 3, 1);
        mainScreen.setHalignment(recessChess, HPos.CENTER);
        mainScreen.add(new ImageView(CarreyGif), 1, 6, 3, 1);

            /*
            ** Set image as background **
            BackgroundImage myBI= new BackgroundImage(new Image("Images/.jpg",500,600,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
            mainScreen.setBackground(new Background(myBI));
            */

        //Set backgroudn to a hex-color-value
        mainScreen.setStyle("-fx-background-color: #000000;");

        userProfileScene = new Scene(mainScreen, 500, 600);
        Main.window.setScene(userProfileScene);
    }
}
