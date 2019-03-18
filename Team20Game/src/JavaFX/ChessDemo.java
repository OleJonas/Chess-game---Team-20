package JavaFX;
import Database.DBOps;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import Game.GameEngine;
import java.util.TimerTask;
import java.util.Timer;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;


public class ChessDemo extends Application {

    private Timer timer;

    public static final int TILE_SIZE = 50;

    public static final double imageSize = 0.8;

    public static boolean color = true;

    public static boolean myTurn = true;

    public static int movenr = 0;

    private GameEngine ge = new GameEngine(15, true);

    private final int HEIGHT = ge.getBoard().getBoardState().length;
    private final int WIDTH = ge.getBoard().getBoardState()[0].length;
    public static int gameID = 58;              //new Random().nextInt(500000);

    private final String darkTileColor = "#8B4513";
    private final String lightTileColor = "#FFEBCD";

    private boolean isDone = false;

    private SandboxTile[][] board = new SandboxTile[WIDTH][HEIGHT];

    private Group boardGroup = new Group();
    private Group tileGroup = new Group();
    private Group hboxGroup = new Group();

    public static void main(String[] args) {
        launch(args);
    }

    private Parent createContent() {
        Pane root = new Pane();
        Pane bg = new Pane();
        bg.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        bg.setOnMouseClicked(r -> {
            hboxGroup.getChildren().clear();
            hboxGroup = new Group();
        });
        root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Rectangle square = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
                square.setOnMouseClicked(r -> {
                    hboxGroup.getChildren().clear();
                    JavaFX.HighlightBox box = new JavaFX.HighlightBox();
                    hboxGroup.getChildren().add(box);
                });
                square.setFill((x + y) % 2 == 0 ? Color.valueOf(lightTileColor) : Color.valueOf(darkTileColor));
                square.relocate(x * ChessDemo.TILE_SIZE, y * ChessDemo.TILE_SIZE);
                boardGroup.getChildren().add(square);
                if (ge.getBoard().getBoardState()[x][y] != null) {
                    boolean myColor;
                    if (color) {
                        if (ge.getBoard().getBoardState()[x][y].getColor()) {
                            myColor = true;
                        } else {
                            myColor = false;
                        }
                    } else {
                        if (ge.getBoard().getBoardState()[x][y].getColor()) {
                            myColor = false;
                        } else {
                            myColor = true;
                        }
                    }
                    SandboxTile tile = new SandboxTile(x, y, HEIGHT, ge, hboxGroup, tileGroup, board);
                    if (!color) {
                        ImageView temp = ge.getBoard().getBoardState()[x][y].getImageView();
                        temp.getTransforms().add(new Rotate(180, TILE_SIZE / 2, TILE_SIZE / 2));
                        tile.setImageView(temp, TILE_SIZE * (1 - imageSize) / 2, TILE_SIZE * (1 - imageSize) / 2);
                    } else {
                        tile.setImageView(ge.getBoard().getBoardState()[x][y].getImageView(), TILE_SIZE * (1 - imageSize) / 2, TILE_SIZE * (1 - imageSize) / 2);
                    }
                    board[x][y] = tile;
                    tileGroup.getChildren().add(tile);
                }
            }
        }
        if (!color) {
            Rotate rotate180 = new Rotate(180, (TILE_SIZE * WIDTH) / 2, (TILE_SIZE * HEIGHT) / 2);
            root.getTransforms().add(rotate180);
        }
        root.getChildren().addAll(boardGroup, tileGroup, hboxGroup);

        if (!color) {
            myTurn = false;
            movenr = 1;
        }

        return root;
    }

    public void removePiece(int x, int y) {
        tileGroup.getChildren().remove(board[x][y]);
        ge.removePiece(x, y);
    }

    public void enemyMove(int fromX, int fromY, int toX, int toY) {
        board[fromX][fromY].move(toX, toY, board);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Chess Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
        //clockDBThings();
        /*new Thread(()->{
            System.out.println("thread started");
            while(!isDone) {
                    try {
                        pollEnemyMove();
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }).start();
    }

    public void clockDBThings(){
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                serviceDBThings();
            }
        }, 4000, 4000);
    }

    public void serviceDBThings() {
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
                                    pollEnemyMove();
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



    public void pollEnemyMove(){
        System.out.println("PollEnemyMove Started, turn: " + movenr);
        try {
            DBOps db = new DBOps();
            System.out.println("SELECT fromX, fromY, toX, toY FROM GameIDMove WHERE GameID =" + gameID + " AND MoveNumber = " + (movenr) + ";");
            //ArrayList<String> res = db.exQuery("SELECT fromX, fromY, toX, toY FROM GameIDMove WHERE GameID = " + gameID + " AND MoveNumber = " + (movenr + 1) + ";");
            ArrayList<String> fromXlist = db.exQuery("SELECT fromX FROM GameIDMove WHERE GameID =" + gameID + " AND MoveNumber = " + (movenr) + ";", 1);
            if(fromXlist.size()>0) {
                int fromX = Integer.parseInt(fromXlist.get(0));
                int fromY = Integer.parseInt(db.exQuery("SELECT fromY FROM GameIDMove WHERE GameID =" + gameID + " AND MoveNumber = " + (movenr) + ";", 1).get(0));
                int toX = Integer.parseInt(db.exQuery("SELECT toX FROM GameIDMove WHERE GameID =" + gameID + " AND MoveNumber = " + (movenr) + ";", 1).get(0));
                int toY = Integer.parseInt(db.exQuery("SELECT toY FROM GameIDMove WHERE GameID =" + gameID + " AND MoveNumber = " + (movenr) + ";", 1).get(0));
                System.out.println("test" + fromX);
                enemyMove(fromX, fromY, toX, toY);
                myTurn=true;
            }
                if (true) {
                    enemyMove(res.getInt("fromX"), res.getInt("fromY"), res.getInt("toX"), res.getInt("toY"));
                    movenr++;
                    myTurn = true;
                    System.out.println("moved enemy piece");

            System.out.println("polled database");
        } catch (Exception e) {
            e.printStackTrace();
        }
     */
    }
}
