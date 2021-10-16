package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

import java.util.concurrent.TimeUnit;

/**
 * Task that does nothing other than waiting for a given time. Status is Finished
 * after the time has passed.
 */
public class PauseTask extends DefaultTask implements PriorityTask {

    /** Last time that the attack was created. */
    private long lastTimeHit;

    /** When the event listener has already been initialised. */
    private boolean initialised;

    /**
     * Constructor for the pause task which occurs when the enemy is recently hit or
     * when a cutscene is open.
     */
    public PauseTask() {
        lastTimeHit = ServiceLocator.getTimeSource().getTime();
        initialised = false;
    }

    /**
     * Sets the last time the enemy has been hit when the enemy is hit.
     */
    private void enemyHit() {
        lastTimeHit = ServiceLocator.getTimeSource().getTime();
    }

    /**
     * Checks if the game is paused and the priority will change depending on this. If the
     * game is paused, this task will have the highest priority and will stop the enemy from moving.
     *
     * @return integer relating to the priority of the task, higher integer means higher priority
     */
    @Override
    public int getPriority() {
        if (!initialised) {
            this.owner.getEntity().getEvents().addListener("enemyHit", this::enemyHit);
            initialised = true;
        }
        GameTime timeSource = ServiceLocator.getTimeSource();
        if (timeSource.isEnemiesPaused() || timeSource.getTime() < lastTimeHit + 200) {
            return 25;
        } else {
            return -1;
        }
    }
}
