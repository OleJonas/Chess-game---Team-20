public class Board {
    private Piece[][] position;

    public Board() {
        //Default constructor with no arguments will create the starting position of a standard chess game
        this.position = new Piece[8][8];

        //add pawns
        for (int i = 0; i < 8; i++) {
            this.position[i][1] = new Pawn(true, i, 1);
            this.position[i][6] = new Pawn(false, i, 6);
        }
        //Rooks
        this.position[0][7] = new Rook(false, 0, 7);
        this.position[0][0] = new Rook(true, 0, 0);
        this.position[7][7] = new Rook(false, 7, 7);
        this.position[7][0] = new Rook(true, 7, 7);
        //Knights
        this.position[1][0] = new Knight(true,1 ,0);
        this.position[6][0] = new Knight(true, 6, 0);
        this.position[1][7] = new Knight(false, 1,7 );
        this.position[6][7] = new Knight(false, 6, 7);
        //Bishops
        this.position[2][0] = new Bishop(true, 2, 0);
        this.position[5][0] = new Bishop(true, 5, 0);
        this.position[2][7] = new Bishop(false, 2, 7);
        this.position[5][7] = new Bishop(false, 5, 7);
        //Queen & kings
        this.position[3][0] = new Queen(true, 3, 0);
        this.position[4][0] = new King(true, 4, 0);
        this.position[3][7] = new Queen(false, 3, 7);
        this.position[4][7] = new King(false, 4, 7);
    }
}
