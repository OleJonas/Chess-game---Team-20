package JavaFX;
import Database.DBOps;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;
import static JavaFX.FindUser.showFindUserScene;
import static JavaFX.GameScene.*;
import static JavaFX.Settings.showSettings;
import static JavaFX.UserProfile.showUserProfileScene;
import JavaFX.ChessSandbox;

@SuppressWarnings("Duplicates")
class MainScene {
    static Scene mainScene;
    static GridPane leftGrid;
    static Button newGameButton, findUserButton, userProfileButton, settingsButton, createGameButton, joinGameButton, inviteFriendButton, backButton;

    static void showMainScene(){
        Label title = new Label("Recess Chess");
        title.setFont(Font.font("Copperplate", 70));
        title.setStyle("-fx-font-weight: bold");
        title.setTextFill(Color.WHITE);

        //buttons for newGameOption
        createGameButton = new Button("Create Game");
        createGameButton.setOnAction(e -> {
            gameSetup();
            showGameScene();
        });
        joinGameButton = new Button("Join Game");
        joinGameButton.setOnAction(e -> JoinGamePopup.Display());

        //Left GridPane
        leftGrid = new GridPane();
        leftGrid.setVgap(40);
        leftGrid.setPadding(new Insets(150, 150, 100, 250));
        newGameButton = new Button("New Game");
        newGameButton.setOnAction(e -> {
            leftGrid.getChildren().clear();
            leftGrid.setVgap(40);
            leftGrid.setPadding(new Insets(150, 150, 100, 250));
            createGameButton.setPrefSize(150, 80);
            joinGameButton.setPrefSize(150, 80);
            inviteFriendButton.setPrefSize(150, 80);
            backButton.setPrefSize(150, 80);
            leftGrid.add(createGameButton, 0,0);
            leftGrid.setHalignment(createGameButton, HPos.CENTER);
            leftGrid.add(joinGameButton, 0, 1);
            leftGrid.setHalignment(joinGameButton, HPos.CENTER);
            leftGrid.add(inviteFriendButton, 0, 2);
            leftGrid.setHalignment(inviteFriendButton, HPos.CENTER);
            leftGrid.add(backButton, 0, 3);
            leftGrid.setHalignment(backButton, HPos.CENTER);

        });
        findUserButton = new Button("Find User");
        findUserButton.setOnAction(e -> showFindUserScene());
        userProfileButton = new Button("User profile");
        userProfileButton.setOnAction(e -> showUserProfileScene());
        settingsButton = new Button("Settings");
        settingsButton.setOnAction(e -> showSettings());
        newGameButton.setPrefSize(150, 80);
        findUserButton.setPrefSize(150, 80);
        userProfileButton.setPrefSize(150, 80);
        settingsButton.setPrefSize(150, 80);
        leftGrid.add(newGameButton, 0, 0);
        leftGrid.setHalignment(newGameButton, HPos.CENTER);
        leftGrid.add(findUserButton, 0, 1);
        leftGrid.setHalignment(findUserButton, HPos.CENTER);
        leftGrid.add(userProfileButton, 0, 2);
        leftGrid.setHalignment(userProfileButton, HPos.CENTER);
        leftGrid.add(settingsButton, 0, 3);
        leftGrid.setHalignment(settingsButton, HPos.CENTER);


        //updated leftGrid
        inviteFriendButton = new Button("Invite Friend");
        inviteFriendButton.setOnAction(e -> {});
        backButton = new Button("Back to Main");
        backButton.setOnAction(e -> {
            leftGrid.getChildren().clear();
            leftGrid.add(newGameButton, 0, 0);
            leftGrid.setHalignment(newGameButton, HPos.CENTER);
            leftGrid.add(findUserButton, 0, 1);
            leftGrid.setHalignment(findUserButton, HPos.CENTER);
            leftGrid.add(userProfileButton, 0, 2);
            leftGrid.setHalignment(userProfileButton, HPos.CENTER);
            leftGrid.add(settingsButton, 0, 3);
            leftGrid.setHalignment(settingsButton, HPos.CENTER);
        });


        //Right GridPane
        GridPane rightGrid = new GridPane();
        rightGrid.setPadding(new Insets(60, 150, 20, 0));
        rightGrid.setVgap(20);
        Parent chessGame = new ChessSandbox().createContent();
        rightGrid.add(chessGame,0,0);
        Button clearBoard = new Button("Clear Board");
        clearBoard.setOnAction(e -> showMainScene());
        rightGrid.add(clearBoard, 0,1);
        rightGrid.setHalignment(clearBoard, HPos.RIGHT);
        Label sandboxLabel = new Label("This is a sandbox chess game, play as you want!");
        sandboxLabel.setFont(Font.font("Calibri", 20));
        sandboxLabel.setTextFill(Color.WHITE);
        rightGrid.add(sandboxLabel, 0, 1);

        //mainLayout
        GridPane mainLayout = new GridPane();
        mainLayout.setPadding(new Insets(30, 50, 20, 50));
        mainLayout.setHgap(20);
        mainLayout.setVgap(12);
        mainLayout.getColumnConstraints().add(new ColumnConstraints(625));
        mainLayout.getColumnConstraints().add(new ColumnConstraints(725));
        mainLayout.add(title, 0, 0, 2, 1);
        mainLayout.setHalignment(title, HPos.CENTER);
        mainLayout.add(leftGrid, 0, 1);
        mainLayout.setHalignment(leftGrid, HPos.CENTER);
        mainLayout.add(rightGrid, 1,1);
        mainLayout.setHalignment(rightGrid, HPos.CENTER);
        //mainLayout.setGridLinesVisible(true);

        //Set image as background
        BackgroundImage myBI= new BackgroundImage(new Image("Images/Backgrounds/Mahogny.jpg",1200,1200,false,true),
            BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);
        mainLayout.setBackground(new Background(myBI));


        BorderPane layout = new BorderPane();
        layout.setTop(new WindowMenuBar("home").getWindowMenuBar());
        layout.setCenter(mainLayout);

        mainScene = new Scene(layout, 1450, 950);
        Main.window.setScene(mainScene);
    }

