package JavaFX;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import Database.LeaderboardDB;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LeaderboardFX {
    private static GridPane gridPane;
    private static LeaderboardDB leaderboard;
    private static Label label, positionLabel, nameLabel, eloLabel;

    public static GridPane setupLeaderboard(){
        leaderboard = new LeaderboardDB();
        gridPane = new GridPane();
        positionLabel = new Label("Rank");
        nameLabel = new Label("Username");
        eloLabel = new Label("ELO-rating");

        positionLabel.setFont(Font.font("Georgia", 30));
        nameLabel.setFont(Font.font("Georgia", 30));
        eloLabel.setFont(Font.font("Georgia", 30));

        positionLabel.setTextFill(Color.WHITE);
        nameLabel.setTextFill(Color.WHITE);
        eloLabel.setTextFill(Color.WHITE);

        // Setting up main structure of the leaderboard
        gridPane.setVgap(15);
        gridPane.setHgap(15);
        gridPane.add(positionLabel,0,0);
        gridPane.add(nameLabel,1,0);
        gridPane.add(eloLabel,2,0);

        String[] leadersName = leaderboard.getLeadersName();
        String[] leadersELO = leaderboard.getLeadersELO();
        for(int i = 0; i < leadersELO.length; i++){
            label = new Label(""+ (i+1) + ".");
            label.setTextFill(Color.WHITE);
            label.setFont(Font.font("Georgia", 30));
            gridPane.add(label, 0, i+1);

            label = new Label(leadersName[i]);
            label.setTextFill(Color.WHITE);
            label.setFont(Font.font("Georgia", 30));
            gridPane.add(label,1,i+1);

            label = new Label(leadersELO[i]);
            label.setTextFill(Color.WHITE);
            label.setFont(Font.font("Georgia", 30));
            gridPane.add(label, 2,i+1);
        }
        return gridPane;
    }
}
