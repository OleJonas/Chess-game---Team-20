package Database;

import JavaFX.GameScene.ChessGame;
import JavaFX.GameScene.GameScene;
import JavaFX.LoginScreen.Login;
import JavaFX.MainScene.MainScene;

import java.util.ArrayList;

public class Game {
    public static String sql;

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

    public static int getTime(int game_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT time FROM Game WHERE game_id = " + game_id + "; ", 1).get(0));
    }
    public static int getIncrement(int game_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT increment FROM Game WHERE game_id = " + game_id + ";", 1).get(0));
    }

    public static boolean getActive(int game_id){
        DBOps db = new DBOps();
        if (Integer.parseInt(db.exQuery("SELECT active FROM Game WHERE game_id = " + game_id + ";",1).get(0)) == 1){
            return true;
        }
        return false;
    }

    public static int getMode(int game_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT mode FROM Game WHERE game_id = " + game_id + ";",1).get(0));
    }

    public static int getUser_id1(int game_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT user_id1 FROM Game WHERE game_id = " + game_id + ";",1).get(0));
    }
    public static int getUser_id2(int game_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT user_id2 FROM Game WHERE game_id = " + game_id + ";",1).get(0));
    }

    public static int getResult(int game_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT result FROM Game WHERE game_id = " + game_id + ";",1).get(0));
    }

    public static int getRated(int game_id){
        DBOps db = new DBOps();
        return Integer.parseInt(db.exQuery("SELECT rated FROM Game WHERE game_id = " + game_id + ";",1).get(0));
    }

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

    public static int getWhiteELO(int game_id){
        int user_id1 = getUser_id1(game_id);
        return User.getElo(user_id1);
    }

    public static int getBlackELO(int game_id){
        int user_id2 = getUser_id2(game_id);
        return User.getElo(user_id2);
    }
    public static void setResult(int game_id, int user_id){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                db.exUpdate("UPDATE Game SET result = " + user_id + " WHERE game_id = "+game_id + ";");
            }
        });
        t.start();
    }

    public static void setInactiveByGame_id(int game_id){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                db.exUpdate("UPDATE Game SET active = 0 WHERE game_id = " + game_id);
            }
        });
        t.start();
    }

    public static void turnOffAllActiveForUser(int user_id){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                db.exUpdate("UPDATE Game SET active = 0 WHERE user_id = " + user_id);
            }
        });
        t.start();
    }

    public static void turnOffAllOtherActiveForUser(int user_id, int game_id){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                db.exUpdate("UPDATE Game SET active = 0 WHERE user_id = " + user_id + " AND game_id != " + game_id);
            }
        });
        t.start();
    }
    public static void setUser_id1(int game_id, int user_id){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                db.exUpdate("UPDATE Game SET user_id1 = " + user_id + " WHERE game_id = "+ game_id);
            }
        });
        t.start();
    }
    public static void setUser_id2(int game_id, int user_id){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                db.exUpdate("UPDATE Game SET user_id2 = " + user_id + " WHERE game_id = "+ game_id);
            }
        });
        t.start();
    }

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

    public static int pollQueue(String sql, DBOps connection) {
        ArrayList<String> opponent = connection.exQuery(sql, 1);
        if (opponent.size() > 0) {
            return Integer.parseInt(opponent.get(0));
        }
        return -1;
    }

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

    public static void removeActiveFromGame(){
        DBOps temp = new DBOps();
        int game_id = ChessGame.gameID;
        temp.exUpdate("UPDATE Game SET active = 0 WHERE game_id = " + game_id + ";");
    }
}
