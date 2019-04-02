package JavaFX;
import Database.DBOps;
import Database.Game;
import Database.User;
import Game.GameEngine;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.management.monitor.Monitor;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static JavaFX.FindUser.showFindUserScene;
import static JavaFX.GameScene.*;
import static JavaFX.Login.*;
import static JavaFX.Settings.showSettings;
import static JavaFX.Settings.window;
import static JavaFX.UserProfile.showUserProfileScene;
//import JavaFX.ChessSandbox;

@SuppressWarnings("Duplicates")
class MainScene {
    static Scene mainScene;
    static Timer timer = new Timer(true);
    static Button newGameButton, findUserButton, userProfileButton, settingsButton, createGameButton, joinGameButton, inviteFriendButton, cancelGameButton, clearBoard, backToMainButton, leaderboardButton, backToMainButtonWithoutSandboxReload;
    static boolean inQueueCreate = false;
    static boolean inQueueJoin = false;
    static boolean inQueueFriend = false;
    public static boolean searchFriend = false;
    static boolean created = false;
    static boolean joined = false;
    static boolean invitedFriend = false;
    static boolean inGame = false;
    private static boolean syncTurn = false;
    public static String sql;
    private static String user_id;
    static Parent chessGame;
    static Label sandboxLabel;
    static GridPane rightGrid, leftGrid, mainLayout;
    static Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

