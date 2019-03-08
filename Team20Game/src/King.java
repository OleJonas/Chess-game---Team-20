import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class King extends Piece {
    private ImageView imageView;
    public King(boolean color, int x, int y) {
        super(color, x, y);
        try {
            Image image = color? new Image("Images/chessPieces/w_king_1x_ns.png",
                    ChessDemo.TILE_SIZE*ChessDemo.imageSize, ChessDemo.TILE_SIZE*ChessDemo.imageSize*0.9, false, true):
                    new Image("Images/chessPieces/b_king_2x_ns.png",
                            ChessDemo.TILE_SIZE * ChessDemo.imageSize, ChessDemo.TILE_SIZE*ChessDemo.imageSize, false, true);
            imageView = new ImageView(image);

        }catch(Exception e){

        }
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


}