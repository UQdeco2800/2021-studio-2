package com.deco2800.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;

public class PlayerStatsDisplay extends UIComponent {
  private static final float zIndex = 2f;

  @Override
  public void draw(SpriteBatch batch)  {
    drawHealth(batch);
  }

  @Override
  public float getZIndex() {
    return zIndex;
  }

  private void drawHealth(SpriteBatch batch) {
    Texture heart = ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class);
    Float height = 0.5f;
    Float width = 0.5f;
    batch.draw(heart, 0, 10f - height, width, height);

    Label label = new Label("Health: 100", skin);
    label.setScale(0.1f);
    label.setPosition(5, 6);
    label.draw(batch, 1.0f);
  }
}
