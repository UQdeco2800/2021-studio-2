package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.movement.PhysicsMovementComponent;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovementTask implements Task {
  private static final Logger logger = LoggerFactory.getLogger(MovementTask.class);
  private Vector2 target;
  private float stopDistance = 0.01f;
  private Entity entity;
  private Status status = Status.Inactive;
  private PhysicsMovementComponent movementComponent;

  public MovementTask(Vector2 target) {
    this.target = target;
  }

  public MovementTask(Vector2 target, float stopDistance) {
    this(target);
    this.stopDistance = stopDistance;
  }

  @Override
  public void start(Entity entity) {
    this.entity = entity;
    this.movementComponent = entity.getComponent(PhysicsMovementComponent.class);
    movementComponent.setTarget(target);
    movementComponent.setMoving(true);
    status = Status.Active;
    logger.debug("Starting movement towards {}", target);
  }

  @Override
  public void update() {
    if (status != Status.Active) {
      return;
    }

    if (entity.getPosition().dst(target) <= stopDistance) {
      movementComponent.setMoving(false);
      status = Status.Finished;
      logger.debug("Finished moving to {}", target);
    }
  }

  public void setTarget(Vector2 target) {
    this.target = target;
    movementComponent.setTarget(target);
  }

  @Override
  public void stop() {
    movementComponent.setMoving(false);
    status = Status.Inactive;
    logger.debug("Stopping movement");
  }

  @Override
  public Status getStatus() {
    return status;
  }
}
