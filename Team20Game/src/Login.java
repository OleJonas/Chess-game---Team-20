import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import javafx.geometry.Pos;


public class Login extends Application{
    Stage window;
    Button loginButton, signUpButton, signUpalreadyAccountButton, signUpRegisterButton;
    Scene startScene, signUpScene, loggedInScene;
    TextField loginUsernameField, registerUsernameField, registerEmailField;
    PasswordField loginPasswordField, registerPasswordField;
    Label loginComment, registerComment;


    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        window = primaryStage;

        //startScene
        //Textfields
        loginUsernameField = new TextField();
        loginPasswordField = new PasswordField();
        //Label
        loginComment = new Label();
        Label loginLabel = new Label("Login");
        loginLabel.setFont(Font.font("Calibri", 32));
        //loginComment.setTextFill(Color.web("#0076a3")); //For Hexvalues
        loginComment.setTextFill(Color.RED);
        //loginLabel.setAlignment(Pos.CENTER);
        //loginButton
        loginButton = new Button("Login");
        loginButton.setOnAction(e -> { //Actions of clicking the loginbutton
            String loginUsernameInput = loginUsernameField.getText();
            String loginPasswordInput = loginPasswordField.getText();
            boolean UsernameOK = checkUsername(loginUsernameInput);
            boolean PasswordOK = checkPassword(loginPasswordInput, loginUsernameInput);
            if(UsernameOK && PasswordOK) {
                window.setScene(loggedInScene);
                loginUsernameField.clear();
                loginPasswordField.clear();
                loginComment.setText(""); //CLearing the label for next time
            } else {
                loginUsernameField.clear();
                loginPasswordField.clear();
                loginComment.setText("Wrong Username/Password");
            }
        });
        //signupButton
        signUpButton = new Button("Not registered?");
        signUpButton.setOnAction(e -> {
            window.setScene(signUpScene);
            loginComment.setText("");
        });
        //loginLayout
        GridPane loginLayout = new GridPane(); //Creates grid, thinking 3 in width, 100|200|100
        loginLayout.getColumnConstraints().add(new ColumnConstraints(80)); //Setting columnconstraint for left column
        loginLayout.getColumnConstraints().add(new ColumnConstraints(200)); //Setting columnconstraint for second column
        loginLayout.getColumnConstraints().add(new ColumnConstraints(100)); //Setting columnconstraint for left column
        loginLayout.setVgap(8);
        loginLayout.setHgap(15);
        loginLayout.setPadding(new Insets(10,5,10,10));
        loginLayout.add(loginLabel, 1, 0, 4, 1);
        loginLayout.add(new Label("Username:"), 0, 1);
        loginLayout.add(loginUsernameField, 1, 1);
        loginLayout.add(new Label("Password:"), 0, 2);
        loginLayout.add(loginPasswordField, 1, 2);
        loginLayout.add(loginButton, 0, 3);
        loginLayout.add(signUpButton, 1, 3);
        loginLayout.add(loginComment, 1, 4);
        startScene = new Scene(loginLayout, 400,190);

        //signUpScene
        //Textfields
        registerUsernameField = new TextField();
        registerPasswordField = new PasswordField();
        registerEmailField = new TextField();
        //Label
        registerComment = new Label();
        Label signupLabel = new Label("Register");
        signupLabel.setFont(Font.font("Calibri",32));
        registerComment.setTextFill(Color.RED);
        //signUpRegisterButton
        signUpRegisterButton = new Button("Sign up");
        signUpRegisterButton.setOnAction(e -> {
            String registerUsernameInput = registerUsernameField.getText();
            String registerPasswordInput = registerPasswordField.getText();
            String registerEmailInput = registerEmailField.getText();

            if(registerUsernameInput.equals("") || registerUsernameInput.equals(" ")){
                registerComment.setText("Username cannot be blank");
            }else if(registerPasswordInput.equals("") || registerPasswordInput.equals(" ")){
                registerComment.setText("Password cannot be blank");
            } else if(registerEmailInput.equals("") || registerEmailInput.equals(" ")){
                registerComment.setText("Email cannot be blank");
            } else {
                boolean registrationOK = false;
                try {
                    registrationOK = register(registerUsernameInput, registerPasswordInput, registerEmailInput);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                if (registrationOK) {
                    window.setScene(startScene);
                    registerUsernameField.clear();
                    registerPasswordField.clear();
                    registerEmailField.clear();
                    registerComment.setText(""); //CLearing the label for next time
                    loginComment.setText("");
                } else {
                    System.out.println("User already exist!");
                    registerUsernameField.clear();
                    registerPasswordField.clear();
                    registerEmailField.clear();
                    registerComment.setText("User already exist");
                }
            }
        });

        //signUpAlreadyAccountButton
        signUpalreadyAccountButton = new Button("Already registered?");
        signUpalreadyAccountButton.setOnAction(e -> {
            window.setScene(startScene);
            registerUsernameField.clear();
            registerPasswordField.clear();
            registerEmailField.clear();
            registerComment.setText("");
        });
        //signUpLayout
        GridPane signUpLayout = new GridPane();
        signUpLayout.getColumnConstraints().add(new ColumnConstraints(80));
        signUpLayout.getColumnConstraints().add(new ColumnConstraints(200));
        signUpLayout.setPadding(new Insets(10,5,10,10));
        signUpLayout.add(signupLabel, 1, 0);
        signUpLayout.add(new Label("Username:"), 0, 1);
        signUpLayout.add(registerUsernameField, 1, 1);
        signUpLayout.add(new Label("Password:"), 0, 2);
        signUpLayout.add(registerPasswordField, 1, 2);
        signUpLayout.add(new Label("Email:"), 0, 3);
        signUpLayout.add(registerEmailField, 1, 3);
        signUpLayout.add(signUpRegisterButton, 0, 4);
        signUpLayout.add(signUpalreadyAccountButton, 1, 4);
        signUpLayout.add(registerComment, 1, 5);
        signUpScene = new Scene(signUpLayout, 400,190);


        //loggedInScene
        Image CarreyGif = new Image("/Images/CarreyGif.gif");
        Label loggedin = new Label("You're logged in");
        Button backToStart = new Button("Back to start");
        backToStart.setOnAction(e -> window.setScene(startScene));
        //layout
        GridPane loggedInLayout = new GridPane();
        loginLayout.getColumnConstraints().add(new ColumnConstraints(150)); //Setting columnconstraint for left column
        loginLayout.getColumnConstraints().add(new ColumnConstraints(400)); //Setting columnconstraint for second column
        loggedInLayout.add(loggedin, 0, 0);
        loggedInLayout.add(backToStart, 0, 1);
        loggedInLayout.add(new ImageView(CarreyGif), 1, 2);
        loggedInScene = new Scene(loggedInLayout, 500, 400);

        //Display scene 1 at first
        window.setScene(startScene);
        window.setTitle("Login");
        window.show();
    }

