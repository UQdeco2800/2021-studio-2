package com.deco2800.game.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Core rendering system for the game. Controls the game's camera and runs rendering on all
 * renderables each frame.
 */
public class Renderer implements Disposable {
  private static final Logger logger = LoggerFactory.getLogger(Renderer.class);
  private static final float GAME_SCREEN_WIDTH = 20f;

  private final OrthographicCamera camera;
  private final float gameWidth;
  private final SpriteBatch batch;
  private final Stage stage;
  private final RenderService renderService;
  private final Box2DDebugRenderer debugRenderer;
  // TODO: extract physics rendering somewhere else
  private final PhysicsEngine physicsEngine;

  /**
   * Create a new renderer with default settings
   */
  public Renderer() {
    this(
      new OrthographicCamera(),
      GAME_SCREEN_WIDTH,
      new SpriteBatch(),
      ServiceLocator.getRenderService(),
      ServiceLocator.getPhysicsService(),
      new Box2DDebugRenderer()
    );
  }

  /**
   * Create a renderer
   *
   * @param camera Camera to use for rendering.
   * @param gameWidth Desired game width in metres the screen should show. Height is then based on *
   *     the aspect ratio.
   * @param batch Batch to render to.
   */
  public Renderer(
    OrthographicCamera camera,
    float gameWidth,
    SpriteBatch batch,
    RenderService renderService,
    PhysicsService physicsService,
    Box2DDebugRenderer debugRenderer
  ) {
    this.camera = camera;
    this.gameWidth = gameWidth;
    this.batch = batch;
    this.renderService = renderService;
    this.debugRenderer = debugRenderer;

    stage = new Stage(new ScreenViewport(), batch);
    renderService.setStage(stage);
    camera.position.set(0f, 0f, 0f);
    resizeCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    physicsEngine = physicsService.getPhysics();
  }

  public OrthographicCamera getCamera() {
    return camera;
  }

  /**
   * Render everything to the render service.
   */
  public void render() {
    camera.update();
    batch.setProjectionMatrix(camera.combined);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    batch.begin();
    renderService.render(batch);
    batch.end();
    debugRenderer.render(physicsEngine.getWorld(), camera.combined);
  }

  /**
   * Resize the renderer to a new screen size.
   * @param width new screen width
   * @param height new screen height
   */
  public void resize(int width, int height) {
    resizeCamera(width, height);
    resizeStage(width, height);
  }

  private void resizeCamera(int screenWidth, int screenHeight) {
    float ratio = (float) screenHeight / screenWidth;
    camera.viewportWidth = gameWidth;
    camera.viewportHeight = gameWidth * ratio;
    camera.update();
  }

  private void resizeStage(int screenWidth, int screenHeight) {
    stage.getViewport().update(screenWidth, screenHeight, true);
    logger.info("stage {} gdx {}", stage.getViewport().getScreenHeight(), Gdx.graphics.getHeight());
  }

  @Override
  public void dispose() {
    batch.dispose();
  }
}
