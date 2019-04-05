package JavaFX.MainScene.Sandbox;

import Game.GameEngine;
import JavaFX.GameScene.ChessGame;
import JavaFX.GameScene.HighlightBox;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

class SandboxTile extends StackPane {
    private GameEngine gameEngine;
    private int height;
    private Group tileGroup;
    private int currentPositionX;
    private int currentPositionY;
    private double oldX, oldY;

    public SandboxTile(int x, int y, int height, GameEngine gameEngine, Group hboxGroup, Group tileGroup, Group selectedGroup, Group lastMoveGroup, SandboxTile[][] board) {
        super.setWidth(ChessGame.TILE_SIZE);
        setHeight(ChessGame.TILE_SIZE);
        currentPositionX=x;
        currentPositionY=y;
        this.tileGroup = tileGroup;
        this.height = height;
        this.gameEngine = gameEngine;
        getChildren().add(new Rectangle());
        relocate(x * ChessGame.TILE_SIZE , (height-1-y) * ChessGame.TILE_SIZE) ;

        setOnMouseClicked(e-> {
            selectedGroup.getChildren().clear();
            hboxGroup.getChildren().clear();
            Rectangle square = new Rectangle(ChessGame.TILE_SIZE, ChessGame.TILE_SIZE);
            square.setFill(Color.valueOf("#582"));
            square.setOpacity(0.7);
            square.setTranslateX(currentPositionX * ChessGame.TILE_SIZE);
            square.setTranslateY((height - 1 - currentPositionY) * ChessGame.TILE_SIZE);
            selectedGroup.getChildren().add(square);
            ArrayList<Integer> moves = gameEngine.validMoves(currentPositionX, currentPositionY);

            if (moves != null && moves.size() > 0) {
                for (int i = 0; i < moves.size(); i += 2) {
                    SandboxHighlightBox box = new SandboxHighlightBox(moves.get(i), moves.get(i + 1), height,
                            this, hboxGroup, tileGroup, selectedGroup, lastMoveGroup, gameEngine, board);
                    hboxGroup.getChildren().add(box);
                }
            } else if(moves.size() == 0){
                HighlightBox box = new HighlightBox();
                selectedGroup.getChildren().add(box);
                hboxGroup.getChildren().add(box);
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

    public void move(int x, int y, SandboxTile[][] board) {
        oldX = x * ChessGame.TILE_SIZE;
        oldY = (height - 1 - y) * ChessGame.TILE_SIZE;
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