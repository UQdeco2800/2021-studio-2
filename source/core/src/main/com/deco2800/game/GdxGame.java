package com.deco2800.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.deco2800.game.files.UserSettings;
import com.deco2800.game.screens.DeathScreen;
import com.deco2800.game.screens.MainGameScreen;
import com.deco2800.game.screens.MainMenuScreen;
import com.deco2800.game.screens.SettingsScreen;
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
        Gdx.gl.glClearColor(65 / 255f, 62 / 255f, 55 / 255f, 1);

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
            case MAIN_GAME_TEST:
                return new MainGameScreen(this, "test");
            case MAIN_GAME_TUTORIAL:
                return new MainGameScreen(this, "tutorial");
            case SETTINGS:
                return new SettingsScreen(this);
            case DEATHSCREEN:
                return new DeathScreen(this);
            default:
                return null;
        }
    }

    public enum ScreenType {
        MAIN_MENU, MAIN_GAME_FOREST, MAIN_GAME_TEST, MAIN_GAME_TUTORIAL, SETTINGS, DEATHSCREEN
    }

    /**
     * Exit the game.
     */
    public void exit() {
        app.exit();
    }
}
