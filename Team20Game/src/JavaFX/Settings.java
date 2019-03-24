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

import java.io.File;
import java.io.FileFilter;

import static JavaFX.Login.storeSettings;
import static JavaFX.MainScene.showMainScene;
//import static JavaFX.MainScene.showMainScene;


@SuppressWarnings("Duplicates")
class Settings{
    static Scene settingsScene;

    //Settingvariables

    //Color
    static String darkTileColor = "#8B4513";
    static String lightTileColor = "#FFEBCD";
    //Skin
    static String skinName;

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
            storeSettings();
            showMainScene();
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
        mainLayout.setVgap(20);
        mainLayout.getColumnConstraints().add(new ColumnConstraints(250));
        mainLayout.getColumnConstraints().add(new ColumnConstraints(100));
        mainLayout.add(backToMainButton, 0, 0, 2, 1);
        mainLayout.setHalignment(backToMainButton, HPos.LEFT);
        mainLayout.add(title, 0, 0, 2, 1);
        mainLayout.setHalignment(title, HPos.CENTER);
        Label emptylabel = new Label("");
        mainLayout.add(emptylabel, 0, 1);

        //Board Color
        Label boardColorLabel = new Label("Board color");
        boardColorLabel.setFont(Font.font("Copperplate", 25));
        boardColorLabel.setTextFill(Color.WHITE);
        mainLayout.add(boardColorLabel, 0, 2);

        ChoiceBox<String> boardColorChoiceBox = new ChoiceBox<>();
        boardColorChoiceBox.getItems().add("Brown");
        boardColorChoiceBox.getItems().add("Black");
        boardColorChoiceBox.getItems().add("Blue");
        boardColorChoiceBox.getItems().add("Red");
        boardColorChoiceBox.getItems().add("Orange");
        boardColorChoiceBox.getItems().add("Yellow");
        boardColorChoiceBox.getItems().add("Green");
        boardColorChoiceBox.getItems().add("Pink");
        boardColorChoiceBox.setValue("Brown");

        mainLayout.add(boardColorChoiceBox, 1,2);

        //SkinName
        Label skinNameLabel = new Label("Skin name");
        skinNameLabel.setFont(Font.font("Copperplate", 25));
        skinNameLabel.setTextFill(Color.WHITE);
        mainLayout.add(skinNameLabel, 0, 3);

        ChoiceBox<String> skinNameChoiceBox = new ChoiceBox<>();
        skinNameChoiceBox.getItems().add("Standard");
        skinNameChoiceBox.getItems().add("Chrome");
        skinNameChoiceBox.getItems().add("Pink");
        skinNameChoiceBox.setValue("Standard");

        mainLayout.add(skinNameChoiceBox, 1,3);



        //Bottomlayout
        GridPane bottomLayout = new GridPane();
        Button resetChangesButton = new Button("Reset");
        resetChangesButton.setOnAction(e -> {
            //reset color
            darkTileColor = "#8B4513";
            lightTileColor = "#FFEBCD";
            //resetSkin
            skinName = "Standard";
        });
        Button applyChangesButton = new Button("Apply");
        applyChangesButton.setOnAction(e -> {

            //Apply colorchanges
            String colorchoice = boardColorChoiceBox.getValue();
            if(colorchoice.equals("Brown")){
                darkTileColor = "#8B4513";
                lightTileColor = "#FFEBCD";
            } else if(colorchoice.equals("Black")){
                darkTileColor = "#000000";
                lightTileColor = "#FFFFFF";
            } else if(colorchoice.equals("Blue")){
                darkTileColor = "#010b7a";
                lightTileColor = "eeedd5";
            } else if(colorchoice.equals("Red")){
                darkTileColor = "#680101";
                lightTileColor = "eeedd5";
            } else if(colorchoice.equals("Orange")){
                darkTileColor = "#ef9921";
                lightTileColor = "eeedd5";
            } else if(colorchoice.equals("Yellow")){
                darkTileColor = "#f2f20c";
                lightTileColor = "eeedd5";
            } else if(colorchoice.equals("Green")){
                darkTileColor = "#7d945d";
                lightTileColor = "#eeedd5";
            } else if(colorchoice.equals("Pink")){
                darkTileColor = "#ff00b2";
                lightTileColor = "#faff00";
            }

            //Apply skinName
            String skinNameChoice = skinNameChoiceBox.getValue();
            if(skinNameChoice.equals("Standard")){
                skinName = "Standard";
            } else if(skinNameChoice.equals("Chrome")){
                skinName = "Chrome";
            } else if(skinNameChoice.equals("Pink")){
                skinName = "Pink";
            }

        });
        bottomLayout.getColumnConstraints().add(new ColumnConstraints(270));
        bottomLayout.setPadding(new Insets(0, 25, 25, 0));
        bottomLayout.setHgap(20);
        bottomLayout.add(resetChangesButton, 1, 0);
        bottomLayout.add(applyChangesButton, 2, 0 );


        //Set image as background
        BackgroundImage myBI= new BackgroundImage(new Image("Images/Backgrounds/Mahogny.jpg",1200,1200,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        //mainLayout.setBackground(new Background(myBI));

        BorderPane layout = new BorderPane();
        layout.setBackground(new Background(myBI));
        layout.setTop(new WindowMenuBar().getWindowMenuBar());
        layout.setCenter(mainLayout);
        layout.setBottom(bottomLayout);
        settingsScene = new Scene(layout, 455, 500);
        Main.window.setScene(settingsScene);
    }
}
