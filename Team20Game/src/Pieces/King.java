package Pieces;
import JavaFX.ChessDemo;
import JavaFX.ChessGame;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class King extends Piece {
    private ImageView imageView;
    private boolean canCastle;
    public King(boolean color, int x, int y) {
        super(color, x, y);
        this.canCastle = true;
    }

    public ImageView getImageView(){
        try {
            Image image = super.getColor()? new Image("Images/chessPieces/"+ ChessGame.skin +"/w_king_1x_ns.png",
                    ChessDemo.TILE_SIZE*ChessDemo.imageSize, ChessDemo.TILE_SIZE*ChessDemo.imageSize*0.9, false, true):
                    new Image("Images/chessPieces/"+ChessGame.skin+"/b_king_2x_ns.png",
                            ChessDemo.TILE_SIZE * ChessDemo.imageSize, ChessDemo.TILE_SIZE*ChessDemo.imageSize, false, true);
            return imageView = new ImageView(image);

        }catch(Exception e){

        }
        return null;
    }

    public char getNotation() {
        return 'K';
    }

    public String toString() {
        return super.toString();
    }
    public boolean getCanCastle() { return canCastle; }
    public void setCanCastle(boolean newVal) {
        this.canCastle = newVal;
    }
}