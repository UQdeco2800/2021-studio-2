package com.deco2800.game.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.services.ServiceLocator;

/**
 * Core rendering system for the game. Controls the game's camera and runs rendering on all
 * renderables each frame.
 */
public class Renderer implements Disposable {
  private static final float GAME_SCREEN_WIDTH = 10f;

  private final Camera camera;
  private final float gameWidth;
  private final SpriteBatch batch;
  private final RenderService renderService;

  /**
   * Create a new renderer with default settings
   * @return A new renderer.
   */
  public static Renderer createRenderer() {
    return new Renderer(new OrthographicCamera(), GAME_SCREEN_WIDTH, new SpriteBatch());
  }

  /**
   * Create a renderer
   *
   * @param camera Camera to use for rendering.
   * @param gameWidth Desired game width in metres the screen should show. Height is then based on *
   *     the aspect ratio.
   * @param batch Batch to render to.
   */
  public Renderer(Camera camera, float gameWidth, SpriteBatch batch) {
    this.camera = camera;
    this.gameWidth = gameWidth;
    this.batch = batch;
    this.renderService = ServiceLocator.getRenderService();

    camera.position.set(0f, 0f, 0f);
    resizeCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
