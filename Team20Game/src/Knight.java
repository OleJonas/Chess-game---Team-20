import JavaFX.ChessDemo;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Knight extends Piece {
    private ImageView imageView;
    public Knight(boolean color, int x, int y) {
        super(color, x, y);
        try {
            Image image = color? new Image("Images/chessPieces/w_knight_1x_ns.png", ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE, true, true):
                    new Image("Images/chessPieces/b_knight_1x_ns.png", ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE, true, true);
            imageView = new ImageView(image);

        }catch(Exception e){

        }
    }

    public char getNotation() {
        return 'N';
    }

    public ImageView getImageView(){
        return imageView;
    }

    public String toString() {
        return super.toString();
    }
}