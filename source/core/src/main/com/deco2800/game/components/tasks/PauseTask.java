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

    /**
     * Constructor for the Pause Task to prevent the NPC from moving and attacking.
     */
    public PauseTask() {
    }

    /**
     * Checks if the game is paused and the priority will change depending on this. If the
     * game is paused, this task will have the highest priority and will stop the enemy from moving.
     *
     * @return integer relating to the priority of the task, higher integer means higher priority
     */
    @Override
    public int getPriority() {
        GameTime timeSource = ServiceLocator.getTimeSource();
        if (timeSource.isPaused()) {
            return 25;
        } else {
            return -1;
        }
    }
}
