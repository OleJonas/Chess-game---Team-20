package JavaFX;
import Database.DBOps;
import Game.GameLogic;
import Pieces.*;
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

    public static final int TILE_SIZE = JavaFX.ChessGame.TILE_SIZE;

    public static final double imageSize = JavaFX.ChessGame.imageSize;

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
    private Group lastMoveGroup = new Group();

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
                    TestTile tile = new TestTile(x, y, myColor, HEIGHT, ge, hboxGroup, tileGroup,selectedPieceGroup, lastMoveGroup, board);
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
        root.getChildren().addAll(boardGroup, selectedPieceGroup, lastMoveGroup, tileGroup, hboxGroup);

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
    }
}

class TestTile extends StackPane {
    private ImageView imageView;
    private GameEngine gameEngine;

    private int height;
    private boolean myColor;

    private Group tileGroup;

    private int currentPositionX;
    private int currentPositionY;

    private double oldX, oldY;

    public TestTile(int x, int y, boolean myColor , int height, GameEngine gameEngine, Group hboxGroup, Group tileGroup, Group selectedGroup, Group lastMoveGroup, TestTile[][] board) {
        super.setWidth(ChessDemo.TILE_SIZE);
        setHeight(ChessDemo.TILE_SIZE);
        currentPositionX=x;
        currentPositionY=y;
        this.myColor = myColor;
        this.tileGroup = tileGroup;
        this.height = height;
        this.gameEngine = gameEngine;
        getChildren().add(new Rectangle());
        relocate(x * ChessDemo.TILE_SIZE , (height-1-y) * ChessDemo.TILE_SIZE) ;

        setOnMouseClicked(e->{
            selectedGroup.getChildren().clear();
            hboxGroup.getChildren().clear();
            Rectangle square = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
            square.setFill(Color.valueOf("#696969"));
            square.setOpacity(0.4);
            square.setTranslateX(currentPositionX*ChessDemo.TILE_SIZE);
            square.setTranslateY((height-1-currentPositionY)*ChessDemo.TILE_SIZE);
            selectedGroup.getChildren().add(square);
            if(ChessDemo.myTurn) {
                ArrayList<Integer> moves = gameEngine.validMoves(currentPositionX, currentPositionY);

                if(moves!=null&&moves.size()>0) {
                    for (int i = 0; i < moves.size(); i += 2) {
                        TestHighlightBox box = new TestHighlightBox(moves.get(i), moves.get(i + 1), height,
                                this, hboxGroup, tileGroup, selectedGroup, lastMoveGroup, gameEngine, board);
                        hboxGroup.getChildren().add(box);
                    }
                }
                else if(moves.size() == 0) {
                    HighlightBox box = new HighlightBox();
                    selectedGroup.getChildren().add(box);
                    hboxGroup.getChildren().add(box);
                }
            }

        });
        setOnMouseDragged(e->{
        });
        setOnMouseReleased(e->{
        });
    }
    public boolean getMyColor() {
        return myColor;
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
        public TestHighlightBox(int x, int y, int height, TestTile tile, Group hboxGroup, Group tileGroup, Group selectedGroup, Group lastMoveGroup, GameEngine gameEngine, TestTile[][] board){
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
                Circle circle = new Circle(ChessDemo.TILE_SIZE / 5);
                circle.setFill(Color.valueOf("582"));
                circle.setOpacity(hboxOpacity);
                circle.setTranslateX(ChessDemo.TILE_SIZE/2);
                circle.setTranslateY(ChessDemo.TILE_SIZE/2);
                getChildren().add(circle);

                Rectangle square = new Rectangle(ChessDemo.TILE_SIZE*0.7, ChessDemo.TILE_SIZE*0.7);
                square.setOpacity(0);
                getChildren().add(square);
            }
            setOnMouseClicked(e->{
                specialMoves(x, y, height, tile, hboxGroup, tileGroup, gameEngine, board);
                //ChessDemo.myTurn = false;
                //uploadMove(tile.getX(), tile.getY(), x, y);
                int fromX = tile.getX();
                int fromY = tile.getY();
                int totWhites = gameEngine.myPieces(gameEngine.getBoard(), true)[6];
                int totBlacks = gameEngine.myPieces(gameEngine.getBoard(), false)[6];
                tile.move(x, y, board);
                int updatedWhites = gameEngine.myPieces(gameEngine.getBoard(), true)[6];
                int updatedBlacks = gameEngine.myPieces(gameEngine.getBoard(), false)[6];
                int top=0;

                if((tile.getMyColor()&&ChessDemo.color)||!tile.getMyColor() && !ChessDemo.color) {
                    top = height - 1;
                }

                if(y==top && gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof Pawn){
                    PawnChangeChoiceBox pawnChange = new PawnChangeChoiceBox();
                    pawnChange.Display(ChessDemo.color);
                    Piece newPiece = null;
                    boolean pieceColor = ChessDemo.color?tile.getMyColor():!tile.getMyColor();
                    if (PawnChangeChoiceBox.choice.equals("Queen")) {
                        newPiece = new Queen(pieceColor, x, y);
                    } else if (PawnChangeChoiceBox.choice.equals("Rook")) {
                        newPiece = new Rook(pieceColor, x, y);
                    } else if (PawnChangeChoiceBox.choice.equals("Bishop")) {
                        newPiece = new Bishop(pieceColor, x, y);
                    } else if (PawnChangeChoiceBox.choice.equals("Knight")) {
                        newPiece = new Knight(pieceColor, x, y);
                    }
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
                if(gameEngine.notEnoughPieces(gameEngine.getBoard())) {
                    System.out.println("Remis");
                    int[] elo = gameEngine.getElo(1200, 1000, 2);
                    System.out.println("New White elo: " +elo[0]+ "\nNew Black elo: " +elo[1]);
                }
                if (!(gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof Pawn) && ((totWhites+totBlacks) == (updatedWhites+updatedBlacks))) {
                    gameEngine.setMoveCounter(false);
                    System.out.println(gameEngine.getMoveCounter());
                    if (gameEngine.getMoveCounter() == 100) {
                        System.out.println("Remis");
                        int[] elo = gameEngine.getElo(1000, 1000, 2);
                        System.out.println("New White elo: " +elo[0]+ "\nNew Black elo: " +elo[1]);
                    }
                } else {
                    gameEngine.setMoveCounter(true);
                    System.out.println(gameEngine.getMoveCounter());
                }

                ChessDemo.movenr+=2;
                getChildren().clear();
                hboxGroup.getChildren().clear();
                lastMoveGroup.getChildren().clear();
                selectedGroup.getChildren().clear();

                Rectangle squareTo = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
                squareTo.setFill(Color.valueOf("#582"));
                squareTo.setOpacity(0.9);
                squareTo.setTranslateX(x*ChessDemo.TILE_SIZE);
                squareTo.setTranslateY((height-1-y)*ChessDemo.TILE_SIZE);

                Rectangle squareFrom = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
                squareFrom.setFill(Color.valueOf("#582"));
                squareFrom.setOpacity(0.5);
                squareFrom.setTranslateX(fromX*ChessDemo.TILE_SIZE);
                squareFrom.setTranslateY((height-1-fromY)*ChessDemo.TILE_SIZE);

                Piece[][] boardState = gameEngine.getBoard().getBoardState();
                if (gameEngine.inCheck(boardState, !gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor())) {
                    for (int i = 0; i < boardState.length; i++){
                        for (int j = 0; j < boardState[0].length; j++){
                            if (boardState[i][j] instanceof King){
                                if (boardState[i][j].getColor() == !gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()){
                                    Rectangle check = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
                                    check.setFill(Color.valueOf("#F30000"));
                                    check.setOpacity(1);
                                    check.setTranslateX(i*ChessDemo.TILE_SIZE);
                                    check.setTranslateY((height-1-j)*ChessDemo.TILE_SIZE);
                                    lastMoveGroup.getChildren().add(check);
                                }
                            }
                        }
                    }
                }
                lastMoveGroup.getChildren().add(squareTo);
                lastMoveGroup.getChildren().add(squareFrom);
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

    private void specialMoves(int x, int y, int height, TestTile tile, Group hboxGroup, Group tileGroup, GameEngine gameEngine, TestTile[][] board) {
            if ((Math.abs(x-tile.getX()) == 2 ) && gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof King){
                if(x-tile.getX()>0) {
                    board[7][y].move(x-1, y, board);
                } else {
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
