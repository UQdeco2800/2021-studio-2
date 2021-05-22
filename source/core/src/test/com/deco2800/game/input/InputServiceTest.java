package com.deco2800.game.input;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class InputServiceTest {

  @Test
  void shouldRegisterInputHandler() {
    int keycode = 1;
    InputComponent inputComponent = spy(InputComponent.class);
    when(inputComponent.getPriority()).thenReturn(1);

    InputService inputService = new InputService();
    inputService.register(inputComponent);

    inputService.keyDown(keycode);
    verify(inputComponent).keyDown(keycode);
  }

  @Test
  void shouldUnregisterInputHandler() {
    int keycode = 1;
    InputComponent inputComponent = spy(InputComponent.class);
    when(inputComponent.getPriority()).thenReturn(1);

    InputService inputService = new InputService();
    inputService.register(inputComponent);
    inputService.unregister(inputComponent);

    inputService.keyDown(keycode);
    verify(inputComponent, times(0)).keyDown(keycode);
  }

  @Test
  void shouldCallInputHandlersInPriorityOrder() {
    int keycode = 1;
    InputComponent inputComponent1 = spy(InputComponent.class);
    InputComponent inputComponent2 = spy(InputComponent.class);

    when(inputComponent1.getPriority()).thenReturn(1);
    when(inputComponent2.getPriority()).thenReturn(10);

    when(inputComponent1.keyDown(keycode)).thenReturn(true);
    when(inputComponent2.keyDown(keycode)).thenReturn(true);

    InputService inputService = new InputService();
    inputService.register(inputComponent1);
    inputService.register(inputComponent2);

    inputService.keyDown(keycode);
    verify(inputComponent1, times(0)).keyDown(keycode);
    verify(inputComponent2, times(1)).keyDown(keycode);
  }


  @Test
  void shouldStopIteratingAfterInputHandled() {
    int keycode = 1;
    InputComponent inputComponent1 = spy(InputComponent.class);
    InputComponent inputComponent2 = spy(InputComponent.class);
    InputComponent inputComponent3 = spy(InputComponent.class);

    when(inputComponent1.getPriority()).thenReturn(100);
    when(inputComponent2.getPriority()).thenReturn(10);
    when(inputComponent3.getPriority()).thenReturn(1);

    when(inputComponent1.keyDown(keycode)).thenReturn(false);
    when(inputComponent2.keyDown(keycode)).thenReturn(true);

    InputService inputService = new InputService();
    inputService.register(inputComponent1);
    inputService.register(inputComponent3);
    inputService.register(inputComponent2);

    inputService.keyDown(keycode);
    verify(inputComponent1, times(1)).keyDown(keycode);
    verify(inputComponent2, times(1)).keyDown(keycode);
    verify(inputComponent3, times(0)).keyDown(keycode);
  }
}
