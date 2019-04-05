/*
** This is the class used for creating the chessboard in GameScene
 */

package JavaFX.GameScene;
import Database.DBOps;
import Database.Game;
import Game.GameEngine;
import JavaFX.*;
import JavaFX.LoginScreen.Login;
import JavaFX.MainScene.MainScene;
import JavaFX.MainScene.Settings;
import Pieces.*;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class ChessGame{
    public static Timer timer;
    public static int TILE_SIZE = 80;
    public static final double imageSize = 0.8;
    public static boolean color = true;
    public static boolean myTurn = true;
    public static boolean gameWon = false;
    public static boolean firstMove = true;
    public static boolean lastMove = true;
    public static int movenr = 0;
    public static int whiteELO;
    public static int blackELO;
    private boolean polling = false;
    private boolean serviceRunning =false;
    private String homeSkin;
    private String awaySkin;
    public static String skin = "Standard";
    private GameEngine ge = new GameEngine(0);
    private final int HEIGHT = ge.getBoard().getBoardState().length;
    private final int WIDTH = ge.getBoard().getBoardState()[0].length;
    public static int gameID;
    private String darkTileColor = Settings.darkTileColor;
    private String lightTileColor = Settings.lightTileColor;
    public static boolean isDone = false;
    private Tile[][] board = new Tile[WIDTH][HEIGHT];
    private Group boardGroup = new Group();
    private Group tileGroup = new Group();
    private Group hboxGroup = new Group();
    private Group selectedPieceGroup = new Group();
    private Group lastMoveGroup = new Group();
    private int toeX;
    private int toeY;

    public Parent setupBoard() {
        setupGameEngine();
        setSkins();
        Pane root = new Pane();
        Pane bg = new Pane();
        bg.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        bg.setOnMouseClicked((MouseEvent r) -> {
            hboxGroup.getChildren().clear();
            hboxGroup = new Group();
        });
        root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Rectangle square = new Rectangle(TILE_SIZE, TILE_SIZE);
                square.setOnMouseClicked(r -> {
                    hboxGroup.getChildren().clear();
                    selectedPieceGroup.getChildren().clear();
                    HighlightBox box = new HighlightBox();
                    selectedPieceGroup.getChildren().add(box);
                    hboxGroup.getChildren().add(box);
                });
                square.setFill((x + y) % 2 == 0 ? Color.valueOf(lightTileColor) : Color.valueOf(darkTileColor));
                square.relocate(x * TILE_SIZE, y * TILE_SIZE);
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
        lastMoveGroup.getChildren().clear();

            if (board[fromX][fromY] != null) {
                if (Math.abs(fromY - toY) == 2 && ge.getBoard().getBoardState()[fromX][fromY] instanceof Pawn) {
                    Pawn pawn = (Pawn) ge.getBoard().getBoardState()[fromX][fromY];
                    pawn.setEnPassant(true);
                }
                if (toY == 13) {
                    removePiece(toX, fromY);
                    toY = color ? 2 : 5;
                }
                if (toY == 12) {
                    if (fromY == 0) {
                        if (toX > fromX) {
                            board[7][0].move(toX - 1, 0, board, true);
                            board[4][0].move(6, 0, board, false);
                            toY = 0;
                        } else {
                            board[0][0].move(toX + 1, 0, board, true);
                            board[4][0].move(2, 0, board, false);
                            toY = 0;
                        }
                    } else {
                        if (toX > fromX) {
                            board[7][7].move(toX - 1, 7, board, true);
                            board[4][7].move(6, 7, board, false);
                            toY=7;
                        } else {
                            board[0][7].move(toX + 1, 7, board, true);
                            board[4][7].move(2, 7, board, false);
                            toY = 7;
                        }
                    }

                } else if(toY > 7){
                Piece newPiece = null;
                boolean pieceColor = !color;
                if (!color) {
                    board[fromX][fromY].move(toX, 7, board, false);
                    if (toY == 8) {
                        newPiece = new Queen(pieceColor, toX, 7);
                    } else if (toY == 9) {
                        newPiece = new Knight(pieceColor, toX, 7);
                    } else if (toY == 10) {
                        newPiece = new Rook(pieceColor, toX, 7);
                    } else if (toY == 11) {
                        newPiece = new Bishop(pieceColor, toX, 7);
                    }
                    skin = homeSkin;
                    ImageView tempimg = newPiece.getImageView();
                    skin = awaySkin;
                    ge.setPiece(newPiece, toX, 7);
                    toY=7;

                    tempimg.getTransforms().add(new Rotate(180, TILE_SIZE/2, TILE_SIZE/2));

                    board[toX][7].setImageView(tempimg,
                            TILE_SIZE*(1-imageSize)/2, TILE_SIZE*(1-imageSize)/2);
                    Piece[][] boardState = ge.getBoard().getBoardState();
                    if (ge.inCheck(boardState, !ge.getBoard().getBoardState()[toX][7].getColor())) {
                        for (int i = 0; i < boardState.length; i++){
                            for (int j = 0; j < boardState[0].length; j++){
                                if (boardState[i][j] instanceof King){
                                    if (boardState[i][j].getColor() == !ge.getBoard().getBoardState()[toX][7].getColor()){
                                        Rectangle check = new Rectangle(TILE_SIZE, TILE_SIZE);
                                        check.setFill(Color.valueOf("#F30000"));
                                        check.setOpacity(1);
                                        check.setTranslateX(i*TILE_SIZE);
                                        check.setTranslateY((HEIGHT-1-j)*TILE_SIZE);
                                        lastMoveGroup.getChildren().add(check);
                                    }
                                }
                            }
                        }
                    }
                    Rectangle squareTo = new Rectangle(TILE_SIZE, TILE_SIZE);
                    squareTo.setFill(Color.valueOf("#582"));
                    squareTo.setOpacity(0.9);
                    squareTo.setTranslateX(toX*TILE_SIZE);
                    squareTo.setTranslateY((HEIGHT-1-7)*TILE_SIZE);
                } else {
                    board[fromX][fromY].move(toX, 0, board, false);
                    if (toY == 8) {
                        newPiece = new Queen(pieceColor, toX, 0);
                    } else if (toY == 10) {
                        newPiece = new Rook(pieceColor, toX, 0);
                    } else if (toY == 11) {
                        newPiece = new Bishop(pieceColor, toX, 0);
                    } else if (toY == 9) {
                        newPiece = new Knight(pieceColor, toX, 0);
                    }
                    skin = awaySkin;
                    ImageView tempimg = newPiece.getImageView();
                    skin = homeSkin;
                    ge.setPiece(newPiece, toX, 0);
                    toY= 0;

                    board[toX][0].setImageView(tempimg,
                            TILE_SIZE*(1-imageSize)/2, TILE_SIZE*(1-imageSize)/2);

                    //lastMoveGroup.getChildren().clear();
                    Piece[][] boardState = ge.getBoard().getBoardState();
                    if (ge.inCheck(boardState, !ge.getBoard().getBoardState()[toX][0].getColor())) {
                        for (int i = 0; i < boardState.length; i++){
                            for (int j = 0; j < boardState[0].length; j++){
                                if (boardState[i][j] instanceof King){
                                    if (boardState[i][j].getColor() == !ge.getBoard().getBoardState()[toX][0].getColor()){
                                        Rectangle check = new Rectangle(TILE_SIZE, TILE_SIZE);
                                        check.setFill(Color.valueOf("#F30000"));
                                        check.setOpacity(1);
                                        check.setTranslateX(i*TILE_SIZE);
                                        check.setTranslateY((HEIGHT-1-j)*TILE_SIZE);
                                        lastMoveGroup.getChildren().add(check);
                                    }
                                }
                            }
                        }
                    }
                    Rectangle squareTo = new Rectangle(TILE_SIZE, TILE_SIZE);
                    squareTo.setFill(Color.valueOf("#582"));
                    squareTo.setOpacity(0.9);
                    squareTo.setTranslateX(toX*TILE_SIZE);
                    squareTo.setTranslateY((HEIGHT-1-0)*TILE_SIZE);
                }
            } else {
                board[fromX][fromY].move(toX, toY, board, false);

                //lastMoveGroup.getChildren().clear();
                Piece[][] boardState = ge.getBoard().getBoardState();
                if (ge.inCheck(boardState, !ge.getBoard().getBoardState()[toX][toY].getColor())) {
                    for (int i = 0; i < boardState.length; i++){
                        for (int j = 0; j < boardState[0].length; j++){
                            if (boardState[i][j] instanceof King){
                                if (boardState[i][j].getColor() == !ge.getBoard().getBoardState()[toX][toY].getColor()){
                                    Rectangle check = new Rectangle(TILE_SIZE, TILE_SIZE);
                                    check.setFill(Color.valueOf("#F30000"));
                                    check.setOpacity(1);
                                    check.setTranslateX(i*TILE_SIZE);
                                    check.setTranslateY((HEIGHT-1-j)*TILE_SIZE);
                                    lastMoveGroup.getChildren().add(check);
                                }
                            }
                        }
                    }
                }
                if (ge.isCheckmate(ge.getBoard(), !ge.getBoard().getBoardState()[toX][toY].getColor())) {
                    if (ge.getBoard().getBoardState()[toX][toY].getColor()) {
                        System.out.println("Checkmate for White");
                        if(!color){

                            timer.cancel();
                            MainScene.inGame =false;
                            isDone = true;
                            GameOverPopupBox.Display();
                        }

                        //fill in what happens when game ends here
                    }
                    else {
                        System.out.println("Checkmate for Black");
                        if(color){
                            timer.cancel();
                            MainScene.inGame =false;
                            isDone = true;
                            GameOverPopupBox.Display();
                        }
                    }
                }

                Rectangle squareTo = new Rectangle(TILE_SIZE, TILE_SIZE);
                squareTo.setFill(Color.valueOf("#582"));
                squareTo.setOpacity(0.9);
                squareTo.setTranslateX(toX*TILE_SIZE);
                squareTo.setTranslateY((HEIGHT-1-toY)*TILE_SIZE);
            }

            Rectangle squareFrom = new Rectangle(TILE_SIZE, TILE_SIZE);
            squareFrom.setFill(Color.valueOf("#582"));
            squareFrom.setOpacity(0.5);
            squareFrom.setTranslateX(fromX*TILE_SIZE);
            squareFrom.setTranslateY((HEIGHT-1-fromY)*TILE_SIZE);
            toeX = toX;
            toeY = toY;
            return true;
        }
        return false;
    }

    private void setupGameEngine() {
        ge = new GameEngine(Game.getMode(gameID));
        MainScene.searchFriend = false;
        whiteELO = Game.getWhiteELO(gameID);
        blackELO = Game.getBlackELO(gameID);
        myTurn = true;
        gameWon = false;
        isDone = false;
        firstMove = true;
        movenr = 0;
        color = (Game.getUser_id1(gameID)== Login.userID)?true:false;
        GameScene.myColumn = color?1:2;
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
                //System.out.println("myTurn = " + myTurn + ", polling = " + polling);
                if(gameWon || isDone){
                    timer.cancel();
                }
                else if(!polling && !serviceRunning && !myTurn) {
                    serviceDBThings();
                }
            }
        }, 0, 250);
    }

    public void serviceDBThings() {
        //System.out.println("Started service: service = " + serviceRunning);
            Service<Void> service = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            //Background work
                            final CountDownLatch latch = new CountDownLatch(1);
                            //System.out.println("entered service");
                            serviceRunning = true;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    //System.out.println("Starting myTurn check in service:");
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
            ArrayList<String> fromXlist = db.exQuery("SELECT fromX FROM Move WHERE game_id =" + gameID + " AND movenr = " + (movenr) + ";", 1);
            if(fromXlist.size()>0) {
                int fromX = Integer.parseInt(fromXlist.get(0));
                int fromY = Integer.parseInt(db.exQuery("SELECT fromY FROM Move WHERE game_id =" + gameID + " AND movenr = " + (movenr) + ";", 1).get(0));
                toeX = Integer.parseInt(db.exQuery("SELECT toX FROM Move WHERE game_id =" + gameID + " AND movenr = " + (movenr) + ";", 1).get(0));
                toeY = Integer.parseInt(db.exQuery("SELECT toY FROM Move WHERE game_id =" + gameID + " AND movenr = " + (movenr) + ";", 1).get(0));
                int timeStamp = Integer.parseInt(db.exQuery("SELECT timeStamp FROM Move WHERE game_id =" + gameID + " AND movenr = " + (movenr) + ";", 1).get(0));

                GameScene.allMoves.add(toeY + "" + toeX);
                GameScene.updateMoves();

                if (board[fromX][fromY] != null) {
                    enemyMove(fromX, fromY, toeX, toeY);
                    Piece temp = (Piece)ge.getBoard().getBoardState()[toeX][toeY];
                    int column = 0;
                    int movenrVariableForRow = 1;
                    if(color) {
                        movenrVariableForRow = -1;
                    }
                    column = color ? 2 : 1;
                    if (color) {
                        Label text = new Label(GameScene.spacing + temp.toString());
                        text.setFont(Font.font("Copperplate", 20));
                        GameScene.viewMoves.add(text, column, (movenr + movenrVariableForRow)/2);
                    } else {
                        Label text = new Label(GameScene.spacing + temp.toString());
                        Label nr = new Label((((movenr + 1)/2)) + ". ");
                        text.setFont(Font.font("Copperplate", 20));
                        nr.setFont(Font.font("Copperplate", 20));
                        GameScene.viewMoves.add(nr, 0, (movenr + movenrVariableForRow)/2);
                        GameScene.viewMoves.add(text, column, (movenr + 1)/2);
                    }
                    myTurn = true;
                    GameScene.opponentTime = timeStamp;
                    if(firstMove && !color){
                        System.out.println("started timer in chessGame");
                        if (GameScene.yourTime != 0) {
                            GameScene.refresh();
                        }
                        firstMove = false;
                    }
                    if(GameScene.remiOffered){
                        GameScene.remiOffered = false;
                        GameScene.offerDrawButton.setText("Offer draw");
                        GameScene.offerDrawButton.setOpacity(1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        polling = false;
    }

    public static void setOnMouseClicked(Rectangle square, Group hboxGroup, Group selectedPieceGroup) {
        square.setOnMouseClicked(r -> {
            hboxGroup.getChildren().clear();
            selectedPieceGroup.getChildren().clear();
            HighlightBox box = new HighlightBox();
            selectedPieceGroup.getChildren().add(box);
            hboxGroup.getChildren().add(box);
        });
    }

}