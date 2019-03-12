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

    private static boolean willBeCheck(int fromx, int fromy, int tox, int toy, Piece[][] boardState){
        boardState[tox][toy] = boardState[fromx][fromy];
        boardState[fromx][fromy] = null;
        return inCheck(boardState, boardState[tox][toy].getColor());
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
            if (y + 1 < 8) {
                if (boardState[x][y+1] == null) {
                    validMoves.add(x);
                    validMoves.add(y+1);
                }
            }
            if (y == 1) {
                if (boardState[x][y+2] == null) {
                    validMoves.add(x);
                    validMoves.add(y+2);
                }
            }
            if (x + 1 < 8 && y + 1 < 8) {
                if (boardState[x + 1][y + 1] != null && !boardState[x + 1][y + 1].getColor()) {
                    validMoves.add(x + 1);
                    validMoves.add(y + 1);
                }
            }
            if (x - 1 >= 0 && y + 1 < 8) {
                if (boardState[x - 1][y + 1] != null && !boardState[x - 1][y + 1].getColor()) {
                    validMoves.add(x - 1);
                    validMoves.add(y + 1);

                }
            }
        }
        else {
            if (y - 1 >= 0) {
                if (boardState[x][y-1] == null) {
                    validMoves.add(x);
                    validMoves.add(y-1);
                }
            }
            if (y == 6) {
                if (boardState[x][y-2] == null) {
                    validMoves.add(x);
                    validMoves.add(y-2);
                }
            }
            if (x + 1 < 8 && y - 1 >= 0) {
                if (boardState[x + 1][y - 1] != null && boardState[x + 1][y - 1].getColor()) {
                    validMoves.add(x + 1);
                    validMoves.add(y - 1);
                }
            }
            if (x - 1 >= 0 && y - 1 >= 0) {
                if (boardState[x - 1][y - 1] != null && boardState[x - 1][y - 1].getColor()) {
                    validMoves.add(x - 1);
                    validMoves.add(y - 1);
                }
            }
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

                } else if (boardState[x + move[i]][y + move[i + 1]].getColor() != boardState[x][y].getColor()) {
                    validMoves.add(x + move[i]);
                    validMoves.add(y + move[i + 1]);
                }
                //}
            }
        }
        King king = (King) boardState[x][y];
        if (king.getCanCastle()) {
            if (boardState[x][y].getColor()) {
                if (boardState[0][0] instanceof Rook || boardState[7][0] instanceof Rook) {
                    boolean[] castle = castle(boardState[x][y].getColor(), boardState);
                    if (castle[0]) {
                        validMoves.add(6);
                        validMoves.add(0);
                    }
                    if (castle[1]) {
                        validMoves.add(2);
                        validMoves.add(0);
                    }
                }
            }
            else {
                if (boardState[7][0] instanceof Rook || boardState[7][7] instanceof Rook) {
                    boolean[] castle = castle(boardState[x][y].getColor(), boardState);
                    if (castle[0]) {
                        validMoves.add(6);
                        validMoves.add(7);
                    }
                    if (castle[1]) {
                        validMoves.add(2);
                        validMoves.add(7);
                    }
                }
            }
        }
        return validMoves;
    }

    private static ArrayList<Integer> validMovesKnight(int x, int y, Piece[][] boardState){
        ArrayList<Integer> validMoves = new ArrayList<Integer>();
        int[] move = {2, 1, -2, 1, 2, -1, -2, -1, 1, 2, -1, 2, 1, -2, -1, -2};
        for (int i = 0; i < move.length; i+=2) {
            if (x+move[i] < 8 && x+move[i] >= 0 && y+move[i+1] < 8 && y+move[i+1] >= 0) {
                if (boardState[x+move[i]][y+move[i+1]] == null) {
                    validMoves.add(x+move[i]);
                    validMoves.add(y+move[i+1]);
                }
                else if (boardState[x+move[i]][y+move[i+1]].getColor() != boardState[x][y].getColor()) {
                    validMoves.add(x+move[i]);
                    validMoves.add(y+move[i+1]);
                }
            }
        }
        return validMoves;
    }

    private static ArrayList<Integer> validMovesRook(int x, int y, Piece[][] boardState){
        ArrayList<Integer> validMoves = new ArrayList<Integer>();
        boolean xLeft = true, xRight = true, yDown = true, yUp = true;

        for (int i = 1; i < 8; i++) {
            if (x + i < 8 && xRight) {
                if (boardState[x + i][y] == null) {
                    validMoves.add(x+i);
                    validMoves.add(y);
                } else if(boardState[x][y].getColor() != boardState[x + i][y].getColor()) {
                    validMoves.add(x+i);
                    validMoves.add(y);
                    xRight = false;
                } else {
                    xRight = false;
                }
            }
            if (x - i >= 0 && xLeft) {
                if (boardState[x - i][y] == null) {
                    validMoves.add(x-i);
                    validMoves.add(y);
                } else if(boardState[x][y].getColor() != boardState[x - i][y].getColor()){
                    validMoves.add(x-i);
                    validMoves.add(y);
                    xLeft = false;
                } else {
                    xLeft = false;
                }
            }
            if (y + i < 8 && yUp) {
                if (boardState[x][y + i] == null) {
                    validMoves.add(x);
                    validMoves.add(y+i);
                } else if(boardState[x][y].getColor() != boardState[x][y+i].getColor()){
                    validMoves.add(x);
                    validMoves.add(y+i);
                    yUp = false;
                } else {
                    yUp = false;
                }

            }
            if (y - i >= 0 && yDown) {
                if (boardState[x][y - i] == null) {
                    validMoves.add(x);
                    validMoves.add(y-i);
                } else if(boardState[x][y].getColor() != boardState[x][y-i].getColor()) {
                    validMoves.add(x);
                    validMoves.add(y-i);
                    yDown = false;
                } else {
                    yDown = false;
                }

            }
        }
        return validMoves;
    }

    private static ArrayList<Integer> validMovesBishop(int x, int y, Piece[][] boardState){
        ArrayList<Integer> validMoves = new ArrayList<Integer>();
        boolean rightUp = true, leftDown = true, rightDown = true, leftUp = true;
        for (int i = 1; i < 8; i++)  {
            if (x+i < 8 && y+i<8 && rightUp) {
                if (boardState[x+i][y+i] == null) {
                    validMoves.add(x+i);
                    validMoves.add(y+i);
                } else if (boardState[x][y].getColor() != boardState[x+i][y+i].getColor()) {
                    validMoves.add(x+i);
                    validMoves.add(y+i);
                    rightUp = false;
                } else {
                    rightUp = false;
                }
            }
            if (x - i >= 0 && y - i >= 0 && leftDown) {
                if (boardState[x-i][y-i] == null) {
                    validMoves.add(x-i);
                    validMoves.add(y-i);
                } else if (boardState[x][y].getColor() != boardState[x-i][y-i].getColor()) {
                    validMoves.add(x-i);
                    validMoves.add(y-i);
                    leftDown = false;
                } else {
                    leftDown = false;
                }

            }
            if (x - i >= 0 && y + i < 8 && leftUp) {
                if (boardState[x-i][y+i] == null) {
                    validMoves.add(x-i);
                    validMoves.add(y+i);
                } else if (boardState[x][y].getColor() != boardState[x-i][y+i].getColor()) {
                    validMoves.add(x-i);
                    validMoves.add(y+i);
                    leftUp = false;
                } else {
                    leftUp = false;
                }
            }
            if (x+i < 8 && y-i >= 0 && rightDown) {
                if (boardState[x+i][y-i] == null) {
                    validMoves.add(x+i);
                    validMoves.add(y-i);
                } else if (boardState[x][y].getColor() != boardState[x+i][y-i].getColor()) {
                    validMoves.add(x+i);
                    validMoves.add(y-i);
                    rightDown = false;
                }
                else {
                    rightDown = false;
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
            validMoves = validMovesKnight(x, y, boardState);
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

    private static boolean[] castle(boolean color, Piece[][] boardState) {
        boolean[] castle = {true, true};
        if (color) {
            for (int i = 1; i < 4; i++) {
                if (4 + i < 7) {
                    if (boardState[4+i][0] != null) {
                        castle[0] = false;
                    }
                }
                if (boardState[4-i][0] != null) {
                    castle[1] = false;
                }
            }
        } else {
            for (int i = 1; i < 4; i++) {
                if (4 + i < 7) {
                    if (boardState[4+i][7] != null) {
                        castle[0] = false;
                    }
                }
                if (boardState[4-i][7] != null) {
                    castle[1] = false;
                }
            }
        }
        return castle;
    }
}