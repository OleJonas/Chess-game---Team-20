package JavaFX;
import Database.DBOps;
import Pieces.King;
import Pieces.Pawn;
import Pieces.Queen;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
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

    public static final int TILE_SIZE = ChessGame.TILE_SIZE;

    public static final double imageSize = ChessGame.imageSize;

    public static boolean color = true;

    public static boolean myTurn = true;

    public static int movenr = 0;

    private GameEngine ge = new GameEngine(15, true);

    private final int HEIGHT = ge.getBoard().getBoardState().length;
    private final int WIDTH = ge.getBoard().getBoardState()[0].length;

    private final String darkTileColor = "#8B4513";
    private final String lightTileColor = "#FFEBCD";

    private boolean isDone = false;

    private TestTile[][] board = new TestTile[WIDTH][HEIGHT];

    private Group boardGroup = new Group();
    private Group tileGroup = new Group();
    private Group hboxGroup = new Group();
    private Group selectedPieceGroup = new Group();

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
                    TestTile tile = new TestTile(x, y, myColor, HEIGHT, ge, hboxGroup, tileGroup,selectedPieceGroup, board);
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
        root.getChildren().addAll(boardGroup, selectedPieceGroup, tileGroup, hboxGroup);

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
        //if(toX != null && toY != null) {
        board[fromX][fromY].move(toX, toY, board);
        //}
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

class TestTile extends StackPane {
    private ImageView imageView;
    private GameEngine gameEngine;

    private int height;

    private Group tileGroup;

    private int currentPositionX;
    private int currentPositionY;

    private double oldX, oldY;

    public TestTile(int x, int y, boolean myColor , int height, GameEngine gameEngine, Group hboxGroup, Group tileGroup, Group selectedGroup, TestTile[][] board) {
        super.setWidth(ChessDemo.TILE_SIZE);
        setHeight(ChessDemo.TILE_SIZE);
        currentPositionX=x;
        currentPositionY=y;
        this.tileGroup = tileGroup;
        this.height = height;
        this.gameEngine = gameEngine;
        getChildren().add(new Rectangle());
        relocate(x * ChessDemo.TILE_SIZE , (height-1-y) * ChessDemo.TILE_SIZE) ;

        setOnMouseClicked(e->{
            selectedGroup.getChildren().clear();
            hboxGroup.getChildren().clear();
            Rectangle square = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
            square.setFill(Color.valueOf("#582"));
            square.setOpacity(0.5);
            square.setTranslateX(currentPositionX*ChessDemo.TILE_SIZE);
            square.setTranslateY((height-1-currentPositionY)*ChessDemo.TILE_SIZE);
            selectedGroup.getChildren().add(square);
            if(ChessDemo.myTurn) {
                ArrayList<Integer> moves = gameEngine.validMoves(currentPositionX, currentPositionY);

                if(moves!=null&&moves.size()>0) {
                    for (int i = 0; i < moves.size(); i += 2) {
                        TestHighlightBox box = new TestHighlightBox(moves.get(i), moves.get(i + 1), height,
                                this, hboxGroup, tileGroup, selectedGroup, gameEngine, board);
                        hboxGroup.getChildren().add(box);
                    }
                }
                else if(moves.size() == 0) {
                    HighlightBox box = new HighlightBox();
                    hboxGroup.getChildren().add(box);
                }
            }

        });
        setOnMouseDragged(e->{
        });
        setOnMouseReleased(e->{
        });
    }
    public void setPos(int x, int y){
        currentPositionX = x;
        currentPositionY = y;
    }
    public int getX(){
        return currentPositionX;
    }
    public int getY(){
        return currentPositionY;
    }
    public boolean setImageView(ImageView img, double offsetX, double offsetY){
        if(img == null){
            return false;
        }
        if(!ChessDemo.color){
            img.setTranslateX(-offsetX);
            img.setTranslateY(-offsetY);
        }else{
            img.setTranslateX(offsetX);
            img.setTranslateY(offsetY);
        }
        getChildren().set(0,img);
        return true;
    }
    public void move(int x, int y, TestTile[][] board) {
        oldX = x * ChessDemo.TILE_SIZE;
        oldY = (height - 1 - y) * ChessDemo.TILE_SIZE;
        gameEngine.move(currentPositionX, currentPositionY, x, y);
        gameEngine.move(currentPositionX, currentPositionY, x, y);
        if (board[x][y] != null) {
            tileGroup.getChildren().remove(board[x][y]);
        }
        board[x][y] = this;
        board[currentPositionX][currentPositionY] = null;
        setPos(x, y);
        relocate(oldX, oldY);
    }
}

