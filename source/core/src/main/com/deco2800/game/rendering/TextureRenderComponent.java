package com.deco2800.game.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.services.ServiceLocator;

import java.awt.*;

/** Render a static texture. */
public class TextureRenderComponent extends RenderComponent {
  private final Texture texture;

  /**
   * @param texturePath internal path of static teture to render. Will be scaled to the entity's
   *     scale.
   */
  public TextureRenderComponent(String texturePath) {
    this(ServiceLocator.getResourceService().getAsset(texturePath, Texture.class));
  }

  /** @param texture Static texture to render. Will be scaled to the entity's scale. */
  public TextureRenderComponent(Texture texture) {
    this.texture = texture;
  }

  /** Scale the entity to a width of 1 and a height matching the texture's ratio */
  public void scaleEntity() {
    entity.setScale(1f, (float) texture.getHeight() / texture.getWidth());
  }

  @Override
  public void draw(SpriteBatch batch) {
    Vector2 position = entity.getPosition();
    Vector2 scale = entity.getScale();
    if (scale.x == 0.86f && scale.y == 0.22f) {

      float degree = (float) Math.toDegrees(Math.atan2(position.y, position.x));
      batch.draw(new TextureRegion(texture),
              position.x,
              position.y,
              0,
              0,
              scale.x,
              scale.y,
              1,
              1,
              degree);
    } else {
      batch.draw(texture, position.x, position.y, scale.x, scale.y);
    }
  }
}
