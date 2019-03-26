package Database;

import JavaFX.Login;

public class Game {
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

    public static void setResult(int game_id, int user_id){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();

                db.exUpdate("UPDATE Game SET result = " + user_id + " WHERE game_id = "+game_id + ";");
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
}
