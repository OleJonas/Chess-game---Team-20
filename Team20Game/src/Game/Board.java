package Game;

import Pieces.*;

import java.util.ArrayList;
import java.util.Random;

public class Board {
    private Piece[][] position = new Piece[8][8];
    private ArrayList<Piece> takenPieces = new ArrayList<>();
    private boolean castleKing = true;

    //constructor for creating a new board
    public Board(int mode) {
        boolean pawns = true;
        if (mode == 0) {
            //standard mode

            position[0][0] = new Rook(true, 0, 0);
            position[1][0] = new Knight(true, 1, 0);
            position[2][0] = new Bishop(true, 2, 0);
            position[3][0] = new Queen(true, 3, 0);
            position[4][0] = new King(true, 4, 0);
            position[5][0] = new Bishop(true, 5, 0);
            position[6][0] = new Knight(true, 6, 0);
            position[7][0] = new Rook(true, 7, 0);

            position[0][7] = new Rook(false, 0, 7);
            position[1][7] = new Knight(false, 1, 7);
            position[2][7] = new Bishop(false, 2, 7);
            position[3][7] = new Queen(false, 3, 7);
            position[4][7] = new King(false, 4, 7);
            position[5][7] = new Bishop(false, 5, 7);
            position[6][7] = new Knight(false, 6, 7);
            position[7][7] = new Rook(false, 7, 7);
        } else if (mode > 1000) {
            //fischer random
            //the value of mode is a randomly selected number which is used as a seed to create a random board setup
            //this seed is uploaded to the database so that both computers use the same seed so that the same board is generated
            Random random = new Random(mode);

            int whiteBishops = random.nextInt(4);
            position[whiteBishops*2 + 1][0] = new Bishop(true, whiteBishops*2 + 1, 0);
            position[whiteBishops*2 + 1][7] = new Bishop(false, whiteBishops*2 + 1, 7);

            int blackBishops = random.nextInt(4);
            position[blackBishops*2][0] = new Bishop(true, blackBishops*2, 0);
            position[blackBishops*2][7] = new Bishop(false, blackBishops*2, 7);

            int queen = random.nextInt(6);
            while (true) {
                if (position[queen][0] == null) {
                    position[queen][0] = new Queen(true, queen, 0);
                    position[queen][7] = new Queen(false, queen, 7);
                    break;
                }
                queen = random.nextInt(6);
            }

            int firstKnight = random.nextInt(5);
            while(true) {
                if (position[firstKnight][0] == null) {
                    position[firstKnight][0] = new Knight(true, firstKnight, 0);
                    position[firstKnight][7] = new Knight(false, firstKnight, 7);
                    break;
                }
                firstKnight = random.nextInt(5);
            }

            int secondKnight = random.nextInt(4);
            while (true) {
                if (position[secondKnight][0] == null) {
                    position[secondKnight][0] = new Knight(true, secondKnight, 0);
                    position[secondKnight][7] = new Knight(false, secondKnight, 7);
                    break;
                }
                secondKnight = random.nextInt(5);
            }

            ArrayList<Integer> open = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                if (position[i][0] == null) {
                    open.add(i);
                }
            }
            position[open.get(1)][0] = new King(true, open.get(1), 0);
            position[open.get(1)][7] = new King(false, open.get(1), 7);

            position[open.get(0)][0] = new Rook(true, open.get(0), 0);
            position[open.get(0)][7] = new Rook(false, open.get(0), 7);

            position[open.get(2)][0] = new Rook(true, open.get(2), 0);
            position[open.get(2)][7] = new Rook(false, open.get(2), 7);

        } else if (mode == 2) {
            //horse attack mode, all horses
            position[0][0] = new Knight(true, 0, 0);
            position[1][0] = new Knight(true, 1, 0);
            position[2][0] = new Knight(true, 2, 0);
            position[3][0] = new Knight(true, 3, 0);
            position[4][0] = new King(true, 4, 0);
            position[5][0] = new Knight(true, 5, 0);
            position[6][0] = new Knight(true, 6, 0);
            position[7][0] = new Knight(true, 7, 0);

            position[0][7] = new Knight(false, 0, 7);
            position[1][7] = new Knight(false, 1, 7);
            position[2][7] = new Knight(false, 2, 7);
            position[3][7] = new Knight(false, 3, 7);
            position[4][7] = new King(false, 4, 7);
            position[5][7] = new Knight(false, 5, 7);
            position[6][7] = new Knight(false, 6, 7);
            position[7][7] = new Knight(false, 7, 7);
        } else if (mode == 3) {
            //farmers chess, all pawns
            for (int i = 0; i < 8; i++) {
                if (i != 4) {
                    position[i][0] = new Pawn(true, i, 0);
                    position[i][7] = new Pawn(false, i, 7);
                } else {
                    position[i][0] = new King(true, i, 0);
                    position[i][7] = new King(false, i, 7);
                }
            }

        } else if (mode == 4) {
            //peaseants revolt, a variant where white has 4 knights and pawn, while black has only pawns
            position[4][0] = new King(true, 4, 0);
            position[4][7] = new King(false, 4, 7);

            position[4][6] = new Pawn(false, 4, 6);

            position[1][7] = new Knight(false, 1, 7);
            position[2][7] = new Knight(false, 2, 7);
            position[5][7] = new Knight(false, 5, 7);
            position[6][7] = new Knight(false, 6, 7);

            pawns = false;
        }

