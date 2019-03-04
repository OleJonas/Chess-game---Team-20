import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.*;

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
            } else {
                loginUsernameField.clear();
                loginPasswordField.clear();
                loginComment.setText("Feil Brukernavn/Passord");
            }
        });
        //signupButton
        signUpButton = new Button("Signup");
        signUpButton.setOnAction(e -> window.setScene(signUpScene));
        //loginLayout
        GridPane loginLayout = new GridPane(); //Creates grid
        loginLayout.getColumnConstraints().add(new ColumnConstraints(80)); //Setting columnconstraint for left column
        loginLayout.getColumnConstraints().add(new ColumnConstraints(200)); //Setting columnconstraint for second column
        loginLayout.setPadding(new Insets(10,5,10,10));
        loginLayout.add(new Label("Login"), 0, 0);
        loginLayout.add(new Label("Username:"), 0, 1);
        loginLayout.add(loginUsernameField, 1, 1);
        loginLayout.add(new Label("Password:"), 0, 2);
        loginLayout.add(loginPasswordField, 1, 2);
        loginLayout.add(loginButton, 0, 3);
        loginLayout.add(signUpButton, 1, 3);
        loginLayout.add(loginComment, 1, 4);
        startScene = new Scene(loginLayout, 400,140);


        //signUpScene
        //Textfields
        registerUsernameField = new TextField();
        registerPasswordField = new PasswordField();
        registerEmailField = new TextField();
        //Label
        registerComment = new Label();
        //signUpRegisterButton
        signUpRegisterButton = new Button("Sign up");
        signUpRegisterButton.setOnAction(e -> {
            String registerUsernameInput = registerUsernameField.getText();
            String registerPasswordInput = registerPasswordField.getText();
            String registerEmailInput = registerEmailField.getText();

            boolean registrationOK = false;
            try {
                registrationOK = register(registerUsernameInput, registerPasswordInput, registerEmailInput);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            if (registrationOK) {
                //window.setScene(startScene);
                registerUsernameField.clear();
                registerPasswordField.clear();
                registerEmailField.clear();
                registerComment.setText("Registered!");
            } else {
                System.out.println("User already exist!");
                registerUsernameField.clear();
                registerPasswordField.clear();
                registerEmailField.clear();
                registerComment.setText("User already exist");
            }
        });
        //signUpAlreadyAccountButton
        signUpalreadyAccountButton = new Button("Already registered?");
        signUpalreadyAccountButton.setOnAction(e -> window.setScene(startScene));
        //signUpLayout
        GridPane signUpLayout = new GridPane();
        signUpLayout.getColumnConstraints().add(new ColumnConstraints(80));
        signUpLayout.getColumnConstraints().add(new ColumnConstraints(200));
        signUpLayout.setPadding(new Insets(10,5,10,10));
        signUpLayout.add(new Label("Register"), 0, 0);
        signUpLayout.add(new Label("Username:"), 0, 1);
        signUpLayout.add(registerUsernameField, 1, 1);
        signUpLayout.add(new Label("Password:"), 0, 2);
        signUpLayout.add(registerPasswordField, 1, 2);
        signUpLayout.add(new Label("Email:"), 0, 3);
        signUpLayout.add(registerEmailField, 1, 3);
        signUpLayout.add(signUpRegisterButton, 0, 4);
        signUpLayout.add(signUpalreadyAccountButton, 1, 4);
        signUpLayout.add(registerComment, 1, 5);
        signUpScene = new Scene(signUpLayout, 400,170);


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
            String sqlQuery = "SELECT password FROM ProsjektDatabase WHERE username=\"" + username + "\"";
            ResultSet res = stmt.executeQuery(sqlQuery);

            while (res.next()) {
                matchingPassword = res.getString("password");
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
            String sqlQuery = "INSERT INTO ProsjektDatabase(username, password, email) values('" + username + "','" + password + "','" + email + "');";
            int rowsAffected = stmt.executeUpdate(sqlQuery);
            if(rowsAffected==1) return true;
        }catch (Exception sq) {
            System.out.println("SQL-Feil: " + sq);
        }
        return false;
    }
}

/*
    private boolean register(String username, String password, String email) {
        String url = "jdbc:mysql://mysql.stud.idi.ntnu.no:3306/martijni?user=martijni&password=wrq71s2w";
        try(Connection con = DriverManager.getConnection(url)) {
            Statement stmt = con.createStatement();

            //Checks if username already exists
            String sqlQuery = "SELECT username FROM ProsjektDatabase where username=\""+username+"\"";
            ResultSet res = stmt.executeQuery(sqlQuery);
            if(res.next())return false;

        }catch (SQLSyntaxErrorException e){ //Denne kommer kun om den navnet ikke eksisterer
            try(Connection con = DriverManager.getConnection(url)){
                Statement stmt = con.createStatement();
                String sqlQuery = "INSERT INTO ProsjektDatabase(username, password, email) values(" + username + "," + password + "," + email + ");";
                int rowsAffected = stmt.executeUpdate(sqlQuery);
                if(rowsAffected==1) return true;
                return true;
            }catch (Exception sq) {
                System.out.println("SQL-Feil: " + sq);
            }
        } catch (Exception sq) {
            System.out.println("SQL-Feil: " + sq);
        }
        return false;
    }

*/