    static void showMainScene() {
        User.updateUser();
        Label title = new Label("Recess Chess");
        title.setFont(Font.font("Copperplate", 70));
        title.setStyle("-fx-font-weight: bold");
        title.setTextFill(Color.WHITE);

        Button logOutButton = new Button("Log out");
        logOutButton.setPrefSize(100, 50);
        logOutButton.setOnAction(e -> {
            removeActiveFromGame();
            runLogin();
        });


        //buttons for newGameOption
        createGameButton = new Button("Create Game");
        createGameButton.setOnAction(e -> {
            created = false;
            CreateGamePopupBox.Display(); //opens Popup
            if(created){
                title.setText("Creating Game");
                System.out.println(Login.USERNAME);
                leftGrid.getChildren().clear();
                Label queLabel = new Label("Waiting for\nopponent ...");
                queLabel.setFont(Font.font("Copperplate", 30));
                queLabel.setTextFill(Color.WHITE);
                leftGrid.setVgap(15);
                leftGrid.add(queLabel, 0, 0);
                leftGrid.add(cancelGameButton, 0, 1);
                inQueueCreate = true;
            }
        });
        joinGameButton = new Button("Join Game");
        joinGameButton.setOnAction(e -> {
            joined = false;
            JoinGamePopupBox.Display(); //opens Popup

            if(joined){
                title.setText("Joining Game");
                leftGrid.getChildren().clear();
                Label queLabel = new Label("Waiting for\nopponent ...");
                queLabel.setFont(Font.font("Copperplate", 30));
                queLabel.setTextFill(Color.WHITE);
                leftGrid.setVgap(15);
                leftGrid.add(queLabel, 0, 0);
                leftGrid.add(cancelGameButton, 0, 1);
            }
        });

        inviteFriendButton = new Button("Invite Friend");
        inviteFriendButton.setOnAction(e -> {
            invitedFriend = false;
            InviteFriendPopupBox.Display();

            if (invitedFriend) {
                title.setText("Inviting Friend");
                leftGrid.getChildren().clear();
                Label queLabel = new Label("Waiting for\nopponent ...");
                queLabel.setFont(Font.font("Copperplate", 30));
                queLabel.setTextFill(Color.WHITE);
                leftGrid.setVgap(15);
                leftGrid.add(queLabel, 0, 0);
                leftGrid.add(cancelGameButton, 0, 1);
                inQueueFriend = true;
            }
        });

        //Left GridPane
        leftGrid = new GridPane();
        leftGrid.setVgap(40);
        leftGrid.setPadding(new Insets(150, 100, 100, 170));
        newGameButton = new Button("New Game");
        newGameButton.setOnAction(e -> {
            title.setText("New Game");
            mainLayout.add(backToMainButtonWithoutSandboxReload, 0, 0, 2, 1);
            mainLayout.setHalignment(backToMainButtonWithoutSandboxReload, HPos.LEFT);
            leftGrid.getChildren().clear();
            leftGrid.setVgap(40);
            leftGrid.setPadding(new Insets(150, 100, 100, 170));
            createGameButton.setPrefSize(150, 80);
            joinGameButton.setPrefSize(150, 80);
            inviteFriendButton.setPrefSize(150, 80);
            cancelGameButton.setPrefSize(150, 80);
            leftGrid.add(createGameButton, 0, 0);
            leftGrid.setHalignment(createGameButton, HPos.CENTER);
            leftGrid.add(joinGameButton, 0, 1);
            leftGrid.setHalignment(joinGameButton, HPos.CENTER);
            leftGrid.add(inviteFriendButton, 0, 2);
            leftGrid.setHalignment(inviteFriendButton, HPos.CENTER);
        });

        backToMainButton = new Button("Back");
        Image imageBackToMain = new Image("Images/ButtonImages/ArrowLeft.png");
        ImageView imageViewBackToMain = new ImageView(imageBackToMain);
        imageViewBackToMain.setFitWidth(20);
        imageViewBackToMain.setFitHeight(20);
        backToMainButton.setGraphic(imageViewBackToMain);
        backToMainButton.setOnAction(e -> {
            mainLayout.getChildren().remove(backToMainButton);
            title.setText("Recess Chess");
            leftGrid.getChildren().clear();
            leftGrid.setVgap(40);
            leftGrid.add(newGameButton, 0, 0);
            leftGrid.setHalignment(newGameButton, HPos.CENTER);
            leftGrid.add(findUserButton, 0, 1);
            leftGrid.setHalignment(findUserButton, HPos.CENTER);
            leftGrid.add(userProfileButton, 0, 2);
            leftGrid.setHalignment(userProfileButton, HPos.CENTER);
            leftGrid.add(leaderboardButton,0,3);
            leftGrid.setHalignment(leaderboardButton,HPos.CENTER);
            leftGrid.add(settingsButton, 0, 4);
            leftGrid.setHalignment(settingsButton, HPos.CENTER);

            rightGrid.getChildren().clear();
            reloadSandbox();
        });

        backToMainButtonWithoutSandboxReload = new Button("Back");
        imageViewBackToMain.setFitWidth(20);
        imageViewBackToMain.setFitHeight(20);
        backToMainButtonWithoutSandboxReload.setGraphic(imageViewBackToMain);
        backToMainButtonWithoutSandboxReload.setOnAction(e -> {
            mainLayout.getChildren().remove(backToMainButtonWithoutSandboxReload);
            title.setText("Recess Chess");
            leftGrid.getChildren().clear();
            leftGrid.setVgap(40);
            leftGrid.add(newGameButton, 0, 0);
            leftGrid.setHalignment(newGameButton, HPos.CENTER);
            leftGrid.add(findUserButton, 0, 1);
            leftGrid.setHalignment(findUserButton, HPos.CENTER);
            leftGrid.add(userProfileButton, 0, 2);
            leftGrid.setHalignment(userProfileButton, HPos.CENTER);
            leftGrid.add(leaderboardButton,0,3);
            leftGrid.setHalignment(leaderboardButton,HPos.CENTER);
            leftGrid.add(settingsButton, 0, 4);
            leftGrid.setHalignment(settingsButton, HPos.CENTER);
        });

        findUserButton = new Button("Find User");
        findUserButton.setOnAction(e -> {
            title.setText("Find User");
            mainLayout.add(backToMainButton, 0, 0, 2, 1);
            mainLayout.setHalignment(backToMainButton, HPos.LEFT);
            leftGrid.getChildren().clear();
            rightGrid.getChildren().clear();
            showFindUserScene();
        });

        userProfileButton = new Button("User profile");
        userProfileButton.setOnAction(e -> {
            title.setText("User Profile");
            mainLayout.add(backToMainButton, 0, 0, 2, 1);
            mainLayout.setHalignment(backToMainButton, HPos.LEFT);
            leftGrid.getChildren().clear();
            rightGrid.getChildren().clear();
            showUserProfileScene();
        });

        leaderboardButton = new Button("Leaderboard");
        leaderboardButton.setOnAction(e -> {
            title.setText("Leaderboard");
            mainLayout.add(backToMainButton, 0, 0, 2, 1);
            mainLayout.setHalignment(backToMainButton, HPos.LEFT);
            leftGrid.getChildren().clear();
            rightGrid.getChildren().clear();
            rightGrid.add(LeaderboardFX.setupLeaderboard(), 0,0);
        });

        settingsButton = new Button("Settings");
        settingsButton.setOnAction(e -> showSettings());

        newGameButton.setPrefSize(150, 80);
        findUserButton.setPrefSize(150, 80);
        userProfileButton.setPrefSize(150, 80);
        settingsButton.setPrefSize(150, 80);
        leaderboardButton.setPrefSize(150,80);

        leftGrid.add(newGameButton, 0, 0);
        leftGrid.setHalignment(newGameButton, HPos.CENTER);
        leftGrid.add(findUserButton, 0, 1);
        leftGrid.setHalignment(findUserButton, HPos.CENTER);
        leftGrid.add(userProfileButton, 0, 2);
        leftGrid.setHalignment(userProfileButton, HPos.CENTER);
        leftGrid.add(leaderboardButton,0,3);
        leftGrid.setHalignment(leaderboardButton, HPos.CENTER);
        leftGrid.add(settingsButton, 0, 4);
        leftGrid.setHalignment(settingsButton, HPos.CENTER);


        //updated leftGrid
        cancelGameButton = new Button("Cancel Game");
        cancelGameButton.setOnAction(e -> {
            title.setText("Recess Chess");
            inQueueJoin = false;
            inQueueCreate = false;
            removeActiveFromGame();

            title.setText("New Game");
            leftGrid.getChildren().clear();
            leftGrid.setVgap(40);
            leftGrid.setPadding(new Insets(150, 100, 100, 170));
            createGameButton.setPrefSize(150, 80);
            joinGameButton.setPrefSize(150, 80);
            inviteFriendButton.setPrefSize(150, 80);
            //cancelGameButton.setPrefSize(150, 80);
            leftGrid.add(createGameButton, 0, 0);
            leftGrid.setHalignment(createGameButton, HPos.CENTER);
            leftGrid.add(joinGameButton, 0, 1);
            leftGrid.setHalignment(joinGameButton, HPos.CENTER);
            leftGrid.add(inviteFriendButton, 0, 2);
            leftGrid.setHalignment(inviteFriendButton, HPos.CENTER);
        });

        //Right GridPane
        rightGrid = new GridPane();
        rightGrid.setPadding(new Insets(40, 150, 20, 0));
        rightGrid.setVgap(20);
        chessGame = new ChessSandbox().createContent();
        rightGrid.add(chessGame, 0, 0);
        sandboxLabel = new Label("This is a sandbox chess game, play as you want!");
        sandboxLabel.setFont(Font.font("Calibri", 20));
        sandboxLabel.setTextFill(Color.WHITE);
        clearBoard = new Button("Clear Board");
        clearBoard.setOnAction(e -> {
            reloadSandbox();
        });
        rightGrid.add(clearBoard, 0, 1);
        rightGrid.setHalignment(clearBoard, HPos.RIGHT);
        rightGrid.add(sandboxLabel, 0, 1);

        //mainLayout
        mainLayout = new GridPane();
        mainLayout.setPadding(new Insets(30, 50, 20, 50));
        mainLayout.setHgap(20);
        mainLayout.setVgap(12);
        mainLayout.getColumnConstraints().add(new ColumnConstraints(primaryScreenBounds.getWidth()*0.80*0.35));
        mainLayout.getColumnConstraints().add(new ColumnConstraints(primaryScreenBounds.getWidth()*0.80*0.60));
        mainLayout.add(logOutButton, 0, 0, 2, 1);
        mainLayout.setHalignment(logOutButton, HPos.RIGHT);
        mainLayout.add(title, 0, 0, 2, 1);
        mainLayout.setHalignment(title, HPos.CENTER);
        mainLayout.add(leftGrid, 0, 1);
        mainLayout.setHalignment(leftGrid, HPos.CENTER);
        mainLayout.add(rightGrid, 1, 1);
        mainLayout.setHalignment(rightGrid, HPos.CENTER);
        //mainLayout.setGridLinesVisible(true);

        //Set image as background
        BackgroundImage myBI = new BackgroundImage(new Image("Images/Backgrounds/darkwood.jpg", 1200, 1200, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        mainLayout.setBackground(new Background(myBI));


        BorderPane layout = new BorderPane();
        layout.setTop(new WindowMenuBar().getWindowMenuBar());
        layout.setCenter(mainLayout);

        mainScene = new Scene(layout, primaryScreenBounds.getWidth()*0.80, primaryScreenBounds.getHeight()*0.90);
        Main.window.setScene(mainScene);
        Main.window.setX((primaryScreenBounds.getWidth()-Main.window.getWidth())/2);
        Main.window.setY((primaryScreenBounds.getHeight()-Main.window.getHeight())/4 +Main.window.getHeight()*0.01);
        Main.window.setResizable(true);
        refresh();
        searchFriend = true;
    }

    static void reloadSandbox(){
        rightGrid.getChildren().clear();
        rightGrid.setPadding(new Insets(60, 150, 20, 0));
        rightGrid.setVgap(20);
        chessGame = new ChessSandbox().createContent();
        rightGrid.add(chessGame, 0, 0);
        clearBoard = new Button("Clear Board");
        rightGrid.add(clearBoard, 0, 1);
        rightGrid.setHalignment(clearBoard, HPos.RIGHT);
        rightGrid.add(sandboxLabel, 0, 1);
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

    static void createGame(int mode, int time, int increment, boolean color, int rated) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    DBOps connection = new DBOps();

                    if (color) {
                        ChessGame.color = true;
                        connection.exUpdate("INSERT INTO Game VALUES(DEFAULT," + Login.userID + ", null, DEFAULT, " + time + ", " + increment + ", " + rated + ", null, 1, "+mode+");");
                    } else {
                        ChessGame.color = false;
                        connection.exUpdate("INSERT INTO Game VALUES(DEFAULT, null, " + Login.userID + ", DEFAULT, " + time + ", " + increment + ", " + rated + ", null, 1, "+mode+");");
                    }
                }
            });
            t.start();
    }

    static void createGame(int mode, int time, int increment, boolean color, int rated, int friendid) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                DBOps connection = new DBOps();
                int userid = Login.getUserID();

                if (color) {
                    connection.exUpdate("INSERT INTO Game VALUES(DEFAULT," + userid + ", null, DEFAULT, " + time + ", " + increment + ", " + rated + ", " + friendid + ", 1, "+mode+");");
                } else {
                    connection.exUpdate("INSERT INTO Game VALUES(DEFAULT, null, " + userid + ", DEFAULT, " + time + ", " + increment + ", " + rated + ", " + friendid + ", 1, "+mode+");");
                }
            }
        });
        t.start();

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
        //sql = createSearch(time, increment, color, rated,);
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

    static String createSearchFriend(int friendid) {
        String sql = "SELECT game_id FROM Game WHERE opponent = " +friendid + " AND active = 1;";
        return sql;
    }

    static String createSearch(int mode, int time, int increment, boolean[] color, int rated) {
        String sql = "SELECT game_id FROM Game";
        boolean firstCheck = true;
        if (mode != -1) {
            if (mode == 1) {
                if (firstCheck) {
                    sql += " WHERE mode > 1000 ";
                    firstCheck = false;
                } else {
                    sql += " AND mode > 1000 ";
                }
            }
            else if (firstCheck) {
                sql += " WHERE mode = " +mode;
                firstCheck = false;
            } else {
                sql += " AND mode = " +mode;
            }
        }
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
                    sql += " WHERE user_id2 IS null";
                    firstCheck = false;
                } else {
                    sql += " AND user_id2 IS null";
                }
            } else {
                if (firstCheck) {
                    sql += " WHERE user_id1 IS null";
                    firstCheck = false;
                } else {
                    sql += " AND user_id1 IS null";
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
        }, 0, 3000);
    }

    static void service() {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
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
                                            //System.out.println("Success!");
                                            //System.out.println("Started game with gameID: " + ChessGame.gameID);
                                            inQueueCreate = false;
                                            inGame = true;
                                            showGameScene();
                                        }
                                    } else if(inQueueJoin){
                                        System.out.println("Looking for opponent");
                                        int game_id = pollQueue(sql, connection);
                                        System.out.println(sql);
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
                                            inGame = true;
                                            showGameScene();
                                        }
                                    }else if (inQueueFriend) {
                                        System.out.println("waiting for opponent");
                                        if(!playersReady(connection)) {
                                            connection.exUpdate("UPDATE Game SET active = 0 WHERE game_id = " + ChessGame.gameID);
                                            System.out.println("Success!");
                                            System.out.println("Started game with gameID: " + ChessGame.gameID);
                                            inQueueFriend = false;
                                            inGame = true;
                                            showGameScene();
                                        }
                                    } else if(searchFriend) {
                                        System.out.println("searching friend");
                                        sql = createSearchFriend(Login.getUserID());
                                        System.out.println(sql);
                                        int game_id = pollQueue(sql, connection);
                                        if(game_id!=-1) {
                                            searchFriend = false;
                                            FriendInviteBox.Display(game_id);
                                        }
                                        /*
                                        boolean[] colors = {true, true};
                                        sql = createSearchFriend(Login.getUserID());
                                        System.out.println(sql);
                                        System.out.println("searching friend");
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
                                            searchFriend = false;
                                            removeActiveFromGame();
                                            showGameScene();
                                        }*/
                                    }
                                    /*if (syncTurn) {
                                        int move_nr = Integer.parseInt(connection.exQuery("SELECT MAX(movenr) FROM Move WHERE game_id = " +ChessGame.gameID+";", 1).get(0));
                                        System.out.println(move_nr);
                                        if (ChessGame.color) {
                                            if (move_nr % 2 == 1 && ChessGame.myTurn) {
                                                System.out.println("Synced myTurn");
                                                ChessGame.myTurn = false;
                                            } else if (move_nr % 2 == 0 && !ChessGame.myTurn) {
                                                System.out.println("Synced myTurn");
                                                ChessGame.myTurn = true;
                                            }
                                        } else {
                                            if (move_nr % 2 == 0 && ChessGame.myTurn) {
                                                System.out.println("Synced myTurn");
                                                ChessGame.myTurn = false;
                                            } else if (move_nr % 2 == 1 && !ChessGame.myTurn) {
                                                System.out.println("Synced myTurn");
                                                ChessGame.myTurn = true;
                                            }
                                        }
                                    }*/
                                    if (inGame) {
                                        int result = Game.getResult(ChessGame.gameID);
                                        System.out.println("result: "+ result);
                                        if (result != -1) {
                                            int a = ChessGame.color?2:1;
                                            if(result == a){
                                                DrawOfferPopupBox.Display();
                                                //add accept or decline draw option onscreen here
                                                /*
                                                if(accept){
                                                    inGame = false;
                                                    ChessGame.isDone = true;
                                                    Game.setResult(ChessGame.gameID, 0);
                                                    User.updateEloByGame(ChessGame.gameID);
                                                    GameOverPopupBox.Display();
                                                }else{
                                                    Game.setResult(ChessGame.gameID, -1);
                                                }*/
                                            }else if(result == 0){
                                                User.updateEloByGame(ChessGame.gameID);
                                                ChessGame.isDone = true;
                                                inGame = false;
                                                remiOffered = false;
                                                GameOverPopupBox.Display();
                                            } else if (!remiOffered){
                                                //System.out.println(ChessGame.gameID);
                                                ChessGame.gameWon = true;
                                                inGame = false;
                                                ChessGame.isDone = true;
                                                GameOverPopupBox.Display();
                                            }
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
class InviteFriendPopupBox{
    static ChoiceBox<String> modeChoiceBox = new ChoiceBox<>();
    static ChoiceBox<String> timeChoiceBox = new ChoiceBox<>();
    static ChoiceBox<String> incrementChoiceBox = new ChoiceBox<>();
    static final ToggleGroup ratedGroup = new ToggleGroup();
    static final ToggleGroup colorGroup = new ToggleGroup();
    static TextField searchField;
    static Stage window;
    static Label searchComment;

    public static void Display(){
        modeChoiceBox.getItems().clear();
        timeChoiceBox.getItems().clear();
        incrementChoiceBox.getItems().clear();
        ratedGroup.getToggles().clear();
        colorGroup.getToggles().clear();

        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Invite Friend");

        //Labels
        Label titleLabel = new Label("Game settings");
        titleLabel.setFont(Font.font("Copperplate", 26));
        titleLabel.setStyle("-fx-font-weight: bold");
        titleLabel.setTextFill(Color.WHITE);
        Label modeLabel = new Label("Mode");
        modeLabel.setFont(Font.font("Copperplate", 18));
        modeLabel.setTextFill(Color.WHITE);
        Label timeLabel = new Label("Time");
        timeLabel.setFont(Font.font("Copperplate", 18));
        timeLabel.setTextFill(Color.WHITE);
        Label incrementLabel = new Label("Increment");
        incrementLabel.setFont(Font.font("Copperplate", 18));
        incrementLabel.setTextFill(Color.WHITE);
        Label ratedLabel = new Label("Rated?");
        ratedLabel.setFont(Font.font("Copperplate", 18));
        ratedLabel.setTextFill(Color.WHITE);
        Label colorLabel = new Label("Color");
        colorLabel.setFont(Font.font("Copperplate", 18));
        colorLabel.setTextFill(Color.WHITE);
        Label friendLabel = new Label("Invite friend");
        friendLabel.setFont(Font.font("Copperplate", 18));
        friendLabel.setTextFill(Color.WHITE);

        modeChoiceBox.getItems().add("Standard");
        modeChoiceBox.getItems().add("Fischer Random");
        modeChoiceBox.getItems().add("Horse Attack");
        modeChoiceBox.getItems().add("Farmers Chess");
        modeChoiceBox.getItems().add("Peasants Revolt");
        modeChoiceBox.setValue("Standard");

        //Choiceboxes
        timeChoiceBox.getItems().add("No timer");
        timeChoiceBox.getItems().add("5 min");
        timeChoiceBox.getItems().add("10 min");
        timeChoiceBox.getItems().add("15 min");
        timeChoiceBox.getItems().add("30 min");
        timeChoiceBox.setValue("No timer");

        incrementChoiceBox.getItems().add("No increment");
        incrementChoiceBox.getItems().add("5 sec");
        incrementChoiceBox.getItems().add("10 sec");
        incrementChoiceBox.getItems().add("15 sec");
        incrementChoiceBox.setValue("No increment");

        //Radiobuttons
        HBox ratedButtons = new HBox();
        ratedButtons.setSpacing(5);
        RadioButton yesRatedRadioButton = new RadioButton("Yes");
        yesRatedRadioButton.setTextFill(Color.WHITE);
        yesRatedRadioButton.setToggleGroup(ratedGroup);
        yesRatedRadioButton.setSelected(true);
        RadioButton noRatedRadioButton = new RadioButton("No");
        noRatedRadioButton.setTextFill(Color.WHITE);
        noRatedRadioButton.setToggleGroup(ratedGroup);
        ratedButtons.getChildren().addAll(yesRatedRadioButton, noRatedRadioButton);

        HBox colorButtons = new HBox();
        colorButtons.setSpacing(5);
        RadioButton whiteColorRadioButton = new RadioButton("White");
        whiteColorRadioButton.setTextFill(Color.WHITE);
        whiteColorRadioButton.setToggleGroup(colorGroup);
        RadioButton blackColorRadioButton = new RadioButton("Black");
        blackColorRadioButton.setTextFill(Color.WHITE);
        blackColorRadioButton.setToggleGroup(colorGroup);
        RadioButton anyColorRadioButton = new RadioButton("Any");
        anyColorRadioButton.setTextFill(Color.WHITE);
        anyColorRadioButton.setToggleGroup(colorGroup);
        anyColorRadioButton.setSelected(true);
        colorButtons.getChildren().addAll(anyColorRadioButton, whiteColorRadioButton, blackColorRadioButton);

        //ratedChoicePane
        GridPane ratedChoicePane = new GridPane();
        ratedChoicePane.setHgap(5);
        ratedChoicePane.add(ratedLabel, 0, 0);
        ratedChoicePane.setHalignment(ratedLabel, HPos.CENTER);
        ratedChoicePane.add(ratedButtons, 0, 1);

        //colorChoicePane
        GridPane colorChoicePane = new GridPane();
        colorChoicePane.setHgap(5);
        colorChoicePane.add(colorLabel, 0, 0);
        colorChoicePane.setHalignment(colorLabel, HPos.CENTER);
        colorChoicePane.add(colorButtons, 0, 1);

        //Textfield with label and comment
        Label usernameLabel = new Label("Username: ");
        usernameLabel.setFont(Font.font("Copperplate", 30));
        usernameLabel.setStyle("-fx-font-weight: bold");
        usernameLabel.setTextFill(Color.WHITE);
        searchField = new TextField();
        searchField.setPrefSize(200, 30);
        searchComment = new Label("");
        searchComment.setTextFill(Color.RED);


        //Create Game Button
        Button createGameButton = new Button("Create Game");
        createGameButton.setOnAction(e -> {
            MainScene.invitedFriend = true;
            tryInviteCreate();
        });

        BorderPane windowLayout = new BorderPane();
        GridPane mainLayout = new GridPane();
        mainLayout.setHgap(35);
        mainLayout.setVgap(20);
        mainLayout.setPadding(new Insets(30, 40, 30, 40));
        mainLayout.add(titleLabel, 0, 0, 2, 1);
        mainLayout.setHalignment(titleLabel, HPos.CENTER);
        mainLayout.add(modeLabel, 0, 1);
        mainLayout.add(modeChoiceBox, 1, 1);
        mainLayout.add(timeLabel, 0, 2);
        mainLayout.add(timeChoiceBox, 1, 2);
        mainLayout.add(incrementLabel, 0, 3);
        mainLayout.add(incrementChoiceBox, 1, 3);
        mainLayout.add(ratedChoicePane, 0, 4);
        mainLayout.setHalignment(ratedChoicePane, HPos.CENTER);
        mainLayout.add(colorChoicePane, 1, 4);
        mainLayout.setHalignment(colorChoicePane, HPos.CENTER);
        mainLayout.add(friendLabel, 0, 5);
        mainLayout.add(searchField, 1, 5);
        mainLayout.add(searchComment, 1, 6);

        GridPane bottomLayout = new GridPane();
        bottomLayout.getColumnConstraints().add(new ColumnConstraints(370));
        bottomLayout.setPadding(new Insets(0,25,15,0));
        bottomLayout.add(createGameButton, 0,0);
        bottomLayout.setHalignment(createGameButton, HPos.RIGHT);
        windowLayout.setCenter(mainLayout);
        windowLayout.setBottom(bottomLayout);
        windowLayout.setStyle("-fx-background-color: #404144;");

        Scene scene = new Scene(windowLayout, 430, 470);
        scene.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER)){
                MainScene.invitedFriend = true;
                tryInviteCreate();
            }
        });
        window.setScene(scene);
        window.showAndWait();
    }

    static void tryInviteCreate(){
        String modeChoice = modeChoiceBox.getValue();
        String timeChoice = timeChoiceBox.getValue();
        String incrementChoice = incrementChoiceBox.getValue();
        RadioButton ratedChoice = (RadioButton) ratedGroup.getSelectedToggle();
        String ratedChoiceString = ratedChoice.getText();
        RadioButton colorChoice = (RadioButton) colorGroup.getSelectedToggle();
        String colorChoiceString = colorChoice.getText();
        ChessGame.gameID = MainScene.newGameID();

        int mode = 0;
        if (modeChoice.equals("Fischer Random")) {
            Random random = new Random();
            int seed = random.nextInt(4000) +1000;
            mode = seed;
        } else if (modeChoice.equals("Horse Attack")) {
            mode = 2;
        } else if (modeChoice.equals("Farmers Chess")) {
            mode = 3;
        } else if (modeChoice.equals("Peasants Revolt")) {
            mode = 4;
        }

        int time = 0;
        if (!timeChoice.equals("No timer")) {
            if (timeChoice.startsWith("5")) {
                time = Integer.parseInt(timeChoice.substring(0, 1));
            } else {
                time = Integer.parseInt(timeChoice.substring(0, 2));
            }
        }

        int increment = 0;
        if (!incrementChoice.equals("No increment")) {
            if (incrementChoice.startsWith("1")) {
                increment = Integer.parseInt(incrementChoice.substring(0, 2));
            } else {
                increment = Integer.parseInt(incrementChoice.substring(0, 1));
            }
        }
        boolean color = true;
        if (colorChoiceString.equals("Black")) {
            color = false;
        } else if (colorChoiceString.equals("Any")) {
            Random random = new Random();

            int nr = random.nextInt(2);
            if (nr == 0) {
                color = true;
            } else if (nr == 1){
                color = false;
            }
        }
        int rated = 0;
        if (ratedChoiceString.equals("Yes")) {
            rated = 1;
        }

        int opponent = 0;
        if (searchField.getText() != null) {
            if (User.getGameID(searchField.getText()) != -1) {
                if (USERNAME.equals(searchField.getText())) {
                    searchComment.setText("You can't invite yourself!");
                    return;
                } else {
                    opponent = User.getGameID(searchField.getText());
                }
            } else {
                MainScene.invitedFriend = false;
                searchComment.setText("User doesn't exist");
                System.out.println("Feil");
                return;
            }
        }


        //  MainScene.createGame(0, time, increment, color, rated);  //Here you can change time
        ChessGame.gameID = MainScene.newGameID();
        MainScene.createGame(mode,time, increment, color, rated, opponent);
        System.out.println("Time: " + timeChoice + "\nIncrement: " + incrementChoice + "\nRated: " + ratedChoiceString + "\nColor: " + colorChoiceString);
        window.close();
    }
}

