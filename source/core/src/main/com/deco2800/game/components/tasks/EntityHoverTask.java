package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.utils.math.RandomUtils;

/**
 * Wander around by moving to a random position within a range of the base anchor. Wait a little
 * bit between movements. Requires an entity with a PhysicsMovementComponent.
 */
public class EntityHoverTask extends AnchoredWanderTask implements PriorityTask {

    private final Vector2 givenOffset;
    private Vector2 calculatedOffset;
    private final float moveSpeedScl;

    /**
     * @param waitTime      How long in seconds to wait between wandering.
     * @param anchor        - entity to retreat to
     * @param protectRadius - bounds around the object to stay in
     */
    public EntityHoverTask(Entity anchor, float protectRadius,
                           float waitTime, Vector2 givenOffset, float moveSpeedScl) {
        super(anchor, protectRadius, waitTime);
        this.givenOffset = givenOffset;
        this.moveSpeedScl = moveSpeedScl;
    }

    /**
     * Start wander task - the entity move toward the target but target need to be in range of anchor
     */
    @Override
    public void start() {
        super.start();
        calculatedOffset = getRandomOffsetInRange();
        movementTask.setMoveSpeed(calculateMovespeed());
        movementTask.setTarget(calculatedOffset.cpy().add(base.getCenterPosition()).add(givenOffset));
    }

    /**
     * update the status of current task
     */
    @Override
    public void update() {
        if (currentTask.getStatus() != Status.ACTIVE) {
            if (currentTask == movementTask) {
                startWaiting();
            } else {
                startMoving();
            }
        }
        if (currentTask == movementTask) {
            movementTask.setMoveSpeed(calculateMovespeed());
            movementTask.setTarget(calculatedOffset.cpy().add(base.getCenterPosition()).add(givenOffset));
        }
        currentTask.update();
    }

    /**
     * update task to move task
     */
    @Override
    protected void startMoving() {
        calculatedOffset = getRandomOffsetInRange();
        movementTask.setTarget(calculatedOffset.cpy().add(base.getCenterPosition()).add(givenOffset));
        swapTask(movementTask);
    }

    /**
     * @return random Vector2 offset that the entity will hover at
     */
    private Vector2 getRandomOffsetInRange() {
        Vector2 min;
        Vector2 max;
        if (protectRadius > 0) {
            min = new Vector2(-protectRadius, -protectRadius);
            max = new Vector2(protectRadius, protectRadius);
        } else {
            min = new Vector2(-protectX, -protectY);
            max = new Vector2(protectX, protectY);
        }
        if (max.x > TerrainFactory.MAP_SIZE.x) {
            max = new Vector2(TerrainFactory.MAP_SIZE.x, max.y);
        }
        if (max.y > TerrainFactory.MAP_SIZE.y) {
            max = new Vector2(max.x, TerrainFactory.MAP_SIZE.y);
        }
        return RandomUtils.random(min, max);
    }

    /**
     * Will calculate the move speed based on how far away the hover location is,
     * this prevenets issues with teleportating and dashing enemies
     *
     * @return how fast to approach the hover location
     */
    private Vector2 calculateMovespeed() {
        //Distance to movement location
        Vector2 distance = owner.getEntity().getPosition().cpy()
                .sub(calculatedOffset.cpy().add(base.getCenterPosition()).add(givenOffset));
        distance.x = Math.abs(distance.x);
        distance.y = Math.abs(distance.y);
        return (distance.scl((float) (moveSpeedScl * Math.pow(1f + 0.1, distance.len()))));
    }
}
