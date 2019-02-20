public class Rook extends Piece {
    public Rook(boolean color, int x, int y) {
        super(color, x, y);
    }

    public char getNotation() {
        return 'N';
    }

    public String toString() {
        return super.toString();
    }
}