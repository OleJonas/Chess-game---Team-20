/*
** This is the class used for creating the chessboard in GameScene
 */

package JavaFX;
import Database.DBOps;
import Database.Game;
import Pieces.*;
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

import java.lang.reflect.Array;
import java.util.TimerTask;
import java.util.Timer;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ChessGame{
    private Timer timer;
    public static final int TILE_SIZE = 80;
    public static final double imageSize = 0.8;
    public static boolean color = true;
    public static boolean myTurn = true;
    public static int movenr = 0;
    private boolean polling = false;
    private boolean serviceRunning =false;
    private String homeSkin;
    private String awaySkin;
    public static String skin = "Standard";
    private GameEngine ge = new GameEngine(15, true);
    private final int HEIGHT = ge.getBoard().getBoardState().length;
    private final int WIDTH = ge.getBoard().getBoardState()[0].length;
    public static int gameID;              //new Random().nextInt(500000);
    private String darkTileColor = Settings.darkTileColor;
    private String lightTileColor = Settings.lightTileColor;
    private boolean isDone = false;
    private Tile[][] board = new Tile[WIDTH][HEIGHT];
    private Group boardGroup = new Group();
    private Group tileGroup = new Group();
    private Group hboxGroup = new Group();
    private Group selectedPieceGroup = new Group();
    private Group lastMoveGroup = new Group();

    public Parent setupBoard() {
        setSkins();
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
                    selectedPieceGroup.getChildren().clear();
                    JavaFX.HighlightBox box = new JavaFX.HighlightBox();
                    selectedPieceGroup.getChildren().add(box);
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
                            skin = homeSkin;
                        } else {
                            myColor = false;
                            skin = awaySkin;
                        }
                    } else {
                        if (ge.getBoard().getBoardState()[x][y].getColor()) {
                            myColor = false;
                            skin=homeSkin;
                        } else {
                            myColor = true;
                            skin = awaySkin;
                        }
                    }
                    Tile tile = new Tile(x, y, myColor, HEIGHT, ge, hboxGroup, tileGroup,selectedPieceGroup, lastMoveGroup, board);
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
            myTurn = false;
            movenr = 1;
            skin = awaySkin;
        } else {
            skin = homeSkin;
        }
        root.getChildren().addAll(boardGroup, selectedPieceGroup, lastMoveGroup, tileGroup, hboxGroup);

        clockDBThings();
        return root;
    }

    public void removePiece(int x, int y) {
        tileGroup.getChildren().remove(board[x][y]);
        ge.removePiece(x, y);
    }

    public boolean enemyMove(int fromX, int fromY, int toX, int toY) {
        if (board[fromX][fromY] != null) {
            if(toY > 7){
                Piece newPiece = null;
                boolean pieceColor = !ChessGame.color;
                if (!ChessGame.color) {
                    board[fromX][fromY].move(toX, 7, board);
                    if (toY == 8) {
                        newPiece = new Queen(pieceColor, toX, 7);
                    } else if (toY == 9) {
                        newPiece = new Knight(pieceColor, toX, 7);
                    } else if (toY == 10) {
                        newPiece = new Rook(pieceColor, toX, 7);
                    } else if (toY == 11) {
                        newPiece = new Bishop(pieceColor, toX, 7);
                    }

                    ChessGame.skin = homeSkin;
                    ImageView tempimg = newPiece.getImageView();
                    ChessGame.skin = awaySkin;
                    ge.setPiece(newPiece, toX, 7);

                    tempimg.getTransforms().add(new Rotate(180, ChessDemo.TILE_SIZE/2, ChessDemo.TILE_SIZE/2));

                    board[toX][7].setImageView(tempimg,
                            ChessDemo.TILE_SIZE*(1-ChessDemo.imageSize)/2, ChessDemo.TILE_SIZE*(1-ChessDemo.imageSize)/2);

                    lastMoveGroup.getChildren().clear();
                    Piece[][] boardState = ge.getBoard().getBoardState();
                    if (ge.inCheck(boardState, !ge.getBoard().getBoardState()[toX][7].getColor())) {
                        for (int i = 0; i < boardState.length; i++){
                            for (int j = 0; j < boardState[0].length; j++){
                                if (boardState[i][j] instanceof King){
                                    if (boardState[i][j].getColor() == !ge.getBoard().getBoardState()[toX][7].getColor()){
                                        Rectangle check = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
                                        check.setFill(Color.valueOf("#F30000"));
                                        check.setOpacity(1);
                                        check.setTranslateX(i*ChessDemo.TILE_SIZE);
                                        check.setTranslateY((HEIGHT-1-j)*ChessDemo.TILE_SIZE);
                                        lastMoveGroup.getChildren().add(check);
                                    }
                                }
                            }
                        }
                    }
                    Rectangle squareTo = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
                    squareTo.setFill(Color.valueOf("#582"));
                    squareTo.setOpacity(0.9);
                    squareTo.setTranslateX(toX*ChessDemo.TILE_SIZE);
                    squareTo.setTranslateY((HEIGHT-1-7)*ChessDemo.TILE_SIZE);
                } else {
                    board[fromX][fromY].move(toX, 0, board);
                    if (toY == 8) {
                        newPiece = new Queen(pieceColor, toX, 0);
                    } else if (toY == 10) {
                        newPiece = new Rook(pieceColor, toX, 0);
                    } else if (toY == 11) {
                        newPiece = new Bishop(pieceColor, toX, 0);
                    } else if (toY == 9) {
                        newPiece = new Knight(pieceColor, toX, 0);
                    }
                    ChessGame.skin = awaySkin;
                    ImageView tempimg = newPiece.getImageView();
                    ChessGame.skin = homeSkin;
                    ge.setPiece(newPiece, toX, 0);

                    board[toX][0].setImageView(tempimg,
                            ChessDemo.TILE_SIZE*(1-ChessDemo.imageSize)/2, ChessDemo.TILE_SIZE*(1-ChessDemo.imageSize)/2);

                    lastMoveGroup.getChildren().clear();
                    Piece[][] boardState = ge.getBoard().getBoardState();
                    if (ge.inCheck(boardState, !ge.getBoard().getBoardState()[toX][0].getColor())) {
                        for (int i = 0; i < boardState.length; i++){
                            for (int j = 0; j < boardState[0].length; j++){
                                if (boardState[i][j] instanceof King){
                                    if (boardState[i][j].getColor() == !ge.getBoard().getBoardState()[toX][0].getColor()){
                                        Rectangle check = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
                                        check.setFill(Color.valueOf("#F30000"));
                                        check.setOpacity(1);
                                        check.setTranslateX(i*ChessDemo.TILE_SIZE);
                                        check.setTranslateY((HEIGHT-1-j)*ChessDemo.TILE_SIZE);
                                        lastMoveGroup.getChildren().add(check);
                                    }
                                }
                            }
                        }
                    }
                    Rectangle squareTo = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
                    squareTo.setFill(Color.valueOf("#582"));
                    squareTo.setOpacity(0.9);
                    squareTo.setTranslateX(toX*ChessDemo.TILE_SIZE);
                    squareTo.setTranslateY((HEIGHT-1-0)*ChessDemo.TILE_SIZE);
                }
            } else {
                board[fromX][fromY].move(toX, toY, board);

                lastMoveGroup.getChildren().clear();
                Piece[][] boardState = ge.getBoard().getBoardState();
                if (ge.inCheck(boardState, !ge.getBoard().getBoardState()[toX][toY].getColor())) {
                    for (int i = 0; i < boardState.length; i++){
                        for (int j = 0; j < boardState[0].length; j++){
                            if (boardState[i][j] instanceof King){
                                if (boardState[i][j].getColor() == !ge.getBoard().getBoardState()[toX][toY].getColor()){
                                    Rectangle check = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
                                    check.setFill(Color.valueOf("#F30000"));
                                    check.setOpacity(1);
                                    check.setTranslateX(i*ChessDemo.TILE_SIZE);
                                    check.setTranslateY((HEIGHT-1-j)*ChessDemo.TILE_SIZE);
                                    lastMoveGroup.getChildren().add(check);
                                }
                            }
                        }
                    }
                }
                Rectangle squareTo = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
                squareTo.setFill(Color.valueOf("#582"));
                squareTo.setOpacity(0.9);
                squareTo.setTranslateX(toX*ChessDemo.TILE_SIZE);
                squareTo.setTranslateY((HEIGHT-1-toY)*ChessDemo.TILE_SIZE);
                lastMoveGroup.getChildren().add(squareTo);
            }

            if (ge.isCheckmate(ge.getBoard(), !ge.getBoard().getBoardState()[fromX][fromY].getColor())) {
                if (ge.getBoard().getBoardState()[fromX][fromY].getColor()) {
                    System.out.println("Checkmate for White");
                    /*int[] elo = ge.getElo(1000, 1000, 0);
                    System.out.println("New White elo: " +elo[0]+ "\nNew Black elo: " +elo[1]);
                    */
                    //fill in what happens when game ends here
                }
                else {
                    System.out.println("Checkmate for Black");
                    /*int[] elo = ge.getElo(1000, 1000, 1);
                    System.out.println("New White elo: " +elo[0]+ "\nNew Black elo: " +elo[1]);
                    */
                    //fill in what happens when game ends here
                }
            }
            else if (ge.isStalemate(ge.getBoard(), !ge.getBoard().getBoardState()[fromX][fromY].getColor())) {
                System.out.println("Stalemate");
                int[] elo = ge.getElo(1000, 1000, 2);
                System.out.println("New White elo: " +elo[0]+ "\nNew Black elo: " +elo[1]);
            }
            if (ge.isMoveRepetition()) {
                System.out.println("Repetisjon");
            }

            /*if(ge.notEnoughPieces(ge.getBoard())) {
                System.out.println("Remis");
                int[] elo = ge.getElo(1200, 1000, 2);
                System.out.println("New White elo: " +elo[0]+ "\nNew Black elo: " +elo[1]);
            }
            if (!(ge.getBoard().getBoardState()[fromX][fromY] instanceof Pawn) && ((totWhites+totBlacks) == (updatedWhites+updatedBlacks))) {
                ge.setMoveCounter(false);
                if (ge.getMoveCounter() == 100) {
                    System.out.println("Remis");
                    int[] elo = ge.getElo(1000, 1000, 2);
                    System.out.println("New White elo: " +elo[0]+ "\nNew Black elo: " +elo[1]);
                }
            } else {
                ge.setMoveCounter(true);
            }*/

            Rectangle squareFrom = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
            squareFrom.setFill(Color.valueOf("#582"));
            squareFrom.setOpacity(0.5);
            squareFrom.setTranslateX(fromX*ChessDemo.TILE_SIZE);
            squareFrom.setTranslateY((HEIGHT-1-fromY)*ChessDemo.TILE_SIZE);
            lastMoveGroup.getChildren().add(squareFrom);
            return true;

        }
        return false;
    }

    public boolean setSkins(){
                DBOps db = new DBOps();
                int home_id = Integer.parseInt(db.exQuery("SELECT user_id1 FROM Game WHERE game_id = "+ gameID +";", 1).get(0));
                System.out.println("home id:" + home_id);
                int away_id = Integer.parseInt(db.exQuery("SELECT user_id2 FROM Game WHERE game_id = "+ gameID +";", 1).get(0));
                System.out.println("away_id: " + away_id);
                homeSkin = db.exQuery("SELECT skinName FROM UserSettings WHERE user_id = " + home_id+";",1).get(0);
                awaySkin = db.exQuery("SELECT skinName FROM UserSettings WHERE user_id = " + away_id+";",1).get(0);
                System.out.println("homeSkin: " + homeSkin);
                return true;
    }

    public void clockDBThings(){
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("myTurn = " + myTurn + ", polling = " + polling);
                if(!polling && !serviceRunning && !myTurn) {
                    serviceDBThings();
                }
            }
        }, 0, 1000);
    }

    public void serviceDBThings() {
        System.out.println("Started service: service = " + serviceRunning);
            Service<Void> service = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            //Background work
                            final CountDownLatch latch = new CountDownLatch(1);
                            System.out.println("entered service");
                            serviceRunning = true;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("Starting myTurn check in service:");
                                    pollEnemyMove();
                                    latch.countDown();
                                    serviceRunning =false;
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
        //Only check when its not your turn
        polling = true;
        try {
            DBOps db = new DBOps();
            //System.out.println("SELECT fromX, fromY, toX, toY FROM Move WHERE game_id =" + gameID + " AND movenr = " + (movenr) + ";");
            //ArrayList<String> res = db.exQuery("SELECT fromX, fromY, toX, toY FROM GameIDMove WHERE GameID = " + gameID + " AND MoveNumber = " + (movenr + 1) + ";");
            ArrayList<String> fromXlist = db.exQuery("SELECT fromX FROM Move WHERE game_id =" + gameID + " AND movenr = " + (movenr) + ";", 1);
            if(fromXlist.size()>0) {
                int fromX = Integer.parseInt(fromXlist.get(0));
                int fromY = Integer.parseInt(db.exQuery("SELECT fromY FROM Move WHERE game_id =" + gameID + " AND movenr = " + (movenr) + ";", 1).get(0));
                int toX = Integer.parseInt(db.exQuery("SELECT toX FROM Move WHERE game_id =" + gameID + " AND movenr = " + (movenr) + ";", 1).get(0));
                int toY = Integer.parseInt(db.exQuery("SELECT toY FROM Move WHERE game_id =" + gameID + " AND movenr = " + (movenr) + ";", 1).get(0));
                System.out.println("test" + fromX);
                if (board[fromX][fromY] != null) {
                    enemyMove(fromX, fromY, toX, toY);
                    System.out.println("moved enemy  from : " + fromX + ", " + fromY + ", to: " + toX + ", " + toY);
                    myTurn = true;
                }
            }
                /*if (true) {
                    enemyMove(res.getInt("fromX"), res.getInt("fromY"), res.getInt("toX"), res.getInt("toY"));
                    movenr++;
                    myTurn = true;
                    System.out.println("moved enemy piece");
                }*/
            System.out.println("polled database");
        } catch (Exception e) {
            e.printStackTrace();
        }
        polling = false;
    }
}



