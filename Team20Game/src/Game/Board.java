package Game;

import Pieces.*;
import Pieces.Rook;
import Pieces.King;

import java.util.ArrayList;

public class Board {
    private Piece[][] position = new Piece[8][8];
    private boolean castleRook = true;
    private boolean castleKing = true;
    private ArrayList<Piece> removedPieces;

    public Board() {
        //Default constructor with no arguments will create the starting position of a standard chess game
        this.position = position;

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


        //add pawns
        for (int i = 0; i < 8; i++) {
            position[i][1] = new Pawn(true, i, 1);
            position[i][6] = new Pawn(false, i, 6);
        }

        /*position[0] = new Pieces.Piece[]{new Pieces.Rook(true, 0, 0), new Pieces.Knight(true, 1, 0), new Pieces.Bishop(true, 2, 0), new Pieces.Queen(true, 3, 0),
                new Pieces.King(true, 4, 0), new Pieces.Bishop(true, 5, 0), new Pieces.Knight(true, 6, 0), new Pieces.Rook(true, 7, 0)};
        position[7] = new Pieces.Piece[]{new Pieces.Rook(false, 0, 7), new Pieces.Knight(false, 1, 7), new Pieces.Bishop(false, 2, 7), new Pieces.Queen(false, 3, 7),
                new Pieces.King(false, 4, 7), new Pieces.Bishop(false, 5, 7), new Pieces.Knight(false, 6, 7), new Pieces.Rook(false, 7, 7)};
    */}

    public Board(Piece[][] positions){
        this.position = positions;
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

    public boolean move(int fromX, int fromY, int toX, int toY){
        if (castleRook && position[fromX][fromY] instanceof Rook) {
            Rook rook = (Rook) position[fromX][fromY];
            rook.setCanCastle(false);
            //castleRook = false;
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
                    }
                }
            }
        }

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

    public void removePiece(int x, int y){
        //removedPieces.add(position[x][y]);
        position[x][y] = null;
    }

    public void setPiece(Piece piece, int x, int y){
        position[x][y] = piece;
        //System.out.println("altered board, added piece at" + x + ", " + y +" " + piece);
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