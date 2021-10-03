package com.deco2800.game.components.pause;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.components.mainmenu.MainMenuDisplay;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

public class PauseMenuDisplay extends MainMenuDisplay {

    @Override
    public void create() {
        super.create();
        table.clear();
        entity.getEvents().addListener("togglePause", this::togglePauseGame);
    }

    public void togglePauseGame() {
        GameTime timeSource = ServiceLocator.getTimeSource();

        if (!timeSource.isPaused()) {
            timeSource.pause();
        } else {
            timeSource.unpause();
        }
    }
}
