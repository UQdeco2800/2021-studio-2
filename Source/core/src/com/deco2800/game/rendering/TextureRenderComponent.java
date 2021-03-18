package com.deco2800.game.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Render a static texture.
 */
public class TextureRenderComponent extends RenderComponent {
  private final Texture texture;

  /**
   * @param texture Static texture to render. Will be scaled to the entity's scale.
   */
  public TextureRenderComponent(Texture texture) {
    this.texture = texture;
  }

  @Override
  public void draw(SpriteBatch batch) {
    Vector2 position = entity.getPosition();
    Vector2 scale = entity.getScale();
    batch.draw(texture, position.x, position.y, scale.x, scale.y);
  }
}
