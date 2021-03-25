package com.deco2800.game.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.components.Component;
import com.deco2800.game.services.ServiceLocator;

/**
 * A generic component for rendering an entity. Registers itself with the render service in order to
 * be rendered each frame. Child classes can implement different kinds of rendering behaviour.
 */
public abstract class RenderComponent extends Component implements Renderable {
  @Override
  public void create() {
    ServiceLocator.getRenderService().register(this);
  }

  @Override
  public void dispose() {
    ServiceLocator.getRenderService().unregister(this);
  }

  @Override
  public void render(SpriteBatch batch) {
    draw(batch);
  }

  /**
   * Draw the renderable. Should be called only by the renderer, not manually.
   * @param batch Batch to render to.
   */
  protected abstract void draw(SpriteBatch batch);
}
