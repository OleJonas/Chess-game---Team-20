package GUI.MainScene;

import Database.DBOps;
import Database.Game;
import Database.User;
import GUI.GameScene.ChessGame;
import GUI.GameScene.DrawOfferPopupBox;
import GUI.GameScene.GameOverPopupBox;
import GUI.LoginScreen.Login;
import GUI.MainScene.GameCreation.CreateGamePopupBox;
import GUI.MainScene.GameCreation.FriendInviteBox;
import GUI.MainScene.GameCreation.InviteFriendPopupBox;
import GUI.MainScene.GameCreation.JoinGamePopupBox;
import GUI.MainScene.Sandbox.ChessSandbox;
import GUI.Main;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.WindowEvent;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import static GUI.GameScene.GameScene.*;
import static GUI.LoginScreen.Login.*;
import static GUI.MainScene.UserProfile.setAvatar;
import static GUI.MainScene.UserProfile.showUserProfileScene;

/**
 * <h1>MainScene</h1>
 * This class is used to display the main screen of the program.
 * @since 08.04.2019
 * @author Team 20
 */

@SuppressWarnings("Duplicates")
public class MainScene {
    static Scene mainScene;
    static Timer timer = new Timer(true);
    static Button newGameButton, findUserButton, userProfileButton, settingsButton, createGameButton, joinGameButton,
            inviteFriendButton, cancelGameButton, clearBoard, backToMainButton, leaderboardButton, backToMainButtonWithoutSandboxReload;
    public static boolean inQueueCreate = false;
    public static boolean inQueueJoin = false;
    public static boolean inQueueFriend = false;
    public static boolean searchFriend = false;
    public static boolean inDrawOffer = false;
    public static boolean created = false;
    public static boolean joined = false;
    public static boolean invitedFriend = false;
    public static boolean inGame = false;
    static Parent chessGame;
    static Label sandboxLabel, title;
    static GridPane rightGrid, leftGrid, mainLayout;
    public static Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    private static boolean onMain;

