package JavaFX;

import Game.GameTimer;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class GameTimerFX {

    static int interval;
    static int increment;
    static GameTimer gameTime;
    static Stage window;
    static Scene scene;
    static VBox clockLayOut;
    static Timer timer = new Timer(true);
    static Label time;

    public static GridPane startTime(int minutes, int seconds) {
        interval = minutes;
        increment = seconds;
        time = new Label(""+interval);
        gameTime = new GameTimer(interval, increment);
        time.setFont(Font.font("Copperplate", 50));
        time.setTextFill(Color.web("#05B000"));
        clockLayOut = new VBox(5);
        clockLayOut.setPadding(new Insets(60, 60, 60, 60));
        clockLayOut.getChildren().add(time);
        GridPane gridPane = new GridPane();
        gridPane.add(clockLayOut, 100, 100);
        gameTime.clock();
        refresh();
        return gridPane;
    }


    public static void refresh() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                service();
            }
        }, 1, 1);
    }


    private static void updateTime() {
        time.setText((""+gameTime.getTime()));
        //System.out.println(gameTime.getTime());
    }

    public static void service() {
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
                                    updateTime();
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
}