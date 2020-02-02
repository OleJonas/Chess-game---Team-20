package Database;

import java.util.ArrayList;
import GUI.GameScene.ChatFX;
import GUI.GameScene.ChessGame;
import GUI.LoginScreen.Login;

/**
 * <h1>ChatDB/h1>
 * The purpose of this class is to provide methods for communication via the in-game chat.
 * The class uses methods from the DBOps class.
 * @See DBOps
 * @since 08.04.2019
 * @author Team 20
 */

public class ChatDB{
    private DBOps db;
    private int lastChat = 0;
    private int gameid = ChessGame.gameID;

    /**
     * Constructor making a new DBOps object.
     * @See Database.DBOps#DBOps()
     */
    public ChatDB() {
        this.db = new DBOps();
    }

    /**
     * Method to fetch messages based on which messages that have already been received.
     * This way we just need to keep track of a single integer and row instead of listing out the entire table.
     * @return returns an ArrayList<String> for use in the JavaFX part of the chat. {@link ChatFX#refreshChat()}
     */
    public ArrayList<String> fetchChat(){
        ArrayList<String> out = db.exQuery("SELECT msg FROM Chat WHERE msg_id > " + lastChat + " AND game_id = " + gameid, 1);
        if(out.size() > 0){
            this.lastChat++;
        }
        return out;
    }

    /**
     * Method for writing messages to the database. Checks if there are any previous messages from a game with the corresponding game-id.
     * If not; make the incoming message the first message of the game. This is to make every message associated with a specific game-id.
     * Both the {@link DBOps#writeFirst(int, int, String)} method and the {@link DBOps#writeChat(int, int, int, String)} uses prepared statements --
     * -- to prevent SQL-injection attacks.
     * @param input String input from the user
     */
    private void writeChat(String input){
        String msg = Login.USERNAME + ": " + input;
        if(db.exQuery("SELECT * FROM Chat WHERE game_id = " + gameid,1).size() == 0){
            db.writeFirst(gameid, Login.userID, msg);
            return;
        }
        db.writeChat(gameid, lastChat+1, Login.userID, msg);
    }

    /**
     * Creates a thread to write messages in parallel with normal game execution. This prevents lag caused by database calls.
     * @param input Same input as in {@link ChatDB#writeChat(String)}
     */
    public void writeThreadChat(String input) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                writeChat(input);
            }
        });
        t.start();
    }
}