    private boolean checkUsername(String username){
        String matchingUsername = "";
        String url = "jdbc:mysql://mysql.stud.idi.ntnu.no:3306/martijni?user=martijni&password=wrq71s2w";
        try(Connection con = DriverManager.getConnection(url)) {
            Statement stmt = con.createStatement();
            String sqlQuery = "SELECT username FROM ProsjektDatabase WHERE username=\"" + username + "\"";
            ResultSet res = stmt.executeQuery(sqlQuery);

            while (res.next()) {
                matchingUsername = res.getString("username");
            }
        }catch (Exception sq) {
            System.out.println("SQL-Feil: " + sq);
        }
        if(matchingUsername.equals("")) {
            //System.out.println("This username does not exist");
            return false;
        }
        if(matchingUsername.equals(username)){
            return true;
        }
        return false;
    }

    private boolean checkPassword(String password, String username){
        String matchingPassword = "";
        String url = "jdbc:mysql://mysql.stud.idi.ntnu.no:3306/martijni?user=martijni&password=wrq71s2w";
        try(Connection con = DriverManager.getConnection(url)) {
            Statement stmt = con.createStatement();
            String sqlQuery = "SELECT password, SALT FROM ProsjektDatabase WHERE username=\"" + username + "\"";
            ResultSet res = stmt.executeQuery(sqlQuery);

            while (res.next()) {
                matchingPassword = res.getString("password");
                String saltString = res.getString("SALT");
                System.out.println(matchingPassword);
                System.out.println(saltString.trim());
            }
        }catch (Exception sq) {
            System.out.println("SQL-Feil: " + sq);
        }
        if(matchingPassword.equals("")) {
            System.out.println("This username does not exist");
            return false;
        }
        if(matchingPassword.equals(password)){
            return true;
        }

        System.out.println("Wrong password!");
        return false;
    }

    private boolean register(String username, String password, String email) throws SQLException {
        String url = "jdbc:mysql://mysql.stud.idi.ntnu.no:3306/martijni?user=martijni&password=wrq71s2w";
        try(Connection con = DriverManager.getConnection(url)) {
            Statement stmt = con.createStatement();
            if(checkUsername(username)) return false;
            //Create salt hash password
            byte[] salt = createSalt();
            String passwordHash = generateHash(password, salt);
            String saltString = bytesToStringHex(salt);
            //Insert into database
            String sqlQuery = "INSERT INTO ProsjektDatabase(username, password, email, SALT) values('" + username + "','" + passwordHash + "','" + email + "','" + saltString + "');";
            int rowsAffected = stmt.executeUpdate(sqlQuery);
            if(rowsAffected==1) return true;
        }catch (Exception sq) {
            System.out.println("SQL-Feil: " + sq);
        }
        return false;
    }

    private static String generateHash(String data, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(salt);
        byte[] hash = digest.digest(data.getBytes());
        return bytesToStringHex(hash);
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToStringHex(byte[] bytes){
        char[] hexChars = new char[bytes.length * 2];
        for(int i = 0; i < bytes.length; i++){
            int j = bytes[i] & 0xFF;
            hexChars[i * 2] = hexArray[j >>> 4];
            hexChars[i * 2 + 1] = hexArray[j & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] createSalt() {
        byte[] bytes = new byte[20];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
        return bytes;
    }








}



