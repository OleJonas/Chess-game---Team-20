package Database;

import JavaFX.ChessGame;
import JavaFX.GameScene;
import JavaFX.Login;
import JavaFX.MainScene;

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
                GameScene.yourTime += GameScene.increment;
                if (ChessGame.color) {
                    if(ChessGame.firstMove){
                        db.exUpdate("INSERT INTO Move VALUES (" + ChessGame.gameID + ", " + (movenr +1) +", "+ fromX +", "+fromY+", "+toX+", "+toY+", "  + GameScene.yourTime +");");
                        ChessGame.firstMove = false;
                        GameScene.refresh();
                        System.out.println("started timer in Game class");
                    }

                    else if (Integer.parseInt(db.exQuery("SELECT MAX(movenr) FROM Move WHERE game_id = " +ChessGame.gameID+";", 1).get(0)) % 2 == 0) {
                        db.exUpdate("INSERT INTO Move VALUES (" + ChessGame.gameID + ", " + (movenr +1) +", "+ fromX +", "+fromY+", "+toX+", "+toY+", "  + GameScene.yourTime + ");");
                        System.out.println("INSERT INTO Move VALUES (" + ChessGame.gameID + ", " + (movenr +1) +", "+ fromX +", "+fromY+", "+toX+", "+toY+");");
                    }
                }
                else {
                    if ((Integer.parseInt(db.exQuery("SELECT MAX(movenr) FROM Move WHERE game_id = " +ChessGame.gameID+";", 1).get(0)) % 2 == 1)) {
                        db.exUpdate("INSERT INTO Move VALUES (" + ChessGame.gameID + ", " + (movenr +1) +", "+ fromX +", "+fromY+", "+toX+", "+toY+", "  + GameScene.yourTime +");");
                        System.out.println("INSERT INTO Move VALUES (" + ChessGame.gameID + ", " + (movenr +1) +", "+ fromX +", "+fromY+", "+toX+", "+toY+");");
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

                if (color) {
                    connection.exUpdate("INSERT INTO Game VALUES(DEFAULT," + userid + ", null, DEFAULT, " + time + ", " + increment + ", " + rated + ", " + friendid + ", 1, "+mode+");");
                } else {
                    connection.exUpdate("INSERT INTO Game VALUES(DEFAULT, null, " + userid + ", DEFAULT, " + time + ", " + increment + ", " + rated + ", " + friendid + ", 1, "+mode+");");
                }
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
        everyThingAboutGame += "\nYour chess color: " + (arrayString.get(4) == null?"White": "Black");
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
            //System.out.println("Success!");
            //System.out.println("Started game with gameID: " + ChessGame.gameID);
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
            return true;
        }
        return false;
    }

    public static int searchFriend() {
        DBOps connection = new DBOps();
        sql = createSearchFriend(Login.getUserID());
        System.out.println(sql);
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

    public static String createSearchFriend(int friendid) {
        String sql = "SELECT game_id FROM Game WHERE opponent = " +friendid + " AND active = 1;";
        return sql;
    }

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

    public static void removeActiveFromGame(){
        DBOps temp = new DBOps();
        int game_id = ChessGame.gameID;
        temp.exUpdate("UPDATE Game SET active = 0 WHERE game_id = " + game_id + ";");
    }
}
