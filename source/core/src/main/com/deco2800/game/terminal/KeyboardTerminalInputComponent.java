package com.deco2800.game.terminal;

import com.badlogic.gdx.Input;
import com.deco2800.game.input.InputComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyboardTerminalInputComponent extends InputComponent {
  private static final Logger logger = LoggerFactory.getLogger(KeyboardTerminalInputComponent.class);
  private Terminal terminal;

  public KeyboardTerminalInputComponent() {
    this.setPriority(3);
  }

  @Override
  public void create() {
    super.create();
    terminal = entity.getComponent(Terminal.class);
  }

  @Override
  public boolean keyDown(int keycode) {
    // handle open and close terminal
    if (keycode == Input.Keys.F1) {
        terminal.toggleIsOpen();
        return true;
    }

    // prevent lower priority input handlers triggering if terminal is open
    return terminal.isOpen();
  }

  @Override
  public boolean keyTyped(char character) {
    if (!terminal.isOpen()) {
      return false;
    }

    if (character == '\r' || character == '\n') {
      terminal.processMessage();
    } else if (character == '\b') {
      terminal.handleBackspace();
    } else {
      // append character to message
      terminal.appendToMessage(character);
    }
    return true;
  }

  @Override
  public boolean keyUp(int keycode) {
    // prevent lower priority input handlers triggering if terminal is open
    return terminal.isOpen();
  }
}
