package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.utils.math.RandomUtils;
import java.util.concurrent.TimeUnit;

/**
 * Return to the base anchor if the entity is too far away.
 * Requires an entity with a PhysicsMovementComponent.
 */
public class AnchoredRetreatTask extends DefaultTask implements PriorityTask {

  private final Entity base;
  private final float protectRadius;
  private final float protectX;
  private final float protectY;
  private MovementTask movementTask;
  private boolean retreating = false;
  private long retreatingLastUpdated;

  /**
   *
   * @param anchor - entity to retreat to
   * @param protectRadius - bounds around the object to stay in
   */
  public AnchoredRetreatTask(Entity anchor, float protectRadius) {
    this.base = anchor;
    this.protectRadius = protectRadius;
    this.protectX = 0;
    this.protectY = 0;
  }

  /**
   *
   * @param anchor - entity to retreat to
   * @param protectX - bounds around the object to stay in on the x axis
   * @param protectY - bounds around the object to stay in on the y axis
   */
  public AnchoredRetreatTask(Entity anchor, float protectX, float protectY) {
    this.base = anchor;
    this.protectRadius = 0;
    this.protectX = protectX;
    this.protectY = protectY;
  }

  /**
   *
   * @return 15 to return to base
   *    5 to continue to base but don't need to retreat
   *    -1 if finished retreating
   */
  @Override
  public int getPriority() {
    //Based off distance to base and if retreating
    if (returnToBase()) {
      return (15);
    } else if (!returnToBase() && retreating) {
      // Will cause the entity to attack and retreat if a target is on the edge of its base unless using AnchoredChaseTask
      // Set above ChaseTask priority to force the entity to continue retreating before attacking again
      return (5);
    } else {
      return (-1);
    }
  }

  /**
   * Generate new retreat Task and start retreating to random position around the anchor
   */
  @Override
  public void start() {
    super.start();
    movementTask = new MovementTask(getRandomPosInRange());
    movementTask.create(owner);
    movementTask.start();
    retreating = true;
    this.owner.getEntity().getEvents().trigger("wanderStart");
  }

  @Override
  public void stop() {
    super.stop();
    movementTask.stop();
    retreating = false;
    retreatingLastUpdated = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
  }

  /**
   * Start movement if it is meant to start
   * Stop movement if it is meant to stop
   */
  @Override
  public void update() {
    if (!retreating && returnToBase()) {
      retreating = true;
      retreatingLastUpdated = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
      movementTask.setTarget(getRandomPosInRange());
      movementTask.start();
    } else if (retreating && movementTask.getStatus() != Status.ACTIVE) {
      retreating = false;
      retreatingLastUpdated = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    }
    movementTask.update();
  }

  private float distanceFromBase() {
    return (owner.getEntity().getCenterPosition().dst(base.getCenterPosition()));
  }

  private float distanceFromBaseX() {
    return (Math.abs(owner.getEntity().getCenterPosition().x - base.getCenterPosition().x));
  }

  private float distanceFromBaseY() {
    return (Math.abs(owner.getEntity().getCenterPosition().y - base.getCenterPosition().y));
  }

  /**
   * @return true if the entity is outside of it's base or false if it is in it's base
   *    if the enemy is on the bounds of it's base, true if returning or false is doing something else.
   */
  private boolean returnToBase() {
    // How long to wait before updating returning again
    if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - retreatingLastUpdated <= 2000) {
      return retreating;
    }
    if (protectRadius > 0f) {
      //Deadzone
      if (distanceFromBase() - protectRadius < protectRadius/4) {
        return false;
      } else if (distanceFromBase() - protectRadius > protectRadius/2) {
        return true;
      } else {
        return retreating;
      }
    } else {
      //Deadzone
      if ((distanceFromBaseX() - protectX < protectX / 4 * 3) || (distanceFromBaseY() - protectY < protectY / 4 * 3)) {
        return false;
      } else if ((distanceFromBaseX() - protectX > protectX) || (distanceFromBaseY() - protectY > protectY)) {
        return true;
      } else {
        return retreating;
      }
    }
  }

  /**
   *
   * @return random Vector2 that is in the anchored base
   */
  private Vector2 getRandomPosInRange() {
    Vector2 min;
    Vector2 max;
    if (protectRadius > 0) {
      min = base.getCenterPosition().cpy().sub(protectRadius / 4, protectRadius / 4);
      max = base.getCenterPosition().cpy().add(protectRadius / 4, protectRadius / 4);
    } else {
      min = base.getCenterPosition().cpy().sub(protectX / 4, protectY / 4);
      max = base.getCenterPosition().cpy().add(protectX / 4, protectY / 4);
    }
    return new Vector2 (Math.abs(RandomUtils.random(min, max).x), Math.abs(RandomUtils.random(min, max).y));
  }

}
