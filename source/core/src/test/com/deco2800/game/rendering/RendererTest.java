package com.deco2800.game.rendering;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class RendererTest {
  @Spy OrthographicCamera camera;
  @Mock SpriteBatch spriteBatch;
  @Mock Graphics graphics;
  @Mock RenderService renderService;
  @Mock Box2DDebugRenderer debugRenderer;
  PhysicsService physicsService;

  @BeforeEach
  void beforeEach() {
    Gdx.graphics = graphics;
    physicsService = new PhysicsService(mock(PhysicsEngine.class));
  }

  @Test
  void shouldResizeCamera() {
    when(graphics.getWidth()).thenReturn(100);
    when(graphics.getHeight()).thenReturn(200);
    Renderer renderer =
        new Renderer(camera, 10, spriteBatch, renderService, physicsService, debugRenderer);

    assertEquals(camera.position, Vector3.Zero);
    assertEquals(10, camera.viewportWidth);
    assertEquals(20, camera.viewportHeight);

    renderer.resize(200, 100);
    assertEquals(10, camera.viewportWidth);
    assertEquals(5, camera.viewportHeight);
  }

  @Test
  void shouldRender() {
    Renderer renderer =
        new Renderer(camera, 10, spriteBatch, renderService, physicsService, debugRenderer);
    renderer.render();
    verify(renderService).render(spriteBatch);
  }
}
