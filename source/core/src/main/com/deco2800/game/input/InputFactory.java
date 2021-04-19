package com.deco2800.game.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * InputFactory creates inputType-specific inputFactories which can handle various types of input.
 * Currently only keyboard input and touch is implemented, but InputFactory can be expanded to
 * include more, e.g. touch gestures.
 *
 * <p>Methods to get new input handlers should be defined here.
 */
public abstract class InputFactory {
  private static final Logger logger = LoggerFactory.getLogger(InputFactory.class);

  /** Input device types */
  public enum InputType {
    KEYBOARD, // keyboard and touch
    TOUCH // intended for adding touch gestures
  }

  /**
   * @param inputType the type of input ot be handled by the game
   * @return an InputFactory for the specified input type
   */
  public static InputFactory createFromInputType(InputType inputType) {
    if (inputType == null) {
      logger.error("Null is not a valid input type");
      return null;
    }

    switch (inputType) {
      case KEYBOARD:
        return new KeyboardInputFactory();
      default:
        logger.error("Unrecognised input type: {} ", inputType);
        return null;
    }
  }

  /**
   * Creates an input handler for the player
   *
   * @return Player input handler
   */
  public abstract InputComponent createForPlayer();

  /**
   * Creates an input handler for the terminal
   *
   * @return Terminal input handler
   */
  public abstract InputComponent createForTerminal();
}
