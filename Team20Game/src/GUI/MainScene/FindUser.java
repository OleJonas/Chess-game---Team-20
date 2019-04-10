package GUI.MainScene;

import Database.DBOps;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.ArrayList;

import static GUI.MainScene.MainScene.mainScene;

/**
 * <h1>FindUser</h1>
 * This class is used for setting the "Find User" scene.
 * @since 08.04.2019
 * @author Team 20
 */

@SuppressWarnings("Duplicates")
class FindUser{
    static Scene findProfileScene;
    static Image findAvatarImage;
    static ImageView findAvatarImageView;
    static Label gamesInfoLabel, searchComment;
    static TextField searchField;
    static String gamesInfoString;

    //This will be changed by SQL-queries
    static String findUser_USERNAME;
    static String findUser_AvatarString;
    static int findUser_gamesPlayed;
    static int findUser_gamesWon;
    static int findUser_gamesLost;
    static int findUser_gamesRemis;
    static int findUser_ELOrating;

    /**
     * Sets the "Find User" scene on main scene.
     */
    static void showFindUserScene(){
        gamesInfoString = "";
        findAvatarImage = new Image("Images/Avatars/emptyAvatar.png");
        findAvatarImageView = new ImageView(findAvatarImage);

        //Buttons
        Button backToMainButton = new Button("Back");
        Image imageBackToMain = new Image("Images/ButtonImages/ArrowLeft.png");
        ImageView imageViewBackToMain = new ImageView(imageBackToMain);
        imageViewBackToMain.setFitWidth(20);
        imageViewBackToMain.setFitHeight(20);
        backToMainButton.setGraphic(imageViewBackToMain);
        backToMainButton.setOnAction(e -> {
            MainScene.leftGrid.getChildren().clear();
            MainScene.leftGrid.setVgap(40);
            MainScene.rightGrid.setPadding(new Insets(40, 150, 20, 0));
            MainScene.leftGrid.setPadding(new Insets(150, 100, 100, 170));
            MainScene.leftGrid.add(MainScene.newGameButton, 0, 0);
            MainScene.leftGrid.setHalignment( MainScene.newGameButton, HPos.CENTER);
            MainScene.leftGrid.add(MainScene.findUserButton, 0, 1);
            MainScene.leftGrid.setHalignment( MainScene.findUserButton, HPos.CENTER);
            MainScene.leftGrid.add( MainScene.userProfileButton, 0, 2);
            MainScene.leftGrid.setHalignment( MainScene.userProfileButton, HPos.CENTER);
            MainScene.leftGrid.add( MainScene.leaderboardButton,0,3);
            MainScene.leftGrid.setHalignment( MainScene.leaderboardButton,HPos.CENTER);
            MainScene.leftGrid.add( MainScene.settingsButton, 0, 4);
            MainScene.leftGrid.setHalignment( MainScene.settingsButton, HPos.CENTER);

            MainScene.rightGrid.getChildren().clear();
            MainScene.reloadSandbox();
        });

        //Right GridPane
        MainScene.rightGrid.setPadding(new Insets(100,0,0,350));
        gamesInfoLabel = new Label(gamesInfoString);
        gamesInfoLabel.setFont(Font.font("Georgia", 25));
        gamesInfoLabel.setStyle("-fx-font-weight: bold");
        gamesInfoLabel.setTextFill(Color.WHITE);
        findAvatarImageView = new ImageView(findAvatarImage);
        findAvatarImageView.setFitHeight(250);
        findAvatarImageView.setFitWidth(250);
        MainScene.rightGrid.add(findAvatarImageView, 0, 0);
        MainScene.rightGrid.add(gamesInfoLabel, 0, 1);

        //Left GridPane
        MainScene.leftGrid.setPadding(new Insets(20, 0, 0, 20));
        MainScene.leftGrid.setHgap(10);
        MainScene.leftGrid.setVgap(10);
        Label usernameLabel = new Label("Username: ");
        usernameLabel.setFont(Font.font("Georgia", 30));
        usernameLabel.setStyle("-fx-font-weight: bold");
        usernameLabel.setTextFill(Color.WHITE);
        searchField = new TextField();
        searchField.setPrefSize(200, 30);
        searchComment = new Label("");
        searchComment.setTextFill(Color.RED);
        Button searchButton = new Button("Search");
        searchButton.setPrefSize(100, 30);;
        searchButton.setOnAction(e -> {
            searchButtonPressed();
        });

        mainScene.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER)){
                searchButtonPressed();
            }
        });

        MainScene.leftGrid.add(usernameLabel, 1,1,2,1);
        MainScene.leftGrid.add(searchField, 1, 2);
        MainScene.leftGrid.add(searchButton, 3, 2);
        MainScene.leftGrid.add(searchComment, 1, 3, 2, 1);
        searchField.requestFocus();
    }

    /**
     * Adds info and the avatar of the user.
     */
    static void setUserPane(){
        gamesInfoString = "User: " + findUser_USERNAME + "\nGames Played: " + findUser_gamesPlayed
                + "\nGames Won: " + findUser_gamesWon + "\nGames Lost: " + findUser_gamesLost +
                "\nGames Drawn: " + findUser_gamesRemis + "\nElo-rating: " + findUser_ELOrating;
        gamesInfoLabel.setText(gamesInfoString);
        findAvatarImageView.setImage(new Image("Images/Avatars/" + findUser_AvatarString));
    }

    /**
     * Method for searching for a user.
     * @param username Username input from the textbox.
     * @return Returns true if user is found and false if it is not.
     */
    static boolean searchForUser(String username){
        DBOps connection = new DBOps();
        ArrayList<String> result = connection.findUser(username,6);
        if(result.size() > 0) {
            findUser_USERNAME = username;
            findUser_AvatarString = result.get(0);
            findUser_gamesPlayed = Integer.parseInt(result.get(1));
            findUser_gamesWon = Integer.parseInt(result.get(2));
            findUser_gamesLost = Integer.parseInt(result.get(3));
            findUser_gamesRemis = Integer.parseInt(result.get(4));
            findUser_ELOrating = Integer.parseInt(result.get(5));
            return true;
        }
        return false;
    }

    /**
     * Method for what happens when you press the search button.
     */
    static void searchButtonPressed(){
        if(searchForUser(searchField.getText())){
            setUserPane();
            searchComment.setText("");
        } else {
            searchComment.setText("User doesn't exist");
            findUser_AvatarString = "emptyAvatar.png";
            gamesInfoLabel.setText("");
            findAvatarImageView.setImage(new Image("Images/Avatars/" + findUser_AvatarString));
        }
    }
}