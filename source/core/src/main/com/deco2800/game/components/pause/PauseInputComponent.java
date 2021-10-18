package com.deco2800.game.components.pause;

import com.badlogic.gdx.Input;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

public class PauseInputComponent extends InputComponent {

    private final GameTime timeSource = ServiceLocator.getTimeSource();

    public PauseInputComponent() {
        super(5);
    }

    /**
     * Triggers player events on specific keycodes.
     *
     * @return whether the input was processed
     */
    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.ESCAPE) {
            if (timeSource != null) {
                entity.getEvents().trigger("togglePause");
            }
            return true;
        }
        return false;
    }
}
