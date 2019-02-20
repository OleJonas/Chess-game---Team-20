public class King extends Piece {
    public King(boolean color, int x, int y) {
        super(color, x, y);
    }

    public char getNotation() {
        return 'K';
    }

    public String toString() {
        return super.toString();
    }
}