package JavaFX;
import Database.Game;
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
            newGameButtonPressed();
            MainScene.reloadSandbox();
        });
        gameMenu.getItems().addAll(newGameItem);

        Menu userMenu = new Menu("User");
        MenuItem userProfileMenuItem = new MenuItem("User profile");
        userProfileMenuItem.setOnAction(e -> userProfileButtonPressed());
        userMenu.getItems().add(userProfileMenuItem);
        MenuItem findUserMenuItem = new MenuItem("Find User");
        findUserMenuItem.setOnAction(e -> findUserButtonPressed());
        userMenu.getItems().add(findUserMenuItem);
        MenuItem logOutMenuItem = new MenuItem("Log out");
        logOutMenuItem.setOnAction(e -> {
            Game.removeActiveFromGame();
            MainScene.searchFriend = false;
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
