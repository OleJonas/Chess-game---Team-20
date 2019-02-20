public abstract class Pieces {
    private boolean color;

    public Pieces(boolean color) {
        this.color = color;
    }

    public boolean getColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }
}
