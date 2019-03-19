package Pieces;
import JavaFX.ChessDemo;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pawn extends Piece {
    private ImageView imageView;
    private boolean enPassant = false;
    public Pawn(boolean color, int x, int y) {
        super(color, x, y);
        try {
            Image image = color? new Image("Images/chessPieces/"+ChessDemo.skin+"/w_pawn_1x_ns.png",
                    ChessDemo.TILE_SIZE * ChessDemo.imageSize, ChessDemo.TILE_SIZE* ChessDemo.imageSize, true, true):
                    new Image("Images/chessPieces/"+ChessDemo.skin+"/b_pawn_1x_ns.png",
                            ChessDemo.TILE_SIZE * ChessDemo.imageSize, ChessDemo.TILE_SIZE * ChessDemo.imageSize, true, true);
            imageView = new ImageView(image);

        }catch(Exception e){

        }
    }
    public boolean getEnPassant() { return enPassant; }
    public void setEnPassant(boolean newValue) { enPassant = newValue; }

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