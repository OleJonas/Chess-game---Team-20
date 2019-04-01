package JavaFX;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import java.util.Random;
import static JavaFX.FindUser.showFindUserScene;
import static JavaFX.Login.*;
import static JavaFX.MainScene.*;
import static JavaFX.Settings.showSettings;
import static JavaFX.UserProfile.setAvatar;
import static JavaFX.UserProfile.showUserProfileScene;

public class WindowMenuBar {
    MenuBar windowMenuBar;

    public WindowMenuBar(){
        Menu gameMenu = new Menu("Game");
        MenuItem newGameItem = new MenuItem("New Game");
        newGameItem.setOnAction(e -> {
            leftGrid.getChildren().clear();
            leftGrid.setVgap(40);
            leftGrid.setPadding(new Insets(150, 100, 100, 170));
            createGameButton.setPrefSize(150, 80);
            joinGameButton.setPrefSize(150, 80);
            inviteFriendButton.setPrefSize(150, 80);
            cancelGameButton.setPrefSize(150, 80);
            leftGrid.add(createGameButton, 0,0);
            leftGrid.setHalignment(createGameButton, HPos.CENTER);
            leftGrid.add(joinGameButton, 0, 1);
            leftGrid.setHalignment(joinGameButton, HPos.CENTER);
            leftGrid.add(inviteFriendButton, 0, 2);
            leftGrid.setHalignment(inviteFriendButton, HPos.CENTER);
            leftGrid.add(cancelGameButton, 0, 3);
            leftGrid.setHalignment(cancelGameButton, HPos.CENTER);
            showMainScene();
        });
        gameMenu.getItems().addAll(newGameItem);

        Menu userMenu = new Menu("User");
        MenuItem userProfileMenuItem = new MenuItem("User profile");
        userProfileMenuItem.setOnAction(e -> showUserProfileScene());
        userMenu.getItems().add(userProfileMenuItem);
        MenuItem findUserMenuItem = new MenuItem("Find User");
        findUserMenuItem.setOnAction(e -> showFindUserScene());
        userMenu.getItems().add(findUserMenuItem);
        MenuItem logOutMenuItem = new MenuItem("Log out");
        logOutMenuItem.setOnAction(e -> {
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
        windowMenuBar = new MenuBar();
        windowMenuBar.getMenus().addAll(gameMenu, userMenu, settingsMenu, helpMenu);
    }

    public MenuBar getWindowMenuBar(){
        return windowMenuBar;
    }
}
