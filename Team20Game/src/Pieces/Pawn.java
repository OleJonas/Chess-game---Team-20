package Pieces;

import JavaFX.GameScene.ChessGame;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pawn extends Piece {
    private ImageView imageView;
    private boolean enPassant = false;
    public Pawn(boolean color, int x, int y) {
        super(color, x, y);
    }
    public boolean getEnPassant() { return enPassant; }
    public void setEnPassant(boolean newValue) { enPassant = newValue; }

    public char getNotation() {
        return ' ';
    }

    public ImageView getImageView(){
        try {
            Image image = super.getColor()? new Image("Images/chessPieces/"+ ChessGame.skin+"/w_pawn_1x_ns.png",
                    ChessGame.TILE_SIZE * ChessGame.imageSize, ChessGame.TILE_SIZE* ChessGame.imageSize, true, true):
                    new Image("Images/chessPieces/"+ChessGame.skin+"/b_pawn_1x_ns.png",
                            ChessGame.TILE_SIZE * ChessGame.imageSize, ChessGame.TILE_SIZE * ChessGame.imageSize, true, true);
            return imageView = new ImageView(image);

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}