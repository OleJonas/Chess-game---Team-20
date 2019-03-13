package JavaFX;

import Game.GameEngine;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

class Tile extends StackPane {
    private ImageView imageView;
    private GameEngine gameEngine;

    private int height;

    private Group tileGroup;

    private int currentPositionX;
    private int currentPositionY;

    private double oldX, oldY;

    public Tile(int x, int y, boolean myColor , int height, GameEngine gameEngine, Group hboxGroup, Group tileGroup, Tile[][] board) {
        super.setWidth(ChessDemo.TILE_SIZE);
        setHeight(ChessDemo.TILE_SIZE);
        currentPositionX=x;
        currentPositionY=y;
        this.tileGroup = tileGroup;
        System.out.println(x +  ", " + y + ",,,");
        this.height = height;
        this.gameEngine = gameEngine;
        getChildren().add(new Rectangle());
        relocate(x * ChessDemo.TILE_SIZE , (height-1-y) * ChessDemo.TILE_SIZE) ;

        setOnMouseClicked(e->{
            hboxGroup.getChildren().clear();
            if(ChessDemo.myTurn) {
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
    public void move(int x, int y, Tile[][] board){
        oldX = x*ChessDemo.TILE_SIZE;
        oldY = (height-1-y)*ChessDemo.TILE_SIZE;
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
