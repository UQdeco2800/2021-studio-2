package com.deco2800.game.components.player;

import com.badlogic.gdx.Input;
import com.deco2800.game.input.InputComponent;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {

  public KeyboardPlayerInputComponent () {
    this.setPriority(5);
  }

   /** Called when a key is pressed and triggers player events on specific keycodes.
   *
   * @param keycode one of the constants in {@link Input.Keys}
   * @return whether the input was processed */
  @Override
  public boolean keyDown(int keycode) {
    switch (keycode) {
      case Input.Keys.A:
        entity.getEvents().trigger("walk", 'u');
        return true;
      case Input.Keys.S:
        entity.getEvents().trigger("attack");
        return true;
    }
    return false;
  }
}
