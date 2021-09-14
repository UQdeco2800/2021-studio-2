package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;

/**
 * Advance movement, the enemies chase the target in zig zag movement from distance a far
 **/
public class ZigChaseTask extends ChaseTask implements PriorityTask {

    private final float maxChaseDistance;
    private long start = System.currentTimeMillis();
    private boolean zigLeft = false;
    private float speedMultiplier;

    /**
     * Initialise zig zag chase task - advance movement task
     *
     * @param target           chase target entity
     * @param priority         priority of the task
     * @param viewDistance     max view distance of entity to target
     * @param maxChaseDistance max chase distance of entity to target
     * @param speedMultiplier  multiplier for the speed value
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
        if (((System.currentTimeMillis() - start) / 1000f) > 0.5f
                || getDistanceToTarget() < maxChaseDistance * 3 / 10f) {
            if (getDistanceToTarget() < maxChaseDistance * 3 / 10f) {
                movementTask.setTarget(target.getPosition());
                float speed = 1 * speedMultiplier;
                movementTask.setMoveSpeed(new Vector2(speed, speed));
            } else {
                float speed = 1.5f * speedMultiplier;
                movementTask.setMoveSpeed(new Vector2(speed, speed));
                float angle = 45 * (getDistanceToTarget() / maxChaseDistance);
                if (zigLeft) {
                    movementTask.setTarget(zigLeftRight(-1, angle));
                    zigLeft = false;
                } else {
                    movementTask.setTarget(zigLeftRight(1, angle));
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
