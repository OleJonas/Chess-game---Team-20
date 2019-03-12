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

import static JavaFX.Login.AVATAR;
import static JavaFX.Login.runLogin;
import static JavaFX.UserProfile.setAvatar;
import static JavaFX.UserProfile.showUserProfileScene;

class MainScene {
    static Scene mainScene;

    static void showMainScene(){
        Image ChessBoardImage = new Image("/Images/ChessBoardImage.jpg");
        Label recessChess = new Label("Recess Chess");
        recessChess.setFont(Font.font("Calibri", 40));
        recessChess.setStyle("-fx-font-weight: bold");
        recessChess.setTextFill(Color.WHITE);

        Button joinGame = new Button("Join Game");

        Button findUser = new Button("Find User");

        Button userProfile = new Button("User profile");
        userProfile.setOnAction(e -> showUserProfileScene());

        Button settings = new Button("Settings");

        Button backToStart = new Button("Log out");
        backToStart.setOnAction(e -> {
            setAvatar(AVATAR);
            runLogin();
        });

        //layout
        GridPane mainScreen = new GridPane();
        mainScreen.setPadding(new Insets(20, 20, 20, 20));
        mainScreen.setHgap(20);
        mainScreen.setVgap(12);
            mainScreen.add(backToStart, 0, 0);
            mainScreen.setHalignment(backToStart, HPos.CENTER);
            mainScreen.add(recessChess, 1, 1, 3, 1);
            mainScreen.setHalignment(recessChess, HPos.CENTER);
            mainScreen.add(joinGame, 1, 2, 3, 1);
            mainScreen.setHalignment(joinGame, HPos.CENTER);
            mainScreen.add(findUser, 1, 3, 3, 1);
            mainScreen.setHalignment(findUser, HPos.CENTER);
            mainScreen.add(userProfile, 1, 4, 3, 1);
            mainScreen.setHalignment(userProfile, HPos.CENTER);
            mainScreen.add(settings, 1, 5, 3, 1);
            mainScreen.setHalignment(settings, HPos.CENTER);
            ImageView ChessBoardImageview = new ImageView(ChessBoardImage);
            ChessBoardImageview.setFitHeight(300);
            ChessBoardImageview.setFitWidth(300);
            mainScreen.add(ChessBoardImageview, 1, 6, 3, 1);


            /*
            ** Set image as background **
            BackgroundImage myBI= new BackgroundImage(new Image("Images/.jpg",500,600,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
            mainScreen.setBackground(new Background(myBI));
            */

            //Set backgroudn to a hex-color-value
            mainScreen.setStyle("-fx-background-color: #000000;");

            mainScene = new Scene(mainScreen, 480, 600);
            Main.window.setScene(mainScene);
    }
}
