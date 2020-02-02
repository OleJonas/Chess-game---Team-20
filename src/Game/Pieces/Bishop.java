/**
 * <h1>Bishop</h1>
 * The purpose of this class is to create the four bishops on the chessboard.
 * @since 05.04.2019
 * @author Team 20
 */

package Game.Pieces;

import GUI.GameScene.ChessGame;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @see Game.Pieces.Piece
 */
public class Bishop extends Piece {
    private ImageView imageView;

    /**
     * Constructor for the Bishop class
     * @param color which player the piece belongs to, white = true, black = false
     * @param x the x-value of the piece on the board
     * @param y the y-value of the piece on the board
     */
    public Bishop(boolean color, int x, int y) {
        super(color, x, y);
    }

    /**
     * Returns notation for the bishop class
     * @return notation
     */
    public char getNotation() {
        return 'B';
    }

    /**
     * This method returns the photo of the bishop
     * @return image
     */
    public ImageView getImageView(){
        try {
            Image image = super.getColor()? new Image(
                    "Images/chessPieces/"+ ChessGame.skin+"/w_bishop_1x_ns.png", ChessGame.TILE_SIZE*ChessGame.imageSize, ChessGame.TILE_SIZE*ChessGame.imageSize,true, true):
                    new Image("Images/chessPieces/"+ChessGame.skin+"/b_bishop_1x_ns.png", ChessGame.TILE_SIZE*ChessGame.imageSize, ChessGame.TILE_SIZE*ChessGame.imageSize, true, true);
            return imageView = new ImageView(image);

        }catch(Exception e){

        }
        return null;
    }

    /**
     * This method inherits the toString()-method from the superclass.
     * @return notation with (x,y)-position
     */
    public String toString() {
        return super.toString();
    }
}