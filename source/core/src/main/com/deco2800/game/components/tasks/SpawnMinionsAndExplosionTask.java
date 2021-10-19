package com.deco2800.game.components.tasks;


import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.WeaponFactory;
import com.deco2800.game.services.ServiceLocator;

import java.security.SecureRandom;
import java.util.Random;


/**
 * Spawns in the boss's minions
 */
public class SpawnMinionsAndExplosionTask extends DefaultTask implements PriorityTask {

    /**
     * target entity (player)
     */
    private final Entity target;
    /**
     * game area
     */
    private final GameArea gameArea;
    /**
     * spawn every second hit
     */
    private int spawn = 0;
    /**
     * spawn every second hit
     */
    private int spawnComparator;
    /**
     * spawn every second hit
     */
    private float lastHealth = 0;

    /**
     * trigger the spawn
     */
    private boolean triggered = false;

    /**
     * spawn the minion to help the boss attack the target
     *
     * @param target The entity to chase.
     */
    public SpawnMinionsAndExplosionTask(Entity target) {
        this.target = target;
        this.gameArea = ServiceLocator.getGameAreaService();
        Random rand = new SecureRandom();
        spawnComparator = rand.nextInt(7) + 2;
    }

    /**
     * update the arrow - check whether the entity can shoot the arrow or not
     */
    @Override
    public void update() {
        if (canSpawn()) {
            spawn();
        }
    }

    /**
     * Spawns in enemies according to the classes variables
     */
    public void spawn() {
        Entity elf = NPCFactory.createMeleeElf(target);
        Entity elf2 = NPCFactory.createRangedElf(target, ShootProjectileTask.projectileTypes.NORMAL_ARROW, 0.25f);
        Entity explosion = WeaponFactory.createExplosion(owner.getEntity());

        ServiceLocator.getGameAreaService().incNum();
        ServiceLocator.getGameAreaService().incNum();

        gameArea.decNum();
        gameArea.decNum();
        gameArea.spawnEntityAt(elf, owner.getEntity().getCenterPosition(), true, true);
        gameArea.spawnEntityAt(elf2, owner.getEntity().getCenterPosition(), true, true);
        gameArea.spawnEntityAt(explosion, owner.getEntity().getCenterPosition(), true, true);
        triggered = false;
        spawn = 0;
        Random rand = new SecureRandom();
        spawnComparator = rand.nextInt(7) + 2;
    }

    /**
     * return the priority of task
     *
     * @return 20 can spawn enemy, else -1
     */
    @Override
    public int getPriority() {
        if (canSpawn()) {
            return 30;
        }
        return -1;
    }

    /**
     * check if the minions can be spawned
     *
     * @return true if can spawn, false otherwise
     */
    private boolean canSpawn() {
        float maxHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getMaxHealth();
        float health = owner.getEntity().getComponent(CombatStatsComponent.class).getHealth();
        float ratio = health / maxHealth;
        if (!triggered && health != lastHealth) {
            if (ratio < 0.7 && spawn >= spawnComparator) {
                triggered = true;
            } else {
                spawn++;
            }
            lastHealth = health;
        }
        return triggered;
    }
}
