package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;

/**
 * Chases a target entity until they get too far away or line of sight is lost
 */
public class ChaseTask extends DefaultTask implements PriorityTask {
    protected final Entity target;
    private final int priority;
    private final float viewDistance;
    private final float maxChaseDistance;
    private Vector2 movementSpeed;
    protected MovementTask movementTask;

    /**
     * @param target           The entity to chase.
     * @param priority         Task priority when chasing (0 when not chasing).
     * @param viewDistance     Maximum distance from the entity at which chasing can start.
     * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
     */
    public ChaseTask(Entity target, int priority, float viewDistance, float maxChaseDistance) {
        this.target = target;
        this.priority = priority;
        this.viewDistance = viewDistance;
        this.maxChaseDistance = maxChaseDistance;
    }

    /**
     * @param target           The entity to chase.
     * @param priority         Task priority when chasing (0 when not chasing).
     * @param viewDistance     Maximum distance from the entity at which chasing can start.
     * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
     */
    public ChaseTask(Entity target, int priority, float viewDistance, float maxChaseDistance, Vector2 moveSpeed) {
        this(target, priority, viewDistance, maxChaseDistance);
        this.movementSpeed = moveSpeed.cpy();
    }

    /**
     * start the chase task - the entity will chase toward the position of the target
     */
    @Override
    public void start() {
        super.start();
        if (movementSpeed != null) {
            movementTask = new MovementTask(target.getPosition(), movementSpeed);
        } else {
            movementTask = new MovementTask(target.getPosition());
        }
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
     *
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
     *
     * @return float distance toward target
     */
    protected float getDistanceToTarget() {
        Vector2 direction = target.getCenterPosition().sub(owner.getEntity().getCenterPosition());
        if (owner.getEntity().getPosition().dst(target.getPosition()) < 0.1f) {
            if (direction.angleDeg() > 45 && direction.angleDeg() < 135) {
                owner.getEntity().getEvents().trigger("attackUp");
            } else if (direction.angleDeg() > 135 && direction.angleDeg() < 225) {
                owner.getEntity().getEvents().trigger("attackLeft");
            } else if (direction.angleDeg() > 225 && direction.angleDeg() < 315) {
                owner.getEntity().getEvents().trigger("attackDown");
            } else {
                owner.getEntity().getEvents().trigger("attackRight");
            }
        }
        return owner.getEntity().getPosition().dst(target.getPosition());
    }

    /**
     * return the priority when the task is running
     *
     * @return priority - allow to switch task if target is out of reach
     */
    protected int getActivePriority() {
        float dst = getDistanceToTarget();
        if (dst > maxChaseDistance || !isTargetVisible()) {
            return -1; // Too far, stop chasing
        }
        return priority;
    }

    /**
     * return the priorit when the task is not running
     *
     * @return priority - allow to switch task if target is in view range
     */
    protected int getInactivePriority() {
        float dst = getDistanceToTarget();
        if (dst < viewDistance && isTargetVisible()) {
            return priority;
        }
        return -1;
    }

    /**
     * Check if there are any object between the entity and the target
     *
     * @return true if no object, false otherwise
     */
    public boolean isTargetVisible() {
        return owner.getEntity().canSeeEntity(target);
    }
}
