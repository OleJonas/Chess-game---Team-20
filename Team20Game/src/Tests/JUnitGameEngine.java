package Tests;

import Game.GameEngine;
import GUI.GameScene.ChessGame;
import Game.Pieces.*;
import org.junit.*;
import java.util.ArrayList;
import static org.junit.Assert.*;


// This class tests the GameEngine class. The GameEngine relies on the GameLogic
// -class to function properly. Therefore we also test some GameLogic in this class.
// The Board class is also used quite frequently here

public class JUnitGameEngine {
    private GameEngine ge;

    @Before
    public void beforeTest(){ this.ge = new GameEngine(0);}

    @After
    public void afterTest(){ this.ge = null;}

    @Test
    public void testRemovePiece(){
        ge.removePiece(1, 1);
        assertTrue(ge.getBoard().getBoardState()[1][1] == null);
    }

    @Test
    public void testMyPieces(){

        // Testing for white
        int[] whitePieces = ge.myPieces(ge.getBoard(), true);
        assertTrue(whitePieces[0] == 8 && whitePieces[1] == 1 && whitePieces[2] == 1 && whitePieces[3] == 2
            && whitePieces[4] == 2 && whitePieces[5] == 2 && whitePieces[6] == 16);

        // Testing for black
        int[] blackPieces = ge.myPieces(ge.getBoard(), false);
        assertTrue(blackPieces[0] == 8 && blackPieces[1] == 1 && blackPieces[2] == 1 && blackPieces[3] == 2
                && blackPieces[4] == 2 && blackPieces[5] == 2 && blackPieces[6] == 16);

        ge.removePiece(0,0);
        ge.removePiece(0,1);
        ge.removePiece(1,0);
        ge.removePiece(2,0);
        ge.removePiece(3,0);
        ge.removePiece(4,0);
        ge.removePiece(0,7);
        ge.removePiece(0,6);
        ge.removePiece(1,7);
        ge.removePiece(2,7);
        ge.removePiece(3,7);
        ge.removePiece(4,7);
        whitePieces = ge.myPieces(ge.getBoard(), true);
        blackPieces = ge.myPieces(ge.getBoard(), false);

        assertTrue(whitePieces[0] == 7 && whitePieces[1] == 0 && whitePieces[2] == 0 && whitePieces[3] == 1
                && whitePieces[4] == 1 && whitePieces[5] == 1 && whitePieces[6] == 10);

        assertTrue(blackPieces[0] == 7 && blackPieces[1] == 0 && blackPieces[2] == 0 && blackPieces[3] == 1
                && blackPieces[4] == 1 && blackPieces[5] == 1 && blackPieces[6] == 10);
    }

    @Test
    public void testInCheck(){
        // Moving black king out to the middle of the board and checking with bishop
        ge.move(4,7,3,4, ChessGame.lastMove);
        ge.move(2,0,2,3, ChessGame.lastMove);
        assertTrue(ge.inCheck(ge.getBoard().getBoardState(), false));

        // Checking with rook
        ge.removePiece(2,3);
        ge.move(0,0,0,4, ChessGame.lastMove);
        assertTrue(ge.inCheck(ge.getBoard().getBoardState(), false));

        // Blocking with pawn, should now return false
        ge.move(0,1,2,4, ChessGame.lastMove);
        assertFalse(ge.inCheck(ge.getBoard().getBoardState(), false));

        // Checking with pawn
        ge.removePiece(0,4);
        ge.move(2,4,2,3, ChessGame.lastMove);
        assertTrue(ge.inCheck(ge.getBoard().getBoardState(), false));

        // Checking with queen
        ge.move(3,7,3,1, ChessGame.lastMove);
        assertTrue(ge.inCheck(ge.getBoard().getBoardState(), true));

        // Checking with knight
        ge.removePiece(3,1);
        ge.move(1,7,2,1, ChessGame.lastMove);
        assertTrue(ge.inCheck(ge.getBoard().getBoardState(), true));
    }

    @Test
    public void testIsCheckmate(){
        ge.move(3,0,5,6, ChessGame.lastMove);
        ge.move(0,0,5,5, ChessGame.lastMove);
        assertTrue(ge.isCheckmate(ge.getBoard(), false));

        // Check for black as well
        ge.move(5,6,3,0, ChessGame.lastMove);
        ge.move(5,5,0,0, ChessGame.lastMove);
        ge.move(7,7,5,2, ChessGame.lastMove);
        ge.move(3,7,5,1, ChessGame.lastMove);
        assertTrue(ge.isCheckmate(ge.getBoard(), true));
    }

