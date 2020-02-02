package GUI.GameScene;

import Database.Game;
import Database.User;
import Game.GameEngine;
import GUI.LoginScreen.Login;
import GUI.MainScene.MainScene;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static GUI.GameScene.GameScene.opponentTimer;
import static GUI.GameScene.GameScene.yourTimer;
import static GUI.GameScene.GameScene.opponentTimer;
import static GUI.GameScene.GameScene.yourTimer;
import static GUI.LoginScreen.Login.userID;

/**
 * <h1>GameOverPopupBox</h1>
 * The purpose of this class is to create the pop-up box when the game is over, indicating who won and the change in the ELO-rating
 * @since 08.04.2019
 * @author Team 20
 */

public class GameOverPopupBox {
    static Stage window;

    /**
     * This methods displays the game over window with the information about who won and the change in ELO.
     */
    public static void Display(){
        yourTimer.cancel();
        opponentTimer.cancel();
        int oldElo = ChessGame.color?ChessGame.whiteELO:ChessGame.blackELO;
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Game over");

        //Labels
        int result = Game.getResult(ChessGame.gameID);
        String text = "";
        if (userID == result) {
            text = "VICTORY";
        } else {
            text = "DEFEAT";
        }

        if(result == 0){
            result = 2;
            text = "DRAW";
        }else if(result == Game.getUser_id1(ChessGame.gameID)){
            result = 0;
        }else{
            result = 1;
        }
        Label titleLabel = new Label(text);
        titleLabel.setFont(Font.font("Georgia", 26));
        titleLabel.setStyle("-fx-font-weight: bold");
        titleLabel.setTextFill(Color.WHITE);

        int[] elo = GameEngine.getElo(ChessGame.whiteELO, ChessGame.blackELO, result);

        int myNewElo = 0;
        int enemyElo = 0;
        if (Game.getRated(ChessGame.gameID) == 0) {
            myNewElo = oldElo;
        } else {
            myNewElo = ChessGame.color?elo[0]:elo[1];
        }

        //enemyElo = ChessGame.color?elo[1]:elo[0];
        System.out.println("old ELO: " + oldElo + " your new ELO: "+ myNewElo + " \nEnemy's new ELO: " + enemyElo);
        String newElo = Login.USERNAME + "'s new ELO rating: \n" + myNewElo + " (" +((myNewElo-oldElo)>0?"+":"") +(myNewElo-oldElo) + ")";
        Label eloLabel = new Label(newElo);
        eloLabel.setFont(Font.font("Georgia", 22));
        eloLabel.setStyle("-fx-font-weight: bold");
        eloLabel.setTextFill(Color.WHITE);


        //Create Game Button
        Button leaveGameButton = new Button("Leave Game");
        leaveGameButton.setOnAction(e -> {
            leaveGameButtonPressed();
        });

        window.setOnCloseRequest(e -> {
            leaveGameButtonPressed();
        });

        BorderPane windowLayout = new BorderPane();
        GridPane mainLayout = new GridPane();
        mainLayout.setHgap(35);
        mainLayout.setVgap(20);
        mainLayout.setPadding(new Insets(30, 40, 30, 40));
        mainLayout.add(titleLabel, 0, 0, 2, 1);
        mainLayout.add(eloLabel,0,1,2,1);
        mainLayout.setHalignment(titleLabel, HPos.CENTER);
        mainLayout.setHalignment(eloLabel, HPos.CENTER);

        GridPane bottomLayout = new GridPane();
        bottomLayout.getColumnConstraints().add(new ColumnConstraints(370));
        bottomLayout.setPadding(new Insets(0,25,15,0));
        bottomLayout.add(leaveGameButton, 0,0);
        bottomLayout.setHalignment(leaveGameButton, HPos.CENTER);
        windowLayout.setCenter(mainLayout);
        windowLayout.setBottom(bottomLayout);
        windowLayout.setStyle("-fx-background-color: #404144;");

        Scene scene = new Scene(windowLayout, 350, 310);
        scene.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER)){
                leaveGameButtonPressed();
            }
        });
        window.setScene(scene);
        window.showAndWait();
        User.updateUser();
    }

    /**
     * This method is called when the player clicks on the leave game button, which exits the game.
     */
    static void leaveGameButtonPressed(){
        MainScene.showMainScene();
        MainScene.inGame = false;
        MainScene.searchFriend = true;
        if(MainScene.inDrawOffer){
            DrawOfferPopupBox.close();
        }
        window.close();
    }
}