package GUI.GameScene;

import Game.GameEngine;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

/**
 * <h1>Tile</h1>
 * The purpose of this method is to store the logic for the tiles on the chessboard.
 * @since 08.04.2019
 * @author Team 20
 *
 */
public class Tile extends StackPane {
    private GameEngine gameEngine;
    private int height;
    private boolean myColor;
    private Group tileGroup;
    private Group lastMoveGroup;
    private int currentPositionX;
    private int currentPositionY;
    private double oldX, oldY;

    /**
     * This is the constructor for the Tile class.
     * @param x = x-coordinate of tile
     * @param y = y-coordinate of tile
     * @param myColor = color of the player
     * @param height = the height of the chess board
     * @param gameEngine = the game engine, which contains the methods for the game logic
     * @param hboxGroup = the circles indicating valid moves
     * @param tileGroup = the pieces on the board
     * @param selectedGroup = the tiles are marked when choosing a piece
     * @param lastMoveGroup = the tiles who are marked after doing a move
     * @param board = the chess board
     */
    public Tile(int x, int y, boolean myColor , int height, GameEngine gameEngine, Group hboxGroup, Group tileGroup, Group selectedGroup, Group lastMoveGroup, Tile[][] board) {
        super.setWidth(ChessGame.TILE_SIZE);
        setHeight(ChessGame.TILE_SIZE);
        currentPositionX=x;
        currentPositionY=y;
        this.myColor = myColor;
        this.tileGroup = tileGroup;
        this.height = height;
        this.gameEngine = gameEngine;
        this.lastMoveGroup = lastMoveGroup;
        getChildren().add(new Rectangle());
        relocate(x * ChessGame.TILE_SIZE , (height-1-y) * ChessGame.TILE_SIZE) ;

        setOnMouseClicked(e->{
            selectedGroup.getChildren().clear();
            hboxGroup.getChildren().clear();
            Rectangle square = new Rectangle(ChessGame.TILE_SIZE, ChessGame.TILE_SIZE);
            square.setFill(Color.valueOf("#582"));
            square.setOpacity(0.7);
            square.setTranslateX(currentPositionX*ChessGame.TILE_SIZE);
            square.setTranslateY((height-1-currentPositionY)*ChessGame.TILE_SIZE);
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

    /**
     * @return The color of the players pieces
     */
    public boolean getMyColor() {
        return myColor;
    }

    /**
     * Sets the position of a piece to the parameters (x, y)
     * @param x = x-coordinate of the tile the player wants to move a piece to
     * @param y = y-coordinate of the tile the player wants to move a piece to
     */
    public void setPos(int x, int y){
        currentPositionX = x;
        currentPositionY = y;
    }

    /**
     * @return x-coordinate of a piece
     */
    public int getX(){
        return currentPositionX;
    }

    /**
     * @return y-coordinate of a piece
     */
    public int getY(){
        return currentPositionY;
    }

    /**
     * This methods sets an image of a piece. If the image already exists, the method replaces the image.
     * @param img = the image itself
     * @param offsetX = the offset of x-coordinate
     * @param offsetY = the offset of y-coordinate
     * @return whether the image was set or not
     */
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

    /**
     * This methods is used for moving a piece
     * @param x = x-coordinate of piece
     * @param y = y-ccordinate of piece
     * @param board = the chess board
     * @param castle = whether the king can castle or not
     */
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
        gameEngine.move(currentPositionX, currentPositionY, x, y, ChessGame.lastMove);
        if (board[x][y] != null) {
            tileGroup.getChildren().remove(board[x][y]);
        }
        board[x][y] = this;
        board[currentPositionX][currentPositionY] = null;
        setPos(x, y);
        relocate(oldX, oldY);
    }
}