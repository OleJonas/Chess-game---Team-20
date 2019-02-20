public class Queen extends Piece {
    private char notation;
    public Queen(boolean color, int x, int y, char notation) {
        super(color, x, y);
        this.notation = notation;
    }

    public char getNotation() {
        return 'Q';
    }
}