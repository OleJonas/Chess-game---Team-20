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

    // AAAAAAAAAAAAAAAAAAA
    public void refreshLoop(){
        int delay = 5000;
        int period = 5000;
        refresher.scheduleAtFixedRate(new TimerTask(){
            public void run(){
                fetchChat();
            }
        }, delay, period);
    }

    public String fetchChat(){
        StringBuilder out = new StringBuilder();
        ArrayList<String> add = db.exQuery("SELECT msg FROM chat WHERE msg_id >= " + lastChat, 1);

        for(String s : add){
            lastChat++;
            out.append(s + "\n");
        }

        System.out.println(out.toString());
        return out.toString().trim();
    }

    public void chatClose(){
        db.close();
    }

    public void writeChat(String input){
        String writeToDB = "INSERT INTO chat VALUES (default, '" + input + "')";
        db.exUpdate(writeToDB);
    }
}
