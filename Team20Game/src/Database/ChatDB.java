package Database;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import JavaFX.ChessGame;
import JavaFX.Login;

public class ChatDB{
    private DBOps db;
    private Timer refresher;
    private int lastChat = 1;
    private int gameid = ChessGame.gameID;
    private int user_id;

    public ChatDB() {
        this.db = new DBOps();
        this.refresher = new Timer(true);
        this.user_id = Integer.parseInt(db.exQuery("SELECT user_id FROM User WHERE username = '" + Login.USERNAME + "';", 1).get(0));
    }

    public ArrayList<String> fetchChat(){
        // Might have to change this query later.
        ArrayList<String> out = db.exQuery("SELECT msg FROM Chat WHERE msg_id > " + lastChat + " AND game_id = " + gameid + " LIMIT 1;", 1);
        if(out.size() > 0){
            this.lastChat++;
        }
        return out;
    }

    public void chatClose(){
        db.close();
    }

    private void writeChat(String input){
        // Checks if there already are messages corresponding to current game.
        // If not, sends first message of the game, and resumes normal execution;
        if(db.exQuery("SELECT * FROM Chat WHERE game_id = " + gameid,1).size() == 0){
            db.exUpdate("INSERT INTO Chat(game_id, msg_id, user_id, msg) VALUES (" + gameid + ", 1, " + user_id + ", '" + Login.USERNAME + ": " + input + "')");
            return;
        }
        String writeToDB = "INSERT INTO Chat VALUES (" + gameid + ", default, " + user_id + ", '" + Login.USERNAME + ": " + input + "')";
        db.exUpdate(writeToDB);
    }

    public void writeThreadChat(String input) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                writeChat(input);
            }
        });
        t.start();
    }

    /*public void initChat(String input){
        db.exUpdate("INSERT INTO Chat VALUES (" + gameid +", 1, " + user_id + ", '" + input + "')");
    }*/
}