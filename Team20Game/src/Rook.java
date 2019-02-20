public class Rook extends Piece {
    private char notation;
    public Rook(boolean color, int x, int y, char notation) {
        super(color, x, y);
        this.notation = notation;
    }

    public char getNotation() {
        return 'R';
    }
}