package com.deco2800.game.terminal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.Input;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class KeyboardTerminalInputComponentTest {
  @Test
  void shouldToggleTerminalOpenClose() {
    Terminal terminal = spy(Terminal.class);
    KeyboardTerminalInputComponent terminalInput = new KeyboardTerminalInputComponent(terminal);

    boolean startingIsOpen = terminal.isOpen();

    terminalInput.keyDown(Input.Keys.F1);
    assertNotEquals(startingIsOpen, terminal.isOpen());
    terminalInput.keyDown(Input.Keys.F1);
    assertEquals(startingIsOpen, terminal.isOpen());

    verify(terminal, times(2)).toggleIsOpen();
  }

  @Test
  void shouldUpdateMessageOnKeyTyped() {
    Terminal terminal = mock(Terminal.class);
    when(terminal.isOpen()).thenReturn(true);
    KeyboardTerminalInputComponent terminalInput = new KeyboardTerminalInputComponent(terminal);

    terminalInput.keyTyped('a');
    terminalInput.keyTyped('b');
    verify(terminal).appendToMessage('a');
    verify(terminal).appendToMessage('b');

    terminalInput.keyTyped('\b');
    verify(terminal).handleBackspace();

    terminalInput.keyTyped('\n');
    verify(terminal).processMessage();
  }

  @Test
  void shouldHandleMessageWhenTerminalOpen() {
    Terminal terminal = mock(Terminal.class);
    when(terminal.isOpen()).thenReturn(true);
    KeyboardTerminalInputComponent terminalInput = new KeyboardTerminalInputComponent(terminal);

    assertTrue(terminalInput.keyDown('a'));
    assertTrue(terminalInput.keyUp('a'));

    when(terminal.isOpen()).thenReturn(false);

    assertFalse(terminalInput.keyDown('a'));
    assertFalse(terminalInput.keyUp('a'));
  }
}
