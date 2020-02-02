package GUI.GameScene;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * <h1>PawnChangeChoiceBox</h1>
 * This class is used when a pawn wants to promote to another piece
 * @since 08.04.2019
 * @author Team 20
 */

@SuppressWarnings("Duplicates")
public class PawnChangeChoiceBox{
    public static String choice;

    /**
     * This methods displays the pop-up window when the player wants to promote a pawn.
     * @param color = color of your pieces.
     */
    public static void Display(boolean color){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Promotion");

        Label label = new Label("Choose your new \n           piece:");
        label.setFont(Font.font("Copperplate", 22));
        label.setStyle("-fx-font-weight: bold");
        label.setTextFill(Color.WHITE);

        Button pickBishopButton = new Button();
        Button pickKnightButton = new Button();
        Button pickQueenButton = new Button();
        Button pickRookButton = new Button();

        if(color){
            Image pickBishopImage = new Image("Images/chessPieces/Standard/w_bishop_1x_ns.png",60, 60, true, true);
            Image pickKnightImage = new Image("Images/chessPieces/Standard/w_knight_1x_ns.png", 60, 60, true, true);
            Image pickQueenImage = new Image("Images/chessPieces/Standard/w_queen_1x_ns.png", 60, 60, true, true);
            Image pickRookImage = new Image("Images/chessPieces/Standard/w_rook_1x_ns.png", 60, 60, true, true);

            ImageView bishopImageView = new ImageView(pickBishopImage);
            ImageView knightImageView = new ImageView(pickKnightImage);
            ImageView queenImageView = new ImageView(pickQueenImage);
            ImageView rookImageView = new ImageView(pickRookImage);

            pickBishopButton.setGraphic(bishopImageView);
            pickKnightButton.setGraphic(knightImageView);
            pickQueenButton.setGraphic(queenImageView);
            pickRookButton.setGraphic(rookImageView);
        } else if (!color) {
            Image pickBishopImage = new Image("Images/chessPieces/Standard/b_bishop_1x_ns.png",60, 60, true, true);
            Image pickKnightImage = new Image("Images/chessPieces/Standard/b_knight_1x_ns.png", 60, 60, true, true);
            Image pickQueenImage = new Image("Images/chessPieces/Standard/b_queen_1x_ns.png", 60, 60, true, true);
            Image pickRookImage = new Image("Images/chessPieces/Standard/b_rook_1x_ns.png", 60, 60, true, true);

            ImageView bishopImageView = new ImageView(pickBishopImage);
            ImageView knightImageView = new ImageView(pickKnightImage);
            ImageView queenImageView = new ImageView(pickQueenImage);
            ImageView rookImageView = new ImageView(pickRookImage);

            pickBishopButton.setGraphic(bishopImageView);
            pickKnightButton.setGraphic(knightImageView);
            pickQueenButton.setGraphic(queenImageView);
            pickRookButton.setGraphic(rookImageView);
        }

        pickBishopButton.setPrefSize(80, 80);
        pickKnightButton.setPrefSize(80, 80);
        pickQueenButton.setPrefSize(80, 80);
        pickRookButton.setPrefSize(80, 80);

        pickBishopButton.setOnAction(e -> {choice = "Bishop"; window.close();});
        pickKnightButton.setOnAction(e -> {choice = "Knight"; window.close();});
        pickQueenButton.setOnAction(e -> {choice = "Queen"; window.close();});
        pickRookButton.setOnAction(e -> {choice = "Rook"; window.close();});

        GridPane mainLayout = new GridPane();
        mainLayout.setHgap(10);
        mainLayout.setVgap(10);
        mainLayout.setPadding(new Insets(10, 20, 10, 10));
        mainLayout.getColumnConstraints().add(new ColumnConstraints(100));
        mainLayout.getColumnConstraints().add(new ColumnConstraints(100));
        mainLayout.add(label, 0, 0, 2, 1);
        mainLayout.setHalignment(label, HPos.CENTER);
        mainLayout.add(pickQueenButton, 0,1);
        mainLayout.setHalignment(pickQueenButton, HPos.CENTER);
        mainLayout.add(pickKnightButton, 1, 1);
        mainLayout.setHalignment(pickKnightButton, HPos.CENTER);
        mainLayout.add(pickBishopButton, 0,2);
        mainLayout.setHalignment(pickBishopButton, HPos.CENTER);
        mainLayout.add(pickRookButton, 1, 2);
        mainLayout.setHalignment(pickRookButton, HPos.CENTER);
        mainLayout.setStyle("-fx-background-color: #404144;");

        Scene scene = new Scene(mainLayout, 230, 260);
        window.setScene(scene);
        window.showAndWait();
    }
}