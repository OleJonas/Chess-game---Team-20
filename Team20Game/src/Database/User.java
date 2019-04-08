package Database;

import GUI.LoginScreen.Login;
import Game.GameEngine;

import java.util.ArrayList;

/**
 * <h1> User </h1>
 * This classed is used for receiving or manipulating data related to the "User" table from the database
 * @author Team20
 * @since 08.04.2019
 */
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
     *Method for getting the "Username" column from the table "User" with the corresponding user_id in the database.
     * @param user_id Parameter for the user_id you want to find the username for.
     * @return a username the user_id is linked to in the database.
     */

    public static String getUsername(int user_id){
        DBOps db = new DBOps();
        return db.exQuery("SELECT username FROM User WHERE user_id = " + user_id+ "; ", 1).get(0);
    }

    /**
     *Method to get the value in the "ELOrating" column from the corresponding user_id in the database.
     * @param user_id Parameter for the user_id you want to find the related ELOrating for.
     * @return the value for the ELOrating corresponding with the user_id in the database.
     */
    public static int getElo(int user_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT ELOrating FROM User WHERE user_id = " + user_id+ "; ", 1).get(0));
    }

    /**
     *Method for getting the value from a row in the "gamesPlayed" column from the table "User" in the database.
     * @param user_id Parameter for what user_id to find the information on.
     * @return the value in the column "gamesPlayed" in the "User" table that corresponds with th user_id.
     */
    public static int getGamesPlayed(int user_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT gamesPlayed FROM User WHERE user_id = " + user_id, 1).get(0));
    }

    /**
     *Method for getting the value from a row in the "gamesWon" column from the table "User" in the database.
     * @param user_id Parameter for what user_id to find the information on.
     * @return the value in the column "gamesWon" in the "User" table that corresponds with th user_id.
     */

    public static int getGamesWon(int user_id){
            DBOps db = new DBOps();
            return Integer.parseInt(db.exQuery("SELECT gamesWon FROM User WHERE user_id = " + user_id, 1).get(0));
    }
    /**
     *Method for getting the value from a row in the "gamesLost" column from the table "User" in the database.
     * @param user_id Parameter for what user_id to find the information on.
     * @return the value in the column "gamesLost" in the "User" table that corresponds with th user_id.
     */
    public static int getGamesLost(int user_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT gamesLost FROM User WHERE user_id = " + user_id, 1).get(0));
    }
    /**
     *Method for getting the value from a row in the "gamesRemis" column from the table "User" in the database.
     * @param user_id Parameter for what user_id to find the information on.
     * @return the value in the column "gamesRemis" in the "User" table that corresponds with th user_id.
     */
    public static int getGamesRemis(int user_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT gamesRemis FROM User WHERE user_id = " + user_id, 1).get(0));
    }

    /**
     * Method that calculates games played and updates the column "Gamesplayed" in the database, for the user that is logged in,
     * using the "Game" table, counting the finished games the logged in player has played and updating the column for "games
     * played" based on the outcome.
     */
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
    /**
     * Method that calculates games won by checking how many games that has the logged in user's user_id
     * in the "result" column. the method then updates the "User" table with the amount of games won.
     */
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

    /**
     * method that counts all the games the logged in user's user_id is in either column "user_id1" or "user_id2"
     * and where the "result" column does not contain it's own user_id, -1(inconclusive)And 0(draw).
     * The method then updates the "gamesLost" column in the "User" table in the database with the result of the query.
     */
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

    /**
     * Method that counts the amount of games drawn by counting all the games the user was involved in, then counting how many
     * rows have the value "0" in the column "result".
     * The method then updates the "User" table for the logged in user's user_id with the result of the count.
     */

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

    /**
     * runs all the linked methods to update the userStatistics.
     * @see User#updateGamesPlayed()
     * @see User#updateGamesLost()
     * @see User#updateGamesWon()
     * @see User#updateGamesRemis()
     */

    public static void updateUser(){
        updateGamesPlayed();
        updateGamesLost();
        updateGamesWon();
        updateGamesRemis();
    }

    /**
     * method that updates the elo of one user in the table "User" with a new value for the ELOrating.
     * @param user_id the user_id for the user's ELOrating you want to update.
     * @param elo Parameter for the new value that should be stored in the "ELOrating" column for the user.
     */

    public static void updateElo(int user_id, int elo){
                DBOps db = new DBOps();
                db.exUpdate("UPDATE User SET ELOrating = " + elo + " WHERE user_id = " + user_id + ";");
                db.exUpdate("INSERT INTO userElo VALUES(" + user_id + ", DEFAULT, " +  elo + ");");
                System.out.println("updated ELO for user " + user_id);
    }

    /**
     * Method for updating the ELO rating of the two users involved in a finished game.
     * the method takes the ELO of the two users in the game specified and the result of the game,
     * uses the getELO() method in GameEngine to calculate two new ELO values. The method then updates both user's ELOrating
     * in the "User" table with the new ELO values if the "rated" column's value in the game = 1.
     * @param game_id Parameter for the game you want to update ELO-ratings for.
     */
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
