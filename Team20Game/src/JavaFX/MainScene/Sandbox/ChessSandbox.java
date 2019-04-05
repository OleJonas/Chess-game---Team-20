package JavaFX.MainScene.Sandbox;
import Database.DBOps;
import JavaFX.GameScene.ChessGame;
import JavaFX.LoginScreen.Login;
import JavaFX.MainScene.Settings;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import Game.GameEngine;
import javafx.scene.transform.Rotate;

public class ChessSandbox {

    public static int TILE_SIZE = ChessGame.TILE_SIZE;
    public static final double imageSize = ChessGame.imageSize;
    public static boolean color = true;
    public static boolean myTurn = true;
    public static int movenr = 0;
    public static boolean lastMove = true;
    private GameEngine ge = new GameEngine(0);
    private final int HEIGHT = ge.getBoard().getBoardState().length;
    private final int WIDTH = ge.getBoard().getBoardState()[0].length;
    private final String darkTileColor = Settings.darkTileColor;
    private final String lightTileColor = Settings.lightTileColor;
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
                Rectangle square = new Rectangle(ChessGame.TILE_SIZE, ChessGame.TILE_SIZE);
                ChessGame.setOnMouseClicked(square, hboxGroup, selectedPieceGroup);
                square.setFill((x + y) % 2 == 0 ? Color.valueOf(lightTileColor) : Color.valueOf(darkTileColor));
                square.relocate(x * ChessGame.TILE_SIZE, y * ChessGame.TILE_SIZE);
                boardGroup.getChildren().add(square);
                if (ge.getBoard().getBoardState()[x][y] != null) {
                    SandboxTile tile = new SandboxTile(x, y, HEIGHT, ge, hboxGroup, tileGroup,selectedPieceGroup, lastMoveGroup, board);
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
        board[fromX][fromY].move(toX, toY, board);
    }

    public static boolean setUpSkin(){
        DBOps db = new DBOps();
        ChessGame.skin = db.exQuery("SELECT skinName FROM UserSettings WHERE user_id = " + Login.userID + ";", 1).get(0);
        return true;
    }

    public static boolean changeColor(int x, int y, boolean color, GameEngine ge) {
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
        return myColor;
    }
}