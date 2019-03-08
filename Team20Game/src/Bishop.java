import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;

public class Bishop extends Piece {
    private ImageView imageView;

    public Bishop(boolean color, int x, int y) {
        super(color, x, y);
        try {
            Image image = color? new Image(
                    "Images/chessPieces/w_bishop_1x_ns.png", ChessDemo.TILE_SIZE*ChessDemo.imageSize, ChessDemo.TILE_SIZE*ChessDemo.imageSize,true, true):
                    new Image("Images/chessPieces/b_bishop_1x_ns.png", ChessDemo.TILE_SIZE*ChessDemo.imageSize, ChessDemo.TILE_SIZE*ChessDemo.imageSize, true, true);
            imageView = new ImageView(image);

        }catch(Exception e){

        }
    }

    public char getNotation() {
        return 'B';
    }

    public ImageView getImageView(){
        return imageView;
    }

    public String toString() {
        return super.toString();
    }
}