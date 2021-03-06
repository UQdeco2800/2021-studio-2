package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultMultiTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wander around by moving a random position within a range of the starting position. Wait a little
 * bit between movements. Requires an entity with a PhysicsMovementComponent.
 */
public class WanderTask extends DefaultMultiTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(WanderTask.class);

    private final Vector2 wanderRange;
    private final float waitTime;
    protected MovementTask movementTask;
    protected WaitTask waitTask;
    private Vector2 startPos;

    /**
     * @param wanderRange Distance in X and Y the entity can move from its position when start() is
     *                    called.
     * @param waitTime    How long in seconds to wait between wandering.
     */
    public WanderTask(Vector2 wanderRange, float waitTime) {
        this.wanderRange = wanderRange;
        this.waitTime = waitTime;

    }

    /**
     * return the priority of wander task
     * The priority of wander task is the lowest compare to other task and only higher if
     * other task is set to -1 on purpose to set to wander task
     *
     * @return 1
     */
    @Override
    public int getPriority() {
        return 1; // Low priority task
    }

    /**
     * Start the wandering task
     */
    @Override
    public void start() {
        super.start();
        startPos = owner.getEntity().getPosition();

        waitTask = new WaitTask(waitTime);
        waitTask.create(owner);
        movementTask = new MovementTask(getRandomPosInRange());
        movementTask.create(owner);

        movementTask.start();
        currentTask = movementTask;

        this.owner.getEntity().getEvents().trigger("wanderStart");
    }

    /**
     * update the wandering task
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
     * start waiting task - swap the current task to wait task
     */
    protected void startWaiting() {
        logger.debug("Starting waiting");
        swapTask(waitTask);
    }

    /**
     * start wandering task - swap the task to wandering task
     */

    protected void startMoving() {
        logger.debug("Starting moving");
        movementTask.setTarget(getRandomPosInRange());
        swapTask(movementTask);
    }

    /**
     * randomly move around
     *
     * @return Vector2 position to move around
     */
    protected Vector2 getRandomPosInRange() {
        Vector2 halfRange = wanderRange.cpy().scl(0.5f);
        Vector2 min = startPos.cpy().sub(halfRange);
        Vector2 max = startPos.cpy().add(halfRange);
        return RandomUtils.random(min, max);
    }
}
