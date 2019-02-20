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
        this.position[0] = {new Rook(true), new Knight(true), new Bishop(true),  new Queen(true), new King(true),
                new Bishop(true), new Knight(true),  new Rook(true)};
        this.position[7] = {new Rook(false), new Knight(false), new Bishop(false), new Queen(false), new King(false),
                new Bishop(false), new Knight(false),  new Rook(false)};
    }
    public String toString(){
        String a = "";
        for(int i = 0; i<position.length; i++) {
            for (int j = 0; j < position[i].length; j++) {
                if(position[i][j]==null){
                    a+= "empty, ";
                }else {
                    a += position[i][j].getNotation() + ", ";
                }
            }
            a+="\n";
        }
        return a;
    }
    public static void main(String[] args){
        Board board = new Board();
        System.out.println(board);
    }
}
