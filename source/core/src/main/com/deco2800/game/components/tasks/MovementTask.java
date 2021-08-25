package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent.
 */
public class MovementTask extends DefaultTask {
  private static final Logger logger = LoggerFactory.getLogger(MovementTask.class);

  private final GameTime gameTime;
  protected Vector2 target;
  private float stopDistance = 0.01f;
  private Vector2 moveSpeed = Vector2Utils.ONE;
  private long lastTimeMoved;
  private Vector2 lastPos;
  private PhysicsMovementComponent movementComponent;
  private long timeDiscoveredTarget;

  public MovementTask(Vector2 target) {
    this.target = target;
    this.gameTime = ServiceLocator.getTimeSource();
  }

  public MovementTask(Vector2 target, long time) {
    this(target);
    this.timeDiscoveredTarget = time;
  }

  public MovementTask(Vector2 target, float stopDistance) {
    this(target);
    this.stopDistance = stopDistance;
  }

  public MovementTask(Vector2 target, Vector2 moveSpeed) {
    this(target);
    this.moveSpeed = moveSpeed;
    stopDistance = stopDistance * Math.max(moveSpeed.x, moveSpeed.y);
  }

  @Override
  public void start() {
    super.start();
    this.movementComponent = owner.getEntity().getComponent(PhysicsMovementComponent.class);
    movementComponent.setTarget(target);
    movementComponent.setMaxSpeed(moveSpeed);
    movementComponent.setMoving(true);
    logger.debug("Starting movement towards {}", target);
    lastTimeMoved = gameTime.getTime();
    lastPos = owner.getEntity().getPosition();
  }

  @Override
  public void update() {
    if (isAtTarget()) {
      movementComponent.setMoving(false);
      status = Status.FINISHED;
      logger.debug("Finished moving to {}", target);
    } else {
      checkIfStuck();
      movementComponent.setMaxSpeed(moveSpeed);
    }
  }

  public void setTarget(Vector2 target) {
    this.target = target;
    movementComponent.setTarget(target);
  }


  public void setMoveSpeed(Vector2 moveSpeed) {
    this.moveSpeed = moveSpeed;
  }

  @Override
  public void stop() {
    super.stop();
    movementComponent.setMoving(false);
    logger.debug("Stopping movement");
  }

  protected boolean isAtTarget() {
    return owner.getEntity().getPosition().dst(target) <= stopDistance;
  }

  protected boolean checkIfStuck() {
    if (didMove()) {
      lastTimeMoved = gameTime.getTime();
      lastPos = owner.getEntity().getPosition();
    } else if (gameTime.getTimeSince(lastTimeMoved) > 500L) {
      movementComponent.setMoving(false);
      status = Status.FAILED;
      logger.debug("Got stuck! Failing movement task");
      return (true);
    }
    return (false);
  }

  protected boolean didMove() {
    if (this.getStatus() == Status.ACTIVE) {
      return owner.getEntity().getPosition().dst2(lastPos) > 0.001f;
    }
    return true; // if not started
  }

  /**
   * if time since target is discovered is more than 3 seconds and owner is still chasing target
   * it will start warning other enemies to attack player
   * @return true if enemies chase after target for more than 3 seconds
   */
  protected boolean discover() {
    return gameTime.getTimeSince(timeDiscoveredTarget) > 3000 && this.getStatus() == Status.ACTIVE;
  }
}
