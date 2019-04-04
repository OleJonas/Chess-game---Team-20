package JavaFX;
import Database.ChatDB;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class ChatFX{
    static Scene scene;
    static TextField inText;
    static Button sendButton, closeButton;
    static VBox chatLayout;

    static Timer timer = new Timer(true);
    private static ArrayList<Label> messages = new ArrayList<Label>();
    static ScrollPane container = new ScrollPane();
    static ChatDB chat = new ChatDB();

    public static GridPane createChat(){
        sendButton = new Button("Send");
        closeButton = new Button("Exit");
        inText = new TextField();

        sendButton.setOnAction(e -> {
            if(!inText.getText().trim().equals("")){
                chat.writeThreadChat(inText.getText().trim());
                inText.clear();
            }
        });

        chatLayout = new VBox(5);
        chatLayout.setPadding(new Insets(20,20,20,20));
        chatLayout.getChildren().add(new Label(" "));

        container.setPrefSize(216, 400);
        container.setStyle("-fx-background: #3e1c03;");
        container.setContent(chatLayout);

        GridPane gridPane = new GridPane();
        gridPane.add(container, 0,0);
        gridPane.add(inText,0,1);
        gridPane.add(sendButton, 0,2);
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

    public static void refresh(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                service();
            }
        }, 1000, 1000);
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
