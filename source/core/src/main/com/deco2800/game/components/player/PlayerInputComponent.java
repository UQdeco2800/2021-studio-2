package com.deco2800.game.components.player;

import com.deco2800.game.input.InputComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Input handler for the player. Registers itself with the InputService.
 *
 * <p>Child classes should implement input type specific behaviour. You should only need to edit
 * this class if you are making changes relevant to all player input handlers.
 */
public abstract class PlayerInputComponent extends InputComponent {
  @Override
  public void create() {
    ServiceLocator.getInputService().register(this, 5);
  }
}
