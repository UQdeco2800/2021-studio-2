package com.deco2800.game.components.mainmenu;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import com.deco2800.game.files.PlayerSave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class MainMenuActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuActions.class);
    private final GdxGame game;

    public MainMenuActions(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("startTutorial", this::onStartTutorial);
        entity.getEvents().addListener("exit", this::onExit);
        entity.getEvents().addListener("settings", this::onSettings);
        entity.getEvents().addListener("mainMenu", this::onMenu);
    }

    /**
     * Swaps to the Test Main Game screen.
     */
    private void onStartTutorial() {
        logger.info("Start game");
        PlayerSave.load();
        if (PlayerSave.Save.getHasPlayed()) {
            game.setScreen(GdxGame.ScreenType.GAMEAREA1);
        } else {
            game.setScreen(GdxGame.ScreenType.MAIN_GAME_TUTORIAL);
        }
    }

    /**
     * Exits the game.
     */
    private void onExit() {
        logger.info("Exit game");
        game.exit();
    }

    /**
     * Sets the current screen to menu screen
     */
    private void onMenu() {
        logger.info("Exit game");
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }


    /**
     * Swaps to the Settings screen.
     */
    private void onSettings() {
        logger.info("Launching settings screen");
        game.setScreen(GdxGame.ScreenType.SETTINGS);
    }
}
