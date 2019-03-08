import JavaFX.ChessDemo;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pawn extends Piece {
    private ImageView imageView;
    private int moves;
    public Pawn(boolean color, int x, int y) {
        super(color, x, y);
        this.moves = 0;
        try {
            Image image = color? new Image("Images/chessPieces/w_pawn_1x_ns.png", ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE, true, true):
                    new Image("Images/chessPieces/b_pawn_1x_ns.png", ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE, true, true);
            imageView = new ImageView(image);

        }catch(Exception e){

        }
    }

    public char getNotation() {
        return 'P';
    }

    public ImageView getImageView(){
        return imageView;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}