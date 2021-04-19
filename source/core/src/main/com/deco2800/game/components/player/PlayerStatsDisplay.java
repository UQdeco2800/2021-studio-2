package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.UI.UIComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * A UI component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table table;
  private Image heartImage;
  private Label healthLabel;

  /**
   * Creates reusable UI styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();

    // TODO when player health is implemented
    // player.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    table = new Table();
    table.align(Align.topLeft);
    table.padTop(5f).padLeft(5f);

    // Heart image
    Float heartSideLength = 30f;
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));

    // Health text
    CharSequence healthText = String.format("Health: %d", 0);
    healthLabel = new Label(healthText, defaultWhiteText);
    healthLabel.setFontScale(1.5f);

    table.add(heartImage).size(heartSideLength).pad(5);
    table.add(healthLabel);
    stage.addActor(table);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    int screenHeight = stage.getViewport().getScreenHeight();
    float offsetY = 40f;

    table.setPosition(0,screenHeight - offsetY);
  }

  /**
   * Updates the player's health on the UI.
   * @param health player health
   */
  public void updatePlayerHealthUI(int health) {
    CharSequence text = String.format("Health: %d", health);
    healthLabel.setText(text);
  }
}
