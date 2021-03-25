package com.deco2800.game.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Process game physics using the Box2D library. See the Box2D documentation for examples or use
 * cases.
 */
public class PhysicsEngine implements Disposable {
  private static final Logger logger = LoggerFactory.getLogger(PhysicsEngine.class);
  private static final float MAX_UPDATE_TIME = 0.25f;
  private static final float PHYSICS_TIMESTEP = 0.016f;
  private static final Vector2 GRAVITY = new Vector2(0, -9.8f);
  private static final int VELOCITY_ITERATIONS = 6;
  private static final int POSITION_ITERATIONS = 2;

  private final World world;
  private final GameTime timeSource;
  private float accumulator;

  public PhysicsEngine() {
    this(
      new World(GRAVITY, true),
      ServiceLocator.getTimeSource()
    );
  }

  public PhysicsEngine(World world, GameTime timeSource) {
    this.world = world;
    this.timeSource = timeSource;
  }

  public void update() {
    // Updating physics isn't as easy as triggering an update every frame. Each frame could take a
    // different amount of time to run, but physics simulations are only stable if computed at a
    // consistent frame rate! See: https://gafferongames.com/post/fix_your_timestep/
    float deltaTime = timeSource.getDeltaTime();
    float maxTime = Math.min(deltaTime, MAX_UPDATE_TIME);
    accumulator += maxTime;

    // Depending on how much time has passed, we may compute 0 or more physics steps in one go. If
    // we need to catch up, we'll compute multiple in a row before getting to rendering.
    while (accumulator >= PHYSICS_TIMESTEP) {
      world.step(PHYSICS_TIMESTEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
      accumulator -= PHYSICS_TIMESTEP;
    }
  }

  public Body createBody(BodyDef bodyDef) {
    logger.debug("Creating physics body {}", bodyDef);
    return world.createBody(bodyDef);
  }

  public void destroyBody(Body body) {
    logger.debug("Destroying physics body {}", body);
    world.destroyBody(body);
  }

  public Joint createJoint(JointDef jointDef) {
    logger.debug("Creating physics joint {}", jointDef);
    return world.createJoint(jointDef);
  }

  public void destroyJoint(Joint joint) {
    logger.debug("Destroying physics joint {}", joint);
    world.destroyJoint(joint);
  }

  public World getWorld() {
    return world;
  }

  @Override
  public void dispose() {
    world.dispose();
  }
}