    @Test
    public void testStalemate(){
        // Testing before any changes
        assertFalse(ge.isStalemate(ge.getBoard(), true));

        // Removing all pieces except a white pawn and both kings
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(!((i == 4 && j == 0) || (i == 0 && j == 1) || (i == 4 && j == 7))){
                    ge.removePiece(i, j);
                }
            }
        }

        // Making stalemate with black to move
        ge.move(0,1,4,6, ChessGame.lastMove);
        ge.move(4,0,4,5, ChessGame.lastMove);
        System.out.println(ge.getBoard().toString());
        assertTrue(ge.isStalemate(ge.getBoard(), false));

        // Testing with king vs. king should not give a stalemate
        ge.removePiece(4,6);
        System.out.println(ge.getBoard().toString());
        assertFalse(ge.isStalemate(ge.getBoard(),true));
    }

    @Test
    public void testNotenoughPieces(){
        // removing pieces
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(!((i == 4 && j == 0) || (i == 0 && j == 1) || (i == 4 && j == 7))){
                    ge.removePiece(i, j);
                }
            }
        }
        System.out.println(ge.getBoard().toString());
        assertFalse(ge.notEnoughPieces(ge.getBoard()));

        // Testing with king vs. king should return true
        ge.removePiece(0,1);
        System.out.println(ge.getBoard().toString());
        assertTrue(ge.notEnoughPieces(ge.getBoard()));

        // Same for king and bishop/knight vs. king
        ge.setPiece(new Bishop(true,1, 1),1,1);
        assertTrue(ge.notEnoughPieces(ge.getBoard()));
        ge.removePiece(1,1);
        ge.setPiece(new Knight(true,1,1),1,1);
        assertTrue(ge.notEnoughPieces(ge.getBoard()));
    }

    @Test
    public void testValidMoves(){
        int[] test = null;
        ArrayList<Integer> validMoves = ge.validMoves(0,0);
        assertTrue(validMoves.size() == 0);

        // Testing for pawn
        validMoves = ge.validMoves(0,1);
        test = new int[] {0,2,0,3};
        for(int i = 0; i < test.length; i++){
            assertTrue(validMoves.get(i) == test[i]);
        }

        //Knight
        validMoves = ge.validMoves(1,0);
        test = new int[] {0,2,2,2};
        assertTrue(isItTheSameThingQuestionMark(test, validMoves));

        // Bishop
        validMoves = ge.validMoves(2,0);
        assertTrue(validMoves.size() == 0);

        // Queen
        validMoves = ge.validMoves(3,0);
        assertTrue(validMoves.size() == 0);

        // King
        validMoves = ge.validMoves(4,0);
        assertTrue(validMoves.size() == 0);

        // Moving pieces to create more opportunities...
        ge.move(2,1,2,4, ChessGame.lastMove);
        ge.move(3,1,3,2, ChessGame.lastMove);
        ge.move(4,1,4,3, ChessGame.lastMove);
        ge.move(6,0,5,2, ChessGame.lastMove);
        ge.move(7,1,7,3, ChessGame.lastMove);
        //ge.removePiece(5,2);
        ge.move(3,7,5,2, ChessGame.lastMove);
        ge.move(3,6,3,4, ChessGame.lastMove);

        //Setting en passant
        Pawn pawn = (Pawn)ge.getBoard().getBoardState()[3][4];
        pawn.setEnPassant(true);

        // Checking if valid moves are correctly set:
        // Pawn with en passant opportunity
        validMoves = ge.validMoves(2,4);
        test = new int[] {2,5,3,5};
        assertTrue(isItTheSameThingQuestionMark(test, validMoves));

        // Bishop at c1
        validMoves = ge.validMoves(2,0);
        test = new int[] {3,1,4,2,5,3,6,4,7,5};
        assertTrue(isItTheSameThingQuestionMark(test, validMoves));

        // White queen at d1
        validMoves = ge.validMoves(3,0);
        test = new int[] {0,3,1,2,2,1,3,1,4,1,5,2};
        assertTrue(isItTheSameThingQuestionMark(test, validMoves));

        // White king at e1
        validMoves = ge.validMoves(4,0);
        test = new int[] {3,1};
        assertTrue(isItTheSameThingQuestionMark(test, validMoves));

        // White pawn at e4
        validMoves = ge.validMoves(4,3);
        test = new int[] {3,4,4,4};
        assertTrue(isItTheSameThingQuestionMark(test, validMoves));

        // White pawn at g2
        //System.out.println(ge.getBoard().toString());
        validMoves = ge.validMoves(6,1);
        //System.out.println(validMoves.size());
        test = new int[] {5,2,6,2,6,3};
        for(int i = 0; i < validMoves.size(); ++i){
            System.out.println(validMoves.get(i));
        }
        assertTrue(isItTheSameThingQuestionMark(test, validMoves));

        // White rook at h1
        validMoves = ge.validMoves(7,0);
        test = new int[] {6,0,7,1,7,2};
        assertTrue(isItTheSameThingQuestionMark(test, validMoves));

        // Testing discovered check:
        // Moving black rook in behind white pawn at e4 should prevent white pawn from taking black pawn to the left at d5
        ge.move(0,7,4,4, ChessGame.lastMove);
        validMoves = ge.validMoves(4,3);
        assertTrue(validMoves.size() == 0);
    }

    @Test
    public void testCastling(){
        int[] test = null;

        //Removing all pieces other than king and rooks
        ge.removePiece(1,0);
        ge.removePiece(2,0);
        ge.removePiece(3,0);
        ge.removePiece(5,0);
        ge.removePiece(6,0);

        //Testing for king
        test = new int[] {2,0,3,0,5,0,6,0};
        ArrayList<Integer> validMoves = ge.validMoves(4,0);
        assertTrue(isItTheSameThingQuestionMark(test,validMoves));
    }

    private boolean isItTheSameThingQuestionMark(int[] test, ArrayList<Integer> validMoves){
        int[] liste = test;
        ArrayList<Integer> ny_liste = validMoves;
        int counter = 0;
        for (int i = 0; i < liste.length - 1; i +=2) {
            int x = liste[i];
            int y = liste[i + 1];
            for (int j = 0; j < ny_liste.size() - 1; j += 2) {
                if (x == ny_liste.get(j) && y == ny_liste.get(j+1)) {
                    ++counter;
                    break;
                }
            }
        }

        if (counter == liste.length/2) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args){
        org.junit.runner.JUnitCore.main(JUnitGameEngine.class.getName());
    }
}