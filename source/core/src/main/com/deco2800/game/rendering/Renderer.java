package com.deco2800.game.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.services.ServiceLocator;

/**
 * Core rendering system for the game. Controls the game's camera and runs rendering on all
 * renderables each frame.
 */
public class Renderer implements Disposable {
  private static final float GAME_SCREEN_WIDTH = 10f;

  private final OrthographicCamera camera;
  private final float gameWidth;
  private final SpriteBatch batch;
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
  }

  private void resizeCamera(int screenWidth, int screenHeight) {
    float ratio = (float) screenHeight / screenWidth;
    camera.viewportWidth = gameWidth;
    camera.viewportHeight = gameWidth * ratio;
    camera.update();
  }

  @Override
  public void dispose() {
    batch.dispose();
  }
}
