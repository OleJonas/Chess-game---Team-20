/**
 * <h1>King</h1>
 * The purpose of this class is to create the two kings on the chessboard.
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
public class King extends Piece {
    private ImageView imageView;
    private boolean canCastle;

    /**
     * @see Game.Pieces.Piece#Piece(boolean, int, int)
     */
    public King(boolean color, int x, int y) {
        super(color, x, y);
        this.canCastle = true;
    }

    /**
     * @see Piece#getImageView()
     */
    public ImageView getImageView(){
        try {
            Image image = super.getColor()? new Image("Images/chessPieces/"+ ChessGame.skin +"/w_king_1x_ns.png",
                    ChessGame.TILE_SIZE*ChessGame.imageSize, ChessGame.TILE_SIZE*ChessGame.imageSize*0.9, false, true):
                    new Image("Images/chessPieces/"+ChessGame.skin+"/b_king_2x_ns.png",
                            ChessGame.TILE_SIZE * ChessGame.imageSize, ChessGame.TILE_SIZE*ChessGame.imageSize, false, true);
            return imageView = new ImageView(image);

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public char getNotation() {
        return 'K';
    }

    /**
     *
     * @return
     */
    public String toString() {
        return super.toString();
    }

    /**
     * This method returns a boolean which tells you if the king can castle or not.
     * @return
     */
    public boolean getCanCastle() { return canCastle; }

    /**
     * Method for changing the canCastle-boolean.
     * @param newVal
     */
    public void setCanCastle(boolean newVal) {
        this.canCastle = newVal;
    }
}