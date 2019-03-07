public abstract class Piece {
    private boolean color;
    private int x;
    private int y;

    public Piece(boolean color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public boolean getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public abstract char getNotation();

    /*
    public int moveCounter() {
        counter = 0;
        return counter; //denne metoden skal inkrementere counter hvis spørring til database er vellykket
    }

    public String toString() {
        return getNotation() + "" + ((char) (getX() + 'A')) + "" + (getY() + 1);
    }

    //public abstract boolean move(int x, int y); //Takes chosen coordinates as arguments and compares them to the array returned from legalMove.

    //public abstract int[][] legalMove(int a, int b); //Takes the coordinates of the desired piece as arguments. Returns array of legal moves
*/
}

