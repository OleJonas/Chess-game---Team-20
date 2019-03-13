package Game;

//import JavaFX.GameChatNTimer;

import Pieces.Piece;

import java.util.ArrayList;

public class GameEngine {
   private Board board;
   private int increment;
   private boolean color;
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

   public Board getBoard(){
       return board;
   }

   public ArrayList<Integer> validMoves(int x, int y) {
      return GameLogic.validMoves(x, y, board);
   }

   public boolean move(int fromX, int fromY, int toX, int toY) {
      return board.move(fromX, fromY, toX, toY);
   }

   public void removePiece(int x, int y){
       board.removePiece(x, y);
   }

   public void setPiece(Piece piece, int x, int y){
      board.setPiece(piece, x, y);
   }

   public boolean isDone() {
      return GameLogic.isDone(board);
   }

   /*public double getTime() {
      return time;
   }*/
}
