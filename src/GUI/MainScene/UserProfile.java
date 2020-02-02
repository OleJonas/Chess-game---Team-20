package GUI.MainScene;

import Database.DBOps;
import Database.User;
import GUI.LoginScreen.Login;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import static GUI.LoginScreen.Login.*;

/**
 * <h1>UserProfile</h1>
 * This class is used for setting the "User Profile" scene.
 * @since 08.04.2019
 * @author Team 20
 */


@SuppressWarnings("Duplicates")
class UserProfile{
    static Image avatar;
    static ImageView avatarImageview;

    /**
     * Displays the "User Profile" scene.
     */
    static void showUserProfileScene(){
        User.updateUser();
        updateStats();
        avatar = new Image("Images/Avatars/" + AVATAR);

        //Title

        //GamesInfo
        String gamesInfoString = "User: " + USERNAME + "\nGames Played: " + gamesPlayed
                + "\nGames Won: " + gamesWon + "\nGames Lost: " + gamesLost
                + "\nGames Drawn: " + gamesRemis + "\nElo-rating: " + ELOrating;
        Label gamesInfoLabel = new Label(gamesInfoString);
        gamesInfoLabel.setFont(Font.font("Georgia", 25));
        gamesInfoLabel.setStyle("-fx-font-weight: bold");
        gamesInfoLabel.setTextFill(Color.WHITE);

        //Buttons

        Button nextAvatar = new Button();
        Image imageNextAvatar = new Image("Images/ButtonImages/ArrowRight.png");
        ImageView imageViewNextAvatar = new ImageView(imageNextAvatar);
        imageViewNextAvatar.setFitWidth(30);
        imageViewNextAvatar.setFitHeight(30);
        nextAvatar.setGraphic(imageViewNextAvatar);
        nextAvatar.setOnAction(e -> {
            increaseAvatar();
            avatarImageview.setImage(new Image("Images/Avatars/" + AVATAR));
        });

        Button lastAvatar = new Button();
        Image imageLastAvatar = new Image("Images/ButtonImages/ArrowLeft.png");
        ImageView imageViewLastAvatar = new ImageView(imageLastAvatar);
        imageViewLastAvatar.setFitWidth(30);
        imageViewLastAvatar.setFitHeight(30);
        lastAvatar.setGraphic(imageViewLastAvatar);
        lastAvatar.setOnAction(e -> {
            decreaseAvatar();
            avatarImageview.setImage(new Image("Images/Avatars/" + AVATAR));
        });

        //Right GridPane
        MainScene.rightGrid.getChildren().clear();
        MainScene.rightGrid.setVgap(40);
        MainScene.rightGrid.setPadding(new Insets(50, 200, 100, 250));
        avatarImageview = new ImageView(avatar);
        avatarImageview.setFitHeight(250);
        avatarImageview.setFitWidth(250);
        MainScene.rightGrid.add(avatarImageview, 1, 0, 3, 1);
        Label changeAvatarLabel = new Label("Change avatar");
        changeAvatarLabel.setFont(Font.font("Georgia", 18));
        changeAvatarLabel.setStyle("-fx-font-weight: bold");
        changeAvatarLabel.setTextFill(Color.WHITE);
        MainScene.rightGrid.add(changeAvatarLabel, 1, 1, 3, 1);
        MainScene.rightGrid.setHalignment(changeAvatarLabel, HPos.CENTER);
        MainScene.rightGrid.add(nextAvatar, 1, 1, 3, 1);
        MainScene.rightGrid.setHalignment(nextAvatar, HPos.RIGHT);
        MainScene.rightGrid.add(lastAvatar, 1, 1, 3, 1);
        MainScene.rightGrid.setHalignment(lastAvatar, HPos.LEFT);
        MainScene.rightGrid.add(gamesInfoLabel, 1, 2);

        //Left GridPane
        MainScene.leftGrid.getChildren().clear();
        MainScene.leftGrid.setPadding(new Insets(100, 10, 100, 100));

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setTickLabelFill(Color.WHITE);
        xAxis.setStyle("-fx-font-weight: bold; -fx-color: #FFFFFF;");
        xAxis.setTickLabelGap(1.0);
        yAxis.setTickLabelFill(Color.WHITE);
        yAxis.setStyle("-fx-font-weight: bold;");


        //linechart code inspired by https://docs.oracle.com/javafx/2/charts/line-chart.htm#CIHGBCFI
        final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
        lineChart.setTitle("ELO-history for " + USERNAME);
        XYChart.Series series = new XYChart.Series();
        series.setName("Your official ELO-rating");
        for (int i = 0; i < ELOhistory.size(); i++){
            series.getData().add(new XYChart.Data(i, ELOhistory.get(i)));
        }
        lineChart.getData().add(series);
        MainScene.leftGrid.add(lineChart, 0, 0);
    }

