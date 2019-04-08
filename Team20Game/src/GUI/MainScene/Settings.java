package GUI.MainScene;
import GUI.GameScene.ChessGame;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static GUI.LoginScreen.Login.storeSettings;
//import static JavaFX.MainScene.MainScene.showMainScene;

/**
 * <h|>Settings</h|>
 * This class is used for the Settings popup box.
 * @since 08.04.2019
 * @author Team 20
 */


@SuppressWarnings("Duplicates")
public class Settings{
    static Scene settingsScene;
    static Stage window;
    static ChoiceBox<String> boardColorChoiceBox, skinNameChoiceBox;

    //Color
    public static String darkTileColor = "#8B4513";
    public static String lightTileColor = "#FFEBCD";

    /**
     * Method for showing the Settings menu.
     */
    static void showSettings(){
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Settings");
        GridPane mainLayout = new GridPane();

        //Title
        String settingsTitle = "Settings";
        Label title = new Label(settingsTitle);
        title.setFont(Font.font("Georgia", 40));
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
            window.close();
        });

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
        boardColorLabel.setFont(Font.font("Georgia", 25));
        boardColorLabel.setTextFill(Color.WHITE);
        mainLayout.add(boardColorLabel, 0, 2);

        boardColorChoiceBox = new ChoiceBox<>();
        boardColorChoiceBox.getItems().add("Brown");
        boardColorChoiceBox.getItems().add("Black");
        boardColorChoiceBox.getItems().add("Blue");
        boardColorChoiceBox.getItems().add("Red");
        boardColorChoiceBox.getItems().add("Orange");
        boardColorChoiceBox.getItems().add("Yellow");
        boardColorChoiceBox.getItems().add("Green");
        boardColorChoiceBox.getItems().add("Pink");

        //Fetch selected color for setValue()
        if(darkTileColor.equals("#8B4513") && lightTileColor.equals("#FFEBCD")){
            boardColorChoiceBox.setValue("Brown");
        } else if(darkTileColor.equals("#000000") && lightTileColor.equals("#FFFFFF")){
            boardColorChoiceBox.setValue("Black");
        } else if(darkTileColor.equals("#010b7a") && lightTileColor.equals("eeedd5")){
            boardColorChoiceBox.setValue("Blue");
        } else if(darkTileColor.equals("#680101") && lightTileColor.equals("eeedd5")){
            boardColorChoiceBox.setValue("Red");
        } else if(darkTileColor.equals("#ef9921") && lightTileColor.equals("eeedd5")){
            boardColorChoiceBox.setValue("Orange");
        } else if(darkTileColor.equals("#f2f20c") && lightTileColor.equals("eeedd5")){
            boardColorChoiceBox.setValue("Yellow");
        } else if(darkTileColor.equals("#7d945d") && lightTileColor.equals("#eeedd5")){
            boardColorChoiceBox.setValue("Green");
        } else if(darkTileColor.equals("#ff00b2") && lightTileColor.equals("#faff00")){
            boardColorChoiceBox.setValue("Pink");
        }

        mainLayout.add(boardColorChoiceBox, 1,2);

        //SkinName
        Label skinNameLabel = new Label("Skin name");
        skinNameLabel.setFont(Font.font("Georgia", 25));
        skinNameLabel.setTextFill(Color.WHITE);
        mainLayout.add(skinNameLabel, 0, 3);

        skinNameChoiceBox = new ChoiceBox<>();
        skinNameChoiceBox.getItems().add("Standard");
        skinNameChoiceBox.getItems().add("Chrome");
        skinNameChoiceBox.getItems().add("Pink");
        skinNameChoiceBox.getItems().add("Wood");

        //Fetch selected skinName for setValue()
        if(ChessGame.skin.equals("Standard")){
            skinNameChoiceBox.setValue("Standard");
        } else if(ChessGame.skin.equals("Chrome")){
            skinNameChoiceBox.setValue("Chrome");
        } else if(ChessGame.skin.equals("Pink")){
            skinNameChoiceBox.setValue("Pink");
        }else if(ChessGame.skin.equals("Wood")){
            skinNameChoiceBox.setValue("Wood");
        }

        mainLayout.add(skinNameChoiceBox, 1,3);

        //Bottomlayout
        GridPane bottomLayout = new GridPane();
        Button resetChangesButton = new Button("Reset");
        resetChangesButton.setOnAction(e -> {
            //reset color
            darkTileColor = "#8B4513";
            lightTileColor = "#FFEBCD";
            //resetSkin
            ChessGame.skin = "Standard";

            storeSettings();
            MainScene.reloadSandbox();
        });
        Button applyChangesButton = new Button("Apply");
        applyChangesButton.setOnAction(e -> {
            applyButtonPressed();
        });
        bottomLayout.getColumnConstraints().add(new ColumnConstraints(270));
        bottomLayout.setPadding(new Insets(0, 25, 25, 0));
        bottomLayout.setHgap(20);
        bottomLayout.add(resetChangesButton, 1, 0);
        bottomLayout.add(applyChangesButton, 2, 0 );


        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #404144;");
        //layout.setTop(new WindowMenuBar(true).getWindowMenuBar());
        layout.setCenter(mainLayout);
        layout.setBottom(bottomLayout);
        settingsScene = new Scene(layout, 455, 500);
        settingsScene.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER)){
                applyButtonPressed();
            }
        });
        window.setScene(settingsScene);
        window.showAndWait();
    }

    /**
     * Method for what happens when you press the Apply button.
     */
    static void applyButtonPressed(){
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
            ChessGame.skin = "Standard";
        } else if(skinNameChoice.equals("Chrome")){
            ChessGame.skin = "Chrome";
        } else if(skinNameChoice.equals("Pink")){
            ChessGame.skin = "Pink";
        } else if(skinNameChoice.equals("Wood")){
            ChessGame.skin = "Wood";
        }
        storeSettings();
        MainScene.reloadSandbox();
    }
}
