package com.deco2800.game.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * Globally accessible service for registering renderable components. Any renderable registered with
 * this service has render() called once per frame.
 */
public class RenderService implements Disposable {
  private static final int INITIAL_CAPACITY = 16;

  private final Array<Renderable> renderables = new Array<>(false, INITIAL_CAPACITY);

  /**
   * Register a new renderable.
   * @param renderable new renderable.
   */
  public void register(Renderable renderable) {
    renderables.add(renderable);
  }

  /**
   * Unregister a renderable.
   * @param renderable renderable to unregister.
   */
  public void unregister(Renderable renderable) {
    renderables.removeValue(renderable, true);
  }

  /**
   * Trigger rendering on the given batch. This should be called only from the main renderer.
   * @param batch batch to render to.
   */
  public void render(SpriteBatch batch) {
    for (Renderable renderable : renderables) {
      renderable.render(batch);
    }
  }

  @Override
  public void dispose() {
    renderables.clear();
  }
}
