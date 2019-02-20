public class Bishop extends Piece {
    private char notation;
    public Bishop(boolean color, int x, int y, char notation) {
        super(color, x, y);
        this.notation = notation;
    }

    public char getNotation() {
        return 'B';
    }

    public String toString() {
        return "" + getNotation();
    }
}

