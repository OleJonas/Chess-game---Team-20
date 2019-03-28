package Database;

import java.util.ArrayList;

public class LeaderboardDB {
    private DBOps db;
    private String[] leadersName;
    private String[] leadersELO;
    private final String updateString = "SELECT username, ELOrating FROM User ORDER BY ELOrating DESC LIMIT 5;";
    private ArrayList<String> res;

    public LeaderboardDB(){
        db = new DBOps();
        leadersName = new String[5];
        leadersELO = new String[5];
        getLeaderboard();
    }

    public String[] getLeadersELO(){ return leadersELO;}
    public String[] getLeadersName(){ return leadersName;}

    private void getLeaderboard(){
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
