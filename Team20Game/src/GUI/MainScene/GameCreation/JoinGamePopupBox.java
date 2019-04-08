package GUI.MainScene.GameCreation;

import Database.Game;
import GUI.MainScene.MainScene;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * <h1>JoinGamePOpupBox</h1>
 * This is the class used for matchmaking. A player will select what king of game they want to play, so that they
 * will wait in a queue to join a game with the specified rules.
 * @author Team20
 * @since 08.04.2019
 */
@SuppressWarnings("Duplicates")
public class JoinGamePopupBox{

    private static Stage window;
    private static ChoiceBox<String> modeChoiceBox;
    private static ChoiceBox<String> timeChoiceBox;
    private static ChoiceBox<String> incrementChoiceBox;
    private static final ToggleGroup ratedGroup = new ToggleGroup();
    private static final ToggleGroup colorGroup = new ToggleGroup();

    /**
     * This method displays the menu where the player selects what king of the game they want to play.
     */
    public static void Display(){

        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Join Game");

        //Labels
        Label titleLabel = new Label("Game settings");
        titleLabel.setFont(Font.font("Georgia", 26));
        titleLabel.setStyle("-fx-font-weight: bold");
        titleLabel.setTextFill(Color.WHITE);
        Label modeLabel = new Label("Mode");
        modeLabel.setFont(Font.font("Georgia", 18));
        modeLabel.setTextFill(Color.WHITE);
        Label timeLabel = new Label("Time");
        timeLabel.setFont(Font.font("Georgia", 18));
        timeLabel.setTextFill(Color.WHITE);
        Label incrementLabel = new Label("Increment");
        incrementLabel.setFont(Font.font("Georgia", 18));
        incrementLabel.setTextFill(Color.WHITE);
        Label ratedLabel = new Label("Rated?");
        ratedLabel.setFont(Font.font("Georgia", 18));
        ratedLabel.setTextFill(Color.WHITE);
        Label colorLabel = new Label("Color");
        colorLabel.setFont(Font.font("Georgia", 18));
        colorLabel.setTextFill(Color.WHITE);

        //Choiceboxes
        modeChoiceBox = new ChoiceBox<>();
        modeChoiceBox.getItems().add("Any");
        modeChoiceBox.getItems().add("Standard");
        modeChoiceBox.getItems().add("Fischer Random");
        modeChoiceBox.getItems().add("Horse Attack");
        modeChoiceBox.getItems().add("Farmers Chess");
        modeChoiceBox.getItems().add("Peasants Revolt");
        modeChoiceBox.setValue("Standard");

        timeChoiceBox = new ChoiceBox<>();
        timeChoiceBox.getItems().add("No timer");
        timeChoiceBox.getItems().add("1 min");
        timeChoiceBox.getItems().add("3 min");
        timeChoiceBox.getItems().add("5 min");
        timeChoiceBox.getItems().add("10 min");
        timeChoiceBox.getItems().add("15 min");
        timeChoiceBox.getItems().add("30 min");
        timeChoiceBox.getItems().add("45 min");
        timeChoiceBox.getItems().add("Any");
        timeChoiceBox.setValue("Any");

        incrementChoiceBox = new ChoiceBox<>();
        incrementChoiceBox.getItems().add("No increment");
        incrementChoiceBox.getItems().add("1 sec");
        incrementChoiceBox.getItems().add("2 sec");
        incrementChoiceBox.getItems().add("5 sec");
        incrementChoiceBox.getItems().add("10 sec");
        incrementChoiceBox.getItems().add("15 sec");
        incrementChoiceBox.getItems().add("Any");
        incrementChoiceBox.setValue("Any");

        //Radiobuttons
        HBox ratedButtons = new HBox();
        ratedButtons.setSpacing(5);
        RadioButton yesRatedRadioButton = new RadioButton("Yes");
        yesRatedRadioButton.setTextFill(Color.WHITE);
        yesRatedRadioButton.setToggleGroup(ratedGroup);
        yesRatedRadioButton.setSelected(true);
        RadioButton noRatedRadioButton = new RadioButton("No");
        noRatedRadioButton.setTextFill(Color.WHITE);
        noRatedRadioButton.setToggleGroup(ratedGroup);
        ratedButtons.getChildren().addAll(yesRatedRadioButton, noRatedRadioButton);

        HBox colorButtons = new HBox();
        colorButtons.setSpacing(5);
        RadioButton anyColorRadioButton = new RadioButton("Any");
        anyColorRadioButton.setTextFill(Color.WHITE);
        anyColorRadioButton.setToggleGroup(colorGroup);
        anyColorRadioButton.setSelected(true);
        RadioButton whiteColorRadioButton = new RadioButton("White");
        whiteColorRadioButton.setTextFill(Color.WHITE);
        whiteColorRadioButton.setToggleGroup(colorGroup);
        RadioButton blackColorRadioButton = new RadioButton("Black");
        blackColorRadioButton.setTextFill(Color.WHITE);
        blackColorRadioButton.setToggleGroup(colorGroup);
        colorButtons.getChildren().addAll(anyColorRadioButton, whiteColorRadioButton, blackColorRadioButton);

        //ratedChoicePane
        GridPane ratedChoicePane = new GridPane();
        ratedChoicePane.setHgap(5);
        ratedChoicePane.add(ratedLabel, 0, 0);
        ratedChoicePane.setHalignment(ratedLabel, HPos.CENTER);
        ratedChoicePane.add(ratedButtons, 0, 1);

        //colorChoicePane
        GridPane colorChoicePane = new GridPane();
        colorChoicePane.setHgap(5);
        colorChoicePane.add(colorLabel, 0, 0);
        colorChoicePane.setHalignment(colorLabel, HPos.CENTER);
        colorChoicePane.add(colorButtons, 0, 1);

        //Create Game Button
        Button joinGameButton = new Button("Join Game");
        joinGameButton.setOnAction(e -> {
            tryJoinGame();
            MainScene.joined = true;
        });

        BorderPane windowLayout = new BorderPane();
        GridPane mainLayout = new GridPane();
        mainLayout.setHgap(35);
        mainLayout.setVgap(20);
        mainLayout.setPadding(new Insets(30, 40, 30, 40));
        mainLayout.add(titleLabel, 0, 0, 2, 1);
        mainLayout.setHalignment(titleLabel, HPos.CENTER);
        mainLayout.add(modeLabel, 0, 1);
        mainLayout.add(modeChoiceBox, 1, 1);
        mainLayout.add(timeLabel, 0, 2);
        mainLayout.add(timeChoiceBox, 1, 2);
        mainLayout.add(incrementLabel, 0, 3);
        mainLayout.add(incrementChoiceBox, 1, 3);
        mainLayout.add(ratedChoicePane, 0, 4);
        mainLayout.setHalignment(ratedChoicePane, HPos.CENTER);
        mainLayout.add(colorChoicePane, 1, 4);
        mainLayout.setHalignment(colorChoicePane, HPos.CENTER);

        GridPane bottomLayout = new GridPane();
        bottomLayout.getColumnConstraints().add(new ColumnConstraints(370));
        bottomLayout.setPadding(new Insets(0,25,15,0));
        bottomLayout.add(joinGameButton, 0,0);
        bottomLayout.setHalignment(joinGameButton, HPos.RIGHT);
        windowLayout.setCenter(mainLayout);
        windowLayout.setBottom(bottomLayout);
        windowLayout.setStyle("-fx-background-color: #404144;");

        Scene scene = new Scene(windowLayout, 410, 380);
        scene.setOnKeyPressed(e->{
            if(e.getCode().equals(KeyCode.ENTER)){
                tryJoinGame();
                MainScene.joined = true;
            }
        });
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * When the user has entered the desired specifics of the game and presses join game, this method will run.
     * Here the information from the popup box is used to create an SQL sentence that will allow the player to join
     * games who fulfill the given criteria.
     */
    static void tryJoinGame(){
        String modeChoice = modeChoiceBox.getValue();
        String timeChoice = timeChoiceBox.getValue();
        String incrementChoice = incrementChoiceBox.getValue();
        RadioButton ratedChoice = (RadioButton) ratedGroup.getSelectedToggle();
        String ratedChoiceString = ratedChoice.getText();
        RadioButton colorChoice = (RadioButton) colorGroup.getSelectedToggle();
        String colorChoiceString = colorChoice.getText();

        int mode = -1;
        if (modeChoice.equals("Standard")) {
            mode = 0;
        } else if (modeChoice.equals("Fischer Random")) {
            mode = 1;
        } else if (modeChoice.equals("Horse Attack")) {
            mode = 2;
        } else if (modeChoice.equals("Farmers Chess")) {
            mode = 3;
        } else if (modeChoice.equals("Peasants Revolt")) {
            mode = 4;
        }

        int time = -1;
        if (!timeChoice.equals("Any")) {
            if (timeChoice.equals("No timer")) {
                time = 0;
            } else if (timeChoice.length() == 5) {
                time = Integer.parseInt(timeChoice.substring(0, 1));
            } else {
                time = Integer.parseInt(timeChoice.substring(0, 2));
            }
        }
        int increment = -1;
        if (!incrementChoice.equals("Any")) {
            if (incrementChoice.equals("No increment")) {
                time = 0;
            } else if (incrementChoice.length() == 5) {
                increment = Integer.parseInt(incrementChoice.substring(0, 1));
            } else {
                increment = Integer.parseInt(incrementChoice.substring(0, 2));
            }
        }
        boolean[] color = {false, false};
        if (colorChoiceString.equals("Any")) {
            color[1] = true;
        } else if (colorChoiceString.equals("Black")) {
            color[0] = true;
        } else if (colorChoiceString.equals("White")) {
            color[0] = false;
        }
        int rated = 0;
        if (ratedChoiceString.equals("Yes")) {
            rated = 1;
        }

        Game.sql = Game.createSearch(mode, time, increment, color, rated);
        System.out.println(Game.sql);
        MainScene.inQueueJoin = true;
        System.out.println("Mode: " +modeChoice+ "\nTime: " + timeChoice + "\nIncrement: " + incrementChoice + "\nRated: " + ratedChoiceString + "\nColor: " + colorChoiceString);
        window.close();
    }
}