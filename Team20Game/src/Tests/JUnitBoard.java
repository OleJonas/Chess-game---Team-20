package Tests;

import Game.Board;
import Game.Pieces.*;
import org.junit.*;
import static org.junit.Assert.*;

public class JUnitBoard {
    private Board board;

    @Before
    public void beforeTest(){
        board = new Board(0);
    }

    @After
    public void afterTest(){
        board = null;
    }

    @Test
    public void testMove(){
        Board helper = new Board(0);
        Piece[][] help = helper.getBoardState();

        // White
        // Moving pawn 2 tiles, rook follows right behind, then moving pawn one tile
        board.move(0, 1, 0, 3,true);
        board.move(0, 0, 0, 2,true);
        board.move(1, 1, 1, 2,true);

        // Trying to take own piece, should work with this method
        board.move(1,0,2,0,true);

        // Testing to see if a piece can be moved wherever we want. Should work with this method...
        board.move(4,0,5,5,true);

        // Setting up help board
        help[0][0] = null;
        help[0][1] = null;
        help[0][2] = new Rook(true, 0, 2);
        help[0][3] = new Pawn(true, 0, 3);
        help[1][1] = null;
        help[1][0] = null;
        help[2][0] = new Knight(true, 2, 0);
        help[1][2] = new Pawn(true, 1, 2);
        help[4][0] = null;
        help[5][5] = new King(true, 5, 5);

        System.out.println(board.toString());
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 7; j++){
                if(help[i][j] == null && board.getBoardState()[i][j] == null){
                    // Do nothing
                } else if((help[i][j] == null && board.getBoardState()[i][j] != null) || help[i][j] != null && board.getBoardState()[i][j] == null){
                    // Do nothing
                } else{
                    assertTrue(help[i][j].equals(board.getBoardState()[i][j]));
                }
            }
        }
    }

    public static void main(String[] args){
        org.junit.runner.JUnitCore.main(JUnitBoard.class.getName());
    }
}
