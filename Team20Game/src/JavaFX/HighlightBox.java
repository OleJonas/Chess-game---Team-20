package JavaFX;

import Game.GameEngine;
import Pieces.King;
import Pieces.Pawn;
import Pieces.Queen;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

class HighlightBox extends Pane {
    int x;
    int y;
    int height;
    double hboxOpacity = 0.5;
    public HighlightBox(int x, int y, int height, Tile tile, Group hboxGroup, GameEngine gameEngine, Tile[][] board){
        this.x = x;
        this.y = y;
        this.height = height;
        relocate(x*ChessDemo.TILE_SIZE, (height-1-y)*ChessDemo.TILE_SIZE);
        Rectangle square = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
        square.setFill(Color.valueOf("#582"));
        square.setOpacity(hboxOpacity);
        getChildren().add(square);
        setOnMouseClicked(e->{
            if ((Math.abs(x-tile.getX()) == 2 ) && gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof King){
                if(x-tile.getX()>0) {
                    board[7][y].move(x-1, y, board);
                }else{
                    board[0][y].move(x+1, y, board);
                }
                King king = (King)gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()];
                king.setCanCastle(false);
                System.out.println("Rokkade");
            }
            if (Math.abs(y-tile.getY()) == 2 && gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof Pawn) {
                Pawn pawn = (Pawn) gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()];
                pawn.setEnPassant(true);
                System.out.println("En passant");
            }
            if (tile.getX() + 1 < 8) {
                if (gameEngine.getBoard().getBoardState()[tile.getX()+1][tile.getY()] instanceof Pawn) {
                    if (gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()) {
                        System.out.println("Hallo1");
                        Pawn pawn = (Pawn) gameEngine.getBoard().getBoardState()[tile.getX()+1][tile.getY()];
                        if (pawn.getColor() != gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()) {
                            System.out.println("Hallo2");
                            if (pawn.getEnPassant()) {
                                System.out.println("Hallo3");
                            }
                        }
                    }
                    else {
                        Pawn pawn = (Pawn) gameEngine.getBoard().getBoardState()[tile.getX()+1][tile.getY()];
                        if (pawn.getColor() != gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()) {
                            if (pawn.getEnPassant()) {

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

                            }
                        }
                    }
                    else {
                        Pawn pawn = (Pawn) gameEngine.getBoard().getBoardState()[tile.getX()-1][tile.getY()];
                        if (pawn.getColor() != gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()) {
                            if (pawn.getEnPassant()) {

                            }
                        }
                    }
                }
            }

            tile.move(x, y, board);
            int top=0;
            if(ChessDemo.color) {
                top = height-1;
            }
            if(y==top && gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof Pawn){
                Queen newPiece = new Queen(ChessDemo.color, x, y);
                ImageView tempimg = newPiece.getImageView();
                gameEngine.setPiece(newPiece, x, y);
                if(!ChessDemo.color){
                    tempimg.getTransforms().add(new Rotate(180, ChessDemo.TILE_SIZE/2, ChessDemo.TILE_SIZE/2));
                }
                tile.setImageView(tempimg,
                        ChessDemo.TILE_SIZE*(1-ChessDemo.imageSize)/2, ChessDemo.TILE_SIZE*(1-ChessDemo.imageSize)/2);
            }
            System.out.println("moved piece");
            System.out.println(gameEngine.getBoard());
            getChildren().clear();
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
