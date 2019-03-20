package JavaFX;
import Database.DBOps;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import static JavaFX.FindUser.showFindUserScene;
import static JavaFX.GameScene.*;
import static JavaFX.Login.*;
import static JavaFX.Settings.showSettings;
import static JavaFX.UserProfile.setAvatar;
import static JavaFX.UserProfile.showUserProfileScene;
//import JavaFX.ChessSandbox;

@SuppressWarnings("Duplicates")
class MainScene {
    static Scene mainScene;
    static GridPane leftGrid;
    static Timer timer = new Timer(true);
    static Button newGameButton, findUserButton, userProfileButton, settingsButton, createGameButton, joinGameButton, inviteFriendButton, backButton;
    private static boolean inQueueCreate = false;
    private static boolean inQueueJoin = false;
    private static String sql;
    private static String user_id;

    static void showMainScene() {
        Label title = new Label("Recess Chess");
        title.setFont(Font.font("Copperplate", 70));
        title.setStyle("-fx-font-weight: bold");
        title.setTextFill(Color.WHITE);

        Button logOutButton = new Button("Log out");
        logOutButton.setPrefSize(100, 50);
        logOutButton.setOnAction(e -> {
            runLogin();
        });


        //buttons for newGameOption
        createGameButton = new Button("Create Game");
        createGameButton.setOnAction(e -> {
            System.out.println(Login.USERNAME);
            ChessGame.gameID = newGameID();
            createGame(67, 10, true, 1);
            leftGrid.getChildren().clear();
            Label queLabel = new Label("Waiting for\nopponent ...");
            queLabel.setFont(Font.font("Copperplate", 34));
            queLabel.setTextFill(Color.WHITE);
            leftGrid.getChildren().add(queLabel);
            leftGrid.setVgap(10);
            leftGrid.getChildren().add(backButton);
            inQueueCreate = true;
            //waitForOpponent();


            //showGameScene();
        });
        joinGameButton = new Button("Join Game");
        joinGameButton.setOnAction(e -> {
            boolean[] colors = {true, true};
            //JoinGamePopup.Display()
            //joinGame(25, 5, colors, 1);
            sql = createSearch(67, 10, colors, 1);
            inQueueJoin = true;
            leftGrid.getChildren().clear();
            leftGrid.getChildren().add(backButton);
        });

        //Left GridPane
        leftGrid = new GridPane();
        leftGrid.setVgap(40);
        leftGrid.setPadding(new Insets(150, 150, 100, 250));
        newGameButton = new Button("New Game");
        newGameButton.setOnAction(e -> {
            leftGrid.getChildren().clear();
            leftGrid.setVgap(40);
            leftGrid.setPadding(new Insets(150, 150, 100, 250));
            createGameButton.setPrefSize(150, 80);
            joinGameButton.setPrefSize(150, 80);
            inviteFriendButton.setPrefSize(150, 80);
            backButton.setPrefSize(150, 80);
            leftGrid.add(createGameButton, 0, 0);
            leftGrid.setHalignment(createGameButton, HPos.CENTER);
            leftGrid.add(joinGameButton, 0, 1);
            leftGrid.setHalignment(joinGameButton, HPos.CENTER);
            leftGrid.add(inviteFriendButton, 0, 2);
            leftGrid.setHalignment(inviteFriendButton, HPos.CENTER);
            leftGrid.add(backButton, 0, 3);
            leftGrid.setHalignment(backButton, HPos.CENTER);

        });
        findUserButton = new Button("Find User");
        findUserButton.setOnAction(e -> showFindUserScene());
        userProfileButton = new Button("User profile");
        userProfileButton.setOnAction(e -> showUserProfileScene());
        settingsButton = new Button("Settings");
        settingsButton.setOnAction(e -> showSettings());
        newGameButton.setPrefSize(150, 80);
        findUserButton.setPrefSize(150, 80);
        userProfileButton.setPrefSize(150, 80);
        settingsButton.setPrefSize(150, 80);
        leftGrid.add(newGameButton, 0, 0);
        leftGrid.setHalignment(newGameButton, HPos.CENTER);
        leftGrid.add(findUserButton, 0, 1);
        leftGrid.setHalignment(findUserButton, HPos.CENTER);
        leftGrid.add(userProfileButton, 0, 2);
        leftGrid.setHalignment(userProfileButton, HPos.CENTER);
        leftGrid.add(settingsButton, 0, 3);
        leftGrid.setHalignment(settingsButton, HPos.CENTER);


        //updated leftGrid
        inviteFriendButton = new Button("Invite Friend");
        inviteFriendButton.setOnAction(e -> {
        });
        backButton = new Button("Back to Main");
        backButton.setOnAction(e -> {
            inQueueJoin = false;
            inQueueCreate = false;
            removeActiveFromGame();

            leftGrid.getChildren().clear();
            leftGrid.add(newGameButton, 0, 0);
            leftGrid.setHalignment(newGameButton, HPos.CENTER);
            leftGrid.add(findUserButton, 0, 1);
            leftGrid.setHalignment(findUserButton, HPos.CENTER);
            leftGrid.add(userProfileButton, 0, 2);
            leftGrid.setHalignment(userProfileButton, HPos.CENTER);
            leftGrid.add(settingsButton, 0, 3);
            leftGrid.setHalignment(settingsButton, HPos.CENTER);
        });


        //Right GridPane
        GridPane rightGrid = new GridPane();
        rightGrid.setPadding(new Insets(60, 150, 20, 0));
        rightGrid.setVgap(20);
        Parent chessGame = new ChessSandbox().createContent();
        rightGrid.add(chessGame, 0, 0);
        Button clearBoard = new Button("Clear Board");
        clearBoard.setOnAction(e -> showMainScene());
        rightGrid.add(clearBoard, 0, 1);
        rightGrid.setHalignment(clearBoard, HPos.RIGHT);
        Label sandboxLabel = new Label("This is a sandbox chess game, play as you want!");
        sandboxLabel.setFont(Font.font("Calibri", 20));
        sandboxLabel.setTextFill(Color.WHITE);
        rightGrid.add(sandboxLabel, 0, 1);

        //mainLayout
        GridPane mainLayout = new GridPane();
        mainLayout.setPadding(new Insets(30, 50, 20, 50));
        mainLayout.setHgap(20);
        mainLayout.setVgap(12);
        mainLayout.getColumnConstraints().add(new ColumnConstraints(625));
        mainLayout.getColumnConstraints().add(new ColumnConstraints(725));
        mainLayout.add(logOutButton, 0, 0, 2, 1);
        mainLayout.setHalignment(logOutButton, HPos.LEFT);
        mainLayout.add(title, 0, 0, 2, 1);
        mainLayout.setHalignment(title, HPos.CENTER);
        mainLayout.add(leftGrid, 0, 1);
        mainLayout.setHalignment(leftGrid, HPos.CENTER);
        mainLayout.add(rightGrid, 1, 1);
        mainLayout.setHalignment(rightGrid, HPos.CENTER);
        //mainLayout.setGridLinesVisible(true);

        //Set image as background
        BackgroundImage myBI = new BackgroundImage(new Image("Images/Backgrounds/Mahogny.jpg", 1200, 1200, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        mainLayout.setBackground(new Background(myBI));


        BorderPane layout = new BorderPane();
        layout.setTop(new WindowMenuBar("home").getWindowMenuBar());
        layout.setCenter(mainLayout);

        mainScene = new Scene(layout, 1450, 950);
        Main.window.setScene(mainScene);
        refresh();
    }

    static void removeActiveFromGame(){
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps temp = new DBOps();
                int game_id = ChessGame.gameID;
                temp.exUpdate("UPDATE Game SET active = 0 WHERE game_id = " + game_id + ";");
            }
        });
        t.start();
    }

    static int findUser_id(){
        Thread t = new Thread(new Runnable() {
            public void run() {
                String username = Login.USERNAME;

            }
        });
        return -1;
    }

    static void gameSetup() {
        ChessGame.gameID = newGameID();
    }

    static int newGameID() {
        DBOps connection = new DBOps();
        ArrayList matchingGameIDs = connection.exQuery("SELECT MAX(game_id) FROM Game;", 1); //Change this SQLQuery to match the database
        if (matchingGameIDs.size() == 0) {
            return 1;
        }
        int out = Integer.parseInt((String) matchingGameIDs.get(0));
        return out + 1;
    }

    static void createGame(int time, int increment, boolean color, int rated) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    DBOps connection = new DBOps();
                    int userid = 1;

                    if (color) {
                        connection.exUpdate("INSERT INTO Game VALUES(DEFAULT," + userid + ", null, null, " + time + ", " + increment + ", " + rated + ", null, 1);");
                    } else {
                        connection.exUpdate("INSERT INTO Game VALUES(DEFAULT, null, " + userid + ", null, " + time + ", " + increment + ", " + rated + ", null, 1);");
                    }
                }
            });
            t.start();
    }

    static void createGame(int time, int increment, boolean color, int rated, String username) {
        DBOps connection = new DBOps();
        int userid = 1;

        if (color) {
            connection.exUpdate("INSERT INTO Game VALUES(DEFAULT," + userid + ", null, null, " + time + ", " + increment + ", " + rated + ", " + username + ", 1);");
        } else {
            connection.exUpdate("INSERT INTO Game VALUES(DEFAULT, null, " + userid + ", null, " + time + ", " + increment + ", " + rated + ", " + username + ", 1);");
        }
    }

    //depricated method
    static void waitForOpponent() {
        DBOps connection = new DBOps();
        Thread t = new Thread(new Runnable() {
            public void run() {
                boolean ready = true;
                while(ready && inQueueJoin){
                    ready = playersReady(connection);
                }
                connection.exUpdate("UPDATE Game SET active = 0 WHERE game_id = " +ChessGame.gameID);
                System.out.println("Success!");
            }
        });
        t.start();
    }

    static boolean playersReady(DBOps connection) {
        System.out.println(ChessGame.gameID);
        String sql = "SELECT * FROM Game WHERE user_id1 IS NOT NULL AND user_id2 IS NOT NULL AND game_id = " +ChessGame.gameID +";";
        try {
            if (connection.exQuery(sql, 1).size() > 0) {
                //showGameScene();
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    //deprecated method
    static void joinGame(int time, int increment, boolean[] color, int rated) {
        sql = createSearch(time, increment, color, rated);
        inQueueJoin = true;
        DBOps connection = new DBOps();
        //ArrayList opponent = connection.exQuery(sql, 1);

        Thread t = new Thread(new Runnable() {
            public void run() {
                int game_id = -1;
                while(game_id==-1){
                    game_id = pollQueue(sql, connection);
                }
                //ChessGame.gameID = game_id;

                if (connection.exUpdate("UPDATE Game SET user_id1 = " +2+ " WHERE user_id1 IS NULL AND game_id = " + game_id + ";") == 1) {
                    ChessGame.color = true;
                } else {
                    connection.exUpdate("UPDATE Game SET user_id2 = " +2+ " WHERE user_id2 IS NULL AND game_id = " + game_id + ";");
                    ChessGame.color = false;
                }
            }
        });

        t.start();
    }


    public static int pollQueue(String sql, DBOps connection) {
        ArrayList<String> opponent = connection.exQuery(sql, 1);
        if (opponent.size() > 0) {
            return Integer.parseInt(opponent.get(0));
        }
        return -1;
    }


    static String createSearch(int time, int increment, boolean[] color, int rated) {
        String sql = "SELECT game_id FROM Game";
        boolean firstCheck = true;
        if (time != -1) {
            if (firstCheck) {
                sql += " WHERE time = " +time;
                firstCheck = false;
            } else {
                sql += " AND time = " +time;
            }
        }
        if (increment != -1) {
            if (firstCheck) {
                sql += " WHERE increment = " +increment;
                firstCheck = false;
            } else {
                sql += " AND increment = " +increment;
            }
        }
        if (color[1]) {
            if (firstCheck) {
                sql += " WHERE(user_id1 IS null OR user_id2 IS null)";
                firstCheck = false;
            } else {
                sql += " AND(user_id1 IS null OR user_id2 IS null)";
            }
        } else {
            if (color[0]) {
                if (firstCheck) {
                    sql += " WHERE user_id2 IS null)";
                    firstCheck = false;
                } else {
                    sql += " AND user_id2 IS null)";
                }
            } else {
                if (firstCheck) {
                    sql += " WHERE user_id1 IS null)";
                    firstCheck = false;
                } else {
                    sql += " AND user_id1 IS null)";
                }
            }
        }
        if (rated != -1) {
           if (firstCheck) {
               sql += " WHERE rated = " +rated;
               firstCheck = false;
           } else {
               sql += " AND rated = " +rated;
           }
        }
        sql += " AND active = 1;";
        System.out.println(sql);
        return sql;
    }

    public static void refresh(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                service();
            }
        }, 5000, 5000);
    }

    static void service() {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        System.out.println("hEI");
                        //Background work
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                DBOps connection = new DBOps();
                                try {
                                    if(inQueueCreate){
                                        System.out.println("waiting for opponent");
                                        if(!playersReady(connection)) {
                                            connection.exUpdate("UPDATE Game SET active = 0 WHERE game_id = " + ChessGame.gameID);
                                            System.out.println("Success!");
                                            System.out.println("Started game with gameID: " + ChessGame.gameID);
                                            inQueueCreate = false;
                                            showGameScene();
                                        }
                                    }else if(inQueueJoin){
                                        System.out.println("Looking for opponent");
                                        int game_id = pollQueue(sql, connection);
                                        if(game_id!=-1) {
                                            ChessGame.gameID = game_id;
                                            if (connection.exUpdate("UPDATE Game SET user_id1 = " + Login.userID + " WHERE user_id1 IS NULL AND game_id = " + game_id + ";") == 1) {
                                                ChessGame.color = true;
                                            } else {
                                                connection.exUpdate("UPDATE Game SET user_id2 = " + Login.userID + " WHERE user_id2 IS NULL AND game_id = " + game_id + ";");
                                                ChessGame.color = false;
                                            }
                                            System.out.println("Started game with gameID: " + ChessGame.gameID);
                                            inQueueJoin = false;
                                            showGameScene();
                                        }
                                    }
                                } finally {
                                    latch.countDown();
                                }
                            }
                        });
                        latch.await();
                        //Keep with the background work
                        return null;
                    }
                };
            }
        };
        service.start();
    }


}

