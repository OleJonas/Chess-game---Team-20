public class Board {
    private Piece[][] position;

    public Board() {
        //Default constructor with no arguments will create the starting position of a standard chess game
        this.position = new Piece[8][8];

        //add pawns
        for (int i = 0; i < 8; i++) {
            this.position[i][1] = new Pawn(true);
            this.position[i][6] = new Pawn(false);
        }
        //Rooks
        this.position[0][7] = new Rook(false);
        this.position[0][0] = new Rook(true);
        this.position[7][7] = new Rook(false);
        this.position[7][0] = new Rook(true);
        //Knights
        this.position[1][0] = new Knight(true);
        this.position[6][0] = new Knight(true);
        this.position[1][7] = new Knight(false);
        this.position[6][7] = new Knight(false);
        //Bishops
        this.position[2][0] = new Bishop(true);
        this.position[5][0] = new Bishop(true);
        this.position[2][7] = new Bishop(false);
        this.position[5][7] = new Bishop(false);
        //Queen & kings
        this.position[3][0] = new Queen(true);
        this.position[4][0] = new King(true);
        this.position[3][7] = new Queen(false);
        this.position[4][7] = new King(false);
    }
}