@SuppressWarnings("Duplicates")
class CreateGamePopupBox{

    static ChoiceBox<String> modeChoiceBox = new ChoiceBox<>();
    static ChoiceBox<String> timeChoiceBox = new ChoiceBox<>();
    static ChoiceBox<String> incrementChoiceBox = new ChoiceBox<>();
    static final ToggleGroup ratedGroup = new ToggleGroup();
    static final ToggleGroup colorGroup = new ToggleGroup();
    static Stage window;


    public static void Display(){
        modeChoiceBox.getItems().clear();
        timeChoiceBox.getItems().clear();
        incrementChoiceBox.getItems().clear();
        ratedGroup.getToggles().clear();
        colorGroup.getToggles().clear();

        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Create Game");

        //Labels
        Label titleLabel = new Label("Game settings");
        titleLabel.setFont(Font.font("Copperplate", 26));
        titleLabel.setStyle("-fx-font-weight: bold");
        titleLabel.setTextFill(Color.WHITE);
        Label modeLabel = new Label("Mode");
        modeLabel.setFont(Font.font("Copperplate", 18));
        modeLabel.setTextFill(Color.WHITE);
        Label timeLabel = new Label("Time");
        timeLabel.setFont(Font.font("Copperplate", 18));
        timeLabel.setTextFill(Color.WHITE);
        Label incrementLabel = new Label("Increment");
        incrementLabel.setFont(Font.font("Copperplate", 18));
        incrementLabel.setTextFill(Color.WHITE);
        Label ratedLabel = new Label("Rated?");
        ratedLabel.setFont(Font.font("Copperplate", 18));
        ratedLabel.setTextFill(Color.WHITE);
        Label colorLabel = new Label("Color");
        colorLabel.setFont(Font.font("Copperplate", 18));
        colorLabel.setTextFill(Color.WHITE);

        //Choiceboxes
        modeChoiceBox.getItems().add("Standard");
        modeChoiceBox.getItems().add("Fischer Random");
        modeChoiceBox.getItems().add("Horse Attack");
        modeChoiceBox.getItems().add("Farmers Chess");
        modeChoiceBox.getItems().add("Peasants Revolt");
        modeChoiceBox.setValue("Standard");

        timeChoiceBox.getItems().add("No timer");
        timeChoiceBox.getItems().add("5 min");
        timeChoiceBox.getItems().add("10 min");
        timeChoiceBox.getItems().add("15 min");
        timeChoiceBox.getItems().add("30 min");
        timeChoiceBox.setValue("No timer");

        incrementChoiceBox.getItems().add("No increment");
        incrementChoiceBox.getItems().add("5 sec");
        incrementChoiceBox.getItems().add("10 sec");
        incrementChoiceBox.getItems().add("15 sec");
        incrementChoiceBox.setValue("No increment");

        //Radiobuttons
        HBox ratedButtons = new HBox();
        ratedButtons.setSpacing(5);
        RadioButton yesRatedRadioButton = new RadioButton("Yes");
        yesRatedRadioButton.setTextFill(Color.WHITE);
        yesRatedRadioButton.setToggleGroup(ratedGroup);
        yesRatedRadioButton.setSelected(true);
        RadioButton noRatedRadioButton = new RadioButton("No");
        noRatedRadioButton.setTextFill(Color.WHITE);
        noRatedRadioButton.setToggleGroup(ratedGroup);
        ratedButtons.getChildren().addAll(yesRatedRadioButton, noRatedRadioButton);

        HBox colorButtons = new HBox();
        colorButtons.setSpacing(5);
        RadioButton anyColorRadioButton = new RadioButton("Any");
        anyColorRadioButton.setTextFill(Color.WHITE);
        anyColorRadioButton.setToggleGroup(colorGroup);
        anyColorRadioButton.setSelected(true);
        RadioButton whiteColorRadioButton = new RadioButton("White");
        whiteColorRadioButton.setTextFill(Color.WHITE);
        whiteColorRadioButton.setToggleGroup(colorGroup);
        RadioButton blackColorRadioButton = new RadioButton("Black");
        blackColorRadioButton.setTextFill(Color.WHITE);
        blackColorRadioButton.setToggleGroup(colorGroup);
        colorButtons.getChildren().addAll(anyColorRadioButton, whiteColorRadioButton, blackColorRadioButton);

        //ratedChoicePane
        GridPane ratedChoicePane = new GridPane();
        ratedChoicePane.setHgap(5);
        ratedChoicePane.add(ratedLabel, 0, 0);
        ratedChoicePane.setHalignment(ratedLabel, HPos.CENTER);
        ratedChoicePane.add(ratedButtons, 0, 1);

        //colorChoicePane
        GridPane colorChoicePane = new GridPane();
        colorChoicePane.setHgap(5);
        colorChoicePane.add(colorLabel, 0, 0);
        colorChoicePane.setHalignment(colorLabel, HPos.CENTER);
        colorChoicePane.add(colorButtons, 0, 1);

        //Create Game Button
        Button createGameButton = new Button("Create Game");
        createGameButton.setOnAction(e -> {
            tryCreateGame();
            MainScene.created = true;
        });

        BorderPane windowLayout = new BorderPane();
        GridPane mainLayout = new GridPane();
        mainLayout.setHgap(35);
        mainLayout.setVgap(20);
        mainLayout.setPadding(new Insets(30, 40, 30, 40));
        mainLayout.add(titleLabel, 0, 0, 2, 1);
        mainLayout.setHalignment(titleLabel, HPos.CENTER);
        mainLayout.add(modeLabel, 0, 1);
        mainLayout.add(modeChoiceBox, 1, 1);
        mainLayout.add(timeLabel, 0, 2);
        mainLayout.add(timeChoiceBox, 1, 2);
        mainLayout.add(incrementLabel, 0, 3);
        mainLayout.add(incrementChoiceBox, 1, 3);
        mainLayout.add(ratedChoicePane, 0, 4);
        mainLayout.setHalignment(ratedChoicePane, HPos.CENTER);
        mainLayout.add(colorChoicePane, 1, 4);
        mainLayout.setHalignment(colorChoicePane, HPos.CENTER);

        GridPane bottomLayout = new GridPane();
        bottomLayout.getColumnConstraints().add(new ColumnConstraints(370));
        bottomLayout.setPadding(new Insets(0,25,15,0));
        bottomLayout.add(createGameButton, 0,0);
        bottomLayout.setHalignment(createGameButton, HPos.RIGHT);
        windowLayout.setCenter(mainLayout);
        windowLayout.setBottom(bottomLayout);
        windowLayout.setStyle("-fx-background-color: #404144;");

        Scene scene = new Scene(windowLayout, 410, 380);
        scene.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER)){
                tryCreateGame();
                MainScene.created = true;
            }
        });

        window.setScene(scene);
        window.showAndWait();
    }

    static void tryCreateGame(){

        String modeChoice = modeChoiceBox.getValue();
        String timeChoice = timeChoiceBox.getValue();
        String incrementChoice = incrementChoiceBox.getValue();
        RadioButton ratedChoice = (RadioButton) ratedGroup.getSelectedToggle();
        String ratedChoiceString = ratedChoice.getText();
        RadioButton colorChoice = (RadioButton) colorGroup.getSelectedToggle();
        String colorChoiceString = colorChoice.getText();
        ChessGame.gameID = MainScene.newGameID();

        int mode = 0;
        if (modeChoice.equals("Fischer Random")) {
            Random random = new Random();
            int seed = random.nextInt(4000) +1000;
            mode = seed;
        } else if (modeChoice.equals("Horse Attack")) {
            mode = 2;
        } else if (modeChoice.equals("Farmers Chess")) {
            mode = 3;
        } else if (modeChoice.equals("Peasants Revolt")) {
            mode = 4;
        }

        int time = 0;
        if (!timeChoice.equals("No timer")) {
            if (timeChoice.startsWith("5")) {
                time = Integer.parseInt(timeChoice.substring(0, 1));
            } else {
                time = Integer.parseInt(timeChoice.substring(0, 2));
            }
        }

        int increment = 0;
        if (!incrementChoice.equals("No increment")) {
            if (incrementChoice.startsWith("1")) {
                increment = Integer.parseInt(incrementChoice.substring(0, 2));
            } else {
                increment = Integer.parseInt(incrementChoice.substring(0, 1));
            }
        }
        boolean color = true;
        if (colorChoiceString.equals("Black")) {
            color = false;
        } else if (colorChoiceString.equals("Any")) {
            Random random = new Random();

            int nr = random.nextInt(2);
            if (nr == 0) {
                color = true;
            } else if (nr == 1){
                color = false;
            }
        }
        int rated = 0;
        if (ratedChoiceString.equals("Yes")) {
            rated = 1;
        }

        MainScene.createGame(mode, time, increment, color, rated);  //Here you can change time
        MainScene.inQueueCreate = true;
        System.out.println("Mode: "+modeChoice+ "\nTime: " + timeChoice + "\nIncrement: " + incrementChoice + "\nRated: " + ratedChoiceString + "\nColor: " + colorChoiceString);
        window.close();
    }
}

