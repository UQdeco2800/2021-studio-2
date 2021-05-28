package com.deco2800.game.input;

import com.deco2800.game.components.player.TouchPlayerInputComponent;
import com.deco2800.game.terminal.TouchTerminalInputComponent;

public class TouchInputFactory extends InputFactory{
  /**
   * Creates an input handler for the player
   *
   * @return Player input handler
   */
  @Override
  public InputComponent createForPlayer() {
    return new TouchPlayerInputComponent();
  }

  /**
   * Creates an input handler for the terminal
   *
   * @return Terminal input handler
   */
  public InputComponent createForTerminal() {
    return new TouchTerminalInputComponent();
  }
}
