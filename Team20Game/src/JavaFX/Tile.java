package JavaFX;

import Game.GameEngine;
import JavaFX.HighlightBox;
import JavaFX.ChessGame;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

class Tile extends StackPane {
    private ImageView imageView;
    private GameEngine gameEngine;

    private int height;
    private boolean myColor;

    private Group tileGroup;
    private Group lastMoveGroup;

    private int currentPositionX;
    private int currentPositionY;

    private double oldX, oldY;

    public Tile(int x, int y, boolean myColor , int height, GameEngine gameEngine, Group hboxGroup, Group tileGroup, Group selectedGroup, Group lastMoveGroup, Tile[][] board) {
        super.setWidth(ChessDemo.TILE_SIZE);
        setHeight(ChessDemo.TILE_SIZE);
        currentPositionX=x;
        currentPositionY=y;
        this.myColor = myColor;
        this.tileGroup = tileGroup;
        this.height = height;
        this.gameEngine = gameEngine;
        this.lastMoveGroup = lastMoveGroup;
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
            if(ChessGame.myTurn && myColor) {
                ArrayList<Integer> moves = gameEngine.validMoves(currentPositionX, currentPositionY);

                if(moves!=null&&moves.size()>0) {
                    for (int i = 0; i < moves.size(); i += 2) {
                        HighlightBox box = new HighlightBox(moves.get(i), moves.get(i + 1), height,
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
        if(!ChessGame.color){
            img.setTranslateX(-offsetX);
            img.setTranslateY(-offsetY);
        }else{
            img.setTranslateX(offsetX);
            img.setTranslateY(offsetY);
        }
        getChildren().set(0,img);
        return true;
    }
    public void move(int x, int y, Tile[][] board, boolean castle) {
        if(!castle) {
            Rectangle squareFrom = new Rectangle(ChessGame.TILE_SIZE, ChessGame.TILE_SIZE);
            squareFrom.setFill(Color.valueOf("#582"));
            squareFrom.setOpacity(0.6);
            squareFrom.setTranslateX(currentPositionX * ChessGame.TILE_SIZE);
            squareFrom.setTranslateY((height - 1 - currentPositionY) * ChessGame.TILE_SIZE);

            Rectangle squareTo = new Rectangle(ChessGame.TILE_SIZE, ChessGame.TILE_SIZE);
            squareTo.setFill(Color.valueOf("#582"));
            squareTo.setOpacity(0.9);
            squareTo.setTranslateX(x * ChessGame.TILE_SIZE);
            squareTo.setTranslateY((height - 1 - y) * ChessGame.TILE_SIZE);
            lastMoveGroup.getChildren().addAll(squareFrom, squareTo);
        }

        oldX = x * ChessGame.TILE_SIZE;
        oldY = (height - 1 - y) * ChessGame.TILE_SIZE;
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
