package JavaFX.GameScene;


import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

@SuppressWarnings("Duplicates")
class FinishedGameResetAlert{

    public static void Display(){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("");

        Label label = new Label("Checkmate!");
        label.setFont(Font.font("Copperplate", 24));
        label.setStyle("-fx-font-weight: bold");
        label.setTextFill(Color.WHITE);

        Button startOverButton = new Button("Start over");
        startOverButton.setOnAction(e -> {
            window.close();
        });

        GridPane mainLayout = new GridPane();
        mainLayout.setHgap(10);
        mainLayout.setVgap(20);
        mainLayout.setPadding(new Insets(30, 60, 30, 60));
        mainLayout.add(label, 0, 0);
        mainLayout.setHalignment(label, HPos.CENTER);
        mainLayout.add(startOverButton, 0, 1);
        mainLayout.setHalignment(startOverButton, HPos.CENTER);
        mainLayout.setStyle("-fx-background-color: #404144;");

        Scene scene = new Scene(mainLayout, 260, 150);
        window.setScene(scene);
        window.showAndWait();
    }
}