    static void gameSetup(){
        ChessGame.gameID = newGameID();
    }

    static int newGameID(){
        DBOps connection = new DBOps();
        ArrayList matchingGameIDs = connection.exQuery("SELECT MAX(GameID) from GameIDMove group by GameID", 1); //Change this SQLQuery to match the database
        if(matchingGameIDs.size() == 0){
            return 1;
        }
        int out = Integer.parseInt( (String) matchingGameIDs.get(0));
        return out + 1;
    }


}

@SuppressWarnings("Duplicates")
class JoinGamePopup{

    public static void Display(){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Join Game");

        Label label = new Label("GameID:");
        label.setFont(Font.font("Copperplate", 24));
        label.setStyle("-fx-font-weight: bold");
        label.setTextFill(Color.WHITE);

        Label comment = new Label("");
        comment.setTextFill(Color.RED);

        TextField gameIDInputField = new TextField();
        gameIDInputField.setPrefWidth(260);

        Button connectButton = new Button("Connect");
        connectButton.setOnAction(e -> {
            String gameIDInputString = gameIDInputField.getText();
            if(isInt(gameIDInputString)){
                int inputInt = Integer.parseInt(gameIDInputString);

                if (checkGameId(inputInt)){
                    ChessGame.gameID = inputInt;
                    window.close();
                    showGameScene();
                }

                ChessGame.gameID = inputInt;
                window.close();
                showGameScene();
            } else {comment.setText("Not a valid number!");}
        });

        GridPane mainLayout = new GridPane();
        mainLayout.setHgap(10);
        mainLayout.setVgap(20);
        mainLayout.setPadding(new Insets(30, 30, 30, 30));
        mainLayout.add(label, 0, 0, 2, 1);
        mainLayout.setHalignment(label, HPos.CENTER);
        mainLayout.add(gameIDInputField, 0, 1);
        mainLayout.add(comment, 0, 2);
        mainLayout.add(connectButton, 0, 2);
        mainLayout.setHalignment(connectButton, HPos.RIGHT);
        mainLayout.setStyle("-fx-background-color: #404144;");

        Scene scene = new Scene(mainLayout, 330, 180);
        window.setScene(scene);
        window.showAndWait();
    }

    static boolean isInt(String string){
        try{
            int out = Integer.parseInt(string);
        } catch (Exception a){
            return false;
        }
        return true;
    }

    static boolean checkGameId(int gameid){ //Here you have to check if the GameID exists!
        DBOps connection = new DBOps();
        ArrayList matchingGameIDs = connection.exQuery("SELECT GameID from GameIDMove where GameID = '" + gameid + "'", 1); //Change this SQLQuery to match the database
        if(matchingGameIDs.size() > 0){
            return true;
        } else {
            return false;
        }
    }
}



