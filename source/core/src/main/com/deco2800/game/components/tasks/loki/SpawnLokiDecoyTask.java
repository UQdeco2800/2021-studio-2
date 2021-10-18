package com.deco2800.game.components.tasks.loki;


import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.ServiceLocator;

import java.util.concurrent.TimeUnit;


/**
 * Spawns in the boss's minions
 */
public class SpawnLokiDecoyTask extends DefaultTask implements PriorityTask {

    /**
     * target entity (player)
     */
    private final Entity target;
    /**
     * game area
     */
    private final GameArea gameArea;
    /**
     * number of time enemy is spawn
     */
    private int spawn = 0;

    /**
     * THe delay between each attacks for this task.
     */
    private final long cooldownMS;

    /**
     * Last time that the attack was created.
     */
    private long lastFiredTime;

    /**
     * spawn the minion to help the boss attack the target
     *
     * @param target     The entity to chase.
     * @param cooldownMS the time as a float between the ability being casted by Loki
     */
    public SpawnLokiDecoyTask(Entity target, long cooldownMS) {
        this.target = target;
        this.gameArea = ServiceLocator.getGameAreaService();
        this.cooldownMS = cooldownMS;
        this.lastFiredTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    }

    /**
     * update the arrow - check whether the entity can shoot the arrow or not
     */
    @Override
    public void update() {
        if (canSpawn()) {
            owner.getEntity().getComponent(PhysicsMovementComponent.class).setMoving(false);

            spawn();
            spawn++;
        }
    }

    /**
     * Spawns in enemies according to the classes variables
     */
    public void spawn() {
        lastFiredTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
        Entity loki;
        ServiceLocator.getGameAreaService().incNum();
        if (spawn % 2 != 0) {
            loki = NPCFactory.createMeleeLokiDecoy(target);
        } else {
            loki = NPCFactory.createRangedLokiDecoy(target);
        }
        gameArea.spawnEntityAt(loki, owner.getEntity().getCenterPosition(), true, true);
    }

    /**
     * return the priority of task
     *
     * @return 20 can spawn enemy, else -1
     */
    @Override
    public int getPriority() {
        if (canSpawn()) {
            return 20;
        }
        return -1;
    }

    /**
     * check if the minions can be spawned
     *
     * @return true if can spawn, false otherwise
     */
    private boolean canSpawn() {
        return (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFiredTime >= cooldownMS
                && isTargetVisible() && getDistanceToTarget() < owner.getEntity().getAttackRange());
    }

    /**
     * Returns the distance of the entity to the target
     *
     * @return Returns the distance as a float between the player and the enemy
     */
    private float getDistanceToTarget() {
        return owner.getEntity().getCenterPosition().dst(target.getPosition());
    }

    /**
     * Check if there are any object between the entity and the target
     *
     * @return true if no object, false otherwise
     */
    private boolean isTargetVisible() {
        return owner.getEntity().canSeeEntity(target);
    }
}
