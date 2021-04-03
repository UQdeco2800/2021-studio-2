package com.deco2800.game.ai.movement;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.components.Component;
import com.deco2800.game.math.Vector2Utils;
import com.deco2800.game.physics.PhysicsComponent;

public class PhysicsMovementComponent extends Component {
  private static final float MIN_ACTIVE_FORCE = 0.001f;

  private PhysicsComponent physicsComponent;
  private Vector2 targetPosition;
  private Vector2 maxSpeed = Vector2Utils.One;
  private boolean movementEnabled = true;

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
  }

  @Override
  public void update() {
    if (movementEnabled && targetPosition != null) {
      Body body = physicsComponent.getBody();
      updatePosition(body);
    }
  }

  public void setMoving(boolean movementEnabled) {
    this.movementEnabled = movementEnabled;
  }

  public Vector2 getTarget() {
    return targetPosition;
  }

  public void setTarget(Vector2 target) {
    this.targetPosition = target;
  }

  private void updatePosition(Body body) {
    // Move towards targetPosition based on our current position
    Vector2 direction = targetPosition.cpy().sub(entity.getPosition());
    Vector2 velocity = body.getLinearVelocity();
    Vector2 desiredVelocity = direction.scl(maxSpeed);
    // impulse force = (desired velocity - current velocity) * mass
    Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());

    if (impulse.len2() >= MIN_ACTIVE_FORCE) {
      body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }
  }
}
