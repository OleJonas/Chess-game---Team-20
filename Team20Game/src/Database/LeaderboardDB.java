package Database;

import java.util.ArrayList;

/**
 * <h1>LeaderboardDB</h1>
 * This class fetches stats for the top 5 players for use in the JavaFX part of the leaderboard.
 * @since 05.04.2019
 * @author Team 20
 */

public class LeaderboardDB {
    private DBOps db;
    private String[] leadersName;
    private String[] leadersELO;
    private final String updateString = "SELECT username, ELOrating FROM User ORDER BY ELOrating DESC LIMIT 5;";
    private ArrayList<String> res;

    /**
     * Constructor that creates a new DBOps object to fetch data from the database.
     * Also creates two arrays for the names and ELO-ratings of the top-5 users.
     * Lastly it calls the {@link LeaderboardDB#initLeaderboard()} method to populate the arrays.
     */
    public LeaderboardDB(){
        db = new DBOps();
        leadersName = new String[5];
        leadersELO = new String[5];
        initLeaderboard();
    }

    /**
     * Method to access the leadersELO[] array.
     * @return returns the leadersElo[] array populated with each top-5 members ELO-rating.
     */
    public String[] getLeadersELO(){ return leadersELO;}

    /**
     * Functions the same way as {@link LeaderboardDB#getLeadersELO()}, the only difference being the contents.
     * @return This method returns an array populated with the top-5 members usernames.
     */
    public String[] getLeadersName(){ return leadersName;}

    /**
     * This method populates the leadersELO[] and leadersName[] arrays by calling the {@link DBOps#exQuery(String, int)} method and iterating through it.
     * The way exQuery() functions makes it so that every second entry in the result-set is a username, and every odd number entry is an ELO-rating.
     * To account for this we set entry i/2 in either array equal to res.get(i) depending on i being an even number or not.
     * If it is; the value is inserted into leadersName[i/2], if not; it is inserted into leadersElo[i/2].
     */
    private void initLeaderboard(){
        res = db.exQuery(updateString, 2);

        for(int i = 0; i < res.size(); i++){
            if(i%2 == 0) {
                leadersName[i/2] = res.get(i);
            } else{
                leadersELO[i/2] = res.get(i);
            }
        }
    }
}
