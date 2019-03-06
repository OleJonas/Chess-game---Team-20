public class GameLogic{
    boolean inCheck(Piece[][] board, boolean color){
        int x = 0, y = 2;
        //find position of king
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; i++){
                if (board[i][j] instanceof King){
                    if (board[i][j].getColor() == color){
                        x = i;
                        y = j;
                        i = 10;
                        break;
                    }
                }
            }
        }

        // check threats from cardinal directions
        for (int i = 1; i < 8; i++){
            if (x + i < 8) {
                if (board[x + i][y] != null) {
                    if (board[x + i][y].getColor() != color && (board[x + 1][y] instanceof Queen || board[x + i][y] instanceof Rook)) {
                        return true;
                    }
                }
            }
            if (x - i >= 0) {
                if (board[x - i][y] != null) {
                    if (board[x - i][y].getColor() != color && (board[x - i][y] instanceof Queen || board[x - i][y] instanceof Rook)) {
                        return true;
                    }
                }
            }
            if (y + i < 8) {
                if (board[x][y + i] != null) {
                    if (board[x][y + i].getColor() != color && (board[x][y + i] instanceof Queen || board[x + i][y] instanceof Rook)) {
                        return true;
                    }
                }
            }
            if (y - i >= 0) {
                if (board[x][y - i] != null) {
                    if (board[x][y - i].getColor() != color && (board[x][y - i] instanceof Queen || board[x][y - i] instanceof Rook)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    public static boolean isDone(Board board){
        return false;
    }
    public static int[] validMoves(int x, int y, Board board){
        return null;
    }
}