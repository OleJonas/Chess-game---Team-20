package Pieces;
import JavaFX.ChessDemo;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Queen extends Piece {
    private ImageView imageView;
    public Queen(boolean color, int x, int y) {
        super(color, x, y);
        try {
            Image image = color?new Image("Images/chessPieces/w_queen_1x_ns.png", ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE, true, true) :
                    new Image("Images/chessPieces/b_queen_1x_ns.png", ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE, true, true);
            imageView = new ImageView(image);

        }catch(Exception e){

        }
    }

    public char getNotation() {
        return 'Q';
    }

    public ImageView getImageView(){
        return imageView;
    }

    public String toString() {
        return super.toString();
    }
}