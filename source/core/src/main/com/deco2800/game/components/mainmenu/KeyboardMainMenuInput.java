package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.Input;
import com.deco2800.game.input.InputComponent;

public class KeyboardMainMenuInput extends InputComponent {

  public KeyboardMainMenuInput() {
    this.setPriority(1);
  }

  @Override
  public boolean keyDown(int keycode) {
    switch (keycode) {
      case Input.Keys.SPACE:
        entity.getEvents().trigger("onStartInput");
        return true;
      case Input.Keys.L:
        entity.getEvents().trigger("load");
        return true;
      case Input.Keys.E:
        entity.getEvents().trigger("exit");
        return true;
      case Input.Keys.S:
        entity.getEvents().trigger("settings");
        return true;
    }
    return false;
  }
}
