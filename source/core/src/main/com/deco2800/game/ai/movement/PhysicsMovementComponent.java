package com.deco2800.game.ai.movement;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.components.Component;
import com.deco2800.game.math.Vector2Utils;
import com.deco2800.game.physics.PhysicsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhysicsMovementComponent extends Component {
  private static final Logger logger = LoggerFactory.getLogger(PhysicsMovementComponent.class);

  private PhysicsComponent physicsComponent;
  private Vector2 targetPosition;
  private Vector2 maxSpeed = Vector2Utils.One;
  private boolean movementEnabled = true;
  private boolean isAtTarget = false;

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
    if (!movementEnabled) {
      Body body = physicsComponent.getBody();
      setToVelocity(body, Vector2.Zero);
    }
  }

  public Vector2 getTarget() {
    return targetPosition;
  }

  public void setTarget(Vector2 target) {
    logger.info("Setting target to " + target);
    this.targetPosition = target;
  }

  private void updatePosition(Body body) {
    // Move towards targetPosition based on our current position
    Vector2 direction = targetPosition.cpy().sub(entity.getPosition());
    Vector2 desiredVelocity = direction.setLength(1f).scl(maxSpeed);
    setToVelocity(body, desiredVelocity);
  }

  private void setToVelocity(Body body, Vector2 desiredVelocity) {
    // impulse force = (desired velocity - current velocity) * mass
    Vector2 velocity = body.getLinearVelocity();
    Vector2 impulse = desiredVelocity.cpy().sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }
}
