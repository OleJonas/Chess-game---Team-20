import JavaFX.ChessDemo;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class King extends Piece {
    private ImageView imageView;
    private boolean canCastle;

    public King(boolean color, int x, int y) {
        super(color, x, y);
        try {
            Image image = color? new Image("Images/chessPieces/w_king_1x_ns.png", ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE, true, true):
                    new Image("Images/chessPieces/b_king_2x_ns.png", ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE, true, true);
            imageView = new ImageView(image);

        }catch(Exception e){

        }
        this.canCastle = true;
    }

    public ImageView getImageView(){
        return imageView;
    }

    public char getNotation() {
        return 'K';
    }

    public String toString() {
        return super.toString();
    }

    public void setCanCastle(boolean newVal) {
        this.canCastle = newVal;
    }
}