@SuppressWarnings("Duplicates")
class JoinGamePopupBox{

    private static Stage window;
    private static ChoiceBox<String> modeChoiceBox;
    private static ChoiceBox<String> timeChoiceBox;
    private static ChoiceBox<String> incrementChoiceBox;
    private static final ToggleGroup ratedGroup = new ToggleGroup();
    private static final ToggleGroup colorGroup = new ToggleGroup();


    public static void Display(){
        /*modeChoiceBox.getItems().clear();
        timeChoiceBox.getItems().clear();
        incrementChoiceBox.getItems().clear();
        ratedGroup.getToggles().clear();
        colorGroup.getToggles().clear();
        */

        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Join Game");

        //Labels
        Label titleLabel = new Label("Game settings");
        titleLabel.setFont(Font.font("Copperplate", 26));
        titleLabel.setStyle("-fx-font-weight: bold");
        titleLabel.setTextFill(Color.WHITE);
        Label modeLabel = new Label("Mode");
        modeLabel.setFont(Font.font("Copperplate", 18));
        modeLabel.setTextFill(Color.WHITE);
        Label timeLabel = new Label("Time");
        timeLabel.setFont(Font.font("Copperplate", 18));
        timeLabel.setTextFill(Color.WHITE);
        Label incrementLabel = new Label("Increment");
        incrementLabel.setFont(Font.font("Copperplate", 18));
        incrementLabel.setTextFill(Color.WHITE);
        Label ratedLabel = new Label("Rated?");
        ratedLabel.setFont(Font.font("Copperplate", 18));
        ratedLabel.setTextFill(Color.WHITE);
        Label colorLabel = new Label("Color");
        colorLabel.setFont(Font.font("Copperplate", 18));
        colorLabel.setTextFill(Color.WHITE);

        //Choiceboxes
        modeChoiceBox = new ChoiceBox<>();
        modeChoiceBox.getItems().add("Any");
        modeChoiceBox.getItems().add("Standard");
        modeChoiceBox.getItems().add("Fischer Random");
        modeChoiceBox.getItems().add("Horse Attack");
        modeChoiceBox.getItems().add("Farmers Chess");
        modeChoiceBox.getItems().add("Peasants Revolt");
        modeChoiceBox.setValue("Standard");

        timeChoiceBox = new ChoiceBox<>();
        timeChoiceBox.getItems().add("No timer");
        timeChoiceBox.getItems().add("5 min");
        timeChoiceBox.getItems().add("10 min");
        timeChoiceBox.getItems().add("15 min");
        timeChoiceBox.getItems().add("30 min");
        timeChoiceBox.getItems().add("Any");
        timeChoiceBox.setValue("Any");

        incrementChoiceBox = new ChoiceBox<>();
        incrementChoiceBox.getItems().add("No increment");
        incrementChoiceBox.getItems().add("5 sec");
        incrementChoiceBox.getItems().add("10 sec");
        incrementChoiceBox.getItems().add("15 sec");
        incrementChoiceBox.getItems().add("Any");
        incrementChoiceBox.setValue("Any");

        //Radiobuttons
        HBox ratedButtons = new HBox();
        ratedButtons.setSpacing(5);
        RadioButton yesRatedRadioButton = new RadioButton("Yes");
        yesRatedRadioButton.setTextFill(Color.WHITE);
        yesRatedRadioButton.setToggleGroup(ratedGroup);
        yesRatedRadioButton.setSelected(true);
        RadioButton noRatedRadioButton = new RadioButton("No");
        noRatedRadioButton.setTextFill(Color.WHITE);
        noRatedRadioButton.setToggleGroup(ratedGroup);
        ratedButtons.getChildren().addAll(yesRatedRadioButton, noRatedRadioButton);

        HBox colorButtons = new HBox();
        colorButtons.setSpacing(5);
        RadioButton anyColorRadioButton = new RadioButton("Any");
        anyColorRadioButton.setTextFill(Color.WHITE);
        anyColorRadioButton.setToggleGroup(colorGroup);
        anyColorRadioButton.setSelected(true);
        RadioButton whiteColorRadioButton = new RadioButton("White");
        whiteColorRadioButton.setTextFill(Color.WHITE);
        whiteColorRadioButton.setToggleGroup(colorGroup);
        RadioButton blackColorRadioButton = new RadioButton("Black");
        blackColorRadioButton.setTextFill(Color.WHITE);
        blackColorRadioButton.setToggleGroup(colorGroup);
        colorButtons.getChildren().addAll(anyColorRadioButton, whiteColorRadioButton, blackColorRadioButton);

        //ratedChoicePane
        GridPane ratedChoicePane = new GridPane();
        ratedChoicePane.setHgap(5);
        ratedChoicePane.add(ratedLabel, 0, 0);
        ratedChoicePane.setHalignment(ratedLabel, HPos.CENTER);
        ratedChoicePane.add(ratedButtons, 0, 1);

        //colorChoicePane
        GridPane colorChoicePane = new GridPane();
        colorChoicePane.setHgap(5);
        colorChoicePane.add(colorLabel, 0, 0);
        colorChoicePane.setHalignment(colorLabel, HPos.CENTER);
        colorChoicePane.add(colorButtons, 0, 1);

        //Create Game Button
        Button joinGameButton = new Button("Join Game");
        joinGameButton.setOnAction(e -> {
            tryJoinGame();
            MainScene.joined = true;
        });

        BorderPane windowLayout = new BorderPane();
        GridPane mainLayout = new GridPane();
        mainLayout.setHgap(35);
        mainLayout.setVgap(20);
        mainLayout.setPadding(new Insets(30, 40, 30, 40));
        mainLayout.add(titleLabel, 0, 0, 2, 1);
        mainLayout.setHalignment(titleLabel, HPos.CENTER);
        mainLayout.add(modeLabel, 0, 1);
        mainLayout.add(modeChoiceBox, 1, 1);
        mainLayout.add(timeLabel, 0, 2);
        mainLayout.add(timeChoiceBox, 1, 2);
        mainLayout.add(incrementLabel, 0, 3);
        mainLayout.add(incrementChoiceBox, 1, 3);
        mainLayout.add(ratedChoicePane, 0, 4);
        mainLayout.setHalignment(ratedChoicePane, HPos.CENTER);
        mainLayout.add(colorChoicePane, 1, 4);
        mainLayout.setHalignment(colorChoicePane, HPos.CENTER);

        GridPane bottomLayout = new GridPane();
        bottomLayout.getColumnConstraints().add(new ColumnConstraints(370));
        bottomLayout.setPadding(new Insets(0,25,15,0));
        bottomLayout.add(joinGameButton, 0,0);
        bottomLayout.setHalignment(joinGameButton, HPos.RIGHT);
        windowLayout.setCenter(mainLayout);
        windowLayout.setBottom(bottomLayout);
        windowLayout.setStyle("-fx-background-color: #404144;");

        Scene scene = new Scene(windowLayout, 410, 380);
        scene.setOnKeyPressed(e->{
            if(e.getCode().equals(KeyCode.ENTER)){
                tryJoinGame();
                MainScene.joined = true;
            }
        });
        window.setScene(scene);
        window.showAndWait();
    }

