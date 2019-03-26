package Game;

//import JavaFX.GameChatNTimer;

import Pieces.Piece;

import java.util.ArrayList;
import java.util.HashMap;

public class GameEngine {
   private Board board;
   private int increment;
   private boolean color;
   private int moveCounter = 0;
   private HashMap<String, Integer> rep;
   //private GameChatNTimer gameChatNTimer;

   public GameEngine(int gameTime, int increment, int mode) {
      board = new Board(mode);
      rep = new HashMap<String, Integer>();
      //this.gameChatNTimer = new GameChatNTimer(gameTime, increment);
   }

   public GameEngine(int time, int mode) {
      board = new Board(mode);
      rep = new HashMap<String, Integer>();
      //this.gameChatNTimer = new GameChatNTimer(time);
   }

   public Board getBoard(){ return board; }

   public ArrayList<Integer> validMoves(int x, int y) { return GameLogic.validMoves(x, y, board); }

   public boolean move(int fromX, int fromY, int toX, int toY) {
      return board.move(fromX, fromY, toX, toY);
   }

   public void removePiece(int x, int y){ board.removePiece(x, y); }

   public boolean isCheckmate(Board board, boolean color) { return GameLogic.isCheckmate(board, color); }

   public boolean isStalemate(Board board, boolean color) {
      return GameLogic.isStalemate(board, color);
   }

   public boolean notEnoughPieces(Board board) {return GameLogic.notEnoughPieces(board);}

   public void setPiece(Piece piece, int x, int y){
      board.setPiece(piece, x, y);
   }

   public boolean inCheck(Piece[][] board, boolean color) { return GameLogic.inCheck(board, color); }

   public int getMoveCounter() { return moveCounter; }

   public void setMoveCounter(boolean reset) { if (!reset) moveCounter++; else { moveCounter = 0;}}

   public int[] myPieces(Board board, boolean color) { return GameLogic.myPieces(board, color); }

   public int[] getElo(int whiteElo, int blackElo, int score) { return GameLogic.getElo(whiteElo, blackElo, score); }

   public boolean isMoveRepetition() { return GameLogic.isMoveRepetition(rep, board); }
}
