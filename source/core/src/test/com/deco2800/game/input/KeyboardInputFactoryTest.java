package com.deco2800.game.input;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.deco2800.game.components.player.KeyboardPlayerInputComponent;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class KeyboardInputFactoryTest {
  @Test
  void shouldReturnKeyboardPlayerInput() {
    KeyboardInputFactory keyboardInputFactory = new KeyboardInputFactory();
    assertTrue(keyboardInputFactory.createForPlayer() instanceof KeyboardPlayerInputComponent);
  }
}