    /**
     * Displays the main screen of the GUI.
     */
    public static void showMainScene() {
        User.updateUser();
        onMain = true;

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));

        title = new Label("Recess Chess");
        title.setEffect(dropShadow);
        title.setFont(Font.font("Georgia", 70));
        title.setStyle("-fx-font-weight: bold");
        title.setTextFill(Color.WHITE);

        Button logOutButton = new Button("Log out");
        logOutButton.setPrefSize(100, 50);
        logOutButton.setOnAction(e -> {
            Game.removeActiveFromGame();
            searchFriend = false;
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
                queLabel.setFont(Font.font("Georgia", 30));
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
                queLabel.setFont(Font.font("Georgia", 30));
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
                queLabel.setFont(Font.font("Georgia", 30));
                queLabel.setTextFill(Color.WHITE);
                leftGrid.setVgap(15);
                leftGrid.add(queLabel, 0, 0);
                leftGrid.add(cancelGameButton, 0, 1);
                inQueueFriend = true;
                invitedFriend = false;
            }
        });

        //Left GridPane
        leftGrid = new GridPane();
        leftGrid.setVgap(40);
        leftGrid.setPadding(new Insets(150, 100, 100, 170));
        newGameButton = new Button("New Game");
        newGameButton.setStyle("-fx-background-color: #29AC29");
        newGameButton.setTextFill(Color.WHITE);
        newGameButton.setOnAction(e -> {
            newGameButtonPressed();
        });

        backToMainButton = new Button("Back");
        Image imageBackToMain = new Image("Images/ButtonImages/ArrowLeft.png");
        ImageView imageViewBackToMain = new ImageView(imageBackToMain);
        imageViewBackToMain.setFitWidth(20);
        imageViewBackToMain.setFitHeight(20);
        backToMainButton.setGraphic(imageViewBackToMain);
        backToMainButton.setOnAction(e -> {
            setAvatar(AVATAR);
            mainLayout.getChildren().remove(backToMainButton);
            title.setText("Recess Chess");
            mainLayout.setVgap(12);
            leftGrid.getChildren().clear();
            leftGrid.setVgap(40);
            leftGrid.setPadding(new Insets(150, 100, 100, 170));
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
            BackgroundImage empty = new BackgroundImage(new Image("Images/Avatars/emptyAvatar.png", 700, 700, false, true),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            rightGrid.setBackground(new Background(empty));
            reloadSandbox();
        });

        backToMainButtonWithoutSandboxReload = new Button("Back");
        Image imageBackToMain2 = new Image("Images/ButtonImages/ArrowLeft.png");
        ImageView imageViewBackToMain2 = new ImageView(imageBackToMain2);
        imageViewBackToMain2.setFitWidth(20);
        imageViewBackToMain2.setFitHeight(20);
        backToMainButtonWithoutSandboxReload.setGraphic(imageViewBackToMain2);
        backToMainButtonWithoutSandboxReload.setOnAction(e -> {
            mainLayout.getChildren().remove(backToMainButtonWithoutSandboxReload);
            title.setText("Recess Chess");
            mainLayout.setVgap(12);
            leftGrid.getChildren().clear();
            leftGrid.setVgap(40);
            rightGrid.setPadding(new Insets(40, 150, 20, 0));
            leftGrid.setPadding(new Insets(150, 100, 100, 170));
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

            BackgroundImage empty = new BackgroundImage(new Image("Images/Avatars/emptyAvatar.png", 700, 700, false, true),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            rightGrid.setBackground(new Background(empty));
        });

        findUserButton = new Button("Find User");
        findUserButton.setOnAction(e -> {
            findUserButtonPressed();
        });

        userProfileButton = new Button("User profile");
        userProfileButton.setOnAction(e -> {
            userProfileButtonPressed();
        });

        leaderboardButton = new Button("Leaderboard");
        leaderboardButton.setOnAction(e -> {
            leaderboardButtonPressed();
        });

        settingsButton = new Button("Settings");
        settingsButton.setOnAction(e -> Settings.showSettings());

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
            inQueueFriend = false;
            invitedFriend = false;
            Game.removeActiveFromGame();

            title.setText("New Game");
            leftGrid.getChildren().clear();
            leftGrid.setVgap(40);
            leftGrid.setPadding(new Insets(150, 100, 100, 170));
            createGameButton.setPrefSize(150, 80);
            joinGameButton.setPrefSize(150, 80);
            inviteFriendButton.setPrefSize(150, 80);
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
        sandboxLabel = new Label("This is a sandbox chess game!");
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
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        mainLayout.setBackground(new Background(myBI));


        BorderPane layout = new BorderPane();
        //layout.setTop(new WindowMenuBar().getWindowMenuBar()); //WindowMenuBar has to be fixed if we're going to use it
        layout.setCenter(mainLayout);

        mainScene = new Scene(layout, primaryScreenBounds.getWidth()*0.82, primaryScreenBounds.getHeight()*0.90);

        Main.window.setScene(mainScene);
        Main.window.setX((primaryScreenBounds.getWidth()- Main.window.getWidth())/2);
        Main.window.setY((primaryScreenBounds.getHeight()- Main.window.getHeight())/4 + Main.window.getHeight()*0.01);
        Main.window.setResizable(true);
        Main.window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
               if(inGame){
                   Game.setResult(ChessGame.gameID,
                           ChessGame.color?Game.getUser_id2(ChessGame.gameID):
                                   Game.getUser_id1(ChessGame.gameID));
                   User.updateEloByGame(ChessGame.gameID);
               }
            }
        });

        refresh();
        searchFriend = true;
    }

    /**
     * Method for changing the scene when the "New Scene" button is pressed.
     */
    static void newGameButtonPressed(){
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
    }

    /**
     * Method for changing the scene when the "Find User" button is pressed.
     */
    static void findUserButtonPressed(){
        title.setText("Find User");
        mainLayout.add(backToMainButton, 0, 0, 2, 1);
        mainLayout.setHalignment(backToMainButton, HPos.LEFT);
        leftGrid.getChildren().clear();
        rightGrid.getChildren().clear();
        FindUser.showFindUserScene();
    }

    /**
     * Method for changing the scene when the "User Profile" button is pressed.
     */
    static void userProfileButtonPressed(){
        title.setText("User Profile");
        mainLayout.add(backToMainButton, 0, 0, 2, 1);
        mainLayout.setHalignment(backToMainButton, HPos.LEFT);
        leftGrid.getChildren().clear();
        rightGrid.getChildren().clear();
        showUserProfileScene();
    }

    /**
     * Method for changing the scene when the "Leaderboard" button is pressed.
     */
    static void leaderboardButtonPressed(){
        title.setText("Leaderboard");
        mainLayout.add(backToMainButton, 0, 0, 2, 1);
        mainLayout.setHalignment(backToMainButton, HPos.LEFT);
        leftGrid.getChildren().clear();
        rightGrid.getChildren().clear();
        mainLayout.setVgap(70);
        rightGrid.add(LeaderboardFX.setupLeaderboard(), 0,0);
        rightGrid.setPadding(new Insets(150,50,200,180));
        BackgroundImage frame = new BackgroundImage(new Image("Images/frame.png", 700, 600, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        rightGrid.setBackground(new Background(frame));
    }

    /**
     * Method for resetting the sandbox on the main screen.
     */
    static void reloadSandbox(){
        rightGrid.getChildren().clear();
        rightGrid.setPadding(new Insets(40, 150, 20, 0));
        rightGrid.setVgap(20);
        chessGame = new ChessSandbox().createContent();
        rightGrid.add(chessGame, 0, 0);
        rightGrid.add(clearBoard, 0, 1);
        rightGrid.setHalignment(clearBoard, HPos.RIGHT);
        rightGrid.add(sandboxLabel, 0, 1);
    }

    /**
     * Method for creating a new game id when creating a game.
     * @return Finds the largest game id in the database and returns a id one number larger.
     */
    public static int newGameID() {
        DBOps connection = new DBOps();
        ArrayList matchingGameIDs = connection.exQuery("SELECT MAX(game_id) FROM Game;", 1); //Change this SQLQuery to match the database
        if (matchingGameIDs.size() == 0) {
            return 1;
        }
        int out = Integer.parseInt((String) matchingGameIDs.get(0));
        return out + 1;
    }

    /**
     * Starting the service with a period of 3 sec.
     */
    public static void refresh(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                service();
            }
        }, 0, 3000);
    }

    /**
     * Service that is used to find opponents while in queue. Also is used when you are in a game to check whether your opponent has resigned or offered a draw.
     */
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
                                try {
                                    if(inQueueCreate){
                                        if(Game.startGame()){
                                            showGameScene();
                                        }
                                    } else if(inQueueJoin){
                                        if(Game.joinGame()){
                                            showGameScene();
                                        }
                                    }else if (inQueueFriend) {
                                        if(Game.joinFriend()){
                                            showGameScene();
                                        }else if (!Game.getActive(ChessGame.gameID)){
                                            inQueueFriend =false;
                                            showMainScene();
                                        }
                                    } else if(searchFriend) {
                                        int game_id = Game.searchFriend();
                                        if (game_id != -1) {
                                            FriendInviteBox.Display(game_id);
                                        }
                                    }

                                    if (inGame) {
                                        int result = Game.getResult(ChessGame.gameID);
                                        System.out.println("result: "+ result);
                                        if (result != -1) {
                                            int a = ChessGame.color?2:1;
                                            if(result == a){
                                                if(!inDrawOffer) {
                                                    DrawOfferPopupBox.Display();
                                                }
                                            }else if(result == 0){
                                                User.updateEloByGame(ChessGame.gameID);
                                                ChessGame.isDone = true;
                                                inGame = false;
                                                remiOffered = false;
                                                GameOverPopupBox.Display();
                                            } else if (!remiOffered){
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