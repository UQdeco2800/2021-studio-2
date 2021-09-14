package com.deco2800.game.components.mainmenu;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class MainMenuActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuActions.class);
    private GdxGame game;

    public MainMenuActions(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("startForest", this::onStartForest);
        entity.getEvents().addListener("startTest", this::onStartTest);
        entity.getEvents().addListener("startTutorial", this::onStartTutorial);
        entity.getEvents().addListener("exit", this::onExit);
        entity.getEvents().addListener("settings", this::onSettings);
    }

    /**
     * Swaps to the Forest Main Game screen.
     */
    private void onStartForest() {
        logger.info("Start game");
        game.setScreen(GdxGame.ScreenType.MAIN_GAME_FOREST);
    }

    /**
     * Swaps to the Test Main Game screen.
     */
    private void onStartTest() {
        logger.info("Start game");
        game.setScreen(GdxGame.ScreenType.MAIN_GAME_TEST);
    }

    /**
     * Swaps to the Test Main Game screen.
     */
    private void onStartTutorial() {
        logger.info("Start game");
        game.setScreen(GdxGame.ScreenType.MAIN_GAME_TUTORIAL);
    }


    /**
     * Intended for loading a saved game state.
     * Load functionality is not actually implemented.
     */
    private void onLoad() {
        logger.info("Load game");
    }

    /**
     * Exits the game.
     */
    private void onExit() {
        logger.info("Exit game");
        game.exit();
    }

    /**
     * Swaps to the Settings screen.
     */
    private void onSettings() {
        logger.info("Launching settings screen");
        game.setScreen(GdxGame.ScreenType.SETTINGS);
    }
}
