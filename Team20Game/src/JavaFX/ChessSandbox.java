package JavaFX;
import Database.DBOps;
import Game.GameLogic;
import Pieces.*;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import Game.GameEngine;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;

//import static JavaFX.MainScene.showMainScene;


public class ChessSandbox {

    private Timer timer;

    public static int TILE_SIZE = JavaFX.ChessGame.TILE_SIZE;

    public static final double imageSize = JavaFX.ChessGame.imageSize;

    public static boolean color = true;

    public static boolean myTurn = true;

    public static boolean myColor = true;

    public static int movenr = 0;

    public static boolean lastMove = true;

    private GameEngine ge = new GameEngine(0,0);


    private final int HEIGHT = ge.getBoard().getBoardState().length;
    private final int WIDTH = ge.getBoard().getBoardState()[0].length;

    private final String darkTileColor = Settings.darkTileColor;
    private final String lightTileColor = Settings.lightTileColor;

    private boolean isDone = false;

    private SandboxTile[][] board = new SandboxTile[WIDTH][HEIGHT];

    private Group boardGroup = new Group();
    private Group tileGroup = new Group();
    private Group hboxGroup = new Group();
    private Group selectedPieceGroup = new Group();
    private Group lastMoveGroup = new Group();

    public Parent createContent() {
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
                ChessDemo.setOnMouseClicked(square, hboxGroup, selectedPieceGroup);
                square.setFill((x + y) % 2 == 0 ? Color.valueOf(lightTileColor) : Color.valueOf(darkTileColor));
                square.relocate(x * ChessDemo.TILE_SIZE, y * ChessDemo.TILE_SIZE);
                boardGroup.getChildren().add(square);
                if (ge.getBoard().getBoardState()[x][y] != null) {
                    boolean myColor;
                    myColor = ChessDemo.changeColor(x, y, color, ge);
                    SandboxTile tile = new SandboxTile(x, y, myColor, HEIGHT, ge, hboxGroup, tileGroup,selectedPieceGroup, lastMoveGroup, board);
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
    static boolean setUpSkin(){
        DBOps db = new DBOps();
        ChessGame.skin = db.exQuery("SELECT skinName FROM UserSettings WHERE user_id = " + Login.userID + ";", 1).get(0);
        return true;
    }
}

class SandboxHighlightBox extends Pane{
    int x;
    int y;
    int height;
    double hboxOpacity = 0.7;
    String shapeOfBox = "circle";

    public SandboxHighlightBox(int x, int y, int height, SandboxTile tile, Group hboxGroup, Group tileGroup, Group selectedGroup, Group lastMoveGroup, GameEngine gameEngine, SandboxTile[][] board){
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
            if (gameEngine.getBoard().getBoardState()[x][y] != null) {
                gameEngine.getBoard().addTakenPiece(gameEngine.getBoard().getBoardState()[x][y]);
            }
            tile.move(x, y, board);
            GameLogic.getDisplayPieces(gameEngine.getBoard());
            ChessSandbox.lastMove = gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor();
            int top=0;
            if((tile.getMyColor()&&ChessDemo.color)||!tile.getMyColor() && !ChessDemo.color) {
                top = height - 1;
            }

            if(y==top && gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof Pawn){
                PawnChangeChoiceBox pawnChange = new PawnChangeChoiceBox();
                pawnChange.Display(gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor());
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
            if (!(gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof Pawn)) {
                gameEngine.setMoveCounter(false);
                if (gameEngine.getMoveCounter() == 100) {
                    System.out.println("Remis");
                    int[] elo = gameEngine.getElo(1000, 1000, 2);
                    System.out.println("New White elo: " +elo[0]+ "\nNew Black elo: " +elo[1]);
                }
            } else {
                gameEngine.setMoveCounter(true);
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

    public SandboxHighlightBox() {
        if(shapeOfBox.equalsIgnoreCase("rectangle")) {
            Rectangle square = new Rectangle(0, 0);
            getChildren().add(square);
        } else {
            Circle circle = new Circle(0);
            getChildren().add(circle);
        }
    }

    private void specialMoves(int x, int y, int height, SandboxTile tile, Group hboxGroup, Group tileGroup, GameEngine gameEngine, SandboxTile[][] board) {
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
                        if (pawn.getEnPassant() && x == tile.getX() + 1) {
                            //System.out.println("Hallo3");
                            tileGroup.getChildren().remove(board[tile.getX()+1][tile.getY()]);
                            gameEngine.removePiece(tile.getX()+1, tile.getY());
                            gameEngine.getBoard().addTakenPiece(new Pawn(!gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor(), 0, 0));
                        }
                    }
                }
                else {
                    Pawn pawn = (Pawn) gameEngine.getBoard().getBoardState()[tile.getX()+1][tile.getY()];
                    if (pawn.getColor() != gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()) {
                        if (pawn.getEnPassant() && x == tile.getX() + 1) {
                            tileGroup.getChildren().remove(board[tile.getX()+1][tile.getY()]);
                            gameEngine.removePiece(tile.getX()+1, tile.getY());
                            gameEngine.getBoard().addTakenPiece(new Pawn(!gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor(), 0, 0));
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
                        if (pawn.getEnPassant() && x == tile.getX() - 1) {
                            tileGroup.getChildren().remove(board[tile.getX()-1][tile.getY()]);
                            gameEngine.removePiece(tile.getX()-1, tile.getY());
                            gameEngine.getBoard().addTakenPiece(new Pawn(!gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor(), 0, 0));
                        }
                    }
                }
                else {
                    Pawn pawn = (Pawn) gameEngine.getBoard().getBoardState()[tile.getX()-1][tile.getY()];
                    if (pawn.getColor() != gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()) {
                        if (pawn.getEnPassant() && x == tile.getX() - 1) {
                            tileGroup.getChildren().remove(board[tile.getX()-1][tile.getY()]);
                            gameEngine.removePiece(tile.getX()-1, tile.getY());
                            gameEngine.getBoard().addTakenPiece(new Pawn(!gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor(), 0, 0));
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

class SandboxTile extends StackPane {
    private ImageView imageView;
    private GameEngine gameEngine;

    private int height;
    private boolean myColor;

    private Group tileGroup;

    private int currentPositionX;
    private int currentPositionY;

    private double oldX, oldY;

    public SandboxTile(int x, int y, boolean myColor , int height, GameEngine gameEngine, Group hboxGroup, Group tileGroup, Group selectedGroup, Group lastMoveGroup, SandboxTile[][] board) {
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
            square.setFill(Color.valueOf("#582"));
            square.setOpacity(0.7);
            square.setTranslateX(currentPositionX*ChessDemo.TILE_SIZE);
            square.setTranslateY((height-1-currentPositionY)*ChessDemo.TILE_SIZE);
            selectedGroup.getChildren().add(square);
            if(ChessDemo.myTurn) {
                ArrayList<Integer> moves = gameEngine.validMoves(currentPositionX, currentPositionY);

                if(moves!=null&&moves.size()>0) {
                    for (int i = 0; i < moves.size(); i += 2) {
                        SandboxHighlightBox box = new SandboxHighlightBox(moves.get(i), moves.get(i + 1), height,
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
        TestTile.translator(img, offsetX, offsetY);
        getChildren().set(0,img);
        return true;
    }
    public void move(int x, int y, SandboxTile[][] board) {
        oldX = x * ChessDemo.TILE_SIZE;
        oldY = (height - 1 - y) * ChessDemo.TILE_SIZE;
        gameEngine.move(currentPositionX, currentPositionY, x, y, ChessSandbox.lastMove);
        if (board[x][y] != null) {
            tileGroup.getChildren().remove(board[x][y]);
        }
        board[x][y] = this;
        board[currentPositionX][currentPositionY] = null;
        setPos(x, y);
        relocate(oldX, oldY);
    }
}

@SuppressWarnings("Duplicates")
class PawnChangeChoiceBox{
    static String choice;

    public static void Display(boolean color){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Promotion");

        Label label = new Label("Choose your new \n           piece:");
        label.setFont(Font.font("Copperplate", 22));
        label.setStyle("-fx-font-weight: bold");
        label.setTextFill(Color.WHITE);

        Button pickBishopButton = new Button();
        Button pickKnightButton = new Button();
        Button pickQueenButton = new Button();
        Button pickRookButton = new Button();

        if(color){
            Image pickBishopImage = new Image("Images/chessPieces/Standard/w_bishop_1x_ns.png",60, 60, true, true);
            Image pickKnightImage = new Image("Images/chessPieces/Standard/w_knight_1x_ns.png", 60, 60, true, true);
            Image pickQueenImage = new Image("Images/chessPieces/Standard/w_queen_1x_ns.png", 60, 60, true, true);
            Image pickRookImage = new Image("Images/chessPieces/Standard/w_rook_1x_ns.png", 60, 60, true, true);

            ImageView bishopImageView = new ImageView(pickBishopImage);
            ImageView knightImageView = new ImageView(pickKnightImage);
            ImageView queenImageView = new ImageView(pickQueenImage);
            ImageView rookImageView = new ImageView(pickRookImage);

            pickBishopButton.setGraphic(bishopImageView);
            pickKnightButton.setGraphic(knightImageView);
            pickQueenButton.setGraphic(queenImageView);
            pickRookButton.setGraphic(rookImageView);
        } else if (!color) {
            Image pickBishopImage = new Image("Images/chessPieces/Standard/b_bishop_1x_ns.png",60, 60, true, true);
            Image pickKnightImage = new Image("Images/chessPieces/Standard/b_knight_1x_ns.png", 60, 60, true, true);
            Image pickQueenImage = new Image("Images/chessPieces/Standard/b_queen_1x_ns.png", 60, 60, true, true);
            Image pickRookImage = new Image("Images/chessPieces/Standard/b_rook_1x_ns.png", 60, 60, true, true);

            ImageView bishopImageView = new ImageView(pickBishopImage);
            ImageView knightImageView = new ImageView(pickKnightImage);
            ImageView queenImageView = new ImageView(pickQueenImage);
            ImageView rookImageView = new ImageView(pickRookImage);

            pickBishopButton.setGraphic(bishopImageView);
            pickKnightButton.setGraphic(knightImageView);
            pickQueenButton.setGraphic(queenImageView);
            pickRookButton.setGraphic(rookImageView);
        }

        pickBishopButton.setPrefSize(80, 80);
        pickKnightButton.setPrefSize(80, 80);
        pickQueenButton.setPrefSize(80, 80);
        pickRookButton.setPrefSize(80, 80);

        pickBishopButton.setOnAction(e -> {choice = "Bishop"; window.close();});
        pickKnightButton.setOnAction(e -> {choice = "Knight"; window.close();});
        pickQueenButton.setOnAction(e -> {choice = "Queen"; window.close();});
        pickRookButton.setOnAction(e -> {choice = "Rook"; window.close();});

        GridPane mainLayout = new GridPane();
        mainLayout.setHgap(10);
        mainLayout.setVgap(10);
        mainLayout.setPadding(new Insets(10, 20, 10, 10));
        mainLayout.getColumnConstraints().add(new ColumnConstraints(100));
        mainLayout.getColumnConstraints().add(new ColumnConstraints(100));
        mainLayout.add(label, 0, 0, 2, 1);
        mainLayout.setHalignment(label, HPos.CENTER);
        mainLayout.add(pickQueenButton, 0,1);
        mainLayout.setHalignment(pickQueenButton, HPos.CENTER);
        mainLayout.add(pickKnightButton, 1, 1);
        mainLayout.setHalignment(pickKnightButton, HPos.CENTER);
        mainLayout.add(pickBishopButton, 0,2);
        mainLayout.setHalignment(pickBishopButton, HPos.CENTER);
        mainLayout.add(pickRookButton, 1, 2);
        mainLayout.setHalignment(pickRookButton, HPos.CENTER);
        mainLayout.setStyle("-fx-background-color: #404144;");

        Scene scene = new Scene(mainLayout, 230, 260);
        window.setScene(scene);
        window.showAndWait();
    }

}

@SuppressWarnings("Duplicates")
class FinishedGameResetAlert{

    public static void Display(){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("");

        Label label = new Label("Checkmate!");
        label.setFont(Font.font("Copperplate", 24));
        label.setStyle("-fx-font-weight: bold");
        label.setTextFill(Color.WHITE);

        Button startOverButton = new Button("Start over");
        startOverButton.setOnAction(e -> {
            //showMainScene();
            window.close();
        });

        GridPane mainLayout = new GridPane();
        mainLayout.setHgap(10);
        mainLayout.setVgap(20);
        mainLayout.setPadding(new Insets(30, 60, 30, 60));
        mainLayout.add(label, 0, 0);
        mainLayout.setHalignment(label, HPos.CENTER);
        mainLayout.add(startOverButton, 0, 1);
        mainLayout.setHalignment(startOverButton, HPos.CENTER);
        mainLayout.setStyle("-fx-background-color: #404144;");

        Scene scene = new Scene(mainLayout, 260, 150);
        window.setScene(scene);
        window.showAndWait();
    }
}

//Choicebox with listview
/*
class PawnChangeChoiceBox{
    static String choice;

    public static void Display(){
        Stage window = new Stage();
        choice = "Queen";

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Promotion");

        Label label = new Label("Choose your new piece:");
        label.setFont(Font.font("Copperplate", 18));
        label.setStyle("-fx-font-weight: bold");
        label.setTextFill(Color.WHITE);

        ListView<String> pieces = new ListView<>();
        pieces.getItems().addAll("Queen", "Knight", "Bishop", "Rook");

        Label comment = new Label("");
        comment.setTextFill(Color.RED);

        Button choosePieceButton = new Button("Choose");
        choosePieceButton.setOnAction(e -> {
            choice = pieces.getSelectionModel().getSelectedItem();
            if(choice == null) {
                comment.setText("You have to choose one!");
            }else{
                window.close();
            }
        });

        GridPane mainLayout = new GridPane();
        mainLayout.setHgap(10);
        mainLayout.setVgap(10);
        mainLayout.setPadding(new Insets(10, 20, 10, 10));
        mainLayout.getColumnConstraints().add(new ColumnConstraints(115));
        mainLayout.getColumnConstraints().add(new ColumnConstraints(115));
        mainLayout.add(label, 0, 0, 2, 1);
        mainLayout.setHalignment(label, HPos.CENTER);
        mainLayout.add(pieces, 0, 1, 2, 1);
        mainLayout.add(comment, 0, 2, 2, 1);
        mainLayout.add(choosePieceButton, 1, 2);
        mainLayout.setHalignment(choosePieceButton, HPos.RIGHT);
        mainLayout.setStyle("-fx-background-color: #404144;");

        Scene scene = new Scene(mainLayout, 260, 250);
        window.setScene(scene);
        window.showAndWait();
    }

}

 */



