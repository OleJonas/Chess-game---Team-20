public class Knight extends Piece {
    private char notation;
    public Knight(boolean color, int x, int y, char notation) {
        super(color, x, y);
        this.notation = notation;
    }

    public char getNotation() {
        return 'N';
    }
}