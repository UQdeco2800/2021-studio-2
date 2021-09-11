package com.deco2800.game.components.tasks;


import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.ServiceLocator;


/**
 * Spawns an arrow to shoot at a target
 */
public class SpawnMinionsTask extends DefaultTask implements PriorityTask {

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
    private static int spawn = 0;

    /**
     * spawn the minion to help the boss attack the target
     *
     * @param target The entity to chase.
     */
    public SpawnMinionsTask(Entity target) {
        this.target = target;
        this.gameArea = ServiceLocator.getGameAreaService();
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
        Entity elf = NPCFactory.createMeleeElf(target);
        Entity elf2 = NPCFactory.createRangedElf(target, "normalArrow", 0.15f);

        gameArea.spawnEntityAt(elf, owner.getEntity().getCenterPosition(), true, true);
        gameArea.spawnEntityAt(elf2, owner.getEntity().getCenterPosition(), true, true);

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
     * check if the boss is not inside the bound
     *
     * @return true if not, false otherwise
     */
    public boolean mapBound() {
        return owner.getEntity().getPosition().x < 0
                && owner.getEntity().getPosition().y < 0
                && owner.getEntity().getPosition().x > 30
                && owner.getEntity().getPosition().y > 30;
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

        if (ServiceLocator.getGameAreaService().getNumEnemy() == 0 && ratio < 0.5 && mapBound()) {
            return true;
        }

        if (ratio < 0.5 && spawn < 1) {
            return true;
        }
        return ratio < 0.25 && spawn < 2;
    }
}
