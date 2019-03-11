package JavaFX;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import javafx.scene.image.ImageView;
import java.awt.*;
import java.util.ArrayList;
import Game.GameEngine;


public class ChessDemo extends Application {

    public static final int TILE_SIZE = 100 ;
    public static final double imageSize = 1;
    public static boolean myTurn = true;
    private GameEngine ge = new GameEngine(15, true);

    private final int HEIGHT = ge.getBoard().getBoardState().length;
    private final int WIDTH = ge.getBoard().getBoardState()[0].length;

    private final String darkTileColor = "#8B4513";
    private final String lightTileColor = "#FFEBCD";

    private Tile[][] board = new Tile[8][8];

    private Group boardGroup = new Group();
    private Group tileGroup = new Group();
    private Group hboxGroup = new Group();

    public static void main(String[] args) {
        launch(args);
    }

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        root.getChildren().addAll(boardGroup, tileGroup, hboxGroup);
        for(int x = 0; x<WIDTH; x++){
            for(int y = 0; y<HEIGHT; y++){
                Rectangle square = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
                square.setFill((x+y)%2==0 ? Color.valueOf(darkTileColor): Color.valueOf(lightTileColor));
                square.relocate(x*ChessDemo.TILE_SIZE, y*ChessDemo.TILE_SIZE);
                boardGroup.getChildren().add(square);
                if(ge.getBoard().getBoardState()[x][y]!=null){
                    Tile tile = new Tile(x, y, HEIGHT, ge, hboxGroup, tileGroup, board);
                    tile.setImageView(ge.getBoard().getBoardState()[x][y].getImageView());
                    board[x][y] = tile;
                    tileGroup.getChildren().add(tile);
                }
            }
        }
        System.out.println(ge.getBoard());
        Rotate rotate180 = new Rotate(180);
        /*root.getTransforms().add(rotate180);
        root.setTranslateX(TILE_SIZE*WIDTH);
        root.setTranslateY(TILE_SIZE*HEIGHT);
        */
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
    private GameEngine gameEngine;

    private int height;

    private Group tileGroup;

    private int currentPositionX;
    private int currentPositionY;

    private double mouseX, mouseY;
    private double oldX, oldY;

    public Tile(int x, int y, int height, GameEngine gameEngine, Group hboxGroup, Group tileGroup, Tile[][] board) {
        super.setWidth(ChessDemo.TILE_SIZE);
        setHeight(ChessDemo.TILE_SIZE);
        currentPositionX=x;
        currentPositionY=y;
        this.tileGroup = tileGroup;
        System.out.println(x +  ", " + y + ",,,");
        this.height = height;
        this.gameEngine = gameEngine;
        relocate(x * ChessDemo.TILE_SIZE /*+ ChessDemo.TILE_SIZE * (ChessDemo.imageSize/16)*/, ((height-1-y) * ChessDemo.TILE_SIZE)) ;
        /*Rectangle square = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
        square.setFill(light ? Color.valueOf("#feb"): Color.valueOf("#582"));
        square.relocate(x*ChessDemo.TILE_SIZE, (height-1-y)*ChessDemo.TILE_SIZE);
        getChildren().add(square);
        */
        setOnMouseClicked(e->{
            if(ChessDemo.myTurn) {
                hboxGroup.getChildren().clear();
                ArrayList<Integer> moves = gameEngine.validMoves(currentPositionX, currentPositionY);

                if(moves!=null&&moves.size()>0) {
                    for (int i = 0; i < moves.size(); i += 2) {
                        HighlightBox box = new HighlightBox(moves.get(i), moves.get(i + 1), height,
                                this, hboxGroup, gameEngine, board);
                        hboxGroup.getChildren().add(box);
                    }
                }
            }
            /*
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
            */
        });
        setOnMouseDragged(e->{
            /*relocate(e.getSceneX() - mouseX + oldX - (ChessDemo.TILE_SIZE/2), e.getSceneY() - mouseY + oldY -(ChessDemo.TILE_SIZE/2));*/
        });
        setOnMouseReleased(e->{
           /* oldX = e.getSceneX();
            oldY = e.getSceneY();*/
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
    public boolean setImageView(ImageView img){
        if(img == null){
            return false;
        }
        getChildren().add(img);
        return true;
    }
    public void move(int x, int y, Tile[][] board){
        oldX = x*ChessDemo.TILE_SIZE;
        oldY = (height-1-y)*ChessDemo.TILE_SIZE;
        gameEngine.move(currentPositionX,currentPositionY,x,y);
        if(board[x][y]!=null){
            tileGroup.getChildren().remove(board[x][y]);
        }
        board[x][y] = this;
        board[currentPositionX][currentPositionY] = null;
        setPos(x,y);
        relocate(oldX, oldY);
    }
}
class HighlightBox extends Pane{
    int x;
    int y;
    int height;
    public HighlightBox(int x, int y, int height, Tile tile, Group hboxGroup, GameEngine gameEngine, Tile[][] board){
        this.x = x;
        this.y = y;
        this.height = height;
        relocate(x*ChessDemo.TILE_SIZE, (height-1-y)*ChessDemo.TILE_SIZE);
        Rectangle square = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
        square.setFill(Color.valueOf("#582"));
        square.setOpacity(0.8);
        getChildren().add(square);
        setOnMouseClicked(e->{
            tile.move(x, y, board);
            System.out.println("moved piece");
            System.out.println(gameEngine.getBoard());
            hboxGroup.getChildren().clear();
            //ChessDemo.myTurn = false;
        });
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
