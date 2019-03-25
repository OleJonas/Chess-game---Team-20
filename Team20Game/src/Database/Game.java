package Database;

import JavaFX.Login;

public class Game {
    public static void setResult(int game_id, int user_id){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                db.exUpdate("UPDATE Game SET result = " + user_id + "WHERE game_id = "+ game_id);
            }
        });
        t.start();
    }
    public static void setUser_id1(int game_id, int user_id){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                db.exUpdate("UPDATE Game SET user_id1 = " + user_id + "WHERE game_id = "+ game_id);
            }
        });
        t.start();
    }
    public static void setUser_id2(int game_id, int user_id){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                db.exUpdate("UPDATE Game SET user_id2 = " + user_id + "WHERE game_id = "+ game_id);
            }
        });
        t.start();
    }
}
