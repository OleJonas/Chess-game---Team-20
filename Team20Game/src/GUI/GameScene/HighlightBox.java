package GUI.GameScene;

import Database.Game;
import Database.User;
import Game.GameEngine;
import GUI.LoginScreen.Login;
import GUI.MainScene.MainScene;
import Game.Pieces.*;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;

/**
 * <h1>HighlightBox</h1>
 * The purpose of this class is to have the logic for what happens on the chessboard for every move that is made through the game.
 * @since 08.04.2019
 * @author Team 20
 */

public class HighlightBox extends Pane{
    int x;
    int y;
    int height;
    double hboxOpacity = 0.7;
    boolean mode = true;
    String shapeOfBox = "circle";

    /**
     * The constructor for the HighlightBox class
     * @param x = x-coordinate of tile
     * @param y = y-coordinate of tile
     * @param height = the height of the chess-board
     * @param tile = the tile chosen by the player
     * @param hboxGroup = the circles indicating valid moves
     * @param tileGroup = the pieces on the board
     * @param selectedGroup = the tiles are marked when choosing a piece
     * @param lastMoveGroup = the tiles who are marked after doing a move
     * @param gameEngine = the game engine which contains the method for the game logic.
     * @param board = the chess board
     */
    public HighlightBox(int x, int y, int height, Tile tile, Group hboxGroup, Group tileGroup, Group selectedGroup, Group lastMoveGroup, GameEngine gameEngine, Tile[][] board){
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
            Circle circle = new Circle(ChessGame.TILE_SIZE / 5);
            circle.setFill(Color.valueOf("582"));
            circle.setOpacity(hboxOpacity);
            circle.setTranslateX(ChessGame.TILE_SIZE/2);
            circle.setTranslateY(ChessGame.TILE_SIZE/2);
            getChildren().add(circle);

            Rectangle square = new Rectangle(ChessGame.TILE_SIZE*0.7, ChessGame.TILE_SIZE*0.7);
            square.setOpacity(0);
            getChildren().add(square);
        }

