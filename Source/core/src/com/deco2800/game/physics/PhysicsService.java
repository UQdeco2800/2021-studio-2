package com.deco2800.game.physics;

public class PhysicsService {
  private final com.deco2800.game.physics.PhysicsEngine engine;

  public PhysicsService(com.deco2800.game.physics.PhysicsEngine engine) {
    this.engine = engine;
  }

  public com.deco2800.game.physics.PhysicsEngine getPhysics() {
    return engine;
  }
}
