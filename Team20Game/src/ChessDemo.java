import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import javafx.scene.image.ImageView;
import java.awt.*;
import java.io.FileInputStream;

public class ChessDemo extends Application {

    public static final int TILE_SIZE = 100;
    private GameEngine ge = new GameEngine(15, true);

    private final int HEIGHT = ge.getBoard().getBoardState().length;
    private final int WIDTH = ge.getBoard().getBoardState()[0].length;

    private Tile[][] board = new Tile[HEIGHT][WIDTH];

    private Group tileGroup = new Group();

    public static void main(String[] args) {
        launch(args);
    }

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        root.getChildren().addAll(tileGroup);
        for(int y = 0; y<HEIGHT; y++){
            for(int x = 0; x<WIDTH; x++){
                Tile tile = new Tile((x+y)%2==0, x, y);
                if(ge.getBoard().getBoardState()[x][y]!=null){
                    tile.setImageView(ge.getBoard().getBoardState()[x][y].getImageView());
                }
                board[x][y] = tile;
                tileGroup.getChildren().add(tile);
            }
        }
        return root;
    }
    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Chess Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
class Tile extends StackPane {
    private ImageView imageView;
    public Tile(boolean light, int x, int y) {
        super.setWidth(ChessDemo.TILE_SIZE);
        setHeight(ChessDemo.TILE_SIZE);
        relocate(x * ChessDemo.TILE_SIZE, y * ChessDemo.TILE_SIZE);
        Rectangle square = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
        square.setFill(light ? Color.valueOf("#feb"): Color.valueOf("#582"));
        square.relocate(x*ChessDemo.TILE_SIZE, y*ChessDemo.TILE_SIZE);
        getChildren().add(square);

    }
    public boolean setImageView(ImageView img){
        if(img == null){
            return false;
        }
        this.imageView = img;
        getChildren().add(imageView);
        return true;
    }
}
