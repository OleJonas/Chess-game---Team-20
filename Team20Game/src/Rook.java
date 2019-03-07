import javafx.scene.image.*;

public class Rook extends Piece {
    private ImageView imageView;
    public Rook(boolean color, int x, int y) {
        super(color, x, y);
        try {
            Image image = color?
                    new Image("Images/chessPieces/w_rook_1x_ns.png", ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE, true, true):
                    new Image("Images/chessPieces/b_rook_1x_ns.png", ChessDemo.TILE_SIZE, ChessDemo.TILE_SIZE, true, true);
             imageView = new ImageView(image);

        }catch(Exception e){
            imageView = null;
        }

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


}