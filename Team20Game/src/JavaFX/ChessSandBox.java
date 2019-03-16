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
import java.util.Timer;


public class ChessSandBox extends Application {

    public static final int TILE_SIZE = 80;

    public static final double imageSize = 0.8;

    public static boolean color = true;

    public static boolean myTurn = true;

    public static int movenr = 0;

    private GameEngine ge = new GameEngine(15, true);

    private final int HEIGHT = ge.getBoard().getBoardState().length;
    private final int WIDTH = ge.getBoard().getBoardState()[0].length;
    public static int gameID = 48;              //new Random().nextInt(500000);

    private final String darkTileColor = "#8B4513";
    private final String lightTileColor = "#FFEBCD";

    private boolean isDone = false;

    private TileSandbox[][] board = new TileSandbox[WIDTH][HEIGHT];

    private Group boardGroup = new Group();
    private Group tileGroup = new Group();
    private Group hboxGroup = new Group();

    public static void main(String[] args) {
        launch(args);
    }

    private Parent createContent() {
        Pane root = new Pane();
        
        root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Rectangle square = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
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
                    TileSandbox tile = new TileSandbox(x, y, myColor, HEIGHT, ge, hboxGroup, tileGroup, board);
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
    }
}

