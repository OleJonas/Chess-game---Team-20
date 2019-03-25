package JavaFX;

import Database.DBOps;
import Game.GameEngine;
import Pieces.*;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

class HighlightBox extends Pane{
    int x;
    int y;
    int height;
    double hboxOpacity = 0.7;
    boolean mode = true;
    boolean firstMove = true;
    String shapeOfBox = "circle";


    public HighlightBox(int x, int y, int height, Tile tile, Group hboxGroup, Group tileGroup, Group selectedGroup, Group lastMoveGroup, GameEngine gameEngine, Tile[][] board){
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
            Circle circle = new Circle(ChessDemo.TILE_SIZE / 5);
            circle.setFill(Color.valueOf("582"));
            circle.setOpacity(hboxOpacity);
            circle.setTranslateX(ChessDemo.TILE_SIZE/2);
            circle.setTranslateY(ChessDemo.TILE_SIZE/2);
            getChildren().add(circle);

            Rectangle square = new Rectangle(ChessDemo.TILE_SIZE*0.7, ChessDemo.TILE_SIZE*0.7);
            square.setOpacity(0);
            getChildren().add(square);
        }
        setOnMouseClicked(e->{
            specialMoves(x, y, height, tile, hboxGroup, tileGroup, gameEngine, board);
            ChessGame.myTurn = false;

            int fromX = tile.getX();
            int fromY = tile.getY();
            int toX = x;
            int toY = y;
            int totWhites = gameEngine.myPieces(gameEngine.getBoard(), true)[6];
            int totBlacks = gameEngine.myPieces(gameEngine.getBoard(), false)[6];
            tile.move(x, y, board);
            int updatedWhites = gameEngine.myPieces(gameEngine.getBoard(), true)[6];
            int updatedBlacks = gameEngine.myPieces(gameEngine.getBoard(), false)[6];
            int top=0;

            if(ChessGame.color) {
                top = height - 1;
            }

            if(y==top && gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof Pawn){
                PawnChangeChoiceBox pawnChange = new PawnChangeChoiceBox();
                pawnChange.Display(ChessDemo.color);
                Piece newPiece = null;
                boolean pieceColor = ChessGame.color?tile.getMyColor():!tile.getMyColor();
                if (PawnChangeChoiceBox.choice.equals("Queen")) {
                    newPiece = new Queen(pieceColor, x, y);
                    toY = 8;
                } else if (PawnChangeChoiceBox.choice.equals("Rook")) {
                    newPiece = new Rook(pieceColor, x, y);
                    toY = 10;
                } else if (PawnChangeChoiceBox.choice.equals("Bishop")) {
                    newPiece = new Bishop(pieceColor, x, y);
                    toY = 11;
                } else if (PawnChangeChoiceBox.choice.equals("Knight")) {
                    newPiece = new Knight(pieceColor, x, y);
                    toY = 9;
                }
                ImageView tempimg = newPiece.getImageView();
                gameEngine.setPiece(newPiece, x, y);

                if(!ChessGame.color){
                    tempimg.getTransforms().add(new Rotate(180, ChessDemo.TILE_SIZE/2, ChessDemo.TILE_SIZE/2));
                }
                tile.setImageView(tempimg,
                        ChessDemo.TILE_SIZE*(1-ChessDemo.imageSize)/2, ChessDemo.TILE_SIZE*(1-ChessDemo.imageSize)/2);
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
            if (gameEngine.isMoveRepetition()) {
                System.out.println("Repetisjon");
            }

            if(gameEngine.notEnoughPieces(gameEngine.getBoard())) {
                System.out.println("Remis");
                int[] elo = gameEngine.getElo(1200, 1000, 2);
                System.out.println("New White elo: " +elo[0]+ "\nNew Black elo: " +elo[1]);
            }
            if (!(gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof Pawn) && ((totWhites+totBlacks) == (updatedWhites+updatedBlacks))) {
                gameEngine.setMoveCounter(false);
                if (gameEngine.getMoveCounter() == 100) {
                    System.out.println("Remis");
                    int[] elo = gameEngine.getElo(1000, 1000, 2);
                    System.out.println("New White elo: " +elo[0]+ "\nNew Black elo: " +elo[1]);
                }
            } else {
                gameEngine.setMoveCounter(true);
            }

            getChildren().clear();
            hboxGroup.getChildren().clear();
            lastMoveGroup.getChildren().clear();
            selectedGroup.getChildren().clear();

            Rectangle squareTo = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
            squareTo.setFill(Color.valueOf("#582"));
            squareTo.setOpacity(0.9);
            squareTo.setTranslateX(x*ChessDemo.TILE_SIZE);
            squareTo.setTranslateY((height-1-y)*ChessDemo.TILE_SIZE);

            Rectangle squareFrom = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
            squareFrom.setFill(Color.valueOf("#582"));
            squareFrom.setOpacity(0.5);
            squareFrom.setTranslateX(fromX*ChessDemo.TILE_SIZE);
            squareFrom.setTranslateY((height-1-fromY)*ChessDemo.TILE_SIZE);

            Piece[][] boardState = gameEngine.getBoard().getBoardState();
            if (gameEngine.inCheck(boardState, !gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor())) {
                for (int i = 0; i < boardState.length; i++){
                    for (int j = 0; j < boardState[0].length; j++){
                        if (boardState[i][j] instanceof King){
                            if (boardState[i][j].getColor() == !gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()){
                                Rectangle check = new Rectangle(ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE);
                                check.setFill(Color.valueOf("#F30000"));
                                check.setOpacity(1);
                                check.setTranslateX(i*ChessDemo.TILE_SIZE);
                                check.setTranslateY((height-1-j)*ChessDemo.TILE_SIZE);
                                lastMoveGroup.getChildren().add(check);
                            }
                        }
                    }
                }
            }
            lastMoveGroup.getChildren().add(squareTo);
            lastMoveGroup.getChildren().add(squareFrom);
            uploadMove(fromX, fromY, toX, toY, ChessGame.movenr);
            ChessGame.movenr+=2;
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

    private void specialMoves(int x, int y, int height, Tile tile, Group hboxGroup, Group tileGroup, GameEngine gameEngine, Tile[][] board) {
        if ((Math.abs(x-tile.getX()) == 2 ) && gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof King){
            if(x-tile.getX()>0) {
                board[7][y].move(x-1, y, board);
            } else {
                board[0][y].move(x+1, y, board);
            }
            King king = (King)gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()];
            king.setCanCastle(false);
            //System.out.println("Rokkade");
        }
        if (Math.abs(y - tile.getY()) == 2 && gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof Pawn && (y == 3 || y == 4)) {
            Pawn pawn = (Pawn) gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()];
            if (!pawn.getEnPassant()) {
                pawn.setEnPassant(true);
            } else {
                pawn.setEnPassant(false);
            }
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

    private void uploadMove(int fromX, int fromY, int toX, int toY, int movenr){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps db = new DBOps();
                if (ChessGame.color) {
                    if(firstMove){
                        db.exUpdate("INSERT INTO Move VALUES (" + ChessGame.gameID + ", " + (movenr +1) +", "+ fromX +", "+fromY+", "+toX+", "+toY+");");
                        firstMove = false;
                    }
                    else if (Integer.parseInt(db.exQuery("SELECT MAX(movenr) FROM Move WHERE game_id = " +ChessGame.gameID+";", 1).get(0)) % 2 == 0) {
                        db.exUpdate("INSERT INTO Move VALUES (" + ChessGame.gameID + ", " + (movenr +1) +", "+ fromX +", "+fromY+", "+toX+", "+toY+");");
                        System.out.println("INSERT INTO Move VALUES (" + ChessGame.gameID + ", " + (movenr +1) +", "+ fromX +", "+fromY+", "+toX+", "+toY+");");
                    }
                }
                else {
                    if ((Integer.parseInt(db.exQuery("SELECT MAX(movenr) FROM Move WHERE game_id = " +ChessGame.gameID+";", 1).get(0)) % 2 == 1)) {
                        db.exUpdate("INSERT INTO Move VALUES (" + ChessGame.gameID + ", " + (movenr +1) +", "+ fromX +", "+fromY+", "+toX+", "+toY+");");
                        System.out.println("INSERT INTO Move VALUES (" + ChessGame.gameID + ", " + (movenr +1) +", "+ fromX +", "+fromY+", "+toX+", "+toY+");");
                    }
                }
            }
        });
        t.start();
    }
}

