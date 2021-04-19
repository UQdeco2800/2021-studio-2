package com.deco2800.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.services.ServiceLocator;

public class PerformanceDisplay extends UIComponent {
  private static final float zIndex = 5f;
  private Label profileLabel;
  Table table;

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    table = new Table();
    table.align(Align.topLeft);
    table.padTop(5f).padLeft(5f);

    profileLabel = new Label(getStats(), defaultWhiteText);

    table.add(profileLabel);
    stage.addActor(table);
  }

  @Override
  public void draw(SpriteBatch batch) {
    if (ServiceLocator.getRenderService().getDebug().getActive()) {
      table.setVisible(true);
      profileLabel.setText(getStats());
      int screenHeight = stage.getViewport().getScreenHeight();
      float offsetY = 80f;
      table.setPosition(0, screenHeight - offsetY);
    } else {
      table.setVisible(false);
    }
  }

  private String getStats() {
    String message = "Debug\n";
    message =
        message
            .concat(String.format("FPS: %d fps\n", Gdx.graphics.getFramesPerSecond()))
            .concat(String.format("RAM: %d bytes\n", Gdx.app.getJavaHeap() / 1000000));
    return message;
  }

  @Override
  public float getZIndex() {
    return zIndex;
  }
}
