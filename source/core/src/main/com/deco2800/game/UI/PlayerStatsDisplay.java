package com.deco2800.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;

public class PlayerStatsDisplay extends UIComponent {
  private static final float zIndex = 5f;
  private final BitmapFont font = new BitmapFont();
  private Image heart;
  private Label label;
  private Label label1;

  @Override
  public void create() {
    super.create();
    drawHealth();
  }

  private void drawHealth() {
    heart = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));
    Float height = 50f;
    Float width = 50f;
    heart.setPosition(0, 0); // bottom-left corner
    heart.setSize(width, height);
    stage.addActor(heart);

    label = new Label("Health: 100", skin);
    label.setFontScale(1f);
    label.setPosition(0f, 0f); // bottom-left corner
    stage.addActor(label);

    Label.LabelStyle labelStyle = new Label.LabelStyle();
    labelStyle.font = font;
    labelStyle.fontColor = Color.WHITE;

    label1 = new Label("Title (BitmapFont)", labelStyle);
    label1.setPosition(0f, stage.getViewport().getScreenHeight() - 50f);
    label1.setFontScale(0.5f);
    stage.addActor(label1);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    // dynamic position
    label1.setPosition(0f, stage.getViewport().getScreenHeight() - 50f);
  }

  @Override
  public float getZIndex() {
    return zIndex;
  }
}
