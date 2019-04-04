/*
* This class was meant to be used as a windowbar, but due to the optimalization of scene-changes we
* had problems of using this, and will therefore not use it. We wanted to just swap out nodes/components of left/right-
* gridPanes in mainScene, instead of switching scenes, as this takes longer time to perform. 
 */

package JavaFX;
import Database.Game;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import static JavaFX.Login.*;
import static JavaFX.MainScene.*;
import static JavaFX.Settings.showSettings;



public class WindowMenuBar {
    MenuBar windowMenuBar;

    public WindowMenuBar(){
        Menu gameMenu = new Menu("Game");
        MenuItem newGameItem = new MenuItem("New Game");
        newGameItem.setOnAction(e -> {
            MainScene.leftGrid.getChildren().clear();
            MainScene.rightGrid.getChildren().clear();
            newGameButtonPressed();
            MainScene.reloadSandbox();
        });
        gameMenu.getItems().addAll(newGameItem);

        Menu userMenu = new Menu("User");
        MenuItem userProfileMenuItem = new MenuItem("User profile");
        userProfileMenuItem.setOnAction(e -> {
            MainScene.leftGrid.getChildren().clear();
            MainScene.rightGrid.getChildren().clear();
            userProfileButtonPressed();
        });
        userMenu.getItems().add(userProfileMenuItem);
        MenuItem findUserMenuItem = new MenuItem("Find User");
        findUserMenuItem.setOnAction(e -> {
            MainScene.leftGrid.getChildren().clear();
            MainScene.rightGrid.getChildren().clear();
            findUserButtonPressed();
        });
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
