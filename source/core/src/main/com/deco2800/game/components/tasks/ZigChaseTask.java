package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;

/**
 * Advance movement, the enemies chase the target in zig zag movement from distance a far
 **/
public class ZigChaseTask extends ChaseTask implements PriorityTask {

    private final float maxChaseDistance;
    private final float speedMultiplier;
    private long start = System.currentTimeMillis();
    private boolean zigLeft = false;

    /**
     * Initialise zig zag chase task - advance movement task
     *
     * @param target           chase target entity
     * @param priority         priority of the task
     * @param viewDistance     max view distance of entity to target
     * @param maxChaseDistance max chase distance of entity to target
     */
    public ZigChaseTask(Entity target, int priority, float viewDistance, float maxChaseDistance, float speedMultiplier) {
        super(target, priority, viewDistance, maxChaseDistance);
        this.maxChaseDistance = maxChaseDistance;
        this.speedMultiplier = speedMultiplier;
    }

    /**
     * Update the zig zag chase task
     * move zig zag on time based
     */
    @Override
    public void update() {
        if (((System.currentTimeMillis() - start) / 1000.0) > 0.5
                || getDistanceToTarget() < (maxChaseDistance * (2 / 10))) {
            if (getDistanceToTarget() < (maxChaseDistance * (2 / 10))) {
                movementTask.setTarget(target.getCenterPosition());
                movementTask.setMoveSpeed(new Vector2(1f * speedMultiplier, 1 * speedMultiplier));
            } else {
                movementTask.setMoveSpeed(new Vector2(2f * speedMultiplier, 2f * speedMultiplier));
                if (zigLeft) {
                    movementTask.setTarget(zigLeftRight(-1, 45f * (getDistanceToTarget() / maxChaseDistance)));
                    zigLeft = false;
                } else {
                    movementTask.setTarget(zigLeftRight(1, 45f * (getDistanceToTarget() / maxChaseDistance)));
                    zigLeft = true;
                }
            }

            movementTask.update();
            if (movementTask.getStatus() != Status.ACTIVE) {
                movementTask.start();
            }
            start = System.currentTimeMillis();
        }
    }

    /**
     * Return the movement direction when apply angle rotation
     *
     * @param direction current direction of entity
     * @return v3 new vector2 position
     */
    private Vector2 zigLeftRight(int direction, float angle) {
        Vector2 v1 = owner.getEntity().getCenterPosition().cpy();
        Vector2 v2 = target.getCenterPosition().cpy();
        Vector2 v3 = v2.cpy().sub(v1);
        v3.rotateAroundDeg(new Vector2(0, 0), ((-direction) * angle));
        v3.add(v1);
        return (v3);
    }

    /**
     * Return the distance of entity to target
     *
     * @return float distance from entity (owner) to target
     */
    protected float getDistanceToTarget() {
        return owner.getEntity().getPosition().dst(target.getPosition());
    }
} 
