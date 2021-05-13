package com.deco2800.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.game.rendering.RenderComponent;
import com.deco2800.game.rendering.Renderable;
import com.deco2800.game.services.ServiceLocator;

/** A generic component for rendering onto the UI. */
public abstract class UIComponent extends RenderComponent implements Renderable {
  private static final int UI_LAYER = 2;
  protected static final Skin skin =
      new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
  protected Stage stage;
  private final BitmapFont font = new BitmapFont();
//  protected Label.LabelStyle defaultWhiteText;

  @Override
  public void create() {
    super.create();
//    createStyles();
    stage = ServiceLocator.getRenderService().getStage();
  }

//  protected void createStyles() {
//    defaultWhiteText = new Label.LabelStyle();
//    defaultWhiteText.font = font;
//    defaultWhiteText.fontColor = Color.WHITE;
//  }

  @Override
  public int getLayer() {
    return UI_LAYER;
  }

  @Override
  public float getZIndex() {
    return 1f;
  }

  public Vector2 getCenteredPosition(Actor actor) {
    int screenWidth = stage.getViewport().getScreenWidth();
    int screenHeight = stage.getViewport().getScreenHeight();
    return new Vector2(
        (screenWidth - actor.getWidth()) / 2, (screenHeight - actor.getHeight()) / 2);
  }
}
