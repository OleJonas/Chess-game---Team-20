package GUI.MainScene;

import Database.LeaderboardDB;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This class is used for setting up the Leaderboard scene.
 * @since 08.04.2019
 * @author Team 20
 */

public class LeaderboardFX {
    private static GridPane gridPane;
    private static LeaderboardDB leaderboard;
    private static Label label, positionLabel, nameLabel, eloLabel;

    /**
     * Method for setting up the Leaderboard.
     * @return Returns a gridpane of the Leaderboard.
     */
    public static GridPane setupLeaderboard(){
        leaderboard = new LeaderboardDB();
        gridPane = new GridPane();
        positionLabel = new Label("Rank");
        nameLabel = new Label("Username");
        eloLabel = new Label("ELO");

        positionLabel.setFont(Font.font("Georgia", 30));
        nameLabel.setFont(Font.font("Georgia", 30));
        eloLabel.setFont(Font.font("Georgia", 30));

        positionLabel.setTextFill(Color.BLACK);
        nameLabel.setTextFill(Color.BLACK);
        eloLabel.setTextFill(Color.BLACK);

        // Setting up main structure of the leaderboard
        gridPane.setVgap(15);
        gridPane.setHgap(15);
        gridPane.add(positionLabel,0,0);
        gridPane.add(nameLabel,1,0);
        gridPane.add(eloLabel,2,0);

        String[] leadersName = leaderboard.getLeadersName();
        String[] leadersELO = leaderboard.getLeadersELO();
        for(int i = 0; i < leadersELO.length; i++){
            Color color;
            if (i == 0){
                color = Color.web("#c1c109",1.0) ;
            } else if (i == 1){
                color = Color.web("#6b6b6b",1.0) ;
            } else if (i == 2){
                color = Color.SADDLEBROWN;
            } else {
                color = Color.BLACK;
            }

            label = new Label(""+ (i+1) + ".");
            label.setTextFill(color);
            label.setFont(Font.font("Georgia", 25));
            gridPane.add(label, 0, i+1);

            label = new Label(leadersName[i]);
            label.setTextFill(color);
            label.setFont(Font.font("Georgia", 25));
            gridPane.add(label,1,i+1);

            label = new Label(leadersELO[i]);
            label.setTextFill(color);
            label.setFont(Font.font("Georgia", 25));
            gridPane.add(label, 2,i+1);
        }
        return gridPane;
    }
}
