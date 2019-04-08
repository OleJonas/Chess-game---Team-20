package Game;

import Game.Pieces.Piece;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * <h1> GameEngine</h1>
 * this class is used as an interface for the GameLogic class. When the game is played the GUI will interact with
 * this class that calls for the GameLogic class to actually handle all the calculations
 * @author Team20
 * @since 05.04.2019
 */


public class GameEngine {
   private Board board;
   private int moveCounter = 0;
   private HashMap<String, Integer> rep;

   /**
    * Constructor for the gameEngine
    * @param mode Parameter to create the correct setup of the board
    */
   public GameEngine(int mode) {
      board = new Board(mode);
      rep = new HashMap<String, Integer>();
   }

   /**
    * Method the fetch the board object of the game
    * @return The board object.
    */
   public Board getBoard(){ return board; }

   /**
    * Method for fetching all the valid moves given a selected position.
    * @param x The selected x-position on the board.
    * @param y The selected y-position on the board.
    * @return An ArrayList containing all the valid moves.
    */
   public ArrayList<Integer> validMoves(int x, int y) { return GameLogic.validMoves(x, y, board); }

   /**
    * Method for moving a piece on the board.
    * @param fromX The original x-value of the moved piece.
    * @param fromY The original y-value of the moved piece.
    * @param toX The new x-value of the moved piece.
    * @param toY The new y-value of the moved piece.
    * @param lastMove helping variable to fix en-passant in the sandbox game, since it does not enforce alternating moves.
    * @return A boolean describing whether the piece was moved.
    */
   public boolean move(int fromX, int fromY, int toX, int toY, boolean lastMove) { return board.move(fromX, fromY, toX, toY, lastMove); }

   /**
    * Method for removing a piece from the board.
    * @param x The x-position on the board.
    * @param y The y-position on the board.
    */
   public void removePiece(int x, int y){ board.removePiece(x, y); }

   /**
    * Method for checking whether a player is in checkmate.
    * @param board The board that is to be analyzed
    * @param color The color of the player that will be analyzed.
    * @return A boolean describing whether the given player is in mated.
    */
   public boolean isCheckmate(Board board, boolean color) { return GameLogic.isCheckmate(board, color); }

   /**
    * Method for checking whether a player can not make a legal move.
    * @param board The board that is to be analyzed.
    * @param color The color of the player that is to be analyzed.
    * @return A boolean describing whether a player has no legal moves (forcing a draw).
    */
   public boolean isStalemate(Board board, boolean color) {
      return GameLogic.isStalemate(board, color);
   }

   /**
    * Method for checking whether it is a forced given the pieces left.
    * @param board The board that is to be analyzed by the GameLogic.
    * @return A boolean telling if the game is drawn.
    */
   public boolean notEnoughPieces(Board board) {return GameLogic.notEnoughPieces(board);}

   /**
    * Method for placing a piece on the board.
    * @param piece The piece type.
    * @param x The x-position
    * @param y The y-position.
    */
   public void setPiece(Piece piece, int x, int y){
      board.setPiece(piece, x, y);
   }

   /**
    * Method for checking whether a player is in check.
    * @param board The board that is to be analyzed.
    * @param color The color of the player that is to be analyzed.
    * @return A boolean telling if the player is in check.
    */
   public boolean inCheck(Piece[][] board, boolean color) { return GameLogic.inCheck(board, color); }

   /**
    * Method for fetching the number of moves made without a pawn being moved or a piece taken. This is used for
    * finding out if a player can claim a draw after a certain amount of moves are made.
    * @return The number of moves made.
    */
   public int getMoveCounter() { return moveCounter; }

   /**
    * Method for resetting the moveCounter if a pawn is moved or a piece is taken.
    * @param reset whether or not a pawn has been moved or a piece is taken
    */
   public void setMoveCounter(boolean reset) { if (!reset) moveCounter++; else { moveCounter = 0;}}

   /**
    * Method for counting the pieces.
    * @param board The board that is to be analyzed.
    * @param color The color of the player that is to be analyzed.
    * @return An array of how many pieces a player has of the different types.
    */
   public int[] myPieces(Board board, boolean color) { return GameLogic.myPieces(board, color); }

   /**
    * Calculates the new elo-rating of the players given the result and the previous rating of the players.
    * @param whiteElo The original elo of the white player.
    * @param blackElo The original elo of the black player.
    * @param score The result of the match (white win, black win, draw).
    * @return The new elo of the two players
    */
   public static int[] getElo(int whiteElo, int blackElo, int score) { return GameLogic.getElo(whiteElo, blackElo, score); }

   /**
    * method for checking whether a draw can be claimed due to move repetition.
    * @return A boolean value describing if a draw can be claimed.
    */
   public boolean isMoveRepetition() { return GameLogic.isMoveRepetition(rep, board); }
}
