package com.deco2800.game.input;

import com.deco2800.game.components.player.PlayerInputComponent;

/**
 * InputFactory creates inputType-specific inputFactories which can handle various types of input.
 * Currently only keyboard input and touch is implemented, but InputFactory can be expanded to
 * include more, e.g. touch gestures.
 *
 * <p>Methods to get new input handlers should be defined here.
 */
public abstract class InputFactory {
  /** Input device types */
  public enum InputType {
    KEYBOARD, // keyboard and touch
    TOUCH
  }

  /**
   * @param inputType the type of input ot be handled by the game
   * @return an InputFactory for the specified input type
   */
  public static InputFactory createFromInputType(InputType inputType) {
    switch (inputType) {
      case KEYBOARD:
        return new KeyboardInputFactory();
      case TOUCH:
        throw new RuntimeException("Touch with gesture input has not been implemented.");
      default:
        throw new RuntimeException(String.format("Unrecognised input type: %s ", inputType));
    }
  }

  /**
   * Creates an input handler for the player
   *
   * @return Player input handler
   */
  public abstract PlayerInputComponent createForPlayer();
}
