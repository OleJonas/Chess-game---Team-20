
package JavaFX;

import Database.DBOps;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.Set;

import static JavaFX.Register.runRegistration;
import static javafx.geometry.Pos.CENTER;


@SuppressWarnings("Duplicates")
class Login{
    static String USERNAME;
    static String AVATAR;
    static int gamesPlayed;
    static int gamesWon;
    static int gamesLost;
    static int gamesRemis;
    static int ELOrating;

    static Scene startScene;
    static Button loginButton, signUpButton;
    static TextField loginUsernameField;
    static PasswordField loginPasswordField;
    static Label loginComment;


    static void runLogin() {
        //Textfields
        loginUsernameField = new TextField();
        loginPasswordField = new PasswordField();

        //DENNE SETTER BARE INN EN TESTBRUKER FOR Å SLIPPE Å SKRIVE INN BRUKERDETAILS, SLETT FØR PUBLISERING AV APPLIKASJON
        loginUsernameField.setText("Test");
        loginPasswordField.setText("12345");

        //Label
        Label loginLabel = new Label("Login");
        loginLabel.setFont(Font.font("Copperplate", 36));
        loginLabel.setTextFill(Color.WHITE);

        loginComment = new Label();
        loginComment.setTextFill(Color.RED);

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
                USERNAME = loginUsernameInput;
                AVATAR = getAvatar(USERNAME);
                getSettings();
                getGameInfo();
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
        GridPane usernamePane = new GridPane();
        usernamePane.setHgap(15);
        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(Font.font("Copperplate", 18));
        usernameLabel.setTextFill(Color.WHITE);
        usernamePane.add(usernameLabel, 0, 0);
        usernamePane.add(loginUsernameField, 1, 0);

        GridPane passwordPane = new GridPane();
        passwordPane.setHgap(15);
        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Copperplate", 18));
        passwordLabel.setTextFill(Color.WHITE);
        passwordPane.add(passwordLabel, 0, 0);
        passwordPane.add(loginPasswordField, 1, 0);

        HBox buttons = new HBox();
        buttons.getChildren().addAll(loginButton, signUpButton);
        buttons.setAlignment(CENTER);
        buttons.setSpacing(20);

        VBox loginLayout = new VBox(); //Creates grid, thinking 3 in width, 100|200|100
        loginLayout.setPadding(new Insets(20, 50, 20, 50));
        loginLayout.setSpacing(20);
        loginLayout.getChildren().addAll(loginLabel, usernamePane, passwordPane, buttons, loginComment);
        loginLayout.setAlignment(CENTER);
        loginLayout.setStyle("-fx-background-color: #404144;");

        startScene = new Scene(loginLayout, 400, 250);
        Main.window.setScene(startScene);
    }

    static boolean checkUsername(String username){
        String matchingUsername = "";
        DBOps connection = new DBOps();
        try{
            ArrayList<String> result = connection.exQuery("SELECT username FROM User WHERE username=\"" + username + "\"",1);

            if(result.size() > 0){
                matchingUsername = result.get(0);
            }
        }catch (Exception sq) {
            System.out.println("SQL-Feil: " + sq);
        }
        if(matchingUsername.equals(username)){
            return true;
        }
        return false;
    }

    static boolean checkPassword(String password, String username) throws NoSuchAlgorithmException{
        String matchingPassword = "";
        DBOps connection = new DBOps();
        byte[] saltByte = new byte[20];
        try{
            ArrayList<String> result = connection.exQuery("SELECT password, SALT FROM User WHERE username=\"" + username + "\"",2);
            if(result.size() > 0){
                matchingPassword = result.get(0);
                String saltString = result.get(1);
                saltByte = hexStringToByteArray(saltString);
            } else {
                return false;
            }
        }catch (Exception sq) {
            System.out.println("SQL-Feil: " + sq);
        }
        if(matchingPassword.equals(generateHash(password, saltByte))){
            return true;
        }
        System.out.println("Wrong password!");
        return false;
    }

    //Denne metoden registrerer en bruker i User-tabellen med brukernavn, passord, SALT, en default avatar og en user_id (AUTO_INCREMENT)
    static boolean register(String username, String password) throws SQLException {
        DBOps connection = new DBOps();
        try{
            if(checkUsername(username)) return false;
            //Create salt hash password
            byte[] salt = createSalt();
            String passwordHash = generateHash(password, salt);
            String saltString = bytesToStringHex(salt);
            //Insert into User
            int rowsAffected = connection.exUpdate("INSERT INTO User(username, password, SALT, avatar, gamesPlayed, gamesWon, gamesLost, gamesRemis, ELOrating) values('" + username + "','" + passwordHash + "','" + saltString + "', 'avatar1.jpg', 0, 0, 0, 0, 1000);");
            if(rowsAffected==0) return false;
            //Insert into UserSettings
            rowsAffected = connection.exUpdate("INSERT INTO UserSettings(username, darkTileColor, lightTileColor, skinName) values('" + username + "','#8B4513','#FFEBCD', 'Standard');");
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

    static String getAvatar(String username) {
        String avatar;
        DBOps connection = new DBOps();
        ArrayList<String> result = connection.exQuery("SELECT avatar FROM User WHERE username=\"" + username + "\"",1);
        if(result.size() > 0){
            avatar = result.get(0);
        }else{
            avatar = "avatar1.jpg";
        }
        return avatar;
    }

    static void getGameInfo(){
        DBOps connection = new DBOps();
        ArrayList<String> result = connection.exQuery("SELECT gamesPlayed, gamesWon, gamesLost, gamesRemis, ELOrating FROM User WHERE username=\"" + USERNAME + "\"",5);
        if(result.size() > 0){
            gamesPlayed = Integer.parseInt(result.get(0));
            gamesWon = Integer.parseInt(result.get(1));
            gamesLost = Integer.parseInt(result.get(2));
            gamesRemis = Integer.parseInt(result.get(3));
            ELOrating = Integer.parseInt(result.get(4));
        }
    }

    static void getSettings(){
        DBOps connection = new DBOps();
        ArrayList<String> result = connection.exQuery("SELECT darkTileColor, lightTileColor, skinName FROM UserSettings WHERE username=\"" + USERNAME + "\"",3);
        if(result.size() > 0){
            Settings.darkTileColor = result.get(0);
            Settings.lightTileColor = result.get(1);
            Settings.skinName = result.get(2);
        }
    }

    static boolean storeSettings(){
        DBOps connection = new DBOps();
        int rowsAffected = connection.exUpdate("UPDATE UserSettings SET darkTileColor = '" + Settings.darkTileColor + "', lightTileColor = '" + Settings.lightTileColor + "', skinName = '" + Settings.skinName + "' WHERE username = '" + USERNAME + "';");
        if(rowsAffected==1) return true;
        return false;
    }


}
