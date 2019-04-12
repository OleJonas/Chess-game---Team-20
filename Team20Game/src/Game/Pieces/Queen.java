/**
 * <h1>Queen</h1>
 * The purpose of this class is to create the two queens on the chessboard.
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
public class Queen extends Piece {
    private ImageView imageView;
    public Queen(boolean color, int x, int y) {
        super(color, x, y);
    }

    public char getNotation() {
        return 'Q';
    }

    public ImageView getImageView(){
        try {
            Image image = super.getColor()?new Image("Images/chessPieces/"+ ChessGame.skin+"/w_queen_1x_ns.png", ChessGame.TILE_SIZE*ChessGame.imageSize, ChessGame.TILE_SIZE*ChessGame.imageSize, true, true) :
                    new Image("Images/chessPieces/"+ChessGame.skin+"/b_queen_1x_ns.png", ChessGame.TILE_SIZE * ChessGame.imageSize, ChessGame.TILE_SIZE * ChessGame.imageSize, true, true);
            return imageView = new ImageView(image);

        }catch(Exception e){

        }
        return null;
    }

    public String toString() {
        return super.toString();
    }
}