package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.TestComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.physics.PhysicsComponent;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs the main game.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class MainGameScreen extends ScreenAdapter {
  private final static Logger logger = LoggerFactory.getLogger(MainGameScreen.class);
  private final GdxGame game;
  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;

  public MainGameScreen(GdxGame game) {
    this.game = game;

    logger.debug("Initialising main game screen services");
    ServiceLocator.registerTimeSource(new GameTime());

    PhysicsService physicsService = new PhysicsService();
    ServiceLocator.registerPhysicsService(physicsService);
    physicsEngine = physicsService.getPhysics();

    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());
    renderer = new Renderer();

    logger.debug("Initialising main game screen entities");
    Entity defaultSprite =
      new Entity()
        .addComponent(new TextureRenderComponent(new Texture("badlogic.jpg")))
        .addComponent(new PhysicsComponent())
        .addComponent(new TestComponent());

    defaultSprite.getEvents().addListener("created", () -> System.out.println("Created!"));
    defaultSprite.getEvents().addListener("something", (String arg) -> System.out.println(arg));

    ServiceLocator.getEntityService().register(defaultSprite);
  }

  @Override
  public void render(float delta) {
    physicsEngine.update();
    ServiceLocator.getEntityService().update();
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
    logger.debug("Disposing main game screen");
    renderer.dispose();

    ServiceLocator.getEntityService().dispose();
    ServiceLocator.getRenderService().dispose();
  }
}
