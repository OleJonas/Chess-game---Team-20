package Pieces;

import JavaFX.ChessDemo;
import JavaFX.ChessGame;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bishop extends Piece {
    private ImageView imageView;
    public Bishop(boolean color, int x, int y) {
        super(color, x, y);
    }

    public char getNotation() {
        return 'B';
    }

    public ImageView getImageView(){
        try {
            Image image = super.getColor()? new Image(
                    "Images/chessPieces/"+ ChessGame.skin+"/w_bishop_1x_ns.png", ChessDemo.TILE_SIZE*ChessDemo.imageSize, ChessDemo.TILE_SIZE*ChessDemo.imageSize,true, true):
                    new Image("Images/chessPieces/"+ChessGame.skin+"/b_bishop_1x_ns.png", ChessDemo.TILE_SIZE*ChessDemo.imageSize, ChessDemo.TILE_SIZE*ChessDemo.imageSize, true, true);
            return imageView = new ImageView(image);

        }catch(Exception e){

        }
        return null;
    }

    public String toString() {
        return super.toString();
    }
}