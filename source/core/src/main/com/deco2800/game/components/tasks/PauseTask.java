package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * Task that does nothing other than waiting for a given time. Status is Finished
 * after the time has passed.
 */
public class PauseTask extends DefaultTask implements PriorityTask {

    public PauseTask() {
    }

    /**
     * Checks if the game is paused and the priority will change depending on this. If the
     * game is paused, this task will have the highest priority and will stop the enemy from moving.
     *
     * @return priority integer
     */
    @Override
    public int getPriority() {
        GameTime timeSource = ServiceLocator.getTimeSource();
        if (true) {
            return 25;
        } else {
            return -1;
        }
    }

    /**
     * Update the wait task
     */
    @Override
    public void update() {
    }
}