class TestHighlightBox extends Pane{
        int x;
        int y;
        int height;
        double hboxOpacity = 0.7;
        String shapeOfBox = "circle";
        public TestHighlightBox(int x, int y, int height, TestTile tile, Group hboxGroup, Group tileGroup, Group selectedGroup, GameEngine gameEngine, TestTile[][] board){
            this.x = x;
            this.y = y;
            this.height = height;
            relocate(x * ChessDemo.TILE_SIZE, (height - 1 - y) * ChessDemo.TILE_SIZE);
            if(shapeOfBox.equalsIgnoreCase("rectangle")) {
                Rectangle square = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
                square.setFill(Color.valueOf("#582"));
                square.setOpacity(hboxOpacity);
                getChildren().add(square);
            }else{
                Circle circle = new Circle(ChessDemo.TILE_SIZE / 4);
                circle.setFill(Color.valueOf("582"));
                circle.setOpacity(hboxOpacity);
                circle.setTranslateX(ChessDemo.TILE_SIZE/2);
                circle.setTranslateY(ChessDemo.TILE_SIZE/2);
                getChildren().add(circle);
            }
            setOnMouseClicked(e->{
                specialMoves(x, y, height, tile, hboxGroup, tileGroup, gameEngine, board);
                //ChessDemo.myTurn = false;
                //uploadMove(tile.getX(), tile.getY(), x, y);
                tile.move(x, y, board);
                int top=0;
                if(ChessDemo.color) {
                    top = height-1;
                }
                if(y==top && gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof Pawn){
                    Queen newPiece = new Queen(ChessDemo.color, x, y);
                    ImageView tempimg = newPiece.getImageView();
                    gameEngine.setPiece(newPiece, x, y);
                    if(!ChessDemo.color){
                        tempimg.getTransforms().add(new Rotate(180, ChessDemo.TILE_SIZE/2, ChessDemo.TILE_SIZE/2));
                    }
                    tile.setImageView(tempimg,
                            ChessDemo.TILE_SIZE*(1-ChessDemo.imageSize)/2, ChessDemo.TILE_SIZE*(1-ChessDemo.imageSize)/2);
                }
                if (gameEngine.isCheckmate(gameEngine.getBoard(), !gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor())) {
                    if (gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()) {
                        System.out.println("Checkmate for White");
                        int[] elo = gameEngine.getElo(1000, 1000, 0);
                        System.out.println("New White elo: " +elo[0]+ "\nNew Black elo: " +elo[1]);
                    }
                    else {
                        System.out.println("Checkmate for Black");
                        int[] elo = gameEngine.getElo(1000, 1000, 1);
                        System.out.println("New White elo: " +elo[0]+ "\nNew Black elo: " +elo[1]);
                    }
                }
                else if (gameEngine.isStalemate(gameEngine.getBoard(), !gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor())) {
                    System.out.println("Stalemate");
                    int[] elo = gameEngine.getElo(1000, 1000, 2);
                    System.out.println("New White elo: " +elo[0]+ "\nNew Black elo: " +elo[1]);
                }
                else if(gameEngine.notEnoughPieces(gameEngine.getBoard())) {
                    System.out.println("Remis");
                    int[] elo = gameEngine.getElo(1200, 1000, 2);
                    System.out.println("New White elo: " +elo[0]+ "\nNew Black elo: " +elo[1]);
                }
                //System.out.println("moved piece");
                //System.out.println(gameEngine.getBoard());
                ChessDemo.movenr+=2;
                getChildren().clear();
                hboxGroup.getChildren().clear();
                selectedGroup.getChildren().clear();
            });
        }

