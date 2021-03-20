package com.deco2800.game.rendering;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class RenderComponentTest {
  @Mock
  RenderService service;

  @Test
  void shouldRegisterSelf() {
    ServiceLocator.registerRenderService(service);
    RenderComponent component = spy(RenderComponent.class);
    component.create();
    verify(service).register(component);
  }

  @Test
  void shouldUnregisterOnDispose() {
    ServiceLocator.registerRenderService(service);
    RenderComponent component = spy(RenderComponent.class);
    component.create();
    component.dispose();
    verify(service).unregister(component);
  }

  @Test
  void shouldDrawOnRender() {
    RenderComponent component = spy(RenderComponent.class);
    component.render(null);
    verify(component).draw(any());
  }
}