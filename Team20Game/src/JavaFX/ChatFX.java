package JavaFX;
import Pieces.*;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import Game.GameEngine;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

import Database.ChatDB;
import javafx.application.Application;
import javafx.scene.Scene;

import java.util.Timer;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class ChatFX{
    static Timer timer = new Timer(true);
    static Stage window;
    static Scene scene;
    static TextField inText;
    static Button sendButton, closeButton;
    static ScrollPane container = new ScrollPane();
    private static ArrayList<Label> messages = new ArrayList<Label>();
    static VBox chatLayout;

    static ChatDB chat = new ChatDB();

    public static GridPane createChat(){
        //window = new Stage();
        sendButton = new Button("Send");
        closeButton = new Button("Exit");

        inText = new TextField();

        sendButton.setOnAction(e -> {
            if(!inText.getText().trim().equals("")){
                chat.writeChat(inText.getText().trim());
                inText.clear();
            }
        });

        /*closeButton.setOnAction(e -> {
            chat.chatClose();
            window.close();
        });*/

        chatLayout = new VBox(5);
        chatLayout.setPadding(new Insets(20,20,20,20));
        chatLayout.getChildren().add(new Label(" "));

        container.setPrefSize(216, 400);
        container.setContent(chatLayout);

        GridPane gridPane = new GridPane();
        gridPane.add(container, 0,0);
        gridPane.add(inText,0,1);
        gridPane.add(sendButton, 0,2);
        //gridPane.add(closeButton,1,2);

        return gridPane;
    }

    public static void refresh(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                service();
            }
        }, 5000, 5000);
    }

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

    public static void refreshChat(){
        ArrayList<String> fetch = chat.fetchChat();
        for(String s : fetch){
            chatLayout.getChildren().add(new Label(s));
            messages.add(new Label(s));
        }
    }
}
