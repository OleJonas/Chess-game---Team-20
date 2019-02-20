public class Pawn extends Piece {
    private char notation;
    public Pawn(boolean color, int x, int y, char notation) {
        super(color, x, y);
        this.notation = notation;
    }

    public char getNotation() {
        return ' ';
    }
}