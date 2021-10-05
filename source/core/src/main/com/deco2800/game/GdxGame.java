package com.deco2800.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.deco2800.game.files.UserSettings;
import com.deco2800.game.screens.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.badlogic.gdx.Gdx.app;

/**
 * Entry point of the non-platform-specific game logic. Controls which screen is currently running.
 * The current screen triggers transitions to other screens. This works similarly to a finite state
 * machine (See the State Pattern).
 */
public class GdxGame extends Game {
    private static final Logger logger = LoggerFactory.getLogger(GdxGame.class);

    @Override
    public void create() {
        logger.info("Creating game");
        loadSettings();

        // Sets background to light yellow
        Gdx.gl.glClearColor(49 / 255f, 49 / 255f, 49 / 255f, 1);

        setScreen(ScreenType.MAIN_MENU);
    }

    /**
     * Loads the game's settings.
     */
    private void loadSettings() {
        logger.debug("Loading game settings");
        UserSettings.Settings settings = UserSettings.get();
        UserSettings.applySettings(settings);
    }

    /**
     * Sets the game's screen to a new screen of the provided type.
     *
     * @param screenType screen type
     */
    public void setScreen(ScreenType screenType) {
        logger.info("Setting game screen to {}", screenType);
        Screen currentScreen = getScreen();
        if (currentScreen != null) {
            currentScreen.dispose();
        }
        setScreen(newScreen(screenType));
    }

    /**
     * Use for teleport, track the current player health
     */
    public void setScreen(ScreenType screenType, int currentHealth) {
        logger.info("Setting game screen to {}", screenType);
        Screen currentScreen = getScreen();
        if (currentScreen != null) {
            currentScreen.dispose();
        }
        setScreen(newScreen(screenType, currentHealth));
    }

    @Override
    public void dispose() {
        logger.debug("Disposing of current screen");
        getScreen().dispose();
    }

    /**
     * Create a new screen of the provided type.
     *
     * @param screenType screen type
     * @return new screen
     */
    private Screen newScreen(ScreenType screenType) {
        switch (screenType) {
            case MAIN_MENU:
                return new MainMenuScreen(this);
            case MAIN_GAME_FOREST:
                return new MainGameScreen(this, "forest");
            case MAIN_GAME_TUTORIAL:
                return new MainGameScreen(this, "tutorial");
            case SETTINGS:
                return new SettingsScreen(this);
            case DEATHSCREEN:
                return new DeathScreen(this);
            case END_SCREEN:
                return new EndScreen(this);
            case GAMEAREA0:
                return new MainGameScreen(this, "game0");
            case GAMEAREA1:
                return new MainGameScreen(this, "game1");
            case GAMEAREA2:
                return new MainGameScreen(this, "game2");
            case GAMEAREA3:
                return new MainGameScreen(this, "game3");
            case GAMEAREA4:
                return new MainGameScreen(this, "game4");
            default:
                return null;
        }
    }

    /**
     * Use for teleport, track the current player health
     */
    private Screen newScreen(ScreenType screenType, int currentHealth) {
        switch (screenType) {
            case MAIN_MENU:
                return new MainMenuScreen(this);
            case MAIN_GAME_FOREST:
                return new MainGameScreen(this, "forest", currentHealth);
            case MAIN_GAME_TUTORIAL:
                return new MainGameScreen(this, "tutorial", currentHealth);
            case SETTINGS:
                return new SettingsScreen(this);
            case DEATHSCREEN:
                return new DeathScreen(this);
            case TEST1:
                return new MainGameScreen(this, "test1", currentHealth);
            case GAMEAREA0:
                return new MainGameScreen(this, "game0", currentHealth);
            case GAMEAREA1:
                return new MainGameScreen(this, "game1", currentHealth);
            case GAMEAREA2:
                return new MainGameScreen(this, "game2", currentHealth);
            case GAMEAREA3:
                return new MainGameScreen(this, "game3", currentHealth);
            case GAMEAREA4:
                return new MainGameScreen(this, "game4", currentHealth);
            default:
                return null;
        }
    }

    public enum ScreenType {
        MAIN_MENU, MAIN_GAME_FOREST, MAIN_GAME_TUTORIAL, SETTINGS, DEATHSCREEN, TEST1, GAMEAREA0, GAMEAREA1,
        GAMEAREA2, GAMEAREA3, GAMEAREA4, TEST2, END_SCREEN
    }

    /**
     * Exit the game.
     */
    public void exit() {
        app.exit();
    }
}
