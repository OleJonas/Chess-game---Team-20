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
import static JavaFX.FindUser.showFindUserScene;
import static JavaFX.Settings.showSettings;
import static JavaFX.UserProfile.showUserProfileScene;

@SuppressWarnings("Duplicates")
class GameScene {
    static Scene gameScene;

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
    }
}

