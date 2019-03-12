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
        int[][] move =  {{1, 0, 0, 1, -1, 0, 0, -1}, {1, 1, -1, 1, -1, -1, 1, -1}};
        boolean[][] dir = {{true, true, true, true}, {true, true, true, true}};
        for (int r = 0; r < 2; r++){
            for (int i = 1; i < 8; i++){
                for (int k = 0; k < move[r].length; k += 2){
                    if (x+i*move[r][k] >= 0 && i*x+move[r][k] < 8 && y+i*move[r][k+1] >= 0 && y+i*move[r][k+1] < 8){
                        if (dir[r][k/2]){
                            if (board[x+i*move[r][k]][y+i*move[r][k+1]] != null){
                                if (board[x+i*move[r][k]][y+i*move[r][k+1]].getColor() != color){
                                    if (board[x+i*move[r][k]][y+i*move[r][k+1]] instanceof Queen || board[x+i*move[r][k]][y+i*move[r][k+1]] instanceof Rook) {
                                        System.out.println("Dronning/tårn truer:" + x+i*move[r][i] + " " + y+i*move[r][i+1]);
                                        return true;
                                    }
                                }
                                dir[r][k/2] = false;
                            }
                        }
                    }
                }
            }
        }
        int[] k = {2, 1, 2, -1, -2, 1, -2, -1, 1, 2, -1, 2, 1, -2, -1, -2};
        for (int i = 0; i < k.length; i += 2) {
            if (x + k[i] < 8 && x + k[i] >= 0 && y + k[i + 1] >= 0 && y + k[i + 1] < 8) {
                if (board[x+k[i]][y+k[i+1]] != null){
                    if (board[x+k[i]][y+k[i+1]].getColor() != color && board[x+k[i]][y+k[i+1]] instanceof Knight){
                        System.out.println("Hest truer" + x+k[i] + " " + y+k[i+1]);
                        return true;
                    }
                }
            }
        }
        int[] p = {-1, 1, 1, 1};
        for (int i = 0; i < p.length; i += 2) {
            if (x + k[i] < 8 && x + k[i] >= 0 && y + k[i + 1] >= 0 && y + k[i + 1] < 8) {
                if (board[x+k[i]][y+k[i+1]] != null){
                    if (board[x+k[i]][y+k[i+1]].getColor() != color && board[x+k[i]][y+k[i+1]] instanceof Pawn){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*private static boolean willBeCheck(int fromx, int fromy, int tox, int toy, Piece[][] boardState){
        return false;
    }*/

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
                private_method(x, y, boardState, validMoves, move, i);
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

    private static void private_method(int x, int y, Piece[][] boardState, ArrayList<Integer> validMoves, int[] move, int i) {
        if (boardState[x + move[i]][y + move[i + 1]] == null) {
            validMoves.add(x + move[i]);
            validMoves.add(y + move[i + 1]);

        } else if (boardState[x + move[i]][y + move[i + 1]].getColor() != boardState[x][y].getColor()) {
            validMoves.add(x + move[i]);
            validMoves.add(y + move[i + 1]);
        }
    }

    private static ArrayList<Integer> validMovesKnight(int x, int y, Piece[][] boardState){
        ArrayList<Integer> validMoves = new ArrayList<Integer>();
        int[] move = {2, 1, -2, 1, 2, -1, -2, -1, 1, 2, -1, 2, 1, -2, -1, -2};
        for (int i = 0; i < move.length; i+=2) {
            if (x+move[i] < 8 && x+move[i] >= 0 && y+move[i+1] < 8 && y+move[i+1] >= 0) {
                private_method(x, y, boardState, validMoves, move, i);
            }
        }
        return validMoves;
    }

    private static ArrayList<Integer> validMovesRook(int x, int y, Piece[][] boardState){
        int[]rook = {1, 0, 0, 1, -1, 0, 0, -1};
        boolean[] dir = {true, true, true, true};
        ArrayList<Integer> validMoves = new ArrayList<Integer>();
        for(int i = 1; i < 8; i ++){
            for (int r = 0; r < rook.length; r += 2){
                if (x+i*rook[r] < 8 && x+i*rook[r] >= 0 && y+i*rook[r+1] < 8 && y+i*rook[r+1] >= 0 && dir[r/2]){
                    if (boardState[x+i*rook[r]][y+i*rook[r+1]] == null) {
                        validMoves.add(x+i*rook[r]);
                        validMoves.add(y+i*rook[r+1]);
                    }
                    else if (boardState[x+i*rook[r]][y+i*rook[r+1]].getColor() != boardState[x][y].getColor()){
                        validMoves.add(x+i*rook[r]);
                        validMoves.add(y+i*rook[r+1]);
                    }
                    else{ dir[r/2] = false; }
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

        if (boardState[x][y] instanceof Pawn) { validMoves = validMovesPawn(x, y, boardState); }
        else if (boardState[x][y] instanceof Rook) { validMoves = validMovesRook(x, y, boardState); }
        else if (boardState[x][y] instanceof Knight){ validMoves = validMovesKnight(x, y, boardState); }
        else if (boardState[x][y] instanceof King) { validMoves = validMovesKing(x, y, boardState); }
        else if (boardState[x][y] instanceof Bishop) { validMoves = validMovesBishop(x, y, boardState); }
        else if (boardState[x][y] instanceof Queen) {
            validMoves = validMovesRook(x, y, boardState);
            validMoves.addAll(validMovesBishop(x, y, boardState));
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

    private static boolean isStalemate(int x, int y, Board board) {
        Piece[][] boardState = board.getBoardState();
        if (validMovesKing(x, y, boardState).size() != 0) {
            return false;
        }
        return true;
    }
}