@SuppressWarnings("Duplicates")
class JoinGamePopup{

    public static void Display(){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Join Game");

        Label label = new Label("GameID:");
        label.setFont(Font.font("Copperplate", 24));
        label.setStyle("-fx-font-weight: bold");
        label.setTextFill(Color.WHITE);

        Label comment = new Label("");
        comment.setTextFill(Color.RED);

        TextField gameIDInputField = new TextField();
        gameIDInputField.setPrefWidth(260);

        Button connectButton = new Button("Connect");
        connectButton.setOnAction(e -> {
            String gameIDInputString = gameIDInputField.getText();
            if(isInt(gameIDInputString)){
                int inputInt = Integer.parseInt(gameIDInputString);

                if (checkGameId(inputInt)){
                    ChessGame.gameID = inputInt;
                    window.close();
                    showGameScene();
                }

                ChessGame.gameID = inputInt;
                window.close();
                showGameScene();
            } else {comment.setText("Not a valid number!");}
        });

        GridPane mainLayout = new GridPane();
        mainLayout.setHgap(10);
        mainLayout.setVgap(20);
        mainLayout.setPadding(new Insets(30, 30, 30, 30));
        mainLayout.add(label, 0, 0, 2, 1);
        mainLayout.setHalignment(label, HPos.CENTER);
        mainLayout.add(gameIDInputField, 0, 1);
        mainLayout.add(comment, 0, 2);
        mainLayout.add(connectButton, 0, 2);
        mainLayout.setHalignment(connectButton, HPos.RIGHT);
        mainLayout.setStyle("-fx-background-color: #404144;");

        Scene scene = new Scene(mainLayout, 330, 180);
        window.setScene(scene);
        window.showAndWait();
    }

    static boolean isInt(String string){
        try{
            int out = Integer.parseInt(string);
        } catch (Exception a){
            return false;
        }
        return true;
    }

    static boolean checkGameId(int gameid){ //Here you have to check if the GameID exists!
        DBOps connection = new DBOps();
        ArrayList matchingGameIDs = connection.exQuery("SELECT GameID from GameIDMove where GameID = '" + gameid + "'", 1); //Change this SQLQuery to match the database
        if(matchingGameIDs.size() > 0){
            return true;
        } else {
            return false;
        }
    }
}



