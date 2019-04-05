package Pieces;

import JavaFX.GameScene.ChessGame;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Knight extends Piece {
    private ImageView imageView;
    public Knight(boolean color, int x, int y) {
        super(color, x, y);
    }

    public char getNotation() {
        return 'N';
    }

    public ImageView getImageView(){
        try {
            Image image = super.getColor()? new Image("Images/chessPieces/"+ChessGame.skin+"/w_knight_1x_ns.png", ChessGame.TILE_SIZE * ChessGame.imageSize, ChessGame.TILE_SIZE * ChessGame.imageSize, true, true):
                    new Image("Images/chessPieces/"+ ChessGame.skin+"/b_knight_1x_ns.png", ChessGame.TILE_SIZE * ChessGame.imageSize, ChessGame.TILE_SIZE* ChessGame.imageSize, true, true);
            return imageView = new ImageView(image);

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String toString() {
        return super.toString();
    }
}