    static void tryJoinGame(){
        String modeChoice = modeChoiceBox.getValue();
        String timeChoice = timeChoiceBox.getValue();
        String incrementChoice = incrementChoiceBox.getValue();
        RadioButton ratedChoice = (RadioButton) ratedGroup.getSelectedToggle();
        String ratedChoiceString = ratedChoice.getText();
        RadioButton colorChoice = (RadioButton) colorGroup.getSelectedToggle();
        String colorChoiceString = colorChoice.getText();

        int mode = -1;
        if (modeChoice.equals("Standard")) {
            mode = 0;
        } else if (modeChoice.equals("Fischer Random")) {
            mode = 1;
        } else if (modeChoice.equals("Horse Attack")) {
            mode = 2;
        } else if (modeChoice.equals("Farmers Chess")) {
            mode = 3;
        } else if (modeChoice.equals("Peasants Revolt")) {
            mode = 4;
        }

        int time = -1;
        if (!timeChoice.equals("Any")) {
            if (timeChoice.equals("No timer")) {
                time = 0;
            } else if (timeChoice.startsWith("5")) {
                time = Integer.parseInt(timeChoice.substring(0, 1));
            } else {
                time = Integer.parseInt(timeChoice.substring(0, 2));
            }
        }
        int increment = -1;
        if (!incrementChoice.equals("Any")) {
            if (incrementChoice.equals("No increment")) {
                time = 0;
            } else if (incrementChoice.startsWith("1")) {
                increment = Integer.parseInt(incrementChoice.substring(0, 2));
            } else {
                increment = Integer.parseInt(incrementChoice.substring(0, 1));
            }
        }
        boolean[] color = {false, false};
        if (colorChoiceString.equals("Any")) {
            color[1] = true;
        } else if (colorChoiceString.equals("Black")) {
            color[0] = true;
        } else if (colorChoiceString.equals("White")) {
            color[0] = false;
        }
        int rated = 0;
        if (ratedChoiceString.equals("Yes")) {
            rated = 1;
        }

        MainScene.sql = MainScene.createSearch(mode, time, increment, color, rated);
        System.out.println(MainScene.sql);
        MainScene.inQueueJoin = true;
        System.out.println("Mode: " +modeChoice+ "\nTime: " + timeChoice + "\nIncrement: " + incrementChoice + "\nRated: " + ratedChoiceString + "\nColor: " + colorChoiceString);
        window.close();
    }
}

