package com.deco2800.game.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;

public class PhysicsEngine implements Disposable {
  private static final int MAX_UPDATE_TIME = 250;
  private static final int PHYSICS_TIMESTEP = 16;
  private static final Vector2 GRAVITY = new Vector2(0, -9.8f);
  private static final int VELOCITY_ITERATIONS = 6;
  private static final int POSITION_ITERATIONS = 2;

  private final World world;
  private long lastUpdateTime;
  private int accumulator;

  public static PhysicsEngine createPhysicsEngine() {
    return new PhysicsEngine(new World(GRAVITY, true));
  }

  public PhysicsEngine(
      World world
  ) {
    this.world = world;
    lastUpdateTime = TimeUtils.millis();
  }


  public void update() {
    long deltaTime = TimeUtils.timeSinceMillis(lastUpdateTime);
    int maxTime = (int)Math.min(deltaTime, MAX_UPDATE_TIME);
    accumulator += maxTime;

    while (accumulator >= PHYSICS_TIMESTEP) {
      world.step(PHYSICS_TIMESTEP / 1000f, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
      accumulator -= PHYSICS_TIMESTEP;
    }
    lastUpdateTime = TimeUtils.millis();
  }

  public Body createBody(BodyDef bodyDef) {
    return world.createBody(bodyDef);
  }

  public void destroyBody(Body body) {
    world.destroyBody(body);
  }

  public Joint createJoint(JointDef jointDef) {
    return world.createJoint(jointDef);
  }

  public void destroyJoint(Joint joint) {
    world.destroyJoint(joint);
  }

  @Override
  public void dispose() {
    world.dispose();
  }
}
