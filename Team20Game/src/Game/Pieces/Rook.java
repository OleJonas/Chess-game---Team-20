/**
 * <h1>Rook</h1>
 * The purpose of this class is to create the two rooks on the chessboard.
 * @since 08.04.2019
 * @author Team 20
 */

package Game.Pieces;

import GUI.GameScene.ChessGame;
import javafx.scene.image.*;

/**
 *
 * @see Game.Pieces.Piece
 */
public class Rook extends Piece {
    private ImageView imageView;
    private boolean canCastle;
    public Rook(boolean color, int x, int y) {
        super(color, x, y);
        this.canCastle = true;

    }
    public ImageView getImageView(){
        try {
            Image image = super.getColor()?
                    new Image("Images/chessPieces/"+ ChessGame.skin+"/w_rook_1x_ns.png",
                            ChessGame.TILE_SIZE*ChessGame.imageSize, ChessGame.TILE_SIZE *ChessGame.imageSize, true, true):
                    new Image("Images/chessPieces/"+ChessGame.skin+"/b_rook_1x_ns.png",
                            ChessGame.TILE_SIZE*ChessGame.imageSize, ChessGame.TILE_SIZE*ChessGame.imageSize, true, true);
            return imageView = new ImageView(image);
        }catch(Exception e){
            imageView = null;
        }
        return null;
    }
    public char getNotation() {
        return 'R';
    }

    public String toString() {
        return super.toString();
    }

    /**
     * This method returns a boolean which tells if a rook can be used for castling or not.
     * @return
     */
    public boolean getCanCastle() { return canCastle; }

    public void setCanCastle(boolean newVal) {
        this.canCastle = newVal;
    }
}