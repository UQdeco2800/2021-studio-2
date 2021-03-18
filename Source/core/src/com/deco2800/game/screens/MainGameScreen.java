package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.deco2800.game.GdxGame;
import com.deco2800.game.ecs.Entity;
import com.deco2800.game.ecs.EntityService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Runs the main game.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class MainGameScreen extends ScreenAdapter {
  private final GdxGame game;
  private final Renderer renderer;

  public MainGameScreen(GdxGame game) {
    this.game = game;
    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());
    renderer = Renderer.createRenderer();

    Entity defaultSprite =
        new Entity().addComponent(new TextureRenderComponent(new Texture("badlogic.jpg")));
    ServiceLocator.getEntityService().register(defaultSprite);
  }

  @Override
  public void render(float delta) {
    ServiceLocator.getEntityService().update();
    renderer.render();
  }

  @Override
  public void resize(int width, int height) {
    renderer.resize(width, height);
  }

  @Override
  public void pause() {}

  @Override
  public void resume() {}

  @Override
  public void dispose() {
    renderer.dispose();

    ServiceLocator.getEntityService().dispose();
    ServiceLocator.getRenderService().dispose();
  }
}
