import java.util.ArrayList;

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
    public static ArrayList<Integer> validMoves(int x, int y, Board board) {
        ArrayList<Integer> validMoves = new ArrayList<Integer>();
        Piece[][] boardState = board.getBoardState();

        if (boardState[x][y] instanceof Pawn) {
            if (boardState[x][y].getColor()) {
                if (boardState[x][y+1] == null) {
                    validMoves.add(x);
                    validMoves.add(y+1);
                }
                if (y == 6) {
                    if (boardState[x][y+2] == null) {
                        validMoves.add(x);
                        validMoves.add(y+2);
                    }
                }
                if (boardState[x+1][y+1] != null && (x + 1 < 8) {
                    validMoves.add(x+1);
                    validMoves.add(y+1);
                }
                if (boardState[x-1][y+1] != null && (x - 1 >= 0)) {
                    validMoves.add(x-1);
                    validMoves.add(y+1);
                }
            }
            else {
                if (boardState[x][y-1] == null) {
                    validMoves.add(x);
                    validMoves.add(y-1);
                }
                if (y == 1) {
                    if (boardState[x][y-2] == null) {
                        validMoves.add(x);
                        validMoves.add(y-2);
                    }
                }
                if (boardState[x+1][y-1] != null && (x + 1 < 8) {
                    validMoves.add(x+1);
                    validMoves.add(y-1);
                }
                if (boardState[x-1][y-1] != null && (x - 1 >= 0)) {
                    validMoves.add(x-1);
                    validMoves.add(y-1);
                }
            }

        }

        if (boardState[x][y] instanceof Rook) {
            for (int i = 1; i < 8; i++) {
               boolean xLeft = true;
               boolean xRight = true;
               boolean xUp = true;
               boolean yUp = true;

               if (x + i < 8) {
                   if (boardState[x + i][y] == null && xRight) {
                        validMoves.add(x+i);
                        validMoves.add(y);
                   } else if(boardState[x][y].getColor() != boardState[x + i][y].getColor()) {
                       validMoves.add(x+i);
                       validMoves.add(y);
                       xRight = false;
                   }
               }
               if (x - i >= 0) {
                   if (boardState[x - i][y] == null && xLeft) {
                       validMoves.add(x-i);
                       validMoves.add(y);
                   } else if(boardState[x][y].getColor() != boardState[x - i][y].getColor()){
                       validMoves.add(x-i);
                       validMoves.add(y);
                       xLeft = false;
                   }
               }
               if (y + i < 8) {
                   if (boardState[x][y + i] == null && yUp) {
                       validMoves.add(x);
                       validMoves.add(y+i);
                   } else if(boardState[x][y].getColor() != boardState[x][y+i].getColor()){
                       validMoves.add(x);
                       validMoves.add(y+i);
                       yUp = false;
                   }
               }
               if (y - i >= 0) {
                   if (boardState[x][y - i] == null && yDown) {
                       validMoves.add(x);
                       validMoves.add(y-i);
                   } else if(boardState[x][y].getColor() != boardState[x][y-i].getColor()) {
                       validMoves.add(x);
                       validMoves.add(y-i);
                       yDown = false;
                   }
               }
            }
            return validMoves;
        }

        if (boardState[x][y] instanceof Knight) {
            if ((boardState[x+2][y+1].getColor() != boardState[x][y].getColor() || null)  && (x+2 < 8) && (y+1 < 8)) {
                validMoves.add(x+2);
                validMoves.add(y+1);
            }
            if ((boardState[x-2][y+1].getColor() != boardState[x][y].getColor() || null) && (x-2 >= 0) && (y+1 < 8)) {
                validMoves.add(x-2);
                validMoves.add(y+1);
            }
            if ((boardState[x+2][y-1].getColor() != boardState[x][y].getColor()|| null) && (x+2 < 8) && (y-1 >= 0)) {
                validMoves.add(x+2);
                validMoves.add(y-1);
            }
            if ((boardState[x-2][y-1].getColor() != boardState[x][y].getColor()|| null) && (x-2 >= 0) && (y-1 >= 0)) {
                validMoves.add(x-2);
                validMoves.add(y-1);
            }
            if ((boardState[x+1][y+2].getColor() != boardState[x][y].getColor()|| null) && (x+1 < 8) && (y+2 < 8)) {
                validMoves.add(x+1);
                validMoves.add(y+2);
            }
            if ((boardState[x-1][y+2].getColor() != boardState[x][y].getColor()|| null) && (x-1 >= 0) && (y+2 < 8)) {
                validMoves.add(x-1);
                validMoves.add(y+2);
            }
            if ((boardState[x+1][y-2].getColor() != boardState[x][y].getColor()|| null) && (x+1 < 8) && (y-2 >= 0)) {
                validMoves.add(x+1);
                validMoves.add(y-2);
            }
            if ((boardState[x-1][y-2].getColor() != boardState[x][y].getColor()|| null) && (x-1 >= 0) && (y-2 >= 0)) {
                validMoves.add(x-1);
                validMoves.add(y-2);
            }
            return validMoves;
        }

        if (boardState[x][y] instanceof King) {
            if ((boardState[x-1][y+1].getColor() != boardState[x][y].getColor() || null)  && (x-1 >= 0) && (y+1 < 8)) {
                validMoves.add(x-1);
                validMoves.add(x+1);
            }
            if ((boardState[x][y+1].getColor() != boardState[x][y].getColor() || null) && ((y+1 < 8)) {
                validMoves.add(x);
                validMoves.add(y+1);
            }
            if ((boardState[x+1][y+1].getColor() != boardState[x][y].getColor()|| null) && (x+1 < 8) && (y+1 < 8)) {
                validMoves.add(x+1);
                validMoves.add(y+1);
            }
            if ((boardState[x-1][y-1].getColor() != boardState[x][y].getColor()|| null) && (x-1 >= 0) && (y-1 >= 0)) {
                validMoves.add(x-1);
                validMoves.add(y-1);
            }
            if ((boardState[x][y-1].getColor() != boardState[x][y].getColor()|| null) && ((y-1 >= 0)) {
                validMoves.add(x);
                validMoves.add(y-1);
            }
            if ((boardState[x+1][y-1].getColor() != boardState[x][y].getColor()|| null) && (x+1 < 8) && (y-1 >= 0)) {
                validMoves.add(x+1);
                validMoves.add(y-1);
            }
            if ((boardState[x-1][y].getColor() != boardState[x][y].getColor()|| null) && (x-1 >= 0)) {
                validMoves.add(x-1);
                validMoves.add(y);
            }
            if ((boardState[x+1][y].getColor() != boardState[x][y].getColor()|| null) && (x+1 < 8)) {
                validMoves.add(x+1);
                validMoves.add(y);
            }
            return validMoves;

        }

        if (boardState[x][y] instanceof Queen) {

        }

        if (boardState[x][y] instanceof Bishop) {
            for (int i = 1; i < 8; i++)  {
                boolean RightUp = true;
                boolean RightDown = true;
                boolean LeftUp = true;
                boolean LeftDown = true;

                if (x+i < 8 && y+i<8) {
                    if (boardState[x+i][y+i] == null && RightUp) {
                        validMoves.add(x+i);
                        validMoves.add(y+i);
                    } else if (boardState[x][y].getColor() != boardState[x+i][y+i].getColor()) {
                        validMoves.add(x+i);
                        validMoves.add(y+i);
                        RightUp = false;
                    }
                }

                if (x - i >= 0 && y - i >= 0) {
                    if (boardState[x-i][y-i] == null && LeftDown) {
                        validMoves.add(x-i);
                        validMoves.add(x-i);
                    } else if (boardState[x][y].getColor() != boardState[x-i][y-i].getColor()) {
                        validMoves.add(x-i);
                        validMoves.add(y-i);
                        LeftDown = false;
                    }
                }

                if (x-i >= 0 && y+i < 8) {
                    if (boardState[x-i][y+i] == null && LeftUp) {
                        validMoves.add(x-i);
                        validMoves.add(y+i);
                    } else if (boardState[x][y].getColor() != boardState[x-i][y+1].getColor()) {
                        validMoves.add(x-i);
                        validMoves.add(y+i);
                        LeftUp = false;
                    }
                }

                if (x+i < 8 && y-i < 0) {
                    if (boardState[x+i][y-i] == null && RightDown) {
                        validMoves.add(x+i);
                        validMoves.add(y-i);
                    } else if (boardState[x][y].getColor() != boardState[x+i][y-i].getColor()) {
                        validMoves.add(x+i);
                        validMoves.add(y-i);
                        RightDown = false;
                    }
                }
            }
        }
        return validMoves;
    }
}