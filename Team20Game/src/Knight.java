public class Knight extends Piece {
    public Knight(boolean color, int x, int y) {
        super(color, x, y);
    }

    public char getNotation() {
        return 'N';
    }

    public String toString() {
        return super.toString();
    }
}