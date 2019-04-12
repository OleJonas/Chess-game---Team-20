package GUI.GameScene;
import Database.ChatDB;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

/**
 * <h1>ChatFX</h1>
 * The purpose of this class is to create the JavaFX for chatting with other players.
 * @since 05.04.2019
 * @author Team 20
 */

public class ChatFX{
    static Scene scene;
    static TextField inText;
    static Button sendButton, closeButton;
    static Timer timer = new Timer(true);
    private static ArrayList<Label> messages = new ArrayList<Label>();
    //static FlowPane chatLayout;
    static VBox chatLayout;
    //static JScrollPane container = new JScrollPane();
    static ScrollPane container = new ScrollPane();
    static ChatDB chat = new ChatDB();

    /**
     * Method for creating the scene where the chat is.
     * @return A gridpane with the chat UI
     */

    public static GridPane createChat(){
        sendButton = new Button("Send");
        sendButton.setPrefWidth(60);

        inText = new TextField();
        inText.setPrefWidth(156);

        sendButton.setOnAction(e -> {
            if(!inText.getText().trim().equals("")){
                chat.writeThreadChat(inText.getText().trim());
                inText.clear();
            }
        });

        chatLayout = new VBox(5);
        chatLayout.setPadding(new Insets(8,8,8,8));
        //chatLayout.getChildren().add(new Label(""));

        container.setPrefSize(216, 400);
        //container.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //container.setHmax(216);
        container.setStyle("-fx-background: #3e1c03;");
        container.setContent(chatLayout);


        GridPane gridPane = new GridPane();
        gridPane.getColumnConstraints().add(new ColumnConstraints(156));
        gridPane.getColumnConstraints().add(new ColumnConstraints(60));
        gridPane.add(container, 0,0, 2, 1);
        gridPane.add(inText,0,1);
        gridPane.add(sendButton, 1,1);
        gridPane.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                if(!inText.getText().trim().equals("")){
                    chat.writeThreadChat(inText.getText().trim());
                    inText.clear();
                }
            }
        });
        return gridPane;
    }

    /**
     * Method to refresh the chat every second
     */
    public static void refresh(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                service();
            }
        }, 1000, 1000);
    }

    /**
     * Method for background service.
     */
    static void service() {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    refreshChat();
                                } finally {
                                    latch.countDown();
                                }
                            }
                        });
                        latch.await();
                        //Keep with the background work
                        return null;
                    }
                };
            }
        };
        service.start();
    }

    /**
     * Method for fetching messages from database, and displaying them in the game scene.
     */
    public static void refreshChat(){
        ArrayList<String> fetch = chat.fetchChat();
        for(String s : fetch){
            Label text = new Label(s);
            text.setFont(Font.font("Calibri", 16));
            chatLayout.getChildren().add(text);
            messages.add(text);
        }
    }
}
