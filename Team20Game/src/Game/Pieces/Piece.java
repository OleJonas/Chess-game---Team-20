/**
 * <h1>Piece</h1>
 * The purpoce of this class is to create the different pieces on the chessboard.
 * @since 05.04.2019
 * @author Team 20
 */
package Game.Pieces;

import javafx.scene.image.ImageView;

/**
 * <h1>Piece</h1>
 * This abstract class contains a general description of the pieces. Some fundamental features of a piece are made here,
 * such as position values (x and y), and get/set methods for these values. Some additional features should exist for
 * all types of pieces, such as its letter using algebraic chess notation, that value is an abstract method here,
 * as it must be specified for all pieces.
 * @author Team20
 * @since 08.04.2019
 */
public abstract class Piece{
    private boolean color;
    private int x;
    private int y;

    /**
     * General constructor for the pieces.
     * @param color which player the piece belongs to, white = true, black = false
     * @param x the x-value of the piece on the board
     * @param y the y-value of the piece on the board
     */
    public Piece(boolean color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    /**
     * get the color of the piece
     * @return the color of the piece
     */
    public boolean getColor() {
        return color;
    }

    /**
     * get the x-position of the piece
     * @return the x-position of the piece on the board
     */
    public int getX() {
        return x;
    }

    /**
     * get the y-position of the piece
     * @return the y-position of the piece on the board
     */
    public int getY() {
        return y;
    }

    /**
     * set the x-position of the piece on the board
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * set the y-position of the piece on the board
     */
    public void setY(int y) {
        this.y = y;
    }
    /**
     * @return the letter of the piece in algebraic chess notation
     */
    public abstract char getNotation();

    /**
     * fetch image of the piece
     */
    public abstract ImageView getImageView();

    public String toString() {
        return getNotation() + "" + ((char) (getX() + 'a')) + "" + (getY() + 1);
    }

    /**
     * Equals method for use in JUnit tests
     */
    @Override
    public boolean equals(Object o){
        // If object is compared with itself
        if(o == this){
            return true;
        }

        if(!(o instanceof Piece)){
            return false;
        }

        if(this.getClass().equals(o.getClass())){
            if(this.getX() == ((Piece) o).getX() && this.getY() == ((Piece) o).getY()) {
                return true;
            }
        }

        return false;
    }
}
