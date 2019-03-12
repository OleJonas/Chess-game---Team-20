package Game;

import Pieces.*;
import Pieces.Piece;

import java.util.ArrayList;

public class GameLogic{
    private static boolean inCheck(Piece[][] state, boolean color){
        Piece[][] board = state;
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
        boolean right = true, down = true, left = true, up = true;
        for (int i = 1; i < 8; i++){

            if (x + i < 8 && right) {
                if (board[x + i][y] != null) {
                    if (board[x + i][y].getColor() == color){
                        right = false;
                    }
                    else if((board[x + i][y] instanceof Queen || board[x + i][y] instanceof Rook)) {
                        return true;
                    }
                }
            }
            if (x - i >= 0 && left) {
                if (board[x - i][y] != null) {
                    if (board[x - i][y].getColor() == color){
                        left = false;
                    }
                    else if ((board[x - i][y] instanceof Queen || board[x - i][y] instanceof Rook)) {
                        return true;
                    }
                }
            }
            if (y + i < 8 && up) {
                if (board[x][y + i] != null) {
                    if (board[x][y + i].getColor() == color){
                        up = false;
                    }
                    else if ((board[x][y + i] instanceof Queen || board[x + i][y] instanceof Rook)) {
                        return true;
                    }
                }
            }
            if (y - i >= 0 && down) {
                if (board[x][y - i] != null) {
                    if (board[x][y - i].getColor() == color){
                        down = false;
                    }
                    else if ((board[x][y - i] instanceof Queen || board[x][y - i] instanceof Rook)) {
                        return true;
                    }
                }
            }
        }

        boolean upright = true, downright = true, upleft = true, downleft = true;
        for (int i = 1; i < 8; i++){

            if (x + i < 8 && y + i < 8 && upright) {
                if (board[x + i][y + i] != null) {
                    if (board[x + i][y + i].getColor() == color){
                        upright = false;
                    }
                    else if ((board[x + i][y + i] instanceof Queen || board[x + i][y + i] instanceof Bishop)) {
                        return true;
                    }
                    else if (i == 1 && board[x + i][y + i] instanceof Pawn){
                        return true;
                    }
                }
            }
            if (x + i < 8 && y - i >= 0 && downright) {
                if (board[x + i][y - i] != null) {
                    if (board[x + i][y - i].getColor() == color){
                        downright = false;
                    }
                    else if ((board[x + i][y - i] instanceof Queen || board[x + i][y - i] instanceof Bishop)) {
                        return true;
                    }
                }
            }
            if (x - i >= 0 && y + i < 8 && upleft) {
                if (board[x - i][y + i] != null) {
                    if (board[x - i][y + i].getColor() == color){
                        upleft = false;
                    }
                    else if ((board[x - i][y + i] instanceof Queen || board[x - i][y + i] instanceof Bishop)) {
                        return true;
                    }
                    else if (i == 1 && board[x - i][y + i] instanceof Pawn){
                        return true;
                    }
                }
            }
            if (x - i >= 0 && y  - i >= 0 && downleft) {
                if (board[x - i][y - i] != null) {
                    if (board[x - i][y - i].getColor() == color){
                        downleft = false;
                    }
                    else if ((board[x - i][y - i] instanceof Queen || board[x - i][y - i] instanceof Bishop)) {
                        return true;
                    }
                }
            }
        }

        if (x - 1 >= 0 && y - 2 >= 0){
            if (board[x - 1][ y - 2] instanceof Knight){
                if (board[x - 1][y - 2].getColor() != color){
                    return true;
                }
            }
        }
        if (x - 2 >= 0 && y - 1 >= 0){
            if (board[x - 2][ y - 1] instanceof Knight){
                if (board[x - 2][y - 1].getColor() != color){
                    return true;
                }
            }
        }
        if (x + 1 < 8 && y - 2 >= 0){
            if (board[x + 1][ y - 2] instanceof Knight){
                if (board[x + 1][y - 2].getColor() != color){
                    return true;
                }
            }
        }
        if (x + 2 < 8 && y - 1 >= 0){
            if (board[x  + 2][ y - 1] instanceof Knight){
                if (board[x + 2][y - 1].getColor() != color){
                    return true;
                }
            }
        }
        if (x - 1 >= 0 && y + 2 < 8){
            if (board[x - 1][ y + 2] instanceof Knight){
                if (board[x - 1][y + 2].getColor() != color){
                    return true;
                }
            }
        }
        if (x - 2 >= 0 && y + 1 < 8){
            if (board[x - 2][ y  + 1] instanceof Knight){
                if (board[x - 2][y + 1].getColor() != color){
                    return true;
                }
            }
        }
        if (x + 1 < 8 && y + 2 < 8){
            if (board[x + 1][ y + 2] instanceof Knight){
                if (board[x + 1][y + 2].getColor() != color){
                    return true;
                }
            }
        }
        if (x + 2 < 9 && y + 1 < 8){
            if (board[x + 2][ y  + 1] instanceof Knight){
                if (board[x + 2][y + 1].getColor() != color){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isDone(Board board){
            return false;
    }

    // Suggestion for a game over method checking if time's up. Could add a boolean like checkmate later on.
    // Would also suggest moving this method to GameEngine instead
    public static boolean isDone(Board board, GameTimer timer){
        while(timer.getGameTime() > 0){
            return false;
        }
        return true;
    }

    static private ArrayList<Integer> validMovesPawn(int x, int y, Piece[][] boardState){
        ArrayList<Integer> validMoves = new ArrayList<Integer>();
        if (boardState[x][y].getColor()) {
            if (boardState[x][y+1] == null) {
                validMoves.add(x);
                validMoves.add(y+1);
            }
            if (y == 1) {
                if (boardState[x][y+2] == null) {
                    validMoves.add(x);
                    validMoves.add(y+2);
                }
            }
            if (x + 1 < 8) {
                if (boardState[x + 1][y + 1] != null && !boardState[x + 1][y + 1].getColor()) {
                    validMoves.add(x + 1);
                    validMoves.add(y + 1);
                }
            }
            if (x - 1 >= 0) {
                if (boardState[x - 1][y + 1] != null && !boardState[x - 1][y + 1].getColor()) {
                    validMoves.add(x - 1);
                    validMoves.add(y + 1);
                }
            }

        }
        else {
            if (boardState[x][y-1] == null) {
                validMoves.add(x);
                validMoves.add(y-1);
            }
            if (y == 6) {
                if (boardState[x][y-2] == null) {
                    validMoves.add(x);
                    validMoves.add(y-2);
                }
            }
            if (x + 1 < 8) {
                if (boardState[x + 1][y - 1] != null && !boardState[x + 1][y - 1].getColor()) {
                    validMoves.add(x + 1);
                    validMoves.add(y - 1);
                }
            }
            if (x - 1 >= 0) {
                if (boardState[x - 1][y - 1] != null && !boardState[x - 1][y - 1].getColor()) {
                    validMoves.add(x - 1);
                    validMoves.add(y - 1);
                }
            }
        }
        return validMoves;
    }

    private static ArrayList<Integer> validMovesRook(int x, int y, Piece[][] boardState){
        ArrayList<Integer> validMoves = new ArrayList<Integer>();
        for (int i = 1; i < 8; i++) {
            boolean xLeft = true;
            boolean xRight = true;
            boolean yDown = true;
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

    private static ArrayList<Integer> validMovesKnight(int x, int y, Piece[][] boardState){
        ArrayList<Integer> validMoves = new ArrayList<Integer>();
        if ((boardState[x+2][y+1].getColor() != boardState[x][y].getColor() || boardState[x+2][y+1] == null)  && (x+2 < 8) && (y+1 < 8)) {
            validMoves.add(x+2);
            validMoves.add(y+1);
        }
        if ((boardState[x-2][y+1].getColor() != boardState[x][y].getColor() || boardState[x-2][y+1] == null) && (x-2 >= 0) && (y+1 < 8)) {
            validMoves.add(x-2);
            validMoves.add(y+1);
        }
        if ((boardState[x+2][y-1].getColor() != boardState[x][y].getColor()|| boardState[x+2][y-1] == null) && (x+2 < 8) && (y-1 >= 0)) {
            validMoves.add(x+2);
            validMoves.add(y-1);
        }
        if ((boardState[x-2][y-1].getColor() != boardState[x][y].getColor()|| boardState[x-2][y-1] == null) && (x-2 >= 0) && (y-1 >= 0)) {
            validMoves.add(x-2);
            validMoves.add(y-1);
        }
        if ((boardState[x+1][y+2].getColor() != boardState[x][y].getColor()|| boardState[x+1][y+2] == null) && (x+1 < 8) && (y+2 < 8)) {
            validMoves.add(x+1);
            validMoves.add(y+2);
        }
        if ((boardState[x-1][y+2].getColor() != boardState[x][y].getColor()|| boardState[x-1][y+2] == null) && (x-1 >= 0) && (y+2 < 8)) {
            validMoves.add(x-1);
            validMoves.add(y+2);
        }
        if ((boardState[x+1][y-2].getColor() != boardState[x][y].getColor()|| boardState[x+1][y-2] == null) && (x+1 < 8) && (y-2 >= 0)) {
            validMoves.add(x+1);
            validMoves.add(y-2);
        }
        if ((boardState[x-1][y-2].getColor() != boardState[x][y].getColor()|| boardState[x-1][y-2] == null) && (x-1 >= 0) && (y-2 >= 0)) {
            validMoves.add(x-1);
            validMoves.add(y-2);
        }
        return validMoves;
    }

    private static ArrayList<Integer> validMovesKing(int x, int y, Piece[][] boardState){
        ArrayList<Integer> validMoves = new ArrayList<Integer>();
        int[] move = { 1, 0, 1, 1, 0, 1, -1, 1, -1, 0, -1, -1, 0, -1, 1, -1};
        for (int i = 0; i < move.length; i += 2){
            if (x + move[i] < 8 && x + move[i] >= 0 && y + move[i + 1] >= 0 && y + move[i + 1] < 8){
                //if (!willBeCheck(x, y, x+move[i], y+move[i+1], boardState)) {
                    if (boardState[x + move[i]][y + move[i + 1]] == null) {
                        validMoves.add(x + move[i]);
                        validMoves.add(y + move[i + 1]);

                    } else if (boardState[x + move[i]][y + move[i + 1]].getColor() != boardState[x + move[i]][y + move[i + 1]].getColor()) {
                        validMoves.add(x + move[i]);
                        validMoves.add(y + move[i + 1]);
                    }
                //}
            }
        }
        return validMoves;
    }

    private static boolean willBeCheck(int fromx, int fromy, int tox, int toy, Piece[][] boardState){
        boardState[tox][toy] = boardState[fromx][fromy];
        boardState[fromx][fromy] = null;
        return inCheck(boardState, boardState[tox][toy].getColor());
    }

    private static ArrayList<Integer> validMovesBishop(int x, int y, Piece[][] boardState){
        ArrayList<Integer> validMoves = new ArrayList<Integer>();
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
        return validMoves;
    }

    public static ArrayList<Integer> validMoves(int x, int y, Board board) {
        ArrayList<Integer> validMoves = new ArrayList<Integer>();
        Piece[][] boardState = board.getBoardState();

        if (boardState[x][y] instanceof Pawn) {
            validMoves = validMovesPawn(x, y, boardState);
        }

        else if (boardState[x][y] instanceof Rook) {
            validMoves = validMovesRook(x, y, boardState);
        }

        else if (boardState[x][y] instanceof Knight){
            validMovesKnight(x, y, boardState);
        }

        else if (boardState[x][y] instanceof King) {
            validMoves = validMovesKing(x, y, boardState);
        }

        else if (boardState[x][y] instanceof Queen) {
            validMoves = validMovesRook(x, y, boardState);
            ArrayList<Integer> dia = validMovesBishop(x, y, boardState);

            for (int i = 0; i < dia.size(); i++) {
                validMoves.add(dia.get(i));
            }
        }

        else if (boardState[x][y] instanceof Bishop) {
            validMoves = validMovesBishop(x, y, boardState);
        }
        return validMoves;
    }
}