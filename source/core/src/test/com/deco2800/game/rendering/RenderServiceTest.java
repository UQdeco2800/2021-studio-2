package com.deco2800.game.rendering;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class RenderServiceTest {
  @Test
  void shouldRender() {
    RenderService renderService = new RenderService();
    Renderable renderable = mock(Renderable.class);
    SpriteBatch spriteBatch = mock(SpriteBatch.class);
    renderService.register(renderable);
    renderService.render(spriteBatch);
    verify(renderable).render(spriteBatch);

    renderService.unregister(renderable);
  }

  @Test
  void shouldNotRenderAfterUnregister() {
    RenderService renderService = new RenderService();
    Renderable renderable = mock(Renderable.class);
    SpriteBatch spriteBatch = mock(SpriteBatch.class);
    renderService.register(renderable);
    renderService.unregister(renderable);
    renderService.render(spriteBatch);
    verify(renderable, times(0)).render(any());
  }
}
