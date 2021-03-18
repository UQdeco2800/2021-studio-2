package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
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
import com.deco2800.game.services.ServiceLocator;

/**
 * Runs the main game.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class MainGameScreen extends ScreenAdapter {
  private final GdxGame game;
  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;

  public MainGameScreen(GdxGame game) {
    this.game = game;
    physicsEngine = PhysicsEngine.createPhysicsEngine();
    ServiceLocator.registerPhysicsService(new PhysicsService(physicsEngine));
    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());
    renderer = Renderer.createRenderer();

    PhysicsComponent physicsComponent =
        new PhysicsComponent(BodyType.DynamicBody, new Vector2(1f, 1f));
    Shape shape = new CircleShape();
    shape.setRadius(5f);
    physicsComponent.setShape(shape);

    Entity defaultSprite =
        new Entity()
            .addComponent(new TextureRenderComponent(new Texture("badlogic.jpg")))
            .addComponent(physicsComponent);
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
