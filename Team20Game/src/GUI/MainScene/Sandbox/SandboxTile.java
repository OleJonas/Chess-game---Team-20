package GUI.MainScene.Sandbox;

import Game.GameEngine;
import GUI.GameScene.ChessGame;
import GUI.GameScene.HighlightBox;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
/**
 * <h1>SandboxTile</h1>
 * The purpose of this method is to store the logic for the chess pieces visually on the chessboard.
 * @since 08.04.2019
 * @author Team 20
 *
 */
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

    /**
     * Sets the saved positions of a piece to the parameters (x, y)
     * @param x Parameter for the saved x-coordinate.
     * @param y Parameter for the saved y-coordinate.
     */

    public void setPos(int x, int y){
        currentPositionX = x;
        currentPositionY = y;
    }

    /**
     * Method to get the saved x-position of the piece.
     * @return x-position of the object.
     */

    public int getX(){
        return currentPositionX;
    }

    /**
     * Method to get the saved y-position of the piece.
     * @return y-position of the object.
     */
    public int getY(){
        return currentPositionY;
    }

    /**
     * Method to visually set the image of the piece.
     * @param img imageview you want to attach to the piece(tile).
     * @param offsetX Parameter for how much the image is to be moved from the origin x-position.
     * @param offsetY Parameter for how much the image is to be moved from the origin y-position.
     * @return boolean value for if the method was succesful or not.
     */

    public boolean setImageView(ImageView img, double offsetX, double offsetY){
        if(img == null){
            return false;
        }

        img.setTranslateX(offsetX);
        img.setTranslateY(offsetY);

        getChildren().set(0,img);
        return true;
    }

    /**
     * Method to visually and internally moving the chess piece.
     * @param x Parameter for what x-coordinate to move the piece to.
     * @param y Parameter for what y-coordinate to move the piece to.
     * @param board the board it changes positions for.
     */

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