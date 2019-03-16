package JavaFX;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
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
class MainScene {
    static Scene mainScene;

    static void showMainScene(){
        Image ChessBoardImage = new Image("/Images/ChessBoardImage.jpg");
        Label title = new Label("Recess Chess");
        title.setFont(Font.font("Copperplate", 60));
        title.setStyle("-fx-font-weight: bold");
        title.setTextFill(Color.WHITE);

        Button newGameButton = new Button("New Game");

        Button joinGameButton = new Button("Join Game");

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
        leftGrid.setPadding(new Insets(100, 200, 100, 250));
        //Set buttonsize
        newGameButton.setPrefSize(100, 50);
        joinGameButton.setPrefSize(100,50);
        findUserButton.setPrefSize(100, 50);
        userProfileButton.setPrefSize(100, 50);
        settingsButton.setPrefSize(100, 50);

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
        rightGrid.setPadding(new Insets(50, 100, 100, 50));

        //mainLayout
        GridPane mainLayout = new GridPane();
        mainLayout.setPadding(new Insets(20, 50, 20, 50));
        mainLayout.setHgap(20);
        mainLayout.setVgap(12);
        mainLayout.getColumnConstraints().add(new ColumnConstraints(550));
        mainLayout.getColumnConstraints().add(new ColumnConstraints(550));
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

        mainScene = new Scene(layout, 1200, 800);
        Main.window.setScene(mainScene);
    }
}
