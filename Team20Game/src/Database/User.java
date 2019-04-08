package Database;

import GUI.LoginScreen.Login;
import Game.GameEngine;

import java.util.ArrayList;

public class User {

    /**
     * Method that finds the username related to a user_id in the database
     * @param username Parameter with the username you want to find the user_id for.
     * @return the user_id related to the username parameter, returns -1 if it does not exist
     */
    public static int getUser_idByUsername(String username){
        DBOps db = new DBOps();
        int id = -1;
        ArrayList<String> user_id = db.exQuery("SELECT user_id FROM User WHERE username = '" + username+ "';", 1);
        if (user_id.size() > 0) {
            if (user_id.get(0) != null) {
                id = Integer.parseInt(user_id.get(0));
            }
        }
        return id;
    }

    /**
     *Method for
     * @param user_id
     * @return
     */

    public static String getUsername(int user_id){
        DBOps db = new DBOps();
        return db.exQuery("SELECT username FROM User WHERE user_id = " + user_id+ "; ", 1).get(0);
    }
    public static int getElo(int user_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT ELOrating FROM User WHERE user_id = " + user_id+ "; ", 1).get(0));
    }

    public static int getGamesPlayed(int user_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT gamesPlayed FROM User WHERE user_id = " + user_id, 1).get(0));
    }

    public static int getGamesWon(int user_id){
            DBOps db = new DBOps();
            return Integer.parseInt(db.exQuery("SELECT gamesWon FROM User WHERE user_id = " + user_id, 1).get(0));
    }
    public static int getGamesLost(int user_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT gamesLost FROM User WHERE user_id = " + user_id, 1).get(0));
    }

    public static int getGamesRemis(int user_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT gamesRemis FROM User WHERE user_id = " + user_id, 1).get(0));
    }
    public static void updateGamesPlayed(){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                int gamesPlayed = Integer.parseInt(db.exQuery("SELECT COUNT(game_id) FROM Game WHERE (user_id1 = "
                        + Login.userID +" OR user_id2 = " + Login.userID+ ") AND result != -1;",1).get(0));
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
                        + Login.userID +" OR user_id2 = " + Login.userID+ ") AND result != " + Login.userID + " AND result != -1 AND result != 0",1).get(0));
                db.exUpdate("UPDATE User SET gamesLost = " + gamesLost + " WHERE user_id = " + Login.userID);
            }
        });
        t.start();
    }

    public static void updateGamesRemis(){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                int gamesRemis = Integer.parseInt(db.exQuery("SELECT COUNT(game_id) FROM Game WHERE (user_id1 = "
                        + Login.userID +" OR user_id2 = " + Login.userID+ ") AND result = 0",1).get(0));
                db.exUpdate("UPDATE User SET gamesRemis = " + gamesRemis + " WHERE user_id = " + Login.userID);
            }
        });
        t.start();
    }

    public static void updateUser(){
        updateGamesPlayed();
        updateGamesLost();
        updateGamesWon();
        updateGamesRemis();
    }

    public static void updateElo(int user_id, int elo){
                DBOps db = new DBOps();
                db.exUpdate("UPDATE User SET ELOrating = " + elo + " WHERE user_id = " + user_id + ";");
                db.exUpdate("INSERT INTO userElo VALUES(" + user_id + ", DEFAULT, " +  elo + ");");
                System.out.println("updated ELO for user " + user_id);
    }
    public static void updateEloByGame(int game_id) {
        System.out.println("started updating ELO");

        Thread t = new Thread(new Runnable() {
            int user_id1 = Game.getUser_id1(game_id);
            int user_id2 = Game.getUser_id2(game_id);
            int result = Game.getResult(game_id);
            int rated = Game.getRated(game_id);

            public void run() {

                int ELOuser1 = getElo(user_id1);
                int ELOuser2 = getElo(user_id2);
                int a = 0;
                if(result == user_id2){
                    a = 1;
                }else if(result == 0){
                    a = 2;
                }
                int [] elo = GameEngine.getElo(ELOuser1, ELOuser2, a);
                System.out.println(" white oldELO: "+ ELOuser1+ " white NewELO"+ elo[0] + " black oldELO: "+ ELOuser2 + " black newELO" + elo[1] +
                        "\nResult: " + a);
                if (rated == 1) {
                    updateElo(user_id1, elo[0]);
                    updateElo(user_id2, elo[1]);
                }


            }
        });
        t.start();
    }
}