        //add pawns
        for (int i = 0; i < 8; i++) {
            position[i][1] = new Pawn(true, i, 1);
            if (pawns) {
                position[i][6] = new Pawn(false, i, 6);
            }
        }
    }

    public Piece[][] getBoardState(){
        Piece[][] out = new Piece[position.length][];
        for(int i = 0; i < position.length; i++){
            Piece[] aPosition = position[i];
            int aLength = aPosition.length;
            out[i] = new Piece[aLength];
            System.arraycopy(aPosition, 0, out[i], 0, aLength);
        }
        return out;
    }

    public boolean move(int fromX, int fromY, int toX, int toY, boolean lastMove){
        checkCastleEnPassant(fromX, fromY, lastMove);
        if(position[fromX][fromY]==null){
            return false;
        }
        Piece temp = position[fromX][fromY];
        position[fromX][fromY] = null;
        position[toX][toY]=temp;
        position[toX][toY].setX(toX);
        position[toX][toY].setY(toY);
        return true;
    }

    private void checkCastleEnPassant(int fromX, int fromY, boolean lastMove) {
        if (position[fromX][fromY] instanceof Rook) {
            Rook rook = (Rook) position[fromX][fromY];
            rook.setCanCastle(false);
        }
        if (castleKing && position[fromX][fromY] instanceof King) {
            King king = (King) position[fromX][fromY];
            king.setCanCastle(false);
            castleKing = false;
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (position[i][j] instanceof Pawn) {
                    if (position[fromX][fromY] != null) {
                        if (position[i][j].getColor() != position[fromX][fromY].getColor()) {
                            Pawn pawn = (Pawn) position[i][j];
                            if (pawn.getEnPassant()) {
                                pawn.setEnPassant(false);
                                break;
                            }
                        }

                        if (lastMove == position[fromX][fromY].getColor()) {
                            if (position[i][j].getColor() == position[fromX][fromY].getColor()) {
                                Pawn pawn = (Pawn) position[i][j];
                                if (pawn.getEnPassant()) {
                                    pawn.setEnPassant(false);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void removePiece(int x, int y){
        position[x][y] = null;
    }

    public void addTakenPiece(Piece piece) {
        takenPieces.add(piece);
        System.out.println(piece.toString());
    }

    public ArrayList<Piece> getTakenPieces() {
        return takenPieces;
    }

    public void setPiece(Piece piece, int x, int y){
        position[x][y] = piece;
    }

    public String toString(){
        String a = "";
        for(int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                if(position[j][i]==null){
                    a+= "*** ";
                }else {
                    a += position[j][i] + " ";
                }
            }
            a+="\n";
        }
        return a;
    }
}