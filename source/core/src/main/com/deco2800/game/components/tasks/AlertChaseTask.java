package com.deco2800.game.components.tasks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.raycast.RaycastHit;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

import java.util.concurrent.TimeUnit;

/** Chases a target entity but stays far enough away so it can attack safely */
public class AlertTask extends ChaseTask implements PriorityTask {
  protected final Entity target;
  protected MovementTask movementTask;
  private final float viewDistance;
  private final float alertViewDistance;
  private final float alertChaseDistance;
  private final float maxChaseDistance;
  private final PhysicsEngine physics;
  private final int priority;
  private final DebugRenderer debugRenderer;
  private final RaycastHit hit = new RaycastHit();
  private boolean alert = false;
  private final GameTime gameTime = ServiceLocator.getTimeSource();

  private long timeDiscoveredTarget;
  /**
   * @param target The entity to chase.
   */
  public AlertTask(Entity target, int priority, float viewDistance, float maxChaseDistance) {
    super(target, priority, viewDistance, maxChaseDistance);
    this.target = target;
    this.viewDistance = viewDistance;
    this.maxChaseDistance = maxChaseDistance;
    this.alertViewDistance = this.viewDistance * 2; // the ghost is twice as sensitive
    this.alertChaseDistance = this.maxChaseDistance * 2;
    this.priority = priority;
    physics = ServiceLocator.getPhysicsService().getPhysics();
    debugRenderer = ServiceLocator.getRenderService().getDebug();
  }

  @Override
  public void start() {
    // Start the event -
     // time since the first moment chase enemy up until now is greater than 3 seconds
    super.start();
    //timeDiscoveredTarget = ServiceLocator.getTimeSource().getTime();
    movementTask = new MovementTask(target.getPosition());
    movementTask.create(owner);
    movementTask.start();
    timeDiscoveredTarget = 0;
    this.owner.getEntity().getEvents().trigger("chaseStart"); // consider change it to alert Start

  }

  @Override
  public void stop() {
    super.stop();
    movementTask.stop();
  }

  @Override
  public void update() {
    timeDiscoveredTarget = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    movementTask.setTarget(target.getPosition());
    movementTask.update();
    if (movementTask.getStatus() != Status.ACTIVE) {
      movementTask.start();
      updateAlert(discover());
    } else if (this.getStatus() == Status.ACTIVE) {
      timeDiscoveredTarget = 0;
      super.stop();
    }
  }

  protected float getDistanceToTarget() {
    return owner.getEntity().getPosition().dst(target.getPosition());
  }

  private int getActivePriority() {
    float dst = getDistanceToTarget();
    // update the get active priority so the ghost is alert and has greater chase distance
    if (alert && dst <= alertChaseDistance && isTargetVisible()) {
      return priority;
    }
    if (dst > maxChaseDistance || !isTargetVisible()) {
      return -1; // Too far, stop chasing
    }
    return priority;
  }

  private int getInactivePriority() {
    float dst = getDistanceToTarget();
    // update the get in active priority so that the ghost switch to active sooner
    // if target is not visible, the ghost will ignore despite alert is on.
    if (alert && dst < alertViewDistance && isTargetVisible()) {
      return priority;
    }
    if (dst < viewDistance && isTargetVisible()) {
      return priority;
    }
    return -1;
  }

  // set alert if other ghost found enemy
  public void updateAlert(boolean state) {
    this.alert = state;
  }

  private boolean isTargetVisible() {
    Vector2 from = owner.getEntity().getCenterPosition();
    Vector2 to = target.getCenterPosition();

    // If there is an obstacle in the path to the player, not visible.
    if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
      debugRenderer.drawLine(from, hit.point);
      return false;
    }
    debugRenderer.drawLine(from, to);
    return true;
  }

  /**
   * if time since target is discovered is more than 3 seconds and owner is still chasing target
   * it will start warning other enemies to attack player
   * @return true if enemies chase after target for more than 3 seconds
   */
  protected boolean discover() {
    if (timeDiscoveredTarget == 0) {
      System.out.println(timeDiscoveredTarget);
      return false;
    }
    System.out.println(timeDiscoveredTarget);
    return (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - timeDiscoveredTarget) >= 3000
            && this.getStatus() == Status.ACTIVE;
  }
}


