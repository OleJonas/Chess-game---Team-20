package GUI.MainScene.Sandbox;

import GUI.GameScene.ChessGame;
import Game.GameEngine;
import Game.GameLogic;
import GUI.GameScene.PawnChangeChoiceBox;
import Game.Pieces.*;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
/**
 * <h1> SandboxHighlightBox </h1>
 * This class is a simpler version of highlightbox, that is used in ChessSandbox in the main scene.
 * tailored for a sandbox where the user can move any piece at any time.
 * @author Team20
 * @since 08.04.2019
 */
class SandboxHighlightBox extends Pane {
    int x;
    int y;
    int height;
    double hboxOpacity = 0.7;
    String shapeOfBox = "circle";

    /**
     * The constructor for the HighlightBox class.
     * @param x = x-coordinate of tile.
     * @param y = y-coordinate of tile.
     * @param height = the height of the chess-board.
     * @param tile = the tile containing a piece chosen by the player.
     * @param hboxGroup = the circles indicating valid moves.
     * @param tileGroup = the pieces on the board.
     * @param selectedGroup = the group containing the tiles that are marked when choosing a piece.
     * @param lastMoveGroup = the group containing the tiles that are marked after doing a move.
     * @param gameEngine = the game engine which contains the method for the game logic.
     * @param board = a two dimensional array containing the chess pieces on the coordinates visually on the chess board.
     */

    public SandboxHighlightBox(int x, int y, int height, SandboxTile tile, Group hboxGroup, Group tileGroup, Group selectedGroup, Group lastMoveGroup, GameEngine gameEngine, SandboxTile[][] board){
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
            specialMoves(x, y, tile, tileGroup, gameEngine, board);
            int fromX = tile.getX();
            int fromY = tile.getY();
            if (gameEngine.getBoard().getBoardState()[x][y] != null) {
                gameEngine.getBoard().addTakenPiece(gameEngine.getBoard().getBoardState()[x][y]);
            }
            tile.move(x, y, board);
            GameLogic.getDisplayPieces(gameEngine.getBoard());
            ChessSandbox.lastMove = gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor();

            boolean color = gameEngine.getBoard().getBoardState()[x][y].getColor();
            if(y==height-1 && color && gameEngine.getBoard().getBoardState()[x][y] instanceof Pawn){
                pawnChoiceBox(color, gameEngine, tile);
            } else if(y==0 && !color && gameEngine.getBoard().getBoardState()[x][y] instanceof Pawn){
                pawnChoiceBox(color, gameEngine, tile);
            }

            if (gameEngine.isCheckmate(gameEngine.getBoard(), !gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor())) {
                if (gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor()) {
                    System.out.println("Checkmate for White");
                }
                else {
                    System.out.println("Checkmate for Black");
                }
            }
            else if (gameEngine.isStalemate(gameEngine.getBoard(), !gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor())) {
                System.out.println("Stalemate");
            }
            if(gameEngine.notEnoughPieces(gameEngine.getBoard())) {
                System.out.println("Draw");
            }
            if (!(gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof Pawn)) {
                gameEngine.setMoveCounter(false);
                if (gameEngine.getMoveCounter() == 100) {
                    System.out.println("Draw");
                }
            } else {
                gameEngine.setMoveCounter(true);
            }

            ChessGame.movenr+=2;
            getChildren().clear();
            hboxGroup.getChildren().clear();
            lastMoveGroup.getChildren().clear();
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
            lastMoveGroup.getChildren().add(squareTo);
            lastMoveGroup.getChildren().add(squareFrom);
        });
    }

    /**
     * Method that checks for some "special" moves that can happen in chess, and handles these cases.
     * @param x x-coordinate for where the piece is to be moved.
     * @param y y- coordinate for where the piece is to be moved.
     * @param tile the piece that is to be moved.
     * @param tileGroup the group containing all the pieces.
     * @param gameEngine the gameenginge object the current instance of chessgame is operating on.
     * @param board array with pieces placed in the coordinates they are visually presented on the screen.
     */
    private void specialMoves(int x, int y, SandboxTile tile, Group tileGroup, GameEngine gameEngine, SandboxTile[][] board) {
        if ((Math.abs(x-tile.getX()) == 2 ) && gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof King){
            if(x-tile.getX()>0) {
                board[7][y].move(x-1, y, board);
            } else {
                board[0][y].move(x+1, y, board);
            }
            King king = (King)gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()];
            king.setCanCastle(false);
        }
        if (Math.abs(y-tile.getY()) == 2 && gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()] instanceof Pawn) {
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
                        }
                    }
                }
            }
        }
    }

    /**
     * Method for getting the x-coordinate of the highlighbox.
     * @return x-coordinate for the highlightbox.
     */
    public int getX(){
        return x;
    }

    /**
     * Method for getting the y-coordinate of the highlighbox.
     * @return y-coordinate for the highlighbox.
     */

    public int getY(){
        return y;
    }

    /**
     *Method for setting what to show on the box that pops up in a promotion.
     * @param color the color of the pieces
     * @param gameEngine the gameEnging object this instance of a chess game is using.
     * @param tile the piece that moved for promotion.
     */
    private void pawnChoiceBox(boolean color, GameEngine gameEngine, SandboxTile tile){
        PawnChangeChoiceBox pawnChange = new PawnChangeChoiceBox();
        pawnChange.Display(gameEngine.getBoard().getBoardState()[tile.getX()][tile.getY()].getColor());
        Piece newPiece = null;

        if (PawnChangeChoiceBox.choice.equals("Queen")) {
            newPiece = new Queen(color, x, y);
        } else if (PawnChangeChoiceBox.choice.equals("Rook")) {
            newPiece = new Rook(color, x, y);
        } else if (PawnChangeChoiceBox.choice.equals("Bishop")) {
            newPiece = new Bishop(color, x, y);
        } else if (PawnChangeChoiceBox.choice.equals("Knight")) {
            newPiece = new Knight(color, x, y);
        }

        ImageView tempimg = newPiece.getImageView();
        gameEngine.setPiece(newPiece, x, y);
        tile.setImageView(tempimg, ChessGame.TILE_SIZE*(1-ChessGame.imageSize)/2, ChessGame.TILE_SIZE*(1-ChessGame.imageSize)/2);
    }
}