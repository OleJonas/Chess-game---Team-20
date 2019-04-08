package Database;

import GUI.GameScene.ChessGame;
import GUI.GameScene.GameScene;
import GUI.LoginScreen.Login;
import GUI.MainScene.MainScene;

import java.util.ArrayList;
/**
 * <h1> Game </h1>
 * This classed is used for receiving or manipulating data related to the "game" table from the database
 * @author Team20
 * @since 05.04.2019
 */
public class Game {
    public static String sql;

    /**
    * method to create a game in the database, run in a thread as no feedback is needed from this method.
    * @param mode Parameter to put the correct mode of the game in the database
     * @param time Parameter for the amount of minutes that will be used in the game
     * @param increment Parameter for what increment on every turn the game will be stored to the database
     * @param color Parameter for what color the user puts itself as in the database: true means white and puts the users
     *              user_id into user_id1, false puts it into user_id2
     * @param rated Parameter for if the game will be put in as rated or not in the database
     */
    public static void createGame(int mode, int time, int increment, boolean color, int rated) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps connection = new DBOps();
                if (color) {
                    ChessGame.color = true;
                    connection.exUpdate("INSERT INTO Game VALUES(DEFAULT," + Login.userID + ", null, DEFAULT, " + time + ", " + increment + ", " + rated + ", null, 1, "+mode+");");
                } else {
                    ChessGame.color = false;
                    connection.exUpdate("INSERT INTO Game VALUES(DEFAULT, null, " + Login.userID + ", DEFAULT, " + time + ", " + increment + ", " + rated + ", null, 1, "+mode+");");
                }
            }
        });
        t.start();
    }

    /**
     * Method to upload a move to the "moves" table in the database.
     * Run as a thread as it only has to do a task without returning anything
     * @param fromX Parameter for the x-position on the board the piece moved from
     * @param fromY Parameter for the y-position the board the piece moved from
     * @param toX Parameter for the x-position on the board the piece moved to
     * @param toY Parameter for the y-position on the board the piece moved to
     * @param movenr Parameter for the number of the move to be uploaded to the database
     */
    public static void uploadMove(int fromX, int fromY, int toX, int toY, int movenr){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                GameScene.yourTime += (GameScene.yourTime == 0 ? 0 : GameScene.increment);
                if (ChessGame.color) {
                    if(ChessGame.firstMove){
                        db.exUpdate("INSERT INTO Move VALUES (" + ChessGame.gameID + ", " + (movenr +1) +", "+ fromX +", "+fromY+", "+toX+", "+toY+", "  + GameScene.yourTime +");");
                        ChessGame.firstMove = false;
                        if (GameScene.yourTime != 0) {
                            GameScene.refresh();
                        }
                        System.out.println("started timer in Game class");

                    }

                    else if (Integer.parseInt(db.exQuery("SELECT MAX(movenr) FROM Move WHERE game_id = " +ChessGame.gameID+";", 1).get(0)) % 2 == 0) {
                        db.exUpdate("INSERT INTO Move VALUES (" + ChessGame.gameID + ", " + (movenr +1) +", "+ fromX +", "+fromY+", "+toX+", "+toY+", "  + GameScene.yourTime + ");");
                    }
                }
                else {
                    if ((Integer.parseInt(db.exQuery("SELECT MAX(movenr) FROM Move WHERE game_id = " +ChessGame.gameID+";", 1).get(0)) % 2 == 1)) {
                        db.exUpdate("INSERT INTO Move VALUES (" + ChessGame.gameID + ", " + (movenr +1) +", "+ fromX +", "+fromY+", "+toX+", "+toY+", "  + GameScene.yourTime +");");
                    }
                }
            }
        });
        t.start();
    }

    /**
     * method to create a game in the database where you invite a friend. identical to the linked method, except for
     * the extra ID on "friendid"
     * @see Database.Game#createGame(int mode, int time, int increment, boolean color, int rated)
     *
     * @param friendid Parameter for the id of the friend that is invited to the game
     */

    public static void createGame(int mode, int time, int increment, boolean color, int rated, int friendid) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps connection = new DBOps();
                int userid = Login.getUserID();
                connection.createGame(userid, mode, time, increment, color, rated, friendid);
            }
        });
        t.start();
    }

    /**
     * Method to get the value in the "Time" column from a game in the "Game" table.
     * @param game_id Parameter for the id of the game you want to find the time for.
     * @return the time in minutes for the game.
     */

    public static int getTime(int game_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT time FROM Game WHERE game_id = " + game_id + "; ", 1).get(0));
    }

    /**
     * Method to get the value in the "Increment" column from a game in the "Game" table.
     * @param game_id Parameter for the id of the game you want to find the increment for.
     * @return The increment in seconds for the game.
     */
    public static int getIncrement(int game_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT increment FROM Game WHERE game_id = " + game_id + ";", 1).get(0));
    }

    /**
     * Method to get the value in the "Active" column from a game in the "Game" table.
     * @param game_id Parameter for the id of the game.
     * @return The "Active" value for the game. Active means the creator of the game is still looking for an opponent,
     * not active means the creator is not looking for an opponent anymore
     */

    public static boolean getActive(int game_id){
        DBOps db = new DBOps();
        if (Integer.parseInt(db.exQuery("SELECT active FROM Game WHERE game_id = " + game_id + ";",1).get(0)) == 1){
            return true;
        }
        return false;
    }

    /**
     * Method for getting the value for the column "mode" for a game in the "Game" table.
     * @param game_id game_id for the game you want the information on
     * @return The index for the mode the game is to be played in.
     */

    public static int getMode(int game_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT mode FROM Game WHERE game_id = " + game_id + ";",1).get(0));
    }

    /**
     * Method for getting the value for the column "user_id1" for a game in the "Game" table.
     * @param game_id Parameter for the game you want the information on.
     * @return user_id for white side.
     *
     */
    public static int getUser_id1(int game_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT user_id1 FROM Game WHERE game_id = " + game_id + ";",1).get(0));
    }

    /**
     * Method for getting the value from the column "user_id2" for a game in the "Game" table.
     * @param game_id Parameter for the game you want information on.
     * @return user_id for black side
     */
    public static int getUser_id2(int game_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT user_id2 FROM Game WHERE game_id = " + game_id + ";",1).get(0));
    }
    /**
     * Method for getting the value from the column "user_id2" for a game in the "Game" table.
     * @param game_id Parameter for the game you want information on.
     * @return the result of the game, result is -1 if inconclusive, 1 if white is offering a draw, 2 if black is offering a draw,
     * 0 if game resulted in a draw, or contains the user_id for the winner.
     */
    public static int getResult(int game_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT result FROM Game WHERE game_id = " + game_id + ";",1).get(0));
    }

    /**
     * Method for getting the value from the column "rated" for a game in the "Game" table.
     * @param game_id Parameter for the game you want information on.
     * @return .. an int determining if the games is rated or not, 0 is not rated, 1 is rated.
     */
    public static int getRated(int game_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT rated FROM Game WHERE game_id = " + game_id + ";",1).get(0));
    }

    /**
     * Help-method for #friendInviteInfo that takes in a game_id and creates a string which says everything about the game: mode, time, increment,
     * rated, and color the invited player is playing. this method is used for showing information when receiving an invite
     * to a game. the method checks if user_id1 is filled or not, and decides the color the receiver of the invite is playing
     * based on the result
     * @param game_id Parameter for the id of the game you want to find information about
     * @return a string with all the information about the game.
     */

    public static String getEverythingAboutGame(int game_id){
        String everyThingAboutGame = "";
        DBOps db = new DBOps();
        ArrayList<String> arrayString = db.exQuery("SELECT mode, time, increment, rated, user_id1 FROM Game WHERE game_id = " + game_id + ";",5);
        int mode = Integer.parseInt(arrayString.get(0));
        everyThingAboutGame += "Mode: ";
        if(mode == 0){
            everyThingAboutGame += "Standard";
        }else if(mode == 2){
            everyThingAboutGame += "Horse Attack";
        }else if(mode == 3){
            everyThingAboutGame += "Farmers Chess";
        }else if(mode == 4){
            everyThingAboutGame += "Peasants Revolt";
        }else if(mode >1000){
            everyThingAboutGame += "Fischer Random";
        }
        everyThingAboutGame += "\nYour color: " + (arrayString.get(4) == null?"White": "Black");
        int time = Integer.parseInt(arrayString.get(1));
        everyThingAboutGame += "\nTime: " + (time == 0?"No time":time);
        int increment = Integer.parseInt(arrayString.get(2));
        everyThingAboutGame += "\nIncrement: " + (increment==0?"No increment":increment);
        everyThingAboutGame += "\nRated: "+(Integer.parseInt(arrayString.get(3))==1? "Yes": "No");
        return everyThingAboutGame;
    }

    /**
     * Method to create a list of two strings to be used in a friend-invite. figures out who is inviting and makes a string
     * based on the result, and adds everything about the game as the second string in the list
     * @param game_id Parameter for what game you want information on.
     * @return a list of two strings to be used in a friend invite box.
     */
    public static ArrayList<String> friendInviteInfo(int game_id){
        String a = "";
        DBOps db = new DBOps();
        String friend = db.exQuery("SELECT user_id1 FROM Game WHERE game_id = " + game_id, 1).get(0);
        if(friend!= null){
            a+= User.getUsername(Integer.parseInt(friend)) + " has invited you to a game!";
        }else{
            int friendid = getUser_id2(game_id);
            a += User.getUsername(friendid) + " has invited you to a game!";
        }
        ArrayList<String> b = new ArrayList<>();
        b.add(a);
        b.add(getEverythingAboutGame(game_id));
        return b;
    }

    /**
     * Method to find the ELO of the user playing the white pieces in a chess game.
     * @param game_id Parameter for what game you want to find information on.
     * @return the ELO rating of the player playing the white pieces.
     */

    public static int getWhiteELO(int game_id){
        int user_id1 = getUser_id1(game_id);
        return User.getElo(user_id1);
    }

    /**
     * Method to find the ELO of the user playing the black pieces in a chess game.
     * @param game_id Parameter for what game you want to find information on.
     * @return the ELO rating of the player playing the black pieces.
     */

    public static int getBlackELO(int game_id){
        int user_id2 = getUser_id2(game_id);
        return User.getElo(user_id2);
    }

    /**
     * Method to upload the result of the game to the database.
     * @param game_id Parameter for the game you want to find information on.
     * @param user_id Parameter for the result you want to put into the database.
     */
    public static void setResult(int game_id, int user_id){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                db.exUpdate("UPDATE Game SET result = " + user_id + " WHERE game_id = "+game_id + ";");
            }
        });
        t.start();
    }

    /**
     * Method to turn off the "Active" column from the database with the related game_id, run in a thread,
     * as nothing has to be returned.
     * @param game_id Parameter for the game you want to turn off active.
     */

    public static void setInactiveByGame_id(int game_id){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                db.exUpdate("UPDATE Game SET active = 0 WHERE game_id = " + game_id);
            }
        });
        t.start();
    }

    /**
     * Method to turn off all the active games a user has.
     * @param user_id Parameter of the user you want to turn off all the active games for.
     */
    public static void turnOffAllActiveForUser(int user_id){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                db.exUpdate("UPDATE Game SET active = 0 WHERE user_id = " + user_id);
            }
        });
        t.start();
    }

    /**
     * Method to turn off all the active games the user has, except for the game passed as an argument.
     * @param user_id Parameter for the user you want to turn off the active games for.
     * @param game_id Parameter for the game you dont want to set inactive.
     */
    public static void turnOffAllOtherActiveForUser(int user_id, int game_id){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                db.exUpdate("UPDATE Game SET active = 0 WHERE user_id = " + user_id + " AND game_id != " + game_id);
            }
        });
        t.start();
    }

    /**
     * Method to replace or add to the column "user_id1" of the selected game with a new user_id
     * @param game_id Parameter for the game where you want to set user_id1.
     * @param user_id Parameter for the user_id you want to fill in the column.
     */
    public static void setUser_id1(int game_id, int user_id){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                db.exUpdate("UPDATE Game SET user_id1 = " + user_id + " WHERE game_id = "+ game_id);
            }
        });
        t.start();
    }

    /**
     * The same as the linked method, but for user_id2
     * @see Database.Game#setUser_id1(int, int)
     * @param game_id
     * @param user_id
     */
    public static void setUser_id2(int game_id, int user_id){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                db.exUpdate("UPDATE Game SET user_id2 = " + user_id + " WHERE game_id = "+ game_id);
            }
        });
        t.start();
    }

    /**
     * Method to tell if players are ready by checking if both user_id columns are filled.
     * @param connection Parameter for the connection to be used.
     * @return true if players are ready, false if not.
     */
    private static boolean playersReady(DBOps connection) {
        System.out.println(ChessGame.gameID);
        String sql = "SELECT * FROM Game WHERE user_id1 IS NOT NULL AND user_id2 IS NOT NULL AND game_id = " +ChessGame.gameID +";";
        try {
            if (connection.exQuery(sql, 1).size() > 0) {
                //showGameScene();
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Method to look after a game with the specifications in the sql-string. "polls" the database once.
     * @param sql sql-query looking for a game with specific rules.
     * @param connection Parameter for the database connection to be used.
     * @return the game_id for a game that matches the search, if non-existent, returns -1.
     */
    public static int pollQueue(String sql, DBOps connection) {
        ArrayList<String> opponent = connection.exQuery(sql, 1);
        if (opponent.size() > 0) {
            return Integer.parseInt(opponent.get(0));
        }
        return -1;
    }

    /**
     * Method to "instantiate" a game in the database. checks if players are ready, and
     * turns off active from the game_id the application currently has.
     * readies the game locally if players are ready.
     *
     * @return true if the game is ready and starts the game, returns false if the game is not ready.
     */
    public static boolean startGame(){
        DBOps connection = new DBOps();
        System.out.println("waiting for opponent");
        if(!playersReady(connection)) {
            connection.exUpdate("UPDATE Game SET active = 0 WHERE game_id = " + ChessGame.gameID);
            MainScene.inQueueCreate = false;
            MainScene.inGame = true;
            return true;
        }
        return false;
    }

    /**
     * Method that readies and joins a game, uses method linked to check if a game with the right specifications exist and makes
     * the game locally if found.
     * @see Database.Game#pollQueue(String, DBOps)
     * @return false if game is not found, true if game is found and readied.
     */
    public static boolean joinGame(){
        DBOps connection = new DBOps();
        System.out.println("Looking for opponent");
        int game_id = pollQueue(sql, connection);
        System.out.println(sql);
        if(game_id!=-1) {
            ChessGame.gameID = game_id;
            if (connection.exUpdate("UPDATE Game SET user_id1 = " + Login.userID + " WHERE user_id1 IS NULL AND game_id = " + game_id + ";") == 1) {
                ChessGame.color = true;
            } else {
                connection.exUpdate("UPDATE Game SET user_id2 = " + Login.userID + " WHERE user_id2 IS NULL AND game_id = " + game_id + ";");
                ChessGame.color = false;
            }
            System.out.println("Started game with gameID: " + ChessGame.gameID);
            MainScene.inQueueJoin = false;
            MainScene.inGame = true;
            return true;
        }
        return false;
    }

    /**
     * Method to join a friend's game uses linked method to check if players are ready and sets
     * the booleans needed to start a game
     * @see Database.Game#playersReady(DBOps)
     * @return true if game is readied, false if game is not ready.
     */

    public static boolean joinFriend(){
        DBOps connection = new DBOps();
        System.out.println("waiting for opponent");
        if(!playersReady(connection)) {
            connection.exUpdate("UPDATE Game SET active = 0 WHERE game_id = " + ChessGame.gameID);
            System.out.println("Success!");
            System.out.println("Started game with gameID: " + ChessGame.gameID);
            MainScene.inQueueFriend = false;
            MainScene.inGame = true;
            return true;
        }
        return false;
    }

    /**
     * Method to check if a friend is inviting you. The method uses the linked method to create an SQL string
     * to search for a game where you are invited.
     * @see Game#createSearchFriend(int)
     * @return the game_id of the game you are invited to if found, or -1 if not found.
     */
    public static int searchFriend() {
        DBOps connection = new DBOps();
        sql = createSearchFriend(Login.getUserID());

        int game_id = pollQueue(Game.sql, connection);
        if (game_id != -1) {
            MainScene.searchFriend = false;
            return game_id;
        }
        return -1;
    }

    public static boolean tryAcceptInvite(int game_id){
        DBOps connection = new DBOps();
        ChessGame.gameID = game_id;
        if (getActive(ChessGame.gameID)) {
            if (connection.exUpdate("UPDATE Game SET user_id1 = " + Login.userID + " WHERE user_id1 IS NULL AND game_id = " + game_id + ";") == 1) {
                ChessGame.color = true;
            } else {
                connection.exUpdate("UPDATE Game SET user_id2 = " + Login.userID + " WHERE user_id2 IS NULL AND game_id = " + game_id + ";");
                ChessGame.color = false;
            }
            System.out.println("Started game with gameID: " + ChessGame.gameID);
            MainScene.searchFriend = false;
            MainScene.inGame = true;
            removeActiveFromGame();
            return true;
        }
        return false;
    }

    /**
     * Method to generate a new game_id when a game is created.
     * @return a new game_id to be used when a new game is made.
     */

    public static int newGameID(){
        DBOps connection = new DBOps();
        ArrayList matchingGameIDs = connection.exQuery("SELECT MAX(game_id) FROM Game;", 1); //Change this SQLQuery to match the database
        if (matchingGameIDs.size() == 0) {
            return 1;
        }
        int out = Integer.parseInt((String) matchingGameIDs.get(0));
        return out + 1;
    }

    /**
     * Method used to create an SQL-statement for use in matchmaking.
     * Goes through the passed parameters and outputs a String which depends on aforementioned parameters.
     *
     * @param mode Option selected by user by clicking on an option in the mode-list. Passed by CreateGamePopupBox.tryCreateGame(). Decides game-mode.
     * @param time ... Decides time given to each player, if any. Can be 0. If time = 0, increment can be != 0. This way each player starts with the given increment.
     * @param increment ... Decides increment given to each player. Can be 0. This prevents the players from gaining time by making moves.
     *                  If both time and increment is equal to 0, the game is a "correspondence" game. This mode lasts until a player loses or they agree on a draw.
     * @param color ... The user can choose "any" from the color options. This makes the assignment of this booleans value random.
     *              If color is true, the player is playing white. Color = false makes the player play black.
     *              This parameter also decides if the user starts the game with ChessGame.myTurn = true or false. White makes it true, black makes it false.
     * @param rated ... This parameter decides if the game is rated or not. If false; neither user gains or loses ELO-rating at the end of the game.
     *              If true; the winning player gains ELO and the losing player loses ELO depending on their difference in ELO-rating.
     * @return
     */
    public static String createSearch(int mode, int time, int increment, boolean[] color, int rated) {
        String sql = "SELECT game_id FROM Game";
        boolean firstCheck = true;
        if (mode != -1) {
            if (mode == 1) {
                if (firstCheck) {
                    sql += " WHERE mode > 1000 ";
                    firstCheck = false;
                } else {
                    sql += " AND mode > 1000 ";
                }
            }
            else if (firstCheck) {
                sql += " WHERE mode = " +mode;
                firstCheck = false;
            } else {
                sql += " AND mode = " +mode;
            }
        }
        if (time != -1) {
            if (firstCheck) {
                sql += " WHERE time = " +time;
                firstCheck = false;
            } else {
                sql += " AND time = " +time;
            }
        }
        if (increment != -1) {
            if (firstCheck) {
                sql += " WHERE increment = " +increment;
                firstCheck = false;
            } else {
                sql += " AND increment = " +increment;
            }
        }
        if (color[1]) {
            if (firstCheck) {
                sql += " WHERE(user_id1 IS null OR user_id2 IS null)";
                firstCheck = false;
            } else {
                sql += " AND(user_id1 IS null OR user_id2 IS null)";
            }
        } else {
            if (color[0]) {
                if (firstCheck) {
                    sql += " WHERE user_id2 IS null";
                    firstCheck = false;
                } else {
                    sql += " AND user_id2 IS null";
                }
            } else {
                if (firstCheck) {
                    sql += " WHERE user_id1 IS null";
                    firstCheck = false;
                } else {
                    sql += " AND user_id1 IS null";
                }
            }
        }
        if (rated != -1) {
            if (firstCheck) {
                sql += " WHERE rated = " +rated;
                firstCheck = false;
            } else {
                sql += " AND rated = " +rated;
            }
        }
        sql += " AND active = 1;";
        System.out.println(sql);
        return sql;
    }

    /**
     * This method is used to find a game created by a friend.
     * The user cannot explicitly invoke this method themselves, it is automatically used in the lobby to constantly poll the database for invites from other players.
     * The returned String is used in the method searchFriend();
     * @param userid Parameter containing the users own id. This parameter is used to search for games created with the users own id.
     *               If an active game in the database-table "Game" has a user-id corresponding to this parameter, the user will get a popup via the class {@link FriendInviteBox "FriendInviteBox.class"}
     * @return
     */
    public static String createSearchFriend(int userid) {
        String sql = "SELECT game_id FROM Game WHERE opponent = " + userid + " AND active = 1;";
        return sql;
    }

    /**
     * method to remove the current game from queue, or to set it "inactive".
     * turns off the "active" column for the game_id set in ChessGame.
     */
    public static void removeActiveFromGame(){
        DBOps temp = new DBOps();
        int game_id = ChessGame.gameID;
        temp.exUpdate("UPDATE Game SET active = 0 WHERE game_id = " + game_id + ";");
    }
}