class GameOverPopupBox{

    public static void Display(){
        int oldElo = ChessGame.color?ChessGame.whiteELO:ChessGame.blackELO;
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Game over");


        //Labels
        int result = Game.getResult(ChessGame.gameID);
        String text = ChessGame.gameWon?"VICTORY":"DEFEAT";

        if(result == 0){
            result = 2;
            text = "DRAW";
        }else if(result == Game.getUser_id1(ChessGame.gameID)){
            result = 0;
        }else{
            result = 1;
        }
        Label titleLabel = new Label(text);
        titleLabel.setFont(Font.font("Copperplate", 26));
        titleLabel.setStyle("-fx-font-weight: bold");
        titleLabel.setTextFill(Color.WHITE);

        int[] elo = GameEngine.getElo(ChessGame.whiteELO, ChessGame.blackELO, result);
        int myNewElo = ChessGame.color?elo[0]:elo[1];
        int enemyElo = ChessGame.color?elo[1]:elo[0];
        System.out.println("old ELO: " + oldElo + " your new ELO: "+ myNewElo + " \nEnemy's new ELO: " + enemyElo);
        String newElo = Login.USERNAME + "'s new ELO rating: \n" + myNewElo + " (" +((myNewElo-oldElo)>0?"+":"") +(myNewElo-oldElo) + ")";
        Label eloLabel = new Label(newElo);
        eloLabel.setFont(Font.font("Copperplate", 22));
        eloLabel.setStyle("-fx-font-weight: bold");
        eloLabel.setTextFill(Color.WHITE);


        //Create Game Button
        Button leaveGameButton = new Button("Leave Game");
        leaveGameButton.setOnAction(e -> {
            MainScene.showMainScene();
            MainScene.inGame = false;
            MainScene.searchFriend = true;
            window.close();
        });

        BorderPane windowLayout = new BorderPane();
        GridPane mainLayout = new GridPane();
        mainLayout.setHgap(35);
        mainLayout.setVgap(20);
        mainLayout.setPadding(new Insets(30, 40, 30, 40));
        mainLayout.add(titleLabel, 0, 0, 2, 1);
        mainLayout.add(eloLabel,0,1,2,1);
        mainLayout.setHalignment(titleLabel, HPos.CENTER);
        mainLayout.setHalignment(eloLabel, HPos.CENTER);

        GridPane bottomLayout = new GridPane();
        bottomLayout.getColumnConstraints().add(new ColumnConstraints(370));
        bottomLayout.setPadding(new Insets(0,25,15,0));
        bottomLayout.add(leaveGameButton, 0,0);
        bottomLayout.setHalignment(leaveGameButton, HPos.CENTER);
        windowLayout.setCenter(mainLayout);
        windowLayout.setBottom(bottomLayout);
        windowLayout.setStyle("-fx-background-color: #404144;");

        Scene scene = new Scene(windowLayout, 350, 310);
        window.setScene(scene);
        window.showAndWait();
        User.updateUser();
    }
}
class FriendInviteBox {
    public static void Display(int game_id){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Invite");
        ArrayList<String> text = Game.friendInviteInfo(game_id);
        //Labels
        Label titleLabel = new Label(text.get(0));
        titleLabel.setFont(Font.font("Copperplate", 26));
        titleLabel.setStyle("-fx-font-weight: bold");
        titleLabel.setTextFill(Color.WHITE);

        Label textLabel = new Label(text.get(1));
        textLabel.setFont(Font.font("Copperplate", 22));
        textLabel.setStyle("-fx-font-weight: bold");
        textLabel.setTextFill(Color.WHITE);

        //Create Game Button
        Button acceptInvite = new Button("Accept");
        acceptInvite.setOnAction(e -> {
            DBOps connection = new DBOps();
            ChessGame.gameID = game_id;
            if (Game.getActive(ChessGame.gameID)) {
                if (connection.exUpdate("UPDATE Game SET user_id1 = " + Login.userID + " WHERE user_id1 IS NULL AND game_id = " + game_id + ";") == 1) {
                    ChessGame.color = true;
                } else {
                    connection.exUpdate("UPDATE Game SET user_id2 = " + Login.userID + " WHERE user_id2 IS NULL AND game_id = " + game_id + ";");
                    ChessGame.color = false;
                }
                System.out.println("Started game with gameID: " + ChessGame.gameID);
                MainScene.searchFriend = false;
                MainScene.inGame = true;
                MainScene.removeActiveFromGame();
                window.close();
                showGameScene();
            } else {
                window.close();
                System.out.println("Not active");
            }
        });

        Button declineInvite = new Button("Decline");
        declineInvite.setOnAction(e -> {
            Game.setInactiveByGame_id(game_id);
            MainScene.searchFriend = true;
            window.close();
        });

        BorderPane windowLayout = new BorderPane();
        GridPane mainLayout = new GridPane();
        mainLayout.setHgap(35);
        mainLayout.setVgap(20);
        mainLayout.setPadding(new Insets(30, 40, 30, 40));
        mainLayout.add(titleLabel, 0, 0, 2, 1);
        mainLayout.add(textLabel, 0, 1, 2, 1);
        mainLayout.setHalignment(textLabel, HPos.CENTER);
        mainLayout.setHalignment(titleLabel, HPos.CENTER);

        GridPane bottomLayout = new GridPane();
        bottomLayout.getColumnConstraints().add(new ColumnConstraints(370));
        bottomLayout.setPadding(new Insets(0,25,15,0));
        bottomLayout.add(acceptInvite, 0,0);
        bottomLayout.setHalignment(acceptInvite, HPos.LEFT);
        bottomLayout.add(declineInvite, 1, 0);
        bottomLayout.setHalignment(acceptInvite, HPos.RIGHT);
        windowLayout.setCenter(mainLayout);
        windowLayout.setBottom(bottomLayout);
        windowLayout.setStyle("-fx-background-color: #404144;");

        Scene scene = new Scene(windowLayout, 530, 360);
        window.setScene(scene);
        window.showAndWait();
    }
}