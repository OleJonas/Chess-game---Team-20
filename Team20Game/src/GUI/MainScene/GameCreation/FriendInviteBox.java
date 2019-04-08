package GUI.MainScene.GameCreation;

import Database.Game;
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
import java.util.ArrayList;

import static GUI.GameScene.GameScene.showGameScene;

/**
 * <h1>FriendInviteBox</h1>
 * This class describes the window that shows up for players that are invited to a specific match.
 * @author Team20
 * @since 08.04.2019
 */
public class FriendInviteBox {
    static Stage window;

    /**
     * This method containts the JavaFX to design, show and interact with the invitation box.
     * In order to be able to join both players must update the database with the id of the match they are in,
     * therefore the match id is an argument in this method.
     * @param game_id The id of the match that you are invited to.
     */
    public static void Display(int game_id){
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Invite");
        ArrayList<String> text = Game.friendInviteInfo(game_id);
        //Labels
        Label titleLabel = new Label(text.get(0));
        titleLabel.setFont(Font.font("Georgia", 26));
        titleLabel.setStyle("-fx-font-weight: bold");
        titleLabel.setTextFill(Color.WHITE);

        Label textLabel = new Label(text.get(1));
        textLabel.setFont(Font.font("Georgia", 22));
        textLabel.setStyle("-fx-font-weight: bold");
        textLabel.setTextFill(Color.WHITE);

        //Create Game Button
        Button acceptInvite = new Button("Accept");
        acceptInvite.setOnAction(e -> {
            if(Game.tryAcceptInvite(game_id)){
                MainScene.inGame = true;
                window.close();
                showGameScene();
            } else {
                window.close();
                System.out.println("Not active");
            }
        });

        Button declineInvite = new Button("Decline");
        declineInvite.setOnAction(e -> {
            Game.setInactiveByGame_id(game_id);
            MainScene.searchFriend = true;
            window.close();
        });

        BorderPane windowLayout = new BorderPane();
        GridPane mainLayout = new GridPane();
        mainLayout.setHgap(35);
        mainLayout.setVgap(20);
        mainLayout.setPadding(new Insets(30, 40, 30, 40));
        mainLayout.add(titleLabel, 0, 0, 2, 1);
        mainLayout.add(textLabel, 0, 1, 2, 1);
        mainLayout.setHalignment(textLabel, HPos.CENTER);
        mainLayout.setHalignment(titleLabel, HPos.CENTER);

        GridPane bottomLayout = new GridPane();
        bottomLayout.getColumnConstraints().add(new ColumnConstraints(370));
        bottomLayout.setPadding(new Insets(0,50,15,50));
        bottomLayout.add(acceptInvite, 0,0);
        bottomLayout.setHalignment(acceptInvite, HPos.LEFT);
        bottomLayout.add(declineInvite, 2, 0);
        windowLayout.setCenter(mainLayout);
        windowLayout.setBottom(bottomLayout);
        windowLayout.setStyle("-fx-background-color: #404144;");

        Scene scene = new Scene(windowLayout, 560, 360);
        scene.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER)){
                if(Game.tryAcceptInvite(game_id)){
                    MainScene.inGame = true;
                    window.close();
                    showGameScene();
                } else {
                    window.close();
                    System.out.println("Not active");
                }
            }
        });
        window.setScene(scene);
        window.showAndWait();
    }
}