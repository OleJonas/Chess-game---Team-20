package JavaFX;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;
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

import static JavaFX.Register.runRegistration;


class Login{
    static Scene startScene;
    static Button loginButton, signUpButton;
    static TextField loginUsernameField;
    static PasswordField loginPasswordField;
    static Label loginComment;


    static void runLogin() {
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
            boolean PasswordOK = false;
            try {
                PasswordOK = checkPassword(loginPasswordInput, loginUsernameInput);
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            }
            if (UsernameOK && PasswordOK) {
                loginUsernameField.clear();
                loginPasswordField.clear();
                loginComment.setText(""); //CLearing the label for next time
                MainScene.showMainScene();
            } else {
                loginUsernameField.clear();
                loginPasswordField.clear();
                loginComment.setText("Wrong Username/Password");
            }
        });
        //signupButton
        signUpButton = new Button("Not registered?");
        signUpButton.setOnAction(e -> {
            loginComment.setText("");
            runRegistration();
        });
        //loginLayout
        GridPane loginLayout = new GridPane(); //Creates grid, thinking 3 in width, 100|200|100
        loginLayout.getColumnConstraints().add(new ColumnConstraints(80)); //Setting columnconstraint for left column
        loginLayout.getColumnConstraints().add(new ColumnConstraints(200)); //Setting columnconstraint for second column
        loginLayout.getColumnConstraints().add(new ColumnConstraints(100)); //Setting columnconstraint for left column
        loginLayout.setVgap(8);
        loginLayout.setHgap(15);
        loginLayout.setPadding(new Insets(10, 5, 10, 10));
        loginLayout.add(loginLabel, 1, 0, 4, 1);
        //loginLayout.setHalignment(loginLabel, HPos.CENTER);
        loginLayout.add(new Label("Username:"), 0, 1);
        loginLayout.add(loginUsernameField, 1, 1);
        loginLayout.add(new Label("Password:"), 0, 2);
        loginLayout.add(loginPasswordField, 1, 2);
        loginLayout.add(loginButton, 0, 3);
        loginLayout.add(signUpButton, 1, 3);
        loginLayout.add(loginComment, 1, 4);
        startScene = new Scene(loginLayout, 400, 190);
        Main.window.setScene(startScene);
    }

    static boolean checkUsername(String username){
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

    static boolean checkPassword(String password, String username) throws NoSuchAlgorithmException{
        String matchingPassword = "";
        String url = "jdbc:mysql://mysql.stud.idi.ntnu.no:3306/martijni?user=martijni&password=wrq71s2w";
        byte[] saltByte = new byte[20];
        try(Connection con = DriverManager.getConnection(url)) {
            Statement stmt = con.createStatement();
            String sqlQuery = "SELECT password, SALT FROM ProsjektDatabase WHERE username=\"" + username + "\"";
            ResultSet res = stmt.executeQuery(sqlQuery);
            while (res.next()) {
                matchingPassword = res.getString("password");
                String saltString = res.getString("SALT");
                //System.out.println(matchingPassword);
                //System.out.println(saltString);
                saltByte = hexStringToByteArray(saltString);
                //System.out.println(bytesToStringHex(saltByte));
            }
        }catch (Exception sq) {
            System.out.println("SQL-Feil: " + sq);
        }
        if(matchingPassword.equals("")) {
            System.out.println("This username does not exist");
            return false;
        }
        if(matchingPassword.equals(generateHash(password, saltByte))){
            return true;
        }

        System.out.println("Wrong password!");
        return false;
    }

    static boolean register(String username, String password, String email) throws SQLException {
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

    //Arpit Shah on Youtube, SALT Hashing
    static String generateHash(String data, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(salt);
        byte[] hash = digest.digest(data.getBytes());
        return bytesToStringHex(hash);
    }

    final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    static String bytesToStringHex(byte[] bytes){
        char[] hexChars = new char[bytes.length * 2];
        for(int i = 0; i < bytes.length; i++){
            int j = bytes[i] & 0xFF;
            hexChars[i * 2] = hexArray[j >>> 4];
            hexChars[i * 2 + 1] = hexArray[j & 0x0F];
        }
        return new String(hexChars);
    }

    //From Stackoverflow, Dave L.
    static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    static byte[] createSalt() {
        byte[] bytes = new byte[20];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
        return bytes;
    }
}
