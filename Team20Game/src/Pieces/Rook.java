package Pieces;
import JavaFX.ChessDemo;
import JavaFX.ChessGame;
import javafx.scene.image.*;

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
                            ChessDemo.TILE_SIZE*ChessDemo.imageSize, ChessDemo.TILE_SIZE *ChessDemo.imageSize, true, true):
                    new Image("Images/chessPieces/"+ChessGame.skin+"/b_rook_1x_ns.png",
                            ChessDemo.TILE_SIZE*ChessDemo.imageSize, ChessDemo.TILE_SIZE*ChessDemo.imageSize, true, true);
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
    public boolean getCanCastle() { return canCastle; }
    public void setCanCastle(boolean newVal) {
        this.canCastle = newVal;
    }
}