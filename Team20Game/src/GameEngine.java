public class GameEngine {
   private Board board;
   private int increment;
   private boolean color;

   public GameEngine(int time, int increment, boolean color) {
      board = new Board();
      this.color = color;

      //Sett opp timer. https://stackoverflow.com/questions/14393423/how-to-make-a-countdown-timer-in-java?fbclid=IwAR3TbZbgbxu1SXY3mC8EQVcmvMp1KCMb3yIWaYwWBzU13i8BWSADYCFpQoM

   }

   public GameEngine(int time, boolean color) {
      board = new Board();
      this.color = color;
   }

   public int[] validMoves(int x, int y) {
      return GameLogic.validMoves(x, y, board);
   }

   public void move(int fromX, int fromY, int toX, int toY) {
      board.move(fromX, fromY, toX, toY);
   }

   public boolean isDone() {
      return GameLogic.isDone(board);
   }

   public double getTime() {
      return time;
   }



}
