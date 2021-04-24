package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.deco2800.game.UI.UIComponent;

public class MainMenuDisplay extends UIComponent {
  private static final float zIndex = 2f;
  private Label profileLabel;
  private Label instructions;

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    profileLabel = new Label("Box Boy and the Ghosts", skin);
    instructions = new Label("'Space' to start\n'l' to load\n'e' to exit", defaultWhiteText);
    stage.addActor(profileLabel);
    stage.addActor(instructions);
  }

  @Override
  public void draw(SpriteBatch batch) {
    int screenHeight = stage.getViewport().getScreenHeight();
    int screenWidth = stage.getViewport().getScreenWidth();
    profileLabel.setPosition(screenWidth / 2 - profileLabel.getWidth() / 2, screenHeight / 2 + profileLabel.getHeight());
    instructions.setPosition(screenWidth / 2 - instructions.getWidth() / 2, screenHeight / 2 - instructions.getHeight());
  }

  @Override
  public float getZIndex() {
    return zIndex;
  }

  @Override
  public void dispose() {
    super.dispose();
    profileLabel.remove();
    instructions.remove();
  }
}
