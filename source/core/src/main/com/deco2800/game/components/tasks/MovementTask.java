package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.movement.PhysicsMovementComponent;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.entities.Entity;

public class MovementTask implements Task {
  private final Vector2 target;
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
    status = Status.Active;
  }

  @Override
  public void update() {
    if (status != Status.Active) {
      return;
    }

    if (entity.getPosition().dst(target) <= stopDistance) {
      movementComponent.setMoving(false);
      status = Status.Finished;
    }
  }

  @Override
  public void stop() {
    movementComponent.setMoving(false);
    status = Status.Inactive;
  }

  @Override
  public Status getStatus() {
    return status;
  }
}
