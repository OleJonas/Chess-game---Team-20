package JavaFX;

import java.sql.*;
import java.util.ArrayList;
import java.util.Timer;
import javafx.application.Application;
import javafx.stage.Stage;
import Database.DBOps;
import java.util.TimerTask;

public class Chat extends Application{
    private DBOps db;
    private Timer refresher;
    private int lastChat = 1;

    public Chat() {
        this.db = new DBOps();
        db.createTable("chat");
        db.exUpdate("ALTER TABLE chat ADD msg_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY;");
        db.exUpdate("ALTER TABLE chat ADD msg VARCHAR(140);");
        db.exUpdate("ALTER TABLE chat DROP def;");
        this.refresher = new Timer(true);
    }

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
        ArrayList<String> add = db.exQuery("SELECT msg FROM chat WHERE msg_id >= " + lastChat);

        for(String s : add){
            lastChat++;
            out.append(s + "\n");
        }

        System.out.println(out.toString());
        return out.toString();
    }

    public void writeChat(String input){
        String writeToDB = "INSERT INTO chat VALUES (default, '" + input + "')";
        db.exUpdate(writeToDB);
    }

    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage primaryStage){
        // Enter code
    }
}
