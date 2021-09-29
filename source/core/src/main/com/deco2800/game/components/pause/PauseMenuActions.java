package com.deco2800.game.components.pause;


import com.deco2800.game.GdxGame;
import com.deco2800.game.components.mainmenu.MainMenuActions;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;


public class PauseMenuActions extends MainMenuActions {
    public PauseMenuActions(GdxGame game) {
        super(game);
    }

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("togglePause", this::togglePauseGame);
    }

    public void togglePauseGame() {
        GameTime timeSource = ServiceLocator.getTimeSource();

        if (!timeSource.isPaused()) {
            timeSource.pause();
        } else {
            timeSource.unpause();
        }
        entity.getEvents().trigger("toggleMenu");
    }

}
