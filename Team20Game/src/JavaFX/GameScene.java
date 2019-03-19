package JavaFX;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
//import static JavaFX.FindUser.showFindUserScene;
import static JavaFX.Settings.showSettings;
//import static JavaFX.UserProfile.showUserProfileScene;

@SuppressWarnings("Duplicates")
class GameScene {
    static Scene gameScene;

    //Stats which will be initialized with DBOps while starting a game
    static String player1 = "Jonas";
    static String player2 = "MKarlsen";

    static void showGameScene(){
        Label title = new Label("Recess Chess");
        title.setFont(Font.font("Copperplate", 60));
        title.setStyle("-fx-font-weight: bold");
        title.setTextFill(Color.WHITE);

        //Now im going to code the centerPane, which have to consist of one GridPane, with 2x2 cols/rows. Col 0, row 0 will consist of the title with colspan 2, rowspan 1
        //Column 0, row 2 will have the buttons, and column 1, row 2 will have a sandobox chessboard

        //Left GridPane
        GridPane leftGrid = new GridPane();
        leftGrid.setPadding(new Insets(70, 100, 100, 50));
        Parent chessGame = new ChessGame().setupBoard();
        leftGrid.add(chessGame,0,0);


        //Right GridPane
        GridPane rightGrid = new GridPane();
        rightGrid.setPadding(new Insets(70, 50, 50, 50));
        rightGrid.setHgap(10);
        rightGrid.setVgap(10);
        Label gameidLabel = new Label("GameID: " + ChessGame.gameID);
        gameidLabel.setFont(Font.font("Copperplate", 40));
        gameidLabel.setStyle("-fx-font-weight: bold");
        gameidLabel.setTextFill(Color.WHITE);
        rightGrid.add(gameidLabel, 0, 0);
        Label playersLabel = new Label(player1 + " vs " + player2);
        playersLabel.setFont(Font.font("Copperplate", 40));
        playersLabel.setStyle("-fx-font-weight: bold");
        playersLabel.setTextFill(Color.LIGHTSKYBLUE);
        rightGrid.add(playersLabel, 0, 1);
        rightGrid.add(ChatFX.createChat(), 0, 2);


        //mainLayout
        GridPane mainLayout = new GridPane();
        mainLayout.setPadding(new Insets(20, 50, 20, 50));
        mainLayout.setHgap(20);
        mainLayout.setVgap(12);
        mainLayout.getColumnConstraints().add(new ColumnConstraints(675));
        mainLayout.getColumnConstraints().add(new ColumnConstraints(675));
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
        layout.setTop(new WindowMenuBar().getWindowMenuBar());
        layout.setCenter(mainLayout);

        gameScene = new Scene(layout, 1450, 950);
        Main.window.setScene(gameScene);
        ChatFX.refresh();
    }
}

