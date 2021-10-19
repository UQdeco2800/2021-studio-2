package com.deco2800.game.components.tasks.thor;

import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.services.ServiceLocator;

import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.services.ServiceLocator;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent. Entity will be disposed of after reaching its destination.
 */
public class LightningDamageTask extends DefaultTask implements PriorityTask {

    /**
     * Spawn time of the entity.
     */
    private final long spawnTime;

    public LightningDamageTask() {
        this.spawnTime = ServiceLocator.getTimeSource().getTime();
    }

    /**
     * return the priority of the arrow
     * If arrow is in moving, return 10, else return -1 and dispose the arrow
     *
     * @return int 10 if arrow is moving, -1 if arrow is not
     */
    public int getPriority() {
        if ((ServiceLocator.getTimeSource().getTime() - spawnTime) > 1000) {
            owner.getEntity().prepareDispose();
            return -1;
        }
        return 10;
    }

}