        public TestHighlightBox() {
            if(shapeOfBox.equalsIgnoreCase("rectangle")) {
                Rectangle square = new Rectangle(0, 0);
                getChildren().add(square);
            } else {
                Circle circle = new Circle(0);
                getChildren().add(circle);
            }
        }
/*
    private void uploadMove(int fromX, int fromY, int toX, int toY){
        DBOps db = new DBOps();
        System.out.println("uploaded movenr: " + (ChessDemo.movenr +1));
        db.exUpdate("INSERT INTO GameIDMove VALUES (" + ChessDemo.gameID + ", " + (ChessDemo.movenr +1) +", "+ fromX +", "+fromY+", "+toX+", "+toY+");");
    }
*/

private void specialMoves(int x, int y, int height, TestTile tile, Group hboxGroup, Group tileGroup, GameEngine gameEngine, TestTile[][] board) {
            if ((Math.abs(x-tile.getX()) == 2 ) && gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof King){
                if(x-tile.getX()>0) {
                    board[7][y].move(x-1, y, board);
                }else{
                    board[0][y].move(x+1, y, board);
                }
                King king = (King)gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()];
                king.setCanCastle(false);
                //System.out.println("Rokkade");
            }
            if (Math.abs(y-tile.getY()) == 2 && gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof Pawn) {
                Pawn pawn = (Pawn) gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()];
                pawn.setEnPassant(true);
            }

            if (tile.getX() + 1 < 8) {
                if (gameEngine.getBoard().getBoardState()[tile.getX()+1][tile.getY()] instanceof Pawn) {
                    if (gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()) {
                        //System.out.println("Hallo1");
                        Pawn pawn = (Pawn) gameEngine.getBoard().getBoardState()[tile.getX()+1][tile.getY()];
                        if (pawn.getColor() != gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()) {
                            //System.out.println("Hallo2");
                            if (pawn.getEnPassant()) {
                                //System.out.println("Hallo3");
                                tileGroup.getChildren().remove(board[tile.getX()+1][tile.getY()]);
                                gameEngine.removePiece(tile.getX()+1, tile.getY());
                            }
                        }
                    }
                    else {
                        Pawn pawn = (Pawn) gameEngine.getBoard().getBoardState()[tile.getX()+1][tile.getY()];
                        if (pawn.getColor() != gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()) {
                            if (pawn.getEnPassant()) {
                                tileGroup.getChildren().remove(board[tile.getX()+1][tile.getY()]);
                                gameEngine.removePiece(tile.getX()+1, tile.getY());
                            }
                        }
                    }
                }
            }
            if (tile.getX() - 1 >= 0) {
                if (gameEngine.getBoard().getBoardState()[tile.getX()-1][tile.getY()] instanceof Pawn) {
                    if (gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()) {
                        Pawn pawn = (Pawn) gameEngine.getBoard().getBoardState()[tile.getX()-1][tile.getY()];
                        if (pawn.getColor() != gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()) {
                            if (pawn.getEnPassant()) {
                                tileGroup.getChildren().remove(board[tile.getX()-1][tile.getY()]);
                                gameEngine.removePiece(tile.getX()-1, tile.getY());
                            }
                        }
                    }
                    else {
                        Pawn pawn = (Pawn) gameEngine.getBoard().getBoardState()[tile.getX()-1][tile.getY()];
                        if (pawn.getColor() != gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()) {
                            if (pawn.getEnPassant()) {
                                tileGroup.getChildren().remove(board[tile.getX()-1][tile.getY()]);
                                gameEngine.removePiece(tile.getX()-1, tile.getY());
                            }
                        }
                    }
                }
            }
        }
        public int getX(){
            return x;
        }

        public int getY(){
            return y;
        }
    }
