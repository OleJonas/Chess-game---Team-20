package GUI.MainScene.GameCreation;

import Database.Game;
import Database.User;
import GUI.GameScene.ChessGame;
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
import java.util.Random;

import static GUI.LoginScreen.Login.USERNAME;
/**
 * <h1>InviteFriendPopupBox</h1>
 * This class describes the popup used to create a game invitation.
 * @author Team20
 * @since 08.04.2019
 */
@SuppressWarnings("Duplicates")
public class InviteFriendPopupBox{
    static ChoiceBox<String> modeChoiceBox = new ChoiceBox<>();
    static ChoiceBox<String> timeChoiceBox = new ChoiceBox<>();
    static ChoiceBox<String> incrementChoiceBox = new ChoiceBox<>();
    static final ToggleGroup ratedGroup = new ToggleGroup();
    static final ToggleGroup colorGroup = new ToggleGroup();
    static TextField searchField;
    static Stage window;
    static Label searchComment;

    /**
     * This method shows all the ways to configure a custom match that you will invite a friend to. This is done
     * mainly by using choiceBoxes.
     */
    public static void Display(){
        modeChoiceBox.getItems().clear();
        timeChoiceBox.getItems().clear();
        incrementChoiceBox.getItems().clear();
        ratedGroup.getToggles().clear();
        colorGroup.getToggles().clear();

        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Invite Friend");

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
        Label friendLabel = new Label("Invite friend");
        friendLabel.setFont(Font.font("Georgia", 18));
        friendLabel.setTextFill(Color.WHITE);

        modeChoiceBox.getItems().add("Standard");
        modeChoiceBox.getItems().add("Fischer Random");
        modeChoiceBox.getItems().add("Horse Attack");
        modeChoiceBox.getItems().add("Farmers Chess");
        modeChoiceBox.getItems().add("Peasants Revolt");
        modeChoiceBox.setValue("Standard");

        //Choiceboxes
        timeChoiceBox.getItems().add("No timer");
        timeChoiceBox.getItems().add("1 min");
        timeChoiceBox.getItems().add("3 min");
        timeChoiceBox.getItems().add("5 min");
        timeChoiceBox.getItems().add("10 min");
        timeChoiceBox.getItems().add("15 min");
        timeChoiceBox.getItems().add("30 min");
        timeChoiceBox.getItems().add("45 min");
        timeChoiceBox.setValue("No timer");

        incrementChoiceBox.getItems().add("No increment");
        incrementChoiceBox.getItems().add("1 sec");
        incrementChoiceBox.getItems().add("2 sec");
        incrementChoiceBox.getItems().add("5 sec");
        incrementChoiceBox.getItems().add("10 sec");
        incrementChoiceBox.getItems().add("15 sec");
        incrementChoiceBox.setValue("No increment");

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
        RadioButton whiteColorRadioButton = new RadioButton("White");
        whiteColorRadioButton.setTextFill(Color.WHITE);
        whiteColorRadioButton.setToggleGroup(colorGroup);
        RadioButton blackColorRadioButton = new RadioButton("Black");
        blackColorRadioButton.setTextFill(Color.WHITE);
        blackColorRadioButton.setToggleGroup(colorGroup);
        RadioButton anyColorRadioButton = new RadioButton("Any");
        anyColorRadioButton.setTextFill(Color.WHITE);
        anyColorRadioButton.setToggleGroup(colorGroup);
        anyColorRadioButton.setSelected(true);
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

        //Textfield with label and comment
        Label usernameLabel = new Label("Username: ");
        usernameLabel.setFont(Font.font("Georgia", 30));
        usernameLabel.setStyle("-fx-font-weight: bold");
        usernameLabel.setTextFill(Color.WHITE);
        searchField = new TextField();
        searchField.setPrefSize(200, 30);
        searchComment = new Label("");
        searchComment.setTextFill(Color.RED);


        //Create Game Button
        Button createGameButton = new Button("Create Game");
        createGameButton.setOnAction(e -> {
            MainScene.invitedFriend = true;
            tryInviteCreate();
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
        mainLayout.add(friendLabel, 0, 5);
        mainLayout.add(searchField, 1, 5);
        mainLayout.add(searchComment, 1, 6);

        GridPane bottomLayout = new GridPane();
        bottomLayout.getColumnConstraints().add(new ColumnConstraints(370));
        bottomLayout.setPadding(new Insets(0,25,15,0));
        bottomLayout.add(createGameButton, 0,0);
        bottomLayout.setHalignment(createGameButton, HPos.RIGHT);
        windowLayout.setCenter(mainLayout);
        windowLayout.setBottom(bottomLayout);
        windowLayout.setStyle("-fx-background-color: #404144;");

        Scene scene = new Scene(windowLayout, 430, 470);
        scene.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER)){
                MainScene.invitedFriend = true;
                tryInviteCreate();
            }
        });
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * This methods invites other players to a game that you create. It will check who you invite, so it will handle
     * cases where you try to invite a user that does not exist, or yourself. If no errors occur the game will be
     * created in the end.
     */
    static void tryInviteCreate(){
        String modeChoice = modeChoiceBox.getValue();
        String timeChoice = timeChoiceBox.getValue();
        String incrementChoice = incrementChoiceBox.getValue();
        RadioButton ratedChoice = (RadioButton) ratedGroup.getSelectedToggle();
        String ratedChoiceString = ratedChoice.getText();
        RadioButton colorChoice = (RadioButton) colorGroup.getSelectedToggle();
        String colorChoiceString = colorChoice.getText();
        ChessGame.gameID = Game.newGameID();

        int mode = 0;
        if (modeChoice.equals("Fischer Random")) {
            Random random = new Random();
            int seed = random.nextInt(4000) +1000;
            mode = seed;
        } else if (modeChoice.equals("Horse Attack")) {
            mode = 2;
        } else if (modeChoice.equals("Farmers Chess")) {
            mode = 3;
        } else if (modeChoice.equals("Peasants Revolt")) {
            mode = 4;
        }

        int time = 0;
        if (!timeChoice.equals("No timer")) {
            if (timeChoice.length() == 5) {
                time = Integer.parseInt(timeChoice.substring(0, 1));
            } else {
                time = Integer.parseInt(timeChoice.substring(0, 2));
            }
        }

        int increment = 0;
        if (!incrementChoice.equals("No increment")) {
            if (incrementChoice.length() == 5) {
                increment = Integer.parseInt(incrementChoice.substring(0, 1));
            } else {
                increment = Integer.parseInt(incrementChoice.substring(0, 2));
            }
        }
        boolean color = true;
        if (colorChoiceString.equals("Black")) {
            color = false;
        } else if (colorChoiceString.equals("Any")) {
            Random random = new Random();

            int nr = random.nextInt(2);
            if (nr == 0) {
                color = true;
            } else if (nr == 1){
                color = false;
            }
        }
        int rated = 0;
        if (ratedChoiceString.equals("Yes")) {
            rated = 1;
        }

        int opponent = 0;
        if (searchField.getText() != null) {
            if (User.getUser_idByUsername(searchField.getText()) != -1) {
                if (USERNAME.equalsIgnoreCase(searchField.getText())) {
                    searchComment.setText("You can't invite yourself!");
                    return;
                } else {
                    opponent = User.getUser_idByUsername(searchField.getText());
                }
            } else {
                MainScene.invitedFriend = false;
                searchComment.setText("User doesn't exist");
                System.out.println("Feil");
                return;
            }
        }

        ChessGame.gameID = Game.newGameID();
        Game.createGame(mode,time, increment, color, rated, opponent);
        System.out.println("Time: " + timeChoice + "\nIncrement: " + incrementChoice + "\nRated: " + ratedChoiceString + "\nColor: " + colorChoiceString);
        window.close();
    }
}