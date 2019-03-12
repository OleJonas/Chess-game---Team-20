package Game;

//import JavaFX.GameChatNTimer;

import Pieces.Piece;
import Pieces.Rook;
import Pieces.King;

import java.util.ArrayList;

public class GameEngine {
   private Board board;
   private int increment;
   private boolean color;
   private boolean castleRook = true;
   private boolean castleKing = true;
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
      if (castleRook && board.getBoardState()[fromX][fromY] instanceof Rook) {
         Rook rook = (Rook) board.getBoardState()[fromX][fromY];
         rook.setCanCastle(false);
         castleRook = false;
      }
      if (castleKing && board.getBoardState()[fromX][fromY] instanceof King) {
         King king = (King) board.getBoardState()[fromX][fromY];
         king.setCanCastle(false);
         castleKing = false;
      }
      return board.move(fromX, fromY, toX, toY);
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
