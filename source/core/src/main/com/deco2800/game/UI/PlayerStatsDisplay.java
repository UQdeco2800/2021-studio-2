package com.deco2800.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.services.ServiceLocator;

public class PlayerStatsDisplay extends UIComponent {
  private static final float zIndex = 5f;
  private final BitmapFont font = new BitmapFont();
  Table table;
  private Image heart;
  private Label health;
  private Label.LabelStyle defaultWhiteText;

  @Override
  public void create() {
    super.create();
    createStyles();

    addActors();
  }

  private void createStyles() {
    defaultWhiteText = new Label.LabelStyle();
    defaultWhiteText.font = font;
    defaultWhiteText.fontColor = Color.WHITE;
  }

  private void addActors() {
    table = new Table();
    table.align(Align.topLeft);
    table.padTop(5f).padLeft(5f);

    // Heart image
    Float heartSideLength = 30f;
    heart = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));

    // Health text
    CharSequence healthText = String.format("Health: %d", getPlayerHealth());
    health = new Label(healthText, defaultWhiteText);
    health.setFontScale(1.5f);

    table.add(heart).size(heartSideLength).pad(5);
    table.add(health);
    stage.addActor(table);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    int screenHeight = stage.getViewport().getScreenHeight();
    float offsetY = 40f;

    table.setPosition(0,screenHeight - offsetY);
  }

  @Override
  public float getZIndex() {
    return zIndex;
  }

  private int getPlayerHealth() {
    return 50;
  }
}
