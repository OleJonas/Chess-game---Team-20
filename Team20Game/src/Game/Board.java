package Game;

import Pieces.*;

public class Board {
    private Piece[][] position = new Piece[8][8];

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
        if(position[fromX][fromY]==null){
            return false;
        }
        Piece temp = position[fromY][fromX];
        position[fromY][fromX] = null;
        position[toY][toX]=temp;
        position[toY][toX].setX(toX);
        position[toY][toX].setY(toY);
        return true;
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

    public static void main(String[] args){
        Board board = new Board();
        System.out.println(board);

        Rook rook = new Rook(true, 0, 0);
        System.out.println(rook);
        System.out.println(board.toString());

        Piece[][] boardstate = board.getBoardState();

        Board board2 = new Board(boardstate);
        System.out.println(board2.toString());

        GameEngine ge = new GameEngine(15, true);
        if(ge.move(0,0,1,3)){
            System.out.println("true");
        }
        System.out.println(ge.getBoard());
    }
}