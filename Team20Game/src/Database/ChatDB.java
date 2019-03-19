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
        /*db.createTable("chat");
        db.exUpdate("ALTER TABLE chat ADD msg_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY;");
        db.exUpdate("ALTER TABLE chat ADD msg VARCHAR(140);");
        db.exUpdate("ALTER TABLE chat DROP def;");*/
        this.refresher = new Timer(true);
        this.user_id = Integer.parseInt(db.exQuery("SELECT user_id FROM User WHERE username = '" + Login.USERNAME + "';", 1).get(0));

    }

    public ArrayList<String> fetchChat(){
        // Might have to change this query later.
        ArrayList<String> out = db.exQuery("SELECT msg FROM Chat WHERE msg_id >= " + lastChat + " AND game_id = " + gameid, 1);
        for(int i = 0; i < out.size(); i++){
            lastChat++;
        }
        //System.out.println(out.toString().trim());
        return out;
    }

    public void chatClose(){
        db.close();
    }

    public void writeChat(String input){
        // Checks if there already are messages corresponding to current game.
        // If not, sends first message of the game, and resumes normal execution;
        if(db.exQuery("SELECT * FROM Chat WHERE game_id = " + gameid,1).size() == 0){
            db.exUpdate("INSERT INTO Chat(game_id, msg_id, user_id, msg) VALUES (" + gameid + ", 1, " + user_id + ", '" + input + "')");
            return;
        }
        String writeToDB = "INSERT INTO Chat VALUES (" + gameid + ", default, " + user_id + ", '" + Login.USERNAME + ": " + input + "')";
        db.exUpdate(writeToDB);
    }

    /*public void initChat(String input){
        db.exUpdate("INSERT INTO Chat VALUES (" + gameid +", 1, " + user_id + ", '" + input + "')");
    }*/
}