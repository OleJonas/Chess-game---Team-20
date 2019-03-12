package JavaFX;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.sql.SQLException;

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
        registerComment = new Label();
        Label signupLabel = new Label("Register");
        signupLabel.setFont(Font.font("Calibri", 32));
        registerComment.setTextFill(Color.RED);
        //signUpRegisterButton
        signUpRegisterButton = new Button("Sign up");
        signUpRegisterButton.setOnAction(e -> {
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
        });

        //signUpAlreadyAccountButton
        signUpalreadyAccountButton = new Button("Already registered?");
        signUpalreadyAccountButton.setOnAction(e -> {
            Main.window.setScene(Login.startScene);
            registerUsernameField.clear();
            registerPasswordField.clear();
            registerPasswordAgainField.clear();
            registerComment.setText("");
        });
        //signUpLayout
        GridPane signUpLayout = new GridPane();
        signUpLayout.getColumnConstraints().add(new ColumnConstraints(120));
        signUpLayout.getColumnConstraints().add(new ColumnConstraints(200));
        signUpLayout.setVgap(8);
        signUpLayout.setHgap(15);
        signUpLayout.setPadding(new Insets(10, 5, 10, 10));
        signUpLayout.add(signupLabel, 1, 0);
        signUpLayout.add(new Label("Username:"), 0, 1);
        signUpLayout.add(registerUsernameField, 1, 1);
        signUpLayout.add(new Label("Password:"), 0, 2);
        signUpLayout.add(registerPasswordField, 1, 2);
        signUpLayout.add(new Label("Repeat password:"), 0, 3);
        signUpLayout.add(registerPasswordAgainField, 1, 3);
        signUpLayout.add(signUpRegisterButton, 0, 4);
        signUpLayout.add(signUpalreadyAccountButton, 1, 4);
        signUpLayout.add(registerComment, 1, 5);
        signUpScene = new Scene(signUpLayout, 370, 200);
        Main.window.setScene(signUpScene);

    }
}
