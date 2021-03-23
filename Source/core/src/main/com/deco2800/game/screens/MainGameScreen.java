package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.deco2800.game.GdxGame;
import com.deco2800.game.ecs.Entity;
import com.deco2800.game.ecs.EntityService;
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
    physicsEngine = PhysicsEngine.createPhysicsEngine();
    ServiceLocator.registerPhysicsService(new PhysicsService(physicsEngine));
    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());
    renderer = Renderer.createRenderer();

    logger.debug("Initialising main game screen entities");
    Entity defaultSprite =
      new Entity()
        .addComponent(new TextureRenderComponent(new Texture("badlogic.jpg")))
        .addComponent(new PhysicsComponent());
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
    logger.debug("Resized renderer: ({} x {})", width, height);
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

    logger.info("Main game screen disposed");
  }
}
