package com.deco2800.game.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Can be rendered onto the screen given a Sprite batch.
 */
public interface Renderable extends Comparable<Renderable> {
  /**
   * Render the renderable. Should be called only by the renderer, not manually.
   * @param batch Batch to render to.
   */
  void render(SpriteBatch batch);

  float getZIndex();

  int getLayer();
}
