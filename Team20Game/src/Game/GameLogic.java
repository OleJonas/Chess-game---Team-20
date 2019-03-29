package Game;

import Pieces.*;
import Pieces.Piece;

import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;

public class GameLogic{
    private static HashMap<String, Integer> rep;
    public static boolean inCheck(Piece[][] state, boolean color){
        Piece[][] board = state;
        int x = 0, y = 2;
        outerloop:
        //find position of king
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[0].length; j++){
                if (board[i][j] instanceof King){
                    if (board[i][j].getColor() == color){
                        x = i;
                        y = j;
                        break outerloop;
                    }
                }
            }
        }

        // check king threats from all 8 main directions
        int[][] move =  {{1, 0, 0, 1, -1, 0, 0, -1}, {1, 1, -1, 1, -1, -1, 1, -1}};
        boolean[][] dir = {{true, true, true, true}, {true, true, true, true}};
        for (int r = 0; r < 2; r++){
            for (int i = 1; i < 8; i++){
                for (int k = 0; k < move[r].length; k += 2){
                    if (x+i*move[r][k] >= 0 && x+i*move[r][k] < 8 && y+i*move[r][k+1] >= 0 && y+i*move[r][k+1] < 8){
                        if (dir[r][k/2]){
                            if (board[x+i*move[r][k]][y+i*move[r][k+1]] != null){
                                if (board[x+i*move[r][k]][y+i*move[r][k+1]].getColor() != color){
                                    if (r == 0 && (board[x+i*move[r][k]][y+i*move[r][k+1]] instanceof Queen || board[x+i*move[r][k]][y+i*move[r][k+1]] instanceof Rook)) {
                                        return true;
                                    }
                                    if (r == 1 && (board[x+i*move[r][k]][y+i*move[r][k+1]] instanceof Queen || board[x+i*move[r][k]][y+i*move[r][k+1]] instanceof Bishop)) {
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
                        //System.out.println("Hest trussel:" + (x+k[i]) + " " + (y+k[i+1]));
                        return true;
                    }
                }
            }
        }
        int[] king = { 1, 0, 1, 1, 0, 1, -1, 1, -1, 0, -1, -1, 0, -1, 1, -1};
        for (int i = 0; i < king.length; i += 2){
            if (x + king[i] < 8 && x + king[i] >= 0 && y + king[i + 1] >= 0 && y + king[i + 1] < 8){
                if (board[x+king[i]][y+king[i+1]] != null){
                    if (board[x+king[i]][y+king[i+1]] instanceof King){
                        //System.out.println("Konge trussel:" + (x+king[i]) + " " + (y+king[i+1]));
                        return true;
                    }
                }
            }
        }
        int[] p =  new int[4];
        p[0] = -1;
        p[1] = board[x][y].getColor() ? 1 : -1;
        p[2] = 1;
        p[3] = board[x][y].getColor() ? 1 : -1;

        for (int i = 0; i < p.length; i += 2) {
            if (x + p[i] < 8 && x + p[i] >= 0 && y + p[i + 1] >= 0 && y + p[i + 1] < 8) {
                if (board[x+p[i]][y+p[i+1]] != null){
                    if (board[x+p[i]][y+p[i+1]].getColor() != color && board[x+p[i]][y+p[i+1]] instanceof Pawn){
                        //System.out.println("Bonde trussel:" + (x+p[i]) + " " + (y+p[i+1]));
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
                validMovesKingKnight(x, y, boardState, validMoves, move, i);
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
            } else {
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

    private static void validMovesKingKnight(int x, int y, Piece[][] boardState, ArrayList<Integer> validMoves, int[] move, int i) {
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
                validMovesKingKnight(x, y, boardState, validMoves, move, i);
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
        ArrayList<Integer> validMoves = null;
        Piece[][] boardState = board.getBoardState();
        boolean color = boardState[x][y].getColor();
        //Finding all possible moves
        if (boardState[x][y] != null){
            validMoves = new ArrayList<>();
            if (boardState[x][y] instanceof Pawn) {
                validMoves = validMovesPawn(x, y, boardState);
            } else if (boardState[x][y] instanceof Rook) {
                validMoves = validMovesRook(x, y, boardState);
            } else if (boardState[x][y] instanceof Knight) {
                validMoves = validMovesKnight(x, y, boardState);
            } else if (boardState[x][y] instanceof King) {
                validMoves = validMovesKing(x, y, boardState);
            } else if (boardState[x][y] instanceof Bishop) {
                validMoves = validMovesBishop(x, y, boardState);
            } else if (boardState[x][y] instanceof Queen) {
                validMoves = validMovesRook(x, y, boardState);
                validMoves.addAll(validMovesBishop(x, y, boardState));
            }
            inCheck(boardState, boardState[x][y].getColor());
        }
        //We remove the moves which would put the King in check
        for (int k = 0; k < validMoves.size(); k += 2) {
            Piece[][] tmp = createBoardCopy(boardState);
            if (tmp[x][y] instanceof Pawn) {
                tmp[x][y] = null;
                tmp[validMoves.get(k)][validMoves.get(k+1)] = new Pawn(color, k, k+1);
            } else if (tmp[x][y] instanceof King) {
                tmp[x][y] = null;
                tmp[validMoves.get(k)][validMoves.get(k+1)] = new King(color, k, k+1);
            } else if (tmp[x][y] instanceof Rook) {
                tmp[x][y] = null;
                tmp[validMoves.get(k)][validMoves.get(k+1)] = new Rook(color, k, k+1);
            } else if (tmp[x][y] instanceof Bishop) {
                tmp[x][y] = null;
                tmp[validMoves.get(k)][validMoves.get(k+1)] = new Bishop(color, k, k+1);
            } else if (tmp[x][y] instanceof Knight) {
                tmp[x][y] = null;
                tmp[validMoves.get(k)][validMoves.get(k+1)] = new Knight(color, k, k+1);
            } else if (tmp[x][y] instanceof Queen) {
                tmp[x][y] = null;
                tmp[validMoves.get(k)][validMoves.get(k+1)] = new Queen(color, k, k+1);
            }
            if (inCheck(tmp, color)) {
                validMoves.remove(k);
                validMoves.remove(k);
                k -= 2;
            }
        }
        return validMoves;
    }

    private static boolean[] castle(boolean color, Piece[][] boardState) {
        boolean[] castle = {true, true};
        if (color) {
            canCastle(0, castle, boardState, true);
        } else {
            canCastle(7, castle, boardState, false);
        }
        return castle;

    }

    private static void canCastle(int yValue, boolean[] castle, Piece[][] boardState, boolean color) {
        for (int i = 1; i < 4; i++) {
            if (4 + i < 7) {
                if (boardState[4+i][yValue] != null) {
                    castle[0] = false;
                }
                else {
                    Piece[][] tmp = createBoardCopy(boardState);
                    tmp[4][yValue] = null;
                    tmp[4+i][yValue] = new King(color, 4+i, yValue);
                    if (inCheck(tmp, color)) {
                        castle[0] = false;
                    }
                }
            }
            if (boardState[4-i][yValue] != null) {
                castle[1] = false;
            } else {
                if (4-i > 1) {
                    Piece[][] tmp = createBoardCopy(boardState);
                    tmp[4][yValue] = null;
                    tmp[4-i][yValue] = new King(color, 4-i, yValue);
                    if (inCheck(tmp, color)) {
                        castle[1] = false;
                    }
                }
            }
        }
    }
    //NEW
    private static Piece[][] createBoardCopy(Piece[][] boardState) {
        Piece[][] tmp = new Piece[8][8];
        for (int k = 0; k <8; k++) {
            for (int j = 0; j < 8; j++) {
                tmp[k][j] = boardState[k][j];
            }
        }
        return tmp;
    }
    //NEW
    public static boolean isStalemate(Board board, boolean color) {
        Piece[][] state = board.getBoardState();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (state[i][j] != null) {
                    if (state[i][j].getColor() == color) {
                        if (validMoves(i, j, board).size() > 0) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    //NEW
    public static boolean isCheckmate(Board board, boolean color) {
        if (inCheck(board.getBoardState(), color)) {
            return isStalemate(board, color);
        }
        return false;
    }
    //NEW
    public static int[] myPieces(Board board, boolean myColor) {
        Piece[][] boardState = board.getBoardState();
        int[] counter = new int[7];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (boardState[x][y] != null) {
                    if (boardState[x][y] instanceof Pawn && boardState[x][y].getColor() == myColor) {
                        counter[0]++;
                        counter[6]++;
                    }
                    if (boardState[x][y] instanceof King && boardState[x][y].getColor() == myColor) {
                        counter[1]++;
                        counter[6]++;
                    }
                    if (boardState[x][y] instanceof Queen && boardState[x][y].getColor() == myColor) {
                        counter[2]++;
                        counter[6]++;
                    }
                    if (boardState[x][y] instanceof Rook && boardState[x][y].getColor() == myColor) {
                        counter[3]++;
                        counter[6]++;
                    }
                    if (boardState[x][y] instanceof Bishop && boardState[x][y].getColor() == myColor) {
                        counter[4]++;
                        counter[6]++;
                    }
                    if (boardState[x][y] instanceof Knight && boardState[x][y].getColor() == myColor) {
                        counter[5]++;
                        counter[6]++;
                    }
                }
            }
        }
        return counter;
    }
    //NEW
    public static boolean notEnoughPieces(Board board) {
        if (myPieces(board, true)[6] == 1 && myPieces(board, false)[6] == 1) {
            return true;
        }
        else if (myPieces(board, true)[6] == 2) {
            if (myPieces(board, false)[6] == 1) {
                if (myPieces(board, true)[4] == 1) {
                    return true;
                }
                if (myPieces(board, true)[5] == 1) {
                    return true;
                }
            }
        }
        else if (myPieces(board, false)[6] == 2) {
            if (myPieces(board, true)[6] == 1) {
                if (myPieces(board, false)[4] == 1) {
                    return true;
                }
                if (myPieces(board, false)[5] == 1) {
                    return true;
                }
            }
        }
        return false;
    }
    //NEW
    public static int[] getElo(int whiteElo, int blackElo, int score) {
        int[] result = new int[2];
        double a = 1.0 / (1.0+ Math.pow(10.0, ((double)(blackElo-whiteElo))/400.0));
        double b = 1.0 / (1.0+ Math.pow(10.0, ((double)(whiteElo-blackElo))/400.0));
        double p1 = (score == 2 ? 0.5 : (score == 0 ? 1 : 0));
        result[0] = (int)Math.round((float) whiteElo + 32.0*(p1-a));
        result[1] = (int)Math.round((float) blackElo + 32.0*(1.0-p1-b));
        return result;
    }

    public static void main(String[] args) {
        int[] elo = getElo(1153, 1047, 0);
        int[] elo2 = getElo(1032, 1168, 0);


        System.out.println(elo2[0] + " " + elo2[1]);
    }

    public static boolean isMoveRepetition(HashMap<String, Integer> rep, Board board){
        if (rep.containsKey(board.toString())){
            int oldBoardState = rep.get(board.toString());
            rep.put(board.toString(), oldBoardState + 1);
        }
        else{
            rep.put(board.toString(), new Integer("1"));
        }
        return rep.get(board.toString()).compareTo(2) == 1;
    }
}