package JavaFX;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import static JavaFX.FindUser.showFindUserScene;
import static JavaFX.Login.AVATAR;
import static JavaFX.Login.runLogin;
import static JavaFX.MainScene.showMainScene;
import static JavaFX.Settings.showSettings;
import static JavaFX.UserProfile.setAvatar;
import static JavaFX.UserProfile.showUserProfileScene;

public class WindowMenuBar {
    MenuBar windowMenuBar;

    public WindowMenuBar(){
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
        MenuItem findUserMenuItem = new MenuItem("Find User");
        findUserMenuItem.setOnAction(e -> showFindUserScene());
        userMenu.getItems().add(findUserMenuItem);
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
        windowMenuBar = new MenuBar();
        windowMenuBar.getMenus().addAll(homeMenu, gameMenu, userMenu, settingsMenu, helpMenu);
    }

    public WindowMenuBar(String dersomHome){
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
        MenuItem findUserMenuItem = new MenuItem("Find User");
        findUserMenuItem.setOnAction(e -> showFindUserScene());
        userMenu.getItems().add(findUserMenuItem);
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
        windowMenuBar = new MenuBar();
        windowMenuBar.getMenus().addAll(gameMenu, userMenu, settingsMenu, helpMenu);
    }

    public MenuBar getWindowMenuBar(){
        return windowMenuBar;
    }
}
