package com.deco2800.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.deco2800.game.rendering.RenderComponent;
import com.deco2800.game.rendering.Renderable;
import com.deco2800.game.services.ServiceLocator;

public abstract class UIComponent extends RenderComponent implements Renderable {
  private static final int UI_LAYER = 2;
  protected static final Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
  protected Stage stage;

  @Override
  public void create() {
    super.create();
    stage = ServiceLocator.getRenderService().getStage();
  }

  @Override
  public void render(SpriteBatch batch) {
    super.render(batch);
    batch.end();
    stage.act();
    stage.draw();
    batch.begin();
  }


  @Override
  public int compareTo(Renderable o) {
    return Float.compare(getZIndex(), o.getZIndex());
  }

  @Override
  public int getLayer() {
    return UI_LAYER;
  }
}
