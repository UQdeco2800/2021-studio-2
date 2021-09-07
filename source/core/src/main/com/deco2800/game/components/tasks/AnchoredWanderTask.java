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
public class AnchoredWanderTask extends WanderTask implements PriorityTask {

    private final Entity base;
    private final float protectRadius;
    private final float protectX;
    private final float protectY;

    /**
     * @param waitTime      How long in seconds to wait between wandering.
     * @param anchor        - entity to retreat to
     * @param protectRadius - bounds around the object to stay in
     */
    public AnchoredWanderTask(Entity anchor, float protectRadius, float waitTime) {
        super(new Vector2(protectRadius, protectRadius), waitTime);
        this.base = anchor;
        //times 1.5 as this seems to deliver the best results
        // when the protect radius is shared with the other anchored tasks
        this.protectRadius = (float) (protectRadius * 1.5);
        this.protectX = 0;
        this.protectY = 0;
    }

    /**
     * @param waitTime How long in seconds to wait between wandering.
     * @param anchor   - entity to retreat to
     * @param protectX - bounds around the object to stay in on the x axis
     * @param protectY - bounds around the object to stay in on the y axis
     */
    public AnchoredWanderTask(Entity anchor, float protectX, float protectY, float waitTime) {
        super(new Vector2(protectX, protectY), waitTime);
        this.base = anchor;
        this.protectRadius = 0;
        this.protectX = (float) (protectX * 1.5);
        this.protectY = (float) (protectY * 1.5);
    }

    /**
     * Start wander task - the entity move toward the target but target need to be in range of anchor
     */
    @Override
    public void start() {
        super.start();
        movementTask.setTarget(getRandomPosInRange());
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
        currentTask.update();
    }

    /**
     * update task to waiting
     */
    private void startWaiting() {
        swapTask(waitTask);
    }

    /**
     * update task to move task
     */
    private void startMoving() {
        movementTask.setTarget(getRandomPosInRange());
        swapTask(movementTask);
    }

    /**
     * @return random Vector2 that is in the anchored base
     */
    private Vector2 getRandomPosInRange() {
        Vector2 min;
        Vector2 max;
        if (protectRadius > 0) {
            min = base.getCenterPosition().cpy().sub(protectRadius, protectRadius);
            max = base.getCenterPosition().cpy().add(protectRadius, protectRadius);
        } else {
            min = base.getCenterPosition().cpy().sub(protectX, protectY);
            max = base.getCenterPosition().cpy().add(protectX, protectY);
        }
        //Bug: is still possible for the coords to be too big and off the map
        //Below is a temp but bad fix
        if (max.x > TerrainFactory.MAP_SIZE.x) {
            max = new Vector2(TerrainFactory.MAP_SIZE.x, max.y);
        }
        if (max.y > TerrainFactory.MAP_SIZE.y) {
            max = new Vector2(max.x, TerrainFactory.MAP_SIZE.y);
        }
        return new Vector2(Math.abs(RandomUtils.random(min, max).x), Math.abs(RandomUtils.random(min, max).y));
    }
}
