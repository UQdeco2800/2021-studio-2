package com.deco2800.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.mainmenu.MainMenuActions;
import com.deco2800.game.components.mainmenu.MainMenuDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainMenuScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuScreen.class);
  private final GdxGame game;
  private final Renderer renderer;
  private static final String[] mainMenuTextures = {"images/box_boy_title.png"};

  public MainMenuScreen(GdxGame game) {
    this.game = game;

    // set background to light yellow
    Gdx.gl.glClearColor(248f/255f, 249/255f, 178/255f, 1);

    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());
    ServiceLocator.registerEntityService(new EntityService());

    // for debug renderer
    PhysicsService physicsService = new PhysicsService();
    ServiceLocator.registerPhysicsService(physicsService);

    ServiceLocator.registerRenderService(new RenderService());
    renderer = new Renderer();
    renderer.getCamera().position.set(new Vector3(5f, 5f, 0f));

    loadAssets();
    createUI();
  }

  @Override
  public void render(float delta) {
    renderer.render();
  }

  @Override
  public void resize(int width, int height) {
    renderer.resize(width, height);
    logger.trace("Resized renderer: ({} x {})", width, height);
  }

  @Override
  public void pause() {
    logger.info("Game paused");
  }

  @Override
  public void resume() {
    logger.info("Game resumed");
  }

  @Override
  public void dispose() {
    logger.debug("Disposing main menu screen");

    renderer.dispose();
    unloadAssets();
    ServiceLocator.getRenderService().dispose();
    ServiceLocator.getEntityService().dispose();

    ServiceLocator.clear();
  }

  private void loadAssets() {
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(mainMenuTextures);
    ServiceLocator.getResourceService().loadAll();
  }

  private void unloadAssets() {
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(mainMenuTextures);
  }

  private void createUI() {
    Stage stage = ServiceLocator.getRenderService().getStage();
    Entity UI = new Entity();
    UI.addComponent(new MainMenuDisplay())
        .addComponent(new InputDecorator(stage, 10))
        .addComponent(new MainMenuActions(game));
    ServiceLocator.getEntityService().register(ui);
  }
}
