package com.deco2800.game.terminal;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.deco2800.game.input.InputComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Input handler for the debug terminal for keyboard and touch (mouse) input.
 * This input handler uses keyboard and touch input.
 *
 * <p>The debug terminal can be opened and closed by scrolling vertically and a message can be entered via
 * the keyboard.
 */
public class TouchTerminalInputComponent extends InputComponent {
  Logger logger = LoggerFactory.getLogger(TouchTerminalInputComponent.class);
  private static final int TOGGLE_OPEN_KEY = Input.Keys.F1;
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

  /**
   * Handles input if the terminal is open. This is because keyDown events are
   * triggered alongside keyTyped events. If the user is typing in the terminal, the input shouldn't
   * trigger any other input handlers.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyDown(int)
   */
  @Override
  public boolean keyDown(int keycode) {
    return terminal.isOpen();
  }

  /**
   * Handles input if the terminal is open. If 'enter' is typed, the entered message will be
   * processed, otherwise the message will be updated with the new character.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyTyped(char)
   */
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

  /**
   * Scrolling up will open the terminal and scrolling down will close the terminal.
   *
   * @return whether the input was processed
   * @see InputProcessor#scrolled(float, float)
   */
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
