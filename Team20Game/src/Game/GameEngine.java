package Game;

//import JavaFX.GameChatNTimer;

import Pieces.Piece;
import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;

public class GameEngine {
   private Board board;
   private int increment;
   private boolean color;
   private int moveCounter = 0;
   private Map<Piece[][], Integer> rep;
   //private GameChatNTimer gameChatNTimer;

   public GameEngine(int gameTime, int increment, boolean color) {
      board = new Board();
      this.color = color;
      //this.gameChatNTimer = new GameChatNTimer(gameTime, increment);
   }

   public GameEngine(int time, boolean color) {
      board = new Board();
      this.color = color;
      //this.gameChatNTimer = new GameChatNTimer(time);
   }

   public boolean isMoveRepetition(){
      if (rep.containsKey(board.getBoardState())){
         rep.put(board.getBoardState(), rep.get(board.getBoardState()) + 1);
      }
      else{
         rep.put(board.getBoardState(), 1);
      }
      return rep.get(board.getBoardState()) > 2;
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

   public int[] getElo(int whiteElo, int blackElo, int score) { return GameLogic.getElo(whiteElo, blackElo, score); }

   /*public double getTime() {
      return time;
   }*/
}
