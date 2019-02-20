public class Bishop extends Piece {

    public Bishop(boolean color, int x, int y) {
        super(color, x, y);
    }

    public char getNotation() {
        return 'B';
    }

    public String toString() {
        return super.toString();
    }
}