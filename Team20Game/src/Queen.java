public class Queen extends Piece {
    public Queen(boolean color, int x, int y) {
        super(color, x, y);
    }

    public char getNotation() {
        return 'Q';
    }

    public String toString() {
        return super.toString();
    }
}