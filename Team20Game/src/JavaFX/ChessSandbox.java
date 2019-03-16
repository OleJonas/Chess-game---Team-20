package JavaFX;
import Pieces.King;
import Pieces.Pawn;
import Pieces.Queen;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import Game.GameEngine;

import java.util.ArrayList;


public class ChessSandbox {
    public static final int TILE_SIZE = 60;
    public static final double imageSize = 0.6;

    private GameEngine ge = new GameEngine(15, true);

    private final int HEIGHT = ge.getBoard().getBoardState().length;
    private final int WIDTH = ge.getBoard().getBoardState()[0].length;

    private final String darkSandboxTileColor = "#8B4513";
    private final String lightSandboxTileColor = "#FFEBCD";
    private boolean isDone = false;

    private SandboxTile[][] board = new SandboxTile[WIDTH][HEIGHT];

    private Group boardGroup = new Group();
    private Group tileGroup = new Group();
    private Group hboxGroup = new Group();

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
                Rectangle square = new Rectangle(ChessSandbox.TILE_SIZE, ChessSandbox.TILE_SIZE);
                square.setFill((x + y) % 2 == 0 ? Color.valueOf(lightSandboxTileColor) : Color.valueOf(darkSandboxTileColor));
                square.relocate(x * ChessSandbox.TILE_SIZE, y * ChessSandbox.TILE_SIZE);
                boardGroup.getChildren().add(square);
                if (ge.getBoard().getBoardState()[x][y] != null) {
                    boolean myColor;
                    SandboxTile tile = new SandboxTile(x, y,HEIGHT, ge, hboxGroup, tileGroup, board);
                    tile.setImageView(ge.getBoard().getBoardState()[x][y].getImageView(), TILE_SIZE * (1 - imageSize) / 2, TILE_SIZE * (1 - imageSize) / 2);
                    board[x][y] = tile;
                    tileGroup.getChildren().add(tile);
                }
            }
        }
        root.getChildren().addAll(boardGroup, tileGroup, hboxGroup);
        return root;
    }

    public void removePiece(int x, int y) {
        tileGroup.getChildren().remove(board[x][y]);
        ge.removePiece(x, y);
    }

    public void enemyMove(int fromX, int fromY, int toX, int toY) {
        board[fromX][fromY].move(toX, toY, board);
    }

}

class SandboxHighlightBox extends Pane {
    int x;
    int y;
    int height;
    double hboxOpacity = 0.7;
    String shapeOfBox = "circle";
    public SandboxHighlightBox(int x, int y, int height, SandboxTile tile, Group hboxGroup, Group tileGroup, GameEngine gameEngine, SandboxTile[][] board){
        this.x = x;
        this.y = y;
        this.height = height;
        relocate(x * ChessSandbox.TILE_SIZE, (height - 1 - y) * ChessSandbox.TILE_SIZE);
        if(shapeOfBox.equalsIgnoreCase("rectangle")) {
            Rectangle square = new Rectangle(ChessSandbox.TILE_SIZE, ChessSandbox.TILE_SIZE);
            square.setFill(Color.valueOf("#582"));
            square.setOpacity(hboxOpacity);
            getChildren().add(square);
        }else{
            Circle circle = new Circle(ChessSandbox.TILE_SIZE / 4);
            circle.setFill(Color.valueOf("582"));
            circle.setOpacity(hboxOpacity);
            circle.setTranslateX(ChessSandbox.TILE_SIZE/2);
            circle.setTranslateY(ChessSandbox.TILE_SIZE/2);
            getChildren().add(circle);
        }
        setOnMouseClicked(e->{
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
                //System.out.println("En passant");
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
            tile.move(x, y, board);
            if (gameEngine.isCheckmate(gameEngine.getBoard(), !gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor())) {
                System.out.println("Sjakkmatt");
            }
            int top=0;
            //if(ChessSandbox.color) top = height-1;
            if(y==top && gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof Pawn){
                Queen newPiece = new Queen(true, x, y);
                ImageView tempimg = newPiece.getImageView();
                gameEngine.setPiece(newPiece, x, y);
                //if(!ChessSandbox.color) tempimg.getTransforms().add(new Rotate(180, ChessSandbox.TILE_SIZE/2, ChessSandbox.TILE_SIZE/2));
                tile.setImageView(tempimg,
                        ChessSandbox.TILE_SIZE*(1-ChessSandbox.imageSize)/2, ChessSandbox.TILE_SIZE*(1-ChessSandbox.imageSize)/2);
            }
            getChildren().clear();
            hboxGroup.getChildren().clear();
        });
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
    private Group tileGroup;
    private int currentPositionX;
    private int currentPositionY;
    private double oldX, oldY;
    
    public SandboxTile(int x, int y, int height, GameEngine gameEngine, Group hboxGroup, Group tileGroup, SandboxTile[][] board) {
        super.setWidth(ChessSandbox.TILE_SIZE);
        setHeight(ChessSandbox.TILE_SIZE);
        currentPositionX=x;
        currentPositionY=y;
        this.tileGroup = tileGroup;
        this.height = height;
        this.gameEngine = gameEngine;
        getChildren().add(new Rectangle());
        relocate(x * ChessSandbox.TILE_SIZE , (height-1-y) * ChessSandbox.TILE_SIZE) ;

        setOnMouseClicked(e->{
            hboxGroup.getChildren().clear();
            ArrayList<Integer> moves = gameEngine.validMoves(currentPositionX, currentPositionY);

            if(moves!=null&&moves.size()>0) {
                for (int i = 0; i < moves.size(); i += 2) {
                    SandboxHighlightBox box = new SandboxHighlightBox(moves.get(i), moves.get(i + 1), height,
                            this, hboxGroup, tileGroup, gameEngine, board);
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
        
        img.setTranslateX(offsetX);
        img.setTranslateY(offsetY);
        
        getChildren().set(0,img);
        return true;
    }
    public void move(int x, int y, SandboxTile[][] board){
        oldX = x*ChessSandbox.TILE_SIZE;
        oldY = (height-1-y)*ChessSandbox.TILE_SIZE;
        gameEngine.move(currentPositionX,currentPositionY,x,y);
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



