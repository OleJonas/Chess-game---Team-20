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

class HighlightBox extends Pane {
    int x;
    int y;
    int height;
    double hboxOpacity = 0.7;
    String shapeOfBox = "circle";
    public HighlightBox(int x, int y, int height, Tile tile, Group hboxGroup, Group tileGroup, GameEngine gameEngine, Tile[][] board){
        this.x = x;
        this.y = y;
        this.height = height;
        relocate(x * ChessGame.TILE_SIZE, (height - 1 - y) * ChessGame.TILE_SIZE);
        if(shapeOfBox.equalsIgnoreCase("rectangle")) {
            Rectangle square = new Rectangle(ChessGame.TILE_SIZE, ChessGame.TILE_SIZE);
            square.setFill(Color.valueOf("#582"));
            square.setOpacity(hboxOpacity);
            getChildren().add(square);
        }else{
            Circle circle = new Circle(ChessGame.TILE_SIZE / 4);
            circle.setFill(Color.valueOf("582"));
            circle.setOpacity(hboxOpacity);
            circle.setTranslateX(ChessGame.TILE_SIZE/2);
            circle.setTranslateY(ChessGame.TILE_SIZE/2);
            getChildren().add(circle);
        }
        setOnMouseClicked(e->{
            specialMoves(x, y, height, tile, hboxGroup, tileGroup, gameEngine, board);
            ChessGame.myTurn = false;
            uploadMove(tile.getX(), tile.getY(), x, y);
            tile.move(x, y, board);
            int top=0;
            if(ChessGame.color) {
                top = height-1;
            }
            if(y==top && gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof Pawn){
                Queen newPiece = new Queen(ChessGame.color, x, y);
                ImageView tempimg = newPiece.getImageView();
                gameEngine.setPiece(newPiece, x, y);
                if(!ChessGame.color){
                    tempimg.getTransforms().add(new Rotate(180, ChessGame.TILE_SIZE/2, ChessGame.TILE_SIZE/2));
                }
                tile.setImageView(tempimg,
                        ChessGame.TILE_SIZE*(1-ChessGame.imageSize)/2, ChessGame.TILE_SIZE*(1-ChessGame.imageSize)/2);
            }
            if (gameEngine.isCheckmate(gameEngine.getBoard(), !gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor())) {
                if (gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()) {
                    System.out.println("Checkmate for White");
                    int[] elo = gameEngine.getElo(1000, 1000, 0);
                    System.out.println("New White elo: " +elo[0]+ "\nNew Black elo: " +elo[1]);
                }
                else {
                    System.out.println("Checkmate for Black");
                    int[] elo = gameEngine.getElo(1000, 1000, 1);
                    System.out.println("New White elo: " +elo[0]+ "\nNew Black elo: " +elo[1]);
                }
            }
            else if (gameEngine.isStalemate(gameEngine.getBoard(), !gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor())) {
                System.out.println("Stalemate");
                int[] elo = gameEngine.getElo(1000, 1000, 2);
                System.out.println("New White elo: " +elo[0]+ "\nNew Black elo: " +elo[1]);
            }
            else if(gameEngine.notEnoughPieces(gameEngine.getBoard())) {
                System.out.println("Remis");
                int[] elo = gameEngine.getElo(1200, 1000, 2);
                System.out.println("New White elo: " +elo[0]+ "\nNew Black elo: " +elo[1]);
            }
            //System.out.println("moved piece");
            //System.out.println(gameEngine.getBoard());
            ChessGame.movenr+=2;
            getChildren().clear();
            hboxGroup.getChildren().clear();
        });
    }

    public HighlightBox() {
        if(shapeOfBox.equalsIgnoreCase("rectangle")) {
            Rectangle square = new Rectangle(0, 0);
            getChildren().add(square);
        } else {
            Circle circle = new Circle(0);
            getChildren().add(circle);
        }
    }

    private void uploadMove(int fromX, int fromY, int toX, int toY){
        DBOps db = new DBOps();
        System.out.println("uploaded movenr: " + (ChessGame.movenr +1));
        db.exUpdate("INSERT INTO Move VALUES (" + ChessGame.gameID + ", " + (ChessGame.movenr +1) +", "+ fromX +", "+fromY+", "+toX+", "+toY+");");
    }


    private void specialMoves(int x, int y, int height, Tile tile, Group hboxGroup, Group tileGroup, GameEngine gameEngine, Tile[][] board) {
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
    }
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
