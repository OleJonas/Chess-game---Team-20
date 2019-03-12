package Pieces;
import JavaFX.ChessDemo;
import javafx.scene.image.*;

public class Rook extends Piece {
    private ImageView imageView;
    private boolean canCastle;
    public Rook(boolean color, int x, int y) {
        super(color, x, y);
        try {
            Image image = color?
                    new Image("Images/chessPieces/w_rook_1x_ns.png",
                            ChessDemo.TILE_SIZE*ChessDemo.imageSize, ChessDemo.TILE_SIZE *ChessDemo.imageSize, true, true):
                    new Image("Images/chessPieces/b_rook_1x_ns.png",
                            ChessDemo.TILE_SIZE*ChessDemo.imageSize, ChessDemo.TILE_SIZE*ChessDemo.imageSize, true, true);
             imageView = new ImageView(image);

        }catch(Exception e){
            imageView = null;
        }
        this.canCastle = true;

    }
    public ImageView getImageView(){
        return imageView;
    }
    public char getNotation() {
        return 'R';
    }

    public String toString() {
        return super.toString();
    }
    public boolean getCanCastle() { return canCastle; }
    public void setCanCastle(boolean newVal) {
        this.canCastle = newVal;
    }
}