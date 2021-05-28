package com.deco2800.game.terminal;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.input.InputComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TouchTerminalInputComponent  extends InputComponent {
  Logger logger = LoggerFactory.getLogger(TouchTerminalInputComponent.class);
  private static final int TOGGLE_OPEN_KEY = Input.Keys.F1;// do for pinch
  private Terminal terminal;

  public TouchTerminalInputComponent() {
    super(10);
  }

  public TouchTerminalInputComponent(Terminal terminal) {
    this();
    this.terminal = terminal;
  }


  @Override
  public void create() {
    super.create();
    terminal = entity.getComponent(Terminal.class);
  }

  @Override
  public boolean keyDown(int keycode) {
    return terminal.isOpen();
  }

  @Override
  public boolean keyTyped(char character) {
    if (!terminal.isOpen()) {
      return false;
    }

    if (character == '\r' || character == '\n') {
      if (terminal.processMessage()) {
        terminal.toggleIsOpen();
      }
      terminal.setEnteredMessage("");
      return true;
    } else if (character == '\b') {
      terminal.handleBackspace();
      return true;
    } else if(Character.isLetterOrDigit(character) || character == ' ') {
      // append character to message
      terminal.appendToMessage(character);
      return true;
    }
    return false;
  }

  @Override
  public boolean scrolled(float amountX, float amountY) {
    if (amountY < 0) {
      terminal.setOpen();
    } else if (amountY > 0) {
      terminal.setClosed();
    }
    return true;
  }
}
