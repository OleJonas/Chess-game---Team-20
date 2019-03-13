package Game;

import Pieces.*;
import Pieces.Piece;

import java.util.Collections;
import java.util.ArrayList;

public class GameLogic{
    private static boolean inCheck(Piece[][] state, boolean color){
        Piece[][] board = state;
        int x = 0, y = 2;
        //find position of king
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++){
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
        while(timer.getTime() > 0){
            return false;
        }
        return true;
    }

    static private ArrayList<Integer> validMovesPawn(int x, int y, Piece[][] boardState) {
        // Maybe implement an int that is either 1 or -1 depending on getColor(). This int is then used to multiply with each move.
        // This way we can avoid having basically the same code twice right after eachother.
        ArrayList<Integer> validMoves = new ArrayList<Integer>();
        if (boardState[x][y].getColor()) {
            if (y + 1 < 8) {
                if (boardState[x][y + 1] == null) {
                    validMoves.add(x);
                    validMoves.add(y + 1);
                }
            }
            if (y == 1) {
                if (boardState[x][y + 2] == null && boardState[x][y + 1] == null) {
                    validMoves.add(x);
                    validMoves.add(y + 2);
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
            //En passant
            if (x + 1 < 8) {
                if (boardState[x + 1][y] instanceof Pawn) {
                    if (boardState[x + 1][y].getColor() != boardState[x][y].getColor()) {
                        Pawn pawn = (Pawn) boardState[x + 1][y];
                        if (pawn.getEnPassant()) {
                            validMoves.add(x + 1);
                            validMoves.add(y + 1);
                        }
                    }
                }
            }
            if (x - 1 >= 0) {
                if (boardState[x - 1][y] instanceof Pawn) {
                    if (boardState[x - 1][y].getColor() != boardState[x][y].getColor()) {
                        Pawn pawn = (Pawn) boardState[x - 1][y];
                        if (pawn.getEnPassant()) {
                            validMoves.add(x - 1);
                            validMoves.add(y + 1);
                        }
                    }
                }
            }
        } else {
            if (y - 1 >= 0) {
                if (boardState[x][y - 1] == null) {
                    validMoves.add(x);
                    validMoves.add(y - 1);
                }
            }
            if (y == 6) {
                if (boardState[x][y - 2] == null && boardState[x][y - 1] == null) {
                    validMoves.add(x);
                    validMoves.add(y - 2);
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
            //En passant
            if (x + 1 < 8) {
                if (boardState[x + 1][y] instanceof Pawn) {
                    if (boardState[x + 1][y].getColor() != boardState[x][y].getColor()) {
                        Pawn pawn = (Pawn) boardState[x + 1][y];
                        if (pawn.getEnPassant()) {
                            validMoves.add(x + 1);
                            validMoves.add(y - 1);
                        }
                    }
                }
            }
            if (x - 1 >= 0) {
                if (boardState[x - 1][y] instanceof Pawn) {
                    if (boardState[x - 1][y].getColor() != boardState[x][y].getColor()) {
                        Pawn pawn = (Pawn) boardState[x - 1][y];
                        if (pawn.getEnPassant()) {
                            validMoves.add(x - 1);
                            validMoves.add(y - 1);
                        }
                    }
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
                boolean[] castle = castle(boardState[x][y].getColor(), boardState);
                if (boardState[7][0] instanceof Rook) {
                    Rook rook = (Rook) boardState[7][0];
                    if (castle[0] && rook.getCanCastle()) {
                        validMoves.add(6);
                        validMoves.add(0);
                    }
                }
                if (boardState[0][0] instanceof Rook) {
                    Rook rook = (Rook) boardState[0][0];
                    if (castle[1] && rook.getCanCastle()) {
                        validMoves.add(2);
                        validMoves.add(0);
                    }
                }
            }
            else {
                boolean[] castle = castle(boardState[x][y].getColor(), boardState);
                if (boardState[0][7] instanceof Rook) {
                    Rook rook = (Rook) boardState[0][7];
                    if (castle[1] && rook.getCanCastle()) {
                        validMoves.add(2);
                        validMoves.add(7);
                    }
                }
                if (boardState[7][7] instanceof Rook) {
                    Rook rook = (Rook) boardState[7][7];
                    if (castle[0] && rook.getCanCastle()) {
                        validMoves.add(6);
                        validMoves.add(7);
                    }
                }
            }
        }
        return validMoves;
    }


    private static void private_method(int x, int y, Piece[][] boardState, ArrayList<Integer> validMoves, int[] move, int i) {
        if (boardState[x + move[i]][y + move[i + 1]] == null) {
            Collections.addAll(validMoves,x + move[i], y + move[i + 1]);

        } else if (boardState[x + move[i]][y + move[i + 1]].getColor() != boardState[x][y].getColor()) {
            Collections.addAll(validMoves,x + move[i], y + move[i + 1]);
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
        return validMovesGeneral(x, y, boardState, new int[] {1, 0, 0, 1, -1, 0, 0, -1});
    }
    private static ArrayList<Integer> validMovesBishop(int x, int y, Piece[][] boardState){
        return validMovesGeneral(x, y, boardState, new int[] {1, 1, -1, 1, -1, -1, 1, -1});
    }

    private static ArrayList<Integer> validMovesGeneral(int x, int y, Piece[][] boardState, int[] m){
        ArrayList<Integer> validMoves = new ArrayList<Integer>();
        boolean[] dir = {true, true, true, true};
        for(int i = 1; i < 8; i ++){
            for (int r = 0; r < m.length; r += 2){
                if (x+i*m[r] < 8 && x+i*m[r] >= 0 && y+i*m[r+1] < 8 && y+i*m[r+1] >= 0 && dir[r/2]){
                    if (boardState[x+i*m[r]][y+i*m[r+1]] == null) {
                        Collections.addAll(validMoves, x + i * m[r], y + i * m[r + 1]);
                    }
                    else if (boardState[x+i*m[r]][y+i*m[r+1]].getColor() != boardState[x][y].getColor()){
                        Collections.addAll(validMoves, x + i * m[r], y + i * m[r + 1]);
                        dir[r/2] = false;
                    }
                    else{ dir[r/2] = false; }
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
        boolean color = boardState[x][y].getColor();
        for (int k = 0; k < validMoves.size(); k += 2) {
            Piece[][] tmp = new Piece[8][8];
            for (int i = 0; i <8; i++) {
                for (int j = 0; j < 8; j++) {
                    tmp[i][j] = board.getBoardState()[i][j];
                }
            }
            if (tmp[x][y] instanceof Pawn) {
                tmp[x][y] = null;
                tmp[validMoves.get(k)][validMoves.get(k+1)] = new Pawn(color, k, k+1);
            }
            else if (tmp[x][y] instanceof King) {
                tmp[x][y] = null;
                tmp[validMoves.get(k)][validMoves.get(k+1)] = new King(color, k, k+1);
            }
            else if (tmp[x][y] instanceof Rook) {
                tmp[x][y] = null;
                tmp[validMoves.get(k)][validMoves.get(k+1)] = new Rook(color, k, k+1);
            }
            else if (tmp[x][y] instanceof Bishop) {
                tmp[x][y] = null;
                tmp[validMoves.get(k)][validMoves.get(k+1)] = new Bishop(color, k, k+1);
            }
            else if (tmp[x][y] instanceof Knight) {
                tmp[x][y] = null;
                tmp[validMoves.get(k)][validMoves.get(k+1)] = new Knight(color, k, k+1);
            }
            else if (tmp[x][y] instanceof Queen) {
                tmp[x][y] = null;
                tmp[validMoves.get(k)][validMoves.get(k+1)] = new Queen(color, k, k+1);
            }
            if (inCheck(tmp, color)) {
                validMoves.remove(k);
                validMoves.remove(k+1);
            }
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

    private static boolean isStalemate(int x, int y, Board board, boolean color) {
        Piece[][] boardState = board.getBoardState();
        int counter = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boardState[i][j].getColor() == color) {
                    if (validMoves(x, y, board).size() == 0) {
                        counter++;
                    }
                }
            }
        }
        if (counter == myPieces(board, color)) {
            return true;
        }
        return false;
    }

    private static int myPieces(Board board, boolean myColor) {
        Piece[][] boardState = board.getBoardState();
        int counter = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (boardState[x][y].getColor() == myColor) {
                    counter++;
                }
            }
        }
        return counter;
    }
}