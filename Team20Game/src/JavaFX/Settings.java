package JavaFX;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import static JavaFX.MainScene.showMainScene;


@SuppressWarnings("Duplicates")
class Settings{
    static Scene settingsScene;

    static void showSettings(){
        GridPane mainLayout = new GridPane();

        //Title
        String settingsTitle = "Settings";
        Label title = new Label(settingsTitle);
        title.setFont(Font.font("Copperplate", 40));
        title.setStyle("-fx-font-weight: bold");
        title.setTextFill(Color.WHITE);

        //Buttons
        Button backToMainButton = new Button("Back");
        Image imageBackToMain = new Image("Images/ButtonImages/ArrowLeft.png");
        ImageView imageViewBackToMain = new ImageView(imageBackToMain);
        imageViewBackToMain.setFitWidth(20);
        imageViewBackToMain.setFitHeight(20);
        backToMainButton.setGraphic(imageViewBackToMain);
        backToMainButton.setOnAction(e -> {
            showMainScene();
            //Here you shuld later on create a method for storing the settings to a table in the database called userSettings
        });


/*
        //Right GridPane
        GridPane rightGrid = new GridPane();
        rightGrid.setVgap(10);
        rightGrid.setHgap(10);
        rightGrid.setPadding(new Insets(20, 20, 20, 20));

        //Left GridPane
        GridPane leftGrid = new GridPane();
        leftGrid.setVgap(10);
        leftGrid.setHgap(10);
        leftGrid.setPadding(new Insets(20, 20, 20, 20));
*/

        //mainLayout
        mainLayout.setPadding(new Insets(20, 50, 20, 50));
        mainLayout.setHgap(20);
        mainLayout.setVgap(12);
        mainLayout.getColumnConstraints().add(new ColumnConstraints(250));
        mainLayout.getColumnConstraints().add(new ColumnConstraints(250));
        mainLayout.add(backToMainButton, 0, 0, 2, 1);
        mainLayout.setHalignment(backToMainButton, HPos.LEFT);
        mainLayout.add(title, 0, 0, 2, 1);
        mainLayout.setHalignment(title, HPos.CENTER);
        Label settingLabel = new Label("Setting 1");
        settingLabel.setFont(Font.font("Copperplate", 14));
        mainLayout.add(settingLabel, 0, 1);
        Slider slider1 = new Slider(4, 8, 0.5);
        mainLayout.add(slider1, 1, 1);


        //Set image as background
        BackgroundImage myBI= new BackgroundImage(new Image("Images/Backgrounds/Mahogny.jpg",1200,1200,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        mainLayout.setBackground(new Background(myBI));

        BorderPane layout = new BorderPane();
        layout.setTop(new WindowMenuBar().getWindowMenuBar());
        layout.setCenter(mainLayout);
        settingsScene = new Scene(layout, 600, 500);
        Main.window.setScene(settingsScene);
    }
}
