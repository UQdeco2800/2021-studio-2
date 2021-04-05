package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.physics.PhysicsMovementComponent;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Move to a given position, finishing when you get close enough. Requires an
 * entity with a PhysicsMovementComponent.
 */
public class MovementTask extends DefaultTask {
  private static final Logger logger = LoggerFactory.getLogger(MovementTask.class);
  private Vector2 target;
  private float stopDistance = 0.01f;
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
    super.start(entity);
    this.movementComponent = entity.getComponent(PhysicsMovementComponent.class);
    movementComponent.setTarget(target);
    movementComponent.setMoving(true);
    logger.debug("Starting movement towards {}", target);
  }

  @Override
  public void update() {
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
    super.stop();
    movementComponent.setMoving(false);
    logger.debug("Stopping movement");
  }
}
