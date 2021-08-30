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
import com.deco2800.game.services.ServiceLocator;

/** Chases a target entity until they get too far away or line of sight is lost */
public class ChaseTask extends DefaultTask implements PriorityTask {
  protected final Entity target;
  protected MovementTask movementTask;
  private final int priority;
  private final float viewDistance;
  private final float maxChaseDistance;
  private final PhysicsEngine physics;
  private final DebugRenderer debugRenderer;
  private final RaycastHit hit = new RaycastHit();

  /**
   * @param target The entity to chase.
   * @param priority Task priority when chasing (0 when not chasing).
   * @param viewDistance Maximum distance from the entity at which chasing can start.
   * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
   */
  public ChaseTask(Entity target, int priority, float viewDistance, float maxChaseDistance) {
    this.target = target;
    this.priority = priority;
    this.viewDistance = viewDistance;
    this.maxChaseDistance = maxChaseDistance;
    physics = ServiceLocator.getPhysicsService().getPhysics();
    debugRenderer = ServiceLocator.getRenderService().getDebug();
  }

  /**
   * start the chase task - the entity will chase toward the position of the target
   */
  @Override
  public void start() {
    super.start();
    movementTask = new MovementTask(target.getPosition());
    movementTask.create(owner);
    movementTask.start();
    this.owner.getEntity().getEvents().trigger("chaseStart");
  }

  /**
   * constantly update the position of the target and whether or not the target is
   * run out of detectable distance
   */
  @Override
  public void update() {
    movementTask.setTarget(target.getPosition());
    movementTask.update();
    if (movementTask.getStatus() != Status.ACTIVE) {
      movementTask.start();
    }
  }

  /**
   * Stop the chase task if the condition for chasing is false
   * In this if get priority return -1, stop chase task
   */
  @Override
  public void stop() {
    super.stop();
    movementTask.stop();
  }

  /**
   * Return the priority of chase task when it active (run)
   * or return the priority of chasetask when it inactive (other task run)
   * @return priority
   */
  @Override
  public int getPriority() {
    if (status == Status.ACTIVE) {
      return getActivePriority();
    }

    return getInactivePriority();
  }

  /**
   * Return the distance from the current entity toward the target in flaot (displacement)
   * @return float distance toward target
   */
  protected float getDistanceToTarget() {
    return owner.getEntity().getPosition().dst(target.getPosition());
  }

  /**
   * return the priority when the task is running
   * @return priority - allow to switch task if target is out of reach
   */
  private int getActivePriority() {
    float dst = getDistanceToTarget();
    if (dst > maxChaseDistance || !isTargetVisible()) {
      return -1; // Too far, stop chasing
    }
    return priority;
  }

  /**
   * return the priorit when the task is not running
   * @return priority - allow to switch task if target is in view range
   */
  private int getInactivePriority() {
    float dst = getDistanceToTarget();
    if (dst < viewDistance && isTargetVisible()) {
      return priority;
    }
    return -1;
  }

  /**
   * Check if there are any object between the entity and the target
   * @return true if no object, false otherwise
   */
  public boolean isTargetVisible() {
    Vector2 from = owner.getEntity().getCenterPosition();
    Vector2 to = target.getCenterPosition();

    // If there is an obstacle in the path to the player, not visible.
    if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
      debugRenderer.drawLine(from, hit.point, Color.RED, 1);
      return false;
    }
    Vector2 from2 = owner.getEntity().getPosition();
    Vector2 to2 = target.getPosition();

    // If there is an obstacle in the path to the player, not visible.
    if (physics.raycast(from2, to2, PhysicsLayer.OBSTACLE, hit)) {
      debugRenderer.drawLine(from2, hit.point, Color.RED, 1);
      return false;
    }

    debugRenderer.drawLine(from, to, Color.BLUE, 1);
    return true;
  }
}
