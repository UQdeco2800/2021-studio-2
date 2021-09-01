package com.deco2800.game.components.death;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.mainmenu.MainMenuActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the functionality of the death screen button components
 */
public class DeathActions extends MainMenuActions {

    /**
     * create an instance of this class
     * @param game instance of game
     */
    public DeathActions(GdxGame game) {
        super(game);
    }
}
