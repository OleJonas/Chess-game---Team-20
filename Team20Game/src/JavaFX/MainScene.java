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

    static void showMainScene(){
        Label title = new Label("Recess Chess");
        title.setFont(Font.font("Copperplate", 70));
        title.setStyle("-fx-font-weight: bold");
        title.setTextFill(Color.WHITE);

        Button newGameButton = new Button("New Game");
        newGameButton.setOnAction(e -> {
            gameSetup();
            showGameScene();
        });

        Button joinGameButton = new Button("Join Game");
        joinGameButton.setOnAction(e -> JoinGamePopup.Display());

        Button findUserButton = new Button("Find User");
        findUserButton.setOnAction(e -> showFindUserScene());

        Button userProfileButton = new Button("User profile");
        userProfileButton.setOnAction(e -> showUserProfileScene());

        Button settingsButton = new Button("Settings");
        settingsButton.setOnAction(e -> showSettings());

        //Now im going to code the centerPane, which have to consist of one GridPane, with 2x2 cols/rows. Col 0, row 0 will consist of the title with colspan 2, rowspan 1
        //Column 0, row 2 will have the buttons, and column 1, row 2 will have a sandobox chessboard

        //Left GridPane
        GridPane leftGrid = new GridPane();
        leftGrid.setVgap(40);
        leftGrid.setPadding(new Insets(90, 150, 100, 250));
        //Set buttonsize
        newGameButton.setPrefSize(150, 80);
        joinGameButton.setPrefSize(150,80);
        findUserButton.setPrefSize(150, 80);
        userProfileButton.setPrefSize(150, 80);
        settingsButton.setPrefSize(150, 80);

        leftGrid.add(newGameButton, 0, 0);
        leftGrid.setHalignment(newGameButton, HPos.CENTER);
        leftGrid.add(joinGameButton, 0, 1);
        leftGrid.setHalignment(joinGameButton, HPos.CENTER);
        leftGrid.add(findUserButton, 0, 2);
        leftGrid.setHalignment(findUserButton, HPos.CENTER);
        leftGrid.add(userProfileButton, 0, 3);
        leftGrid.setHalignment(userProfileButton, HPos.CENTER);
        leftGrid.add(settingsButton, 0, 4);
        leftGrid.setHalignment(settingsButton, HPos.CENTER);

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
        layout.setTop(new WindowMenuBar("Home").getWindowMenuBar());
        layout.setCenter(mainLayout);

        mainScene = new Scene(layout, 1450, 950);
        Main.window.setScene(mainScene);
    }

    static boolean gameSetup(){
        try{
            DBOps connection = new DBOps();
            int number = 0;
            boolean distinctGameId = false;
            while(!distinctGameId){
                number = new Random().nextInt(500000);
                if (!JoinGamePopup.checkGameId(number)) distinctGameId = true;
            }
            int rowsAffected = connection.exUpdate("INSERT INTO ........."); //Change this SQLQuery to match the database
            if(rowsAffected > 0){
                player1 = Login.USERNAME;
                ChessGame.gameID = number;
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
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
        ArrayList matchingGameIDs = connection.exQuery("SELECT .......", 1); //Change this SQLQuery to match the database
        if(matchingGameIDs.size() > 0){
            return true;
        } else {
            return false;
        }
    }
}


