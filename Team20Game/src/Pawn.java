public class Pawn extends Piece {
    public Pawn(boolean color, int x, int y) {
        super(color, x, y);
    }

    public char getNotation() {
        return ' ';
    }

    @Override
    public String toString() {
        return (super.getX() + 'A') + "" + (super.getY() + 1);
    }
}