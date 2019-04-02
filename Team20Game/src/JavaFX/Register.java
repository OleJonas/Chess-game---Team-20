package JavaFX;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.sql.SQLException;
import static javafx.geometry.Pos.CENTER;

@SuppressWarnings("Duplicates")
class Register {
    static Scene signUpScene;
    static Button signUpalreadyAccountButton, signUpRegisterButton;
    static TextField registerUsernameField;
    static PasswordField registerPasswordField, registerPasswordAgainField;
    static Label registerComment;

    static void runRegistration(){
        //signUpScene
        //Textfields
        registerUsernameField = new TextField();
        registerPasswordField = new PasswordField();
        registerPasswordAgainField = new PasswordField();
        //Label
        Label signupLabel = new Label("Register");
        signupLabel.setFont(Font.font("Georgia", 36));
        signupLabel.setTextFill(Color.WHITE);
        registerComment = new Label();
        registerComment.setTextFill(Color.RED);
        //signUpRegisterButton
        signUpRegisterButton = new Button("Sign up");
        signUpRegisterButton.setOnAction(e -> tryRegister());

        //signUpAlreadyAccountButton
        signUpalreadyAccountButton = new Button("Already registered?");
        signUpalreadyAccountButton.setOnAction(e -> {
            Main.window.setScene(Login.startScene);
            registerUsernameField.clear();
            registerPasswordField.clear();
            registerPasswordAgainField.clear();
            registerComment.setText("");
        });

        //signupLayout
        GridPane usernamePane = new GridPane();
        usernamePane.setHgap(15);
        Label usernameLabel = new Label("Username:             ");
        usernameLabel.setFont(Font.font("Georgia", 18));
        usernameLabel.setTextFill(Color.WHITE);
        usernamePane.add(usernameLabel, 0, 0);
        usernamePane.add(registerUsernameField, 1, 0);

        GridPane passwordPane = new GridPane();
        passwordPane.setHgap(15);
        Label passwordLabel = new Label("Password:             ");
        passwordLabel.setFont(Font.font("Georgia", 18));
        passwordLabel.setTextFill(Color.WHITE);
        passwordPane.add(passwordLabel, 0, 0);
        passwordPane.add(registerPasswordField, 1, 0);

        GridPane repeatPasswordPane = new GridPane();
        repeatPasswordPane.setHgap(15);
        Label repeatPasswordLabel = new Label("Repeat password: ");
        repeatPasswordLabel.setFont(Font.font("Georgia", 18));
        repeatPasswordLabel.setTextFill(Color.WHITE);
        repeatPasswordPane.add(repeatPasswordLabel, 0, 0);
        repeatPasswordPane.add(registerPasswordAgainField, 1, 0);

        HBox buttons = new HBox();
        buttons.getChildren().addAll(signUpRegisterButton, signUpalreadyAccountButton);
        buttons.setAlignment(CENTER);
        buttons.setSpacing(20);

        VBox signupLayout = new VBox(); //Creates grid, thinking 3 in width, 100|200|100
        signupLayout.setPadding(new Insets(20, 50, 20, 50));
        signupLayout.setSpacing(20);
        signupLayout.getChildren().addAll(signupLabel, usernamePane, passwordPane, repeatPasswordPane, buttons, registerComment);
        signupLayout.setAlignment(CENTER);
        signupLayout.setStyle("-fx-background-color: #404144;");

        signUpScene = new Scene(signupLayout, 450, 280);
        signUpScene.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER)){
                tryRegister();
            }
        });
        Main.window.setScene(signUpScene);
    }

    static void tryRegister(){
        String registerUsernameInput = registerUsernameField.getText();
        String registerPasswordInput = registerPasswordField.getText();
        String registerPasswordAgainInput = registerPasswordAgainField.getText();

        if (registerUsernameInput.equals("") || registerUsernameInput.equals(" ")) {
            registerComment.setText("Username cannot be blank");
        } else if (registerPasswordInput.equals("") || registerPasswordInput.equals(" ")) {
            registerComment.setText("Password cannot be blank");
        } else if (registerPasswordAgainInput.equals("") || registerPasswordAgainInput.equals(" ")) {
            registerComment.setText("Please repeat password");
        } else if (!registerPasswordAgainInput.equals(registerPasswordInput)) {
            registerComment.setText("Passwords not matching");
        }else {
            boolean registrationOK = false;
            try {
                registrationOK = Login.register(registerUsernameInput, registerPasswordInput);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            if (registrationOK) {
                registerUsernameField.clear();
                registerPasswordField.clear();
                registerPasswordAgainField.clear();
                registerComment.setText(""); //CLearing the label for next time
                Login.loginComment.setText("");
                Login.runLogin();
            } else {
                System.out.println("User already exist!");
                registerUsernameField.clear();
                registerPasswordField.clear();
                registerPasswordAgainField.clear();
                registerComment.setText("User already exist");
            }
        }
    }
}
