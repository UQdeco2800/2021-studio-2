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
import com.deco2800.game.utils.math.Vector2Utils;


/**
 * Stopped using this class for ranged attacks as it
 * requires the basic arrow to see the target before it attacks
 * Will be used later on as the ground work for the tracking arrow
 */

/** Chases a target entity's static location until they get too far away or line of sight is lost */
public class AttackTask extends DefaultTask implements PriorityTask {
    private final Entity target;
    private final Vector2 targetLoc;
    private final int priority;
    private final float viewDistance;
    private final float maxChaseDistance;
    private Vector2 moveSpeed = Vector2Utils.ONE;
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();
    private MovementTask movementTask;

    /**
     * @param target The entity to chase.
     * @param priority Task priority when chasing (0 when not chasing).
     * @param viewDistance Maximum distance from the entity at which chasing can start.
     * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
     */
    public AttackTask(Entity target, int priority, float viewDistance, float maxChaseDistance) {
        this.target = target;
        this.targetLoc = null;
        this.priority = priority;
        this.viewDistance = viewDistance;
        this.maxChaseDistance = maxChaseDistance;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
    }

    /**
     * @param target The entity to chase.
     * @param priority Task priority when chasing (0 when not chasing).
     * @param viewDistance Maximum distance from the entity at which chasing can start.
     * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
     * @param moveSpeed speed to approach the target at.
     */
    public AttackTask(Entity target, int priority, float viewDistance, float maxChaseDistance, Vector2 moveSpeed) {
        this(target, priority, viewDistance, maxChaseDistance);
        this.moveSpeed = moveSpeed;
    }
    /**
     * @param targetLoc The location to go to.
     * @param priority Task priority when chasing (0 when not chasing).
     * @param viewDistance Maximum distance from the entity at which chasing can start.
     * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
     */
    public AttackTask(Vector2 targetLoc, int priority, float viewDistance, float maxChaseDistance) {
        this.target = null;
        this.targetLoc = targetLoc;
        this.priority = priority;
        this.viewDistance = viewDistance;
        this.maxChaseDistance = maxChaseDistance;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
    }

    /**
     * @param targetLoc The location to go to.
     * @param priority Task priority when chasing (0 when not chasing).
     * @param viewDistance Maximum distance from the entity at which chasing can start.
     * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
     * @param moveSpeed speed to approach the target at.
     */
    public AttackTask(Vector2 targetLoc, int priority, float viewDistance, float maxChaseDistance, Vector2 moveSpeed) {
        this(targetLoc, priority, viewDistance, maxChaseDistance);
        this.moveSpeed = moveSpeed;
    }

    @Override
    public void start() {
        super.start();
        if (target != null) {
            movementTask = new MovementTask(target.getPosition(), moveSpeed);
        } else {
            movementTask = new MovementTask(targetLoc, moveSpeed);
        }
        movementTask.create(owner);
        movementTask.start();

        this.owner.getEntity().getEvents().trigger("chaseStart");
    }

    @Override
    public void update() {
        movementTask.update();
        if (movementTask.getStatus() != Status.ACTIVE) {
            movementTask.start();
        }
    }

    @Override
    public void stop() {
        super.stop();
        movementTask.stop();
    }

    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return getActivePriority();
        }

        return getInactivePriority();
    }

    private float getDistanceToTarget() {
        if (target != null) {
            return owner.getEntity().getPosition().dst(target.getPosition());
        } else {
            return owner.getEntity().getPosition().dst(targetLoc);
        }
    }

    private int getActivePriority() {
        float dst = getDistanceToTarget();
        if (dst > maxChaseDistance || !isTargetVisible()) {
            return -1; // Too far, stop chasing
        }
        return priority;
    }

    private int getInactivePriority() {
        float dst = getDistanceToTarget();
        if (dst < viewDistance && isTargetVisible()) {
            return priority;
        }
        return -1;
    }

    private boolean isTargetVisible() {
        Vector2 from = owner.getEntity().getCenterPosition();
        Vector2 to;
        if (target != null) {
            to = target.getCenterPosition();
        } else {
            to = targetLoc;
        }
        // If there is an obstacle in the path to the player, not visible.
        if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
            debugRenderer.drawLine(from, hit.point);
            return false;
        }
        Vector2 from2 = owner.getEntity().getPosition();
        Vector2 to2 = target.getPosition();

        // If there is an obstacle in the path to the player, not visible.
        if (physics.raycast(from2, to2, PhysicsLayer.OBSTACLE, hit)) {
            debugRenderer.drawLine(from2, hit.point, Color.RED, 1);
            return false;
        }
        debugRenderer.drawLine(from, to);
        return true;
    }
}
