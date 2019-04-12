package GUI.LoginScreen;

import Database.DBOps;
import GUI.GameScene.ChessGame;
import GUI.Main;
import GUI.MainScene.MainScene;
import GUI.MainScene.Settings;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
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
import java.util.ArrayList;

import static GUI.LoginScreen.Register.runRegistration;
import static GUI.MainScene.Sandbox.ChessSandbox.setUpSkin;
import static javafx.geometry.Pos.CENTER;

/**
 * <h1>Login</h1>
 * The login scene is the first window to face the user.
 * @author Team20
 * @since 08.04.2019
 */
@SuppressWarnings("Duplicates")
public class Login{
    public static String USERNAME;
    public static String AVATAR;
    public static int gamesPlayed;
    public static int gamesWon;
    public static int gamesLost;
    public static int gamesRemis;
    public static int ELOrating;
    public static ArrayList<Integer> ELOhistory = new ArrayList<Integer>();

    static Scene startScene;
    static Button loginButton, signUpButton;
    static TextField loginUsernameField = new TextField();
    static PasswordField loginPasswordField;
    static Label loginComment;
    public static int userID;

    /**
     * Run method for the JavaFX of the login screen.
     */
    public static void runLogin() {
        //Textfields
        loginPasswordField = new PasswordField();

        //Label
        Label loginLabel = new Label("Login");
        loginLabel.setFont(Font.font("Georgia", 36));
        loginLabel.setTextFill(Color.WHITE);

        loginComment = new Label();
        loginComment.setTextFill(Color.RED);

        //loginButton
        loginButton = new Button("Login");
        loginButton.setOnAction(e -> tryLogin());

        //signupButton
        signUpButton = new Button("Not registered?");
        signUpButton.setOnAction(e -> {
            loginUsernameField.setText("");
            loginComment.setText("");
            runRegistration();
        });
        //loginLayout
        GridPane usernamePane = new GridPane();
        usernamePane.setHgap(15);
        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(Font.font("Georgia", 18));
        usernameLabel.setTextFill(Color.WHITE);
        usernamePane.add(usernameLabel, 0, 0);
        usernamePane.add(loginUsernameField, 1, 0);

        GridPane passwordPane = new GridPane();
        passwordPane.setHgap(15);
        Label passwordLabel = new Label("Password: ");
        passwordLabel.setFont(Font.font("Georgia", 18));
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
        startScene.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER)){
                tryLogin();
            }
        });
        Main.window.setScene(startScene);
        Main.window.setX((MainScene.primaryScreenBounds.getWidth()-Main.window.getWidth())/2);
        Main.window.setY((MainScene.primaryScreenBounds.getHeight()-Main.window.getHeight())/4 +Main.window.getHeight()*0.01);
    }

    /**
     * This method is run the attempt to login when the user has filled in their login information
     */
    static void tryLogin(){
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
            userID = getUserID();
            getSettings();
            getGameInfo();
            setUpSkin(); //setup Skin for Sandbox Chessgame
            MainScene.showMainScene();
        } else {
            loginUsernameField.clear();
            loginPasswordField.clear();
            loginComment.setText("Wrong Username/Password");
        }
    }

    /**
     * Support method to check whether a username exists
     * @param username
     * @return A boolean describing whether it already exists.
     */
    static boolean checkUsername(String username){
        DBOps connection = new DBOps();
        String matchingUsername = connection.checkUsername(username);
        if(matchingUsername.toLowerCase().equals(username.toLowerCase())){
            return true;
        }
        return false;
    }

    /**
     * Support method to check if the password matches the username.
     * @param password The password the user has entered.
     * @param username The username the user has entered.
     * @return A boolean that is true if the login information is valid.
     * @throws NoSuchAlgorithmException
     */
    static boolean checkPassword(String password, String username) throws NoSuchAlgorithmException{
        DBOps connection = new DBOps();
        String matchingPassword = "";
        byte[] saltByte = new byte[20];
            ArrayList<String> result = connection.checkPW(username);
            if(result.size() > 0){
                matchingPassword = result.get(0);
                String saltString = result.get(1);
                saltByte = hexStringToByteArray(saltString);
            } else {
                return false;
            }
        if(matchingPassword.equals(generateHash(password, saltByte))){
            return true;
        }
        //System.out.println("Wrong password!");
        return false;
    }

    /**
     * This method creates a new user the the User table in the database. It will use the username and password
     * and then add SALT, default avatar, and a user_id(AUTO_INCREMENT).
     * @param username The username of the user.
     * @param password The password of the user.
     * @return A boolean that is true if the user was successfully created.
     */
    static boolean register(String username, String password){
        DBOps connection = new DBOps();
        if(checkUsername(username)) return false;

        try{
            //Create salt hash password
            byte[] salt = createSalt();
            String passwordHash = generateHash(password, salt);
            String saltString = bytesToStringHex(salt);
            //Insert into User
            if(!connection.register(username, passwordHash, saltString)) return false;
        }catch (Exception nosuchalgorithm) {
            nosuchalgorithm.printStackTrace();
        }
        return true;
    }

    /**
     * Method for SALT hashing using SHA-256
     * @param data The password of the user.
     * @param salt The SALT of that user.
     * @return The hash of that users password.
     * @throws NoSuchAlgorithmException
     */
    static String generateHash(String data, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(salt);
        byte[] hash = digest.digest(data.getBytes());
        return bytesToStringHex(hash);
    }

    final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * Help method for generateHash() method, converts an array of byte-values to an array of hex characters.
     * @see Login#generateHash(String data, byte[] salt)
     * @param bytes The array of byte values.
     * @return The string of hex characters.
     */
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

    /**
     * From Stackoverflow, Dave L., for converting a string of hex characters to a byte array.
     * @param s The string of hex characters.
     * @return The byte array.
     */
    static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * Create a random salt for the password hashing. This method is used in the
     * register(String username, String password) method.
     * @return SALT in the for of a byte array.
     * @see Login#register(String, String)
     */
    static byte[] createSalt() {
        byte[] bytes = new byte[20];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * fetch the avatar of the user, if no avatar is found, return the default one.
     * @param username The user wanting to fetch their avatar.
     * @return The file of their avatar.
     */
    static String getAvatar(String username) {
        DBOps connection = new DBOps();
        String avatar;
        //DBOps connection = new DBOps();
        ArrayList<String> result = connection.exQuery("SELECT avatar FROM User WHERE username=\"" + username + "\"",1);
        if(result.size() > 0){
            avatar = result.get(0);
        }else{
            avatar = "avatar1.jpg";
        }
        return avatar;
    }

    /**
     * Fetch the id (used in the database) of the user, given its username
     * @return The id of the user.
     */
    public static int getUserID(){
        DBOps connection = new DBOps();
        int out = Integer.parseInt(connection.exQuery("SELECT user_id FROM User WHERE username = '" + USERNAME + "';", 1).get(0));
        return out;
    }

    /**
     * Fetch game info from the database table given a username.
     */
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

        ArrayList<String> result2 = connection.exQuery("SELECT elo FROM userElo WHERE userID = \"" + userID + "\"", 1);
        for (int i = 0; i < result2.size(); i++){
            int a = Integer.parseInt(result2.get(i));
            ELOhistory.add(a);
        }
    }

    /**
     * Fetch the relevant settings from the database given the username.
     */
    static void getSettings(){
        DBOps connection = new DBOps();
        ArrayList<String> result = connection.exQuery("SELECT darkTileColor, lightTileColor, skinName FROM UserSettings WHERE username=\"" + USERNAME + "\"",3);
        if(result.size() > 0){
            Settings.darkTileColor = result.get(0);
            Settings.lightTileColor = result.get(1);
            ChessGame.skin = result.get(2);
        }
    }

    /**
     * method to update the database with the settings of a user.
     * @return A boolean value describing if the update was successful.
     */
    public static boolean storeSettings(){
        DBOps connection = new DBOps();
        int rowsAffected = connection.exUpdate("UPDATE UserSettings SET darkTileColor = '" + Settings.darkTileColor + "', lightTileColor = '" + Settings.lightTileColor + "', skinName = '" + ChessGame.skin + "' WHERE username = '" + USERNAME + "';");
        if(rowsAffected==1) return true;
        return false;
    }
}