    /**
     * Method for changing avatar.
     */
    static void increaseAvatar(){
        if(AVATAR.equals("avatar1.jpg")) {
            AVATAR = "avatar2.png";
        }else if(AVATAR.equals("avatar2.png")) {
            AVATAR = "avatar3.png";
        }else if(AVATAR.equals("avatar3.png")) {
            AVATAR = "avatar4.gif";
        }else if(AVATAR.equals("avatar4.gif")) {
            AVATAR = "avatar5.png";
        }else if(AVATAR.equals("avatar5.png")) {
            AVATAR = "avatar6.png";
        }else if(AVATAR.equals("avatar6.png")) {
            AVATAR = "avatar7.png";
        }else if(AVATAR.equals("avatar7.png")) {
            AVATAR = "avatar8.png";
        }else if(AVATAR.equals("avatar8.png")) {
            AVATAR = "avatar9.png";
        }else if(AVATAR.equals("avatar9.png")) {
            AVATAR = "avatar10.png";
        } else if(AVATAR.equals("avatar10.png")){
            AVATAR = "avatar1.jpg";
        }
    }

    /**
     * Method for changing avatar.
     */
    static void decreaseAvatar(){
        if(AVATAR.equals("avatar1.jpg")){
            AVATAR = "avatar10.png";
        }else if(AVATAR.equals("avatar2.png")) {
            AVATAR = "avatar1.jpg";
        }else if(AVATAR.equals("avatar3.png")) {
            AVATAR = "avatar2.png";
        }else if(AVATAR.equals("avatar4.gif")) {
            AVATAR = "avatar3.png";
        }else if(AVATAR.equals("avatar5.png")) {
            AVATAR = "avatar4.gif";
        }else if(AVATAR.equals("avatar6.png")) {
            AVATAR = "avatar5.png";
        }else if(AVATAR.equals("avatar7.png")) {
            AVATAR = "avatar6.png";
        }else if(AVATAR.equals("avatar8.png")) {
            AVATAR = "avatar7.png";
        }else if(AVATAR.equals("avatar9.png")) {
            AVATAR = "avatar8.png";
        }else if(AVATAR.equals("avatar10.png")) {
            AVATAR = "avatar9.png";
        }

    }

    /**
     * Method for updating the stats for user.
     */
    static void updateStats(){
        gamesPlayed = User.getGamesPlayed(Login.userID);
        gamesLost = User.getGamesLost(Login.userID);
        gamesWon = User.getGamesWon(Login.userID);
        gamesRemis = User.getGamesRemis(Login.userID);
        ELOrating = User.getElo(Login.userID);
    }

    /**
     * Method for changing avatar.
     * @param avatar Parameter says what avatar to change to.
     */
    static void setAvatar(String avatar){
        AVATAR = avatar;
        DBOps conncetion = new DBOps();
        conncetion.exUpdate("UPDATE User SET avatar = '" + avatar +"' WHERE username = '" + USERNAME + "';");
    }
}
