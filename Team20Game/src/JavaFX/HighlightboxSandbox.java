package JavaFX;

import Database.DBOps;
import Game.GameEngine;
import Pieces.King;
import Pieces.Pawn;
import Pieces.Queen;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

class HighlightboxSandbox extends Pane {
    int x;
    int y;
    int height;
    double hboxOpacity = 0.7;
    String shapeOfBox = "circle";
    public HighlightboxSandbox(int x, int y, int height, TileSandbox tile, Group hboxGroup, Group tileGroup, GameEngine gameEngine, TileSandbox[][] board){
        this.x = x;
        this.y = y;
        this.height = height;
        relocate(x * ChessDemo.TILE_SIZE, (height - 1 - y) * ChessDemo.TILE_SIZE);
        if(shapeOfBox.equalsIgnoreCase("rectangle")) {
            Rectangle square = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
            square.setFill(Color.valueOf("#582"));
            square.setOpacity(hboxOpacity);
            getChildren().add(square);
        }else{
            Circle circle = new Circle(ChessDemo.TILE_SIZE / 4);
            circle.setFill(Color.valueOf("582"));
            circle.setOpacity(hboxOpacity);
            circle.setTranslateX(ChessDemo.TILE_SIZE/2);
            circle.setTranslateY(ChessDemo.TILE_SIZE/2);
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
            ChessDemo.myTurn = false;
            tile.move(x, y, board);
            if (gameEngine.isCheckmate(gameEngine.getBoard(), !gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor())) {
                System.out.println("Sjakkmatt");
            }
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
            //System.out.println("moved piece");
            //System.out.println(gameEngine.getBoard());
            ChessDemo.movenr+=2;
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
