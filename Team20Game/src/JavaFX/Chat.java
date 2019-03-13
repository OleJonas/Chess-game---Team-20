package JavaFX;

import java.sql.*;
import java.util.Timer;
import javafx.application.Application;
import javafx.stage.Stage;
import Database.DBOps;
import java.util.TimerTask;

public class Chat extends Application{
    private DBOps db;
    private Timer refresher;
    private int lastChat;

    public Chat() throws SQLException {
        this.db = new DBOps();
        this.refresher = new Timer(true);
    }

    public void refresh(){
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
        try {
            ResultSet res = db.exQuery("SELECT * FROM chat WHERE chatNr > " + lastChat);

            while (res.next()) {
                lastChat++;
                out.append(res.getString(lastChat) + "\n");
            }
        } catch(SQLException sql){
            sql.printStackTrace();
        }
        return out.toString();
    }

    public void writeChat(String input){
        String writeToDB = "EXECUTE UPDATE INSERT INTO chat VALUES ('" + input + "')";
        db.exUpdate(writeToDB);
    }

    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage primaryStage){
        // Enter code
    }
}
