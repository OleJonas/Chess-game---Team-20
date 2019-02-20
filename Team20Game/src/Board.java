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
        this.position[0] = {new Rook(true, 0, 0), new Knight(true, 1, 0), new Bishop(true, 2, 0),  new Queen(true, 3, 0),
                new King(true, 4, 0), new Bishop(true, 5, 0), new Knight(true, 6, 0),  new Rook(true, 7, 0)};
        this.position[7] = {new Rook(false, 0, 7), new Knight(false, 1, 7), new Bishop(fals, 2, 7), new Queen(false, 3, 7),
                new King(false, 4, 7), new Bishop(false, 5, 7), new Knight(false, 6, 7),  new Rook(false, 7, 7)};
    }
    public String toString(){
        String a = "";
        for(int j = 0; j<position.length; j++) {
            for (int i = 0; i < position[j].length; i++) {
                if(position[i][j]==null){
                    a+= "empty, ";
                }else {
                    a += position[i][j] + ", ";
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
    }
}
