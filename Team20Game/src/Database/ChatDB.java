package Database;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ChatDB{
    private DBOps db;
    private Timer refresher;
    private int lastChat = 1;

    public ChatDB() {
        this.db = new DBOps();
        db.createTable("chat");
        db.exUpdate("ALTER TABLE chat ADD msg_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY;");
        db.exUpdate("ALTER TABLE chat ADD msg VARCHAR(140);");
        db.exUpdate("ALTER TABLE chat DROP def;");
        this.refresher = new Timer(true);
    }
*/
    /*public void refreshLoop(){
        int delay = 5000;
        int period = 5000;
        refresher.scheduleAtFixedRate(new TimerTask(){
            String chatOut = null;
            public void run(){
                chatOut = fetchChat();
                if(chatOut != "" && chatOut != " " && chatOut != "\n" && chatOut != null) {
                    System.out.println(chatOut);
                }
            }
        }, delay, period);
    }*/

   /* public ArrayList<String> fetchChat(){
        ArrayList<String> out = db.exQuery("SELECT msg FROM chat WHERE msg_id >= " + lastChat, 1);
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
        String writeToDB = "INSERT INTO chat VALUES (default, '" + input + "')";
        db.exUpdate(writeToDB);
    }
}
