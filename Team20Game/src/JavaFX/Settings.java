package JavaFX;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import static JavaFX.Login.*;
import static JavaFX.MainScene.showMainScene;
import static JavaFX.UserProfile.setAvatar;
import static JavaFX.UserProfile.showUserProfileScene;

@SuppressWarnings("Duplicates")
class Settings{
    static Scene settingsScene;

    static void showSettings(){
        GridPane settingsLayout = new GridPane();

        String userTitle = "User Profile";
        Label title = new Label(userTitle);
        title.setFont(Font.font("Copperplate", 40));
        title.setStyle("-fx-font-weight: bold");
        title.setTextFill(Color.WHITE);


        //Buttons
        Button backToMainButton = new Button("Back");
        Image imageBackToMain = new Image("Images/ButtonImages/ArrowLeft.png");
        ImageView imageViewBackToMain = new ImageView(imageBackToMain);
        imageViewBackToMain.setFitWidth(20);
        imageViewBackToMain.setFitHeight(20);
        backToMainButton.setGraphic(imageViewBackToMain);
        backToMainButton.setOnAction(e -> {
            showMainScene();
        });

        //Menubar
        Menu homeMenu = new Menu("Home");
        MenuItem homeMenuItem = new MenuItem("Go to Main Screen");
        homeMenuItem.setOnAction(e -> showMainScene());
        homeMenu.getItems().add(homeMenuItem);

        Menu gameMenu = new Menu("Game");
        MenuItem newGameItem = new MenuItem("New Game");
        newGameItem.setOnAction(e -> System.out.println("Launch new game"));
        MenuItem joinGameItem = new MenuItem("Join Game");
        joinGameItem.setOnAction(e -> System.out.println("Joining game"));
        gameMenu.getItems().addAll(newGameItem, joinGameItem);

        Menu userMenu = new Menu("User");
        MenuItem userProfileMenuItem = new MenuItem("User profile");
        userProfileMenuItem.setOnAction(e -> showUserProfileScene());
        userMenu.getItems().add(userProfileMenuItem);
        MenuItem logOutMenuItem = new MenuItem("Log out");
        logOutMenuItem.setOnAction(e -> {
            setAvatar(AVATAR);
            runLogin();
        });
        userMenu.getItems().add(logOutMenuItem);

        Menu settingsMenu = new Menu("Settings");
        MenuItem openSettings = new MenuItem("Go to settings");
        openSettings.setOnAction(e -> showSettings());
        settingsMenu.getItems().add(openSettings);

        Menu helpMenu = new Menu("Help");
        MenuItem howToLogIn = new MenuItem("How to log in");
        howToLogIn.setOnAction(e -> System.out.println("To log in you have to..."));
        helpMenu.getItems().add(howToLogIn);

        //Mainmenu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(homeMenu, gameMenu, userMenu, settingsMenu, helpMenu);

        //mainlayout
        settingsLayout.setPadding(new Insets(20, 20, 20, 20));
        settingsLayout.add(backToMainButton, 0, 0);


        //Set image as background
        BackgroundImage myBI= new BackgroundImage(new Image("Images/Backgrounds/Mahogny.jpg",550,450,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        settingsLayout.setBackground(new Background(myBI));



        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(settingsLayout);
        settingsScene = new Scene(layout, 550, 450);
        Main.window.setScene(settingsScene);
    }
}

