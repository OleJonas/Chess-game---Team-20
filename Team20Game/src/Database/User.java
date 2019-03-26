package Database;

import JavaFX.Login;

public class User {

    public static void getElo(int user_id){

    }

    public static void updateGamesPlayed(){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                int gamesPlayed = Integer.parseInt(db.exQuery("SELECT COUNT(game_id) FROM Game WHERE (user_id1 = "
                        + Login.userID +" OR user_id2 = " + Login.userID+ ") AND result IS NOT NULL;",1).get(0));
                db.exUpdate("UPDATE User SET gamesPlayed = " + gamesPlayed + " WHERE user_id = " + Login.userID);
            }
        });
        t.start();
    }

    public static void updateGamesWon(){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                int gamesWon = Integer.parseInt(db.exQuery("SELECT COUNT(game_id) FROM Game WHERE result ="
                        + Login.userID + ";",1).get(0));
                db.exUpdate("UPDATE User SET gamesWon = " + gamesWon + " WHERE user_id = " + Login.userID);
            }
        });
        t.start();
    }
    public static void updateGamesLost(){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                int gamesLost = Integer.parseInt(db.exQuery("SELECT COUNT(game_id) FROM Game WHERE (user_id1 = "
                        + Login.userID +" OR user_id2 = " + Login.userID+ ") AND result != " + Login.userID,1).get(0));
                db.exUpdate("UPDATE User SET gamesLost = " + gamesLost + " WHERE user_id = " + Login.userID);
            }
        });
        t.start();
    }

    public static void updateUser(){
        updateGamesPlayed();
        updateGamesLost();
        updateGamesWon();
    }

    public static void updateElo(int user_id, int elo){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                db.exUpdate("UPDATE User SET ELOrating = " + elo + "WHERE user_id = " + user_id);
            }
        });
        t.start();
    }
    public static void updateElobyGame(int game_id) {
        int user_id1 = Game.getUser_id1(game_id);
        int user_id2 = Game.getUser_id2(game_id);

    }
}