        setOnMouseClicked(e->{
            lastMoveGroup.getChildren().clear();
            specialMoves(x, y, tile, tileGroup, gameEngine, board);

            ChessGame.myTurn = false;

            GameScene.allMoves.add(tile.getY() + "" + tile.getX());
            GameScene.updateMoves();

            int fromX = tile.getX();
            int fromY = tile.getY();
            int toX = this.x;
            int toY = this.y;
            int totWhites = gameEngine.myPieces(gameEngine.getBoard(), true)[6];
            int totBlacks = gameEngine.myPieces(gameEngine.getBoard(), false)[6];
            if (gameEngine.getBoard().getBoardState()[x][y] != null) {
                gameEngine.getBoard().addTakenPiece(gameEngine.getBoard().getBoardState()[x][y]);
            }
            tile.move(x, y, board, false);
            Piece temp = (Piece)gameEngine.getBoard().getBoardState()[x][y];
            if (!ChessGame.color) {
                Label text = new Label(GameScene.spacing + temp.toString());
                text.setFont(Font.font("Copperplate", 20));
                GameScene.viewMoves.add(text, GameScene.myColumn, (ChessGame.movenr + 1)/2);
            } else {
                Label text = new Label((((ChessGame.movenr + 1)/2)+1) + ". ");
                text.setFont(Font.font("Copperplate", 20));
                Label nr = new Label(GameScene.spacing + temp.toString());
                nr.setFont(Font.font("Copperplate", 20));
                GameScene.viewMoves.add(text, 0, (ChessGame.movenr + 1)/2);
                GameScene.viewMoves.add(nr, GameScene.myColumn, (ChessGame.movenr + 1)/2);
            }

            ChessGame.lastMove = gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor();
            int updatedWhites = gameEngine.myPieces(gameEngine.getBoard(), true)[6];
            int updatedBlacks = gameEngine.myPieces(gameEngine.getBoard(), false)[6];
            int top=0;

            if(ChessGame.color) {
                top = height - 1;
            }

            if(y==top && gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof Pawn){
                PawnChangeChoiceBox pawnChange = new PawnChangeChoiceBox();
                hboxGroup.getChildren().clear();
                pawnChange.Display(ChessGame.color);

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
                    tempimg.getTransforms().add(new Rotate(180, ChessGame.TILE_SIZE/2, ChessGame.TILE_SIZE/2));
                }
                tile.setImageView(tempimg,
                        ChessGame.TILE_SIZE*(1-ChessGame.imageSize)/2, ChessGame.TILE_SIZE*(1-ChessGame.imageSize)/2);
            }

            Game.uploadMove(fromX, fromY, toX, toY, ChessGame.movenr);
            ChessGame.movenr+=2;

            if (gameEngine.isCheckmate(gameEngine.getBoard(), !gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor())) {
                if (gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()) {
                    System.out.println("Checkmate for White");
                    int[] elo = gameEngine.getElo(1000, 1000, 0);
                    System.out.println("New White elo: " +elo[0]+ "\nNew Black elo: " +elo[1]);
                    if(ChessGame.color){
                        Game.setResult(ChessGame.gameID, Login.userID);
                        User.updateEloByGame(ChessGame.gameID);
                        ChessGame.gameWon = true;
                        MainScene.inGame = false;
                        GameOverPopupBox.Display();
                    }
                }
                else {
                    System.out.println("Checkmate for Black");
                    int[] elo = gameEngine.getElo(1000, 1000, 1);
                    System.out.println("New White elo: " +elo[0]+ "\nNew Black elo: " +elo[1]);
                    if(!ChessGame.color){
                        Game.setResult(ChessGame.gameID, Login.userID);
                        User.updateEloByGame(ChessGame.gameID);
                        ChessGame.gameWon = true;
                        MainScene.inGame = false;
                        GameOverPopupBox.Display();
                    }
                }
            }
            else if (gameEngine.isStalemate(gameEngine.getBoard(), !gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor())) {
                Game.setResult(ChessGame.gameID, 0);
                User.updateEloByGame(ChessGame.gameID);
                ChessGame.timer.cancel();
                MainScene.inGame =false;
                ChessGame.isDone = true;
                GameOverPopupBox.Display();
            }

            if (gameEngine.isMoveRepetition()) {
                Game.setResult(ChessGame.gameID, 0);
                User.updateEloByGame(ChessGame.gameID);
                ChessGame.timer.cancel();
                MainScene.inGame =false;
                ChessGame.isDone = true;
                GameOverPopupBox.Display();
            }

            if(gameEngine.notEnoughPieces(gameEngine.getBoard())) {
                Game.setResult(ChessGame.gameID, 0);
                User.updateEloByGame(ChessGame.gameID);
                ChessGame.timer.cancel();
                MainScene.inGame =false;
                ChessGame.isDone = true;
                GameOverPopupBox.Display();
            }

            if (!(gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof Pawn) && ((totWhites+totBlacks) == (updatedWhites+updatedBlacks))) {
                gameEngine.setMoveCounter(false);
                if (gameEngine.getMoveCounter() == 100) {
                    Game.setResult(ChessGame.gameID, 0);
                    User.updateEloByGame(ChessGame.gameID);
                    ChessGame.timer.cancel();
                    MainScene.inGame =false;
                    ChessGame.isDone = true;
                    GameOverPopupBox.Display();
                }
            } else {
                gameEngine.setMoveCounter(true);
            }

            getChildren().clear();
            hboxGroup.getChildren().clear();
            selectedGroup.getChildren().clear();

            Rectangle squareTo = new Rectangle(ChessGame.TILE_SIZE, ChessGame.TILE_SIZE);
            squareTo.setFill(Color.valueOf("#582"));
            squareTo.setOpacity(0.9);
            squareTo.setTranslateX(x*ChessGame.TILE_SIZE);
            squareTo.setTranslateY((height-1-y)*ChessGame.TILE_SIZE);

            Rectangle squareFrom = new Rectangle(ChessGame.TILE_SIZE, ChessGame.TILE_SIZE);
            squareFrom.setFill(Color.valueOf("#582"));
            squareFrom.setOpacity(0.5);
            squareFrom.setTranslateX(fromX*ChessGame.TILE_SIZE);
            squareFrom.setTranslateY((height-1-fromY)*ChessGame.TILE_SIZE);

            Piece[][] boardState = gameEngine.getBoard().getBoardState();
            if (gameEngine.inCheck(boardState, !gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor())) {
                for (int i = 0; i < boardState.length; i++){
                    for (int j = 0; j < boardState[0].length; j++){
                        if (boardState[i][j] instanceof King){
                            if (boardState[i][j].getColor() == !gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()){
                                Rectangle check = new Rectangle(ChessGame.TILE_SIZE, ChessGame.TILE_SIZE);
                                check.setFill(Color.valueOf("#F30000"));
                                check.setOpacity(1);
                                check.setTranslateX(i*ChessGame.TILE_SIZE);
                                check.setTranslateY((height-1-j)*ChessGame.TILE_SIZE);
                                lastMoveGroup.getChildren().add(check);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * This constructor is used when you click on a tile which is not marked, such that the circles on the other tiles are removed.
     */
    public HighlightBox() {
        if(shapeOfBox.equalsIgnoreCase("rectangle")) {
            Rectangle square = new Rectangle(0, 0);
            getChildren().add(square);
        } else {
            Circle circle = new Circle(0);
            getChildren().add(circle);
        }
    }

    /**
     * This method is used for special moves, such as en passant and castling.
     * @param x = x-coordinate of tile
     * @param y = y-coordinate of tile
     * @param tile = the tile which is chosen.
     * @param tileGroup = the pieces on the highlight board
     * @param gameEngine = the game engine with the logic methods
     * @param board = the chess board.
     */
    private void specialMoves(int x, int y, Tile tile, Group tileGroup, GameEngine gameEngine, Tile[][] board) {
        if ((Math.abs(x-tile.getX()) == 2 ) && gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof King){
            if(x-tile.getX()>0) {
                board[7][y].move(x-1, y, board, true);
            } else {
                board[0][y].move(x+1, y, board, true);
            }
            setY(12);
            King king = (King)gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()];
            king.setCanCastle(false);
        }

        if (Math.abs(y - tile.getY()) == 2 && gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof Pawn) {
            Pawn pawn = (Pawn) gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()];
            pawn.setEnPassant(true);
        }

        if (tile.getX() + 1 < 8) {
            if (gameEngine.getBoard().getBoardState()[tile.getX()+1][tile.getY()] instanceof Pawn) {
                if (gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()) {
                    Pawn pawn = (Pawn) gameEngine.getBoard().getBoardState()[tile.getX()+1][tile.getY()];
                    if (pawn.getColor() != gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()) {
                        if (pawn.getEnPassant() && x == tile.getX() + 1) {
                            tileGroup.getChildren().remove(board[tile.getX()+1][tile.getY()]);
                            gameEngine.removePiece(tile.getX()+1, tile.getY());
                            gameEngine.getBoard().addTakenPiece(new Pawn(!gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor(), 0, 0));
                            setY(13);
                        }
                    }
                }
                else {
                    Pawn pawn = (Pawn) gameEngine.getBoard().getBoardState()[tile.getX()+1][tile.getY()];
                    if (pawn.getColor() != gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()) {
                        if (pawn.getEnPassant() && x == tile.getX() + 1) {
                            tileGroup.getChildren().remove(board[tile.getX()+1][tile.getY()]);
                            gameEngine.removePiece(tile.getX()+1, tile.getY());
                            gameEngine.getBoard().addTakenPiece(new Pawn(!gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor(), 0, 0));
                            setY(13);
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
                        if (pawn.getEnPassant() && x == tile.getX() - 1) {
                            tileGroup.getChildren().remove(board[tile.getX()-1][tile.getY()]);
                            gameEngine.removePiece(tile.getX()-1, tile.getY());
                            gameEngine.getBoard().addTakenPiece(new Pawn(!gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor(), 0, 0));
                            setY(13);
                        }
                    }
                }
                else {
                    Pawn pawn = (Pawn) gameEngine.getBoard().getBoardState()[tile.getX()-1][tile.getY()];
                    if (pawn.getColor() != gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()) {
                        if (pawn.getEnPassant() && x == tile.getX() - 1) {
                            tileGroup.getChildren().remove(board[tile.getX()-1][tile.getY()]);
                            gameEngine.removePiece(tile.getX()-1, tile.getY());
                            gameEngine.getBoard().addTakenPiece(new Pawn(!gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor(), 0, 0));
                            setY(13);
                        }
                    }
                }
            }
        }
    }

    /**
     * Set x-position of a piece.
     * @param x = x-coordinate
     */
    public void setX(int x){this.x = x;}

    /**
     * Set y-position of a piece
     * @param y = y-coordinate
     */
    public void setY(int y){this.y = y;}


    public int getX(){
        return x;
    }

    /**
     * Get y-position of a piece
     * @return y-position of a piece
     */
    public int getY(){
        return y;
    }
}

