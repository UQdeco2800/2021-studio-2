package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.settingsmenu.SettingsMenuDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;

/**
 * The game screen containing the settings.
 */
public class SettingsScreen extends ScreenAdapter {
  private final GdxGame game;
  private final Renderer renderer;

  public SettingsScreen(GdxGame game) {
    this.game = game;

    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());
    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());
    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(5f, 5f);

    createUI();
  }

  @Override
  public void render(float delta) {
    renderer.render();
  }

  @Override
  public void resize(int width, int height) {
    renderer.resize(width, height);
  }

  @Override
  public void dispose() {
    renderer.dispose();
    ServiceLocator.getRenderService().dispose();
    ServiceLocator.getEntityService().dispose();

    ServiceLocator.clear();
  }

  private void createUI() {
    Stage stage = ServiceLocator.getRenderService().getStage();
    Entity UI = new Entity();
    UI.addComponent(new SettingsMenuDisplay(game))
        .addComponent(new InputDecorator(stage, 10));
    ServiceLocator.getEntityService().register(UI);
  }
}
