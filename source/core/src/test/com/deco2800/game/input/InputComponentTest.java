package com.deco2800.game.input;

import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
class InputComponentTest {
  @Test
  void shouldUpdatePriority() {
    int newPriority = 100;
    InputComponent inputComponent = spy(InputComponent.class);

    inputComponent.setPriority(newPriority);
    verify(inputComponent).setPriority(newPriority);

    int priority = inputComponent.getPriority();
    verify(inputComponent).getPriority();

    assertEquals(newPriority, priority);
  }

  @Test
  void shouldRegisterOnCreate() {
    InputService inputService = spy(InputService.class);
    ServiceLocator.registerInputService(inputService);

    InputComponent inputComponent = spy(InputComponent.class);
    inputComponent.create();
    verify(inputService).register(inputComponent);
  }
}
