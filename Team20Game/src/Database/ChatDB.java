package Database;

import java.util.ArrayList;
import JavaFX.GameScene.ChessGame;
import JavaFX.LoginScreen.Login;

public class ChatDB{
    private DBOps db;
    private int lastChat = 0;
    private int gameid = ChessGame.gameID;

    public ChatDB() {
        this.db = new DBOps();
    }

    public ArrayList<String> fetchChat(){
        ArrayList<String> out = db.exQuery("SELECT msg FROM Chat WHERE msg_id > " + lastChat + " AND game_id = " + gameid, 1);
        if(out.size() > 0){
            this.lastChat++;
        }
        return out;
    }

    private void writeChat(String input){
        // Checks if there already are messages corresponding to current game.
        // If not, sends first message of the game, and resumes normal execution;
        String msg = Login.USERNAME + ": " + input;
        if(db.exQuery("SELECT * FROM Chat WHERE game_id = " + gameid,1).size() == 0){
            db.writeFirst(gameid, Login.userID, msg);
            return;
        }
        db.writeChat(gameid, lastChat+1, Login.userID, msg);
    }

    public void writeThreadChat(String input) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                writeChat(input);
            }
        });
        t.start();
    }
}