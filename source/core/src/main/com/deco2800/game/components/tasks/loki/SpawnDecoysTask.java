package com.deco2800.game.components.tasks.loki;


import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.ServiceLocator;


/**
 * Spawns in the boss's minions
 */
public class SpawnDecoysTask extends DefaultTask implements PriorityTask {

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
     * Spawned enemies.
     */
    private boolean spawned = false;

    /**
     * spawn the minion to help the boss attack the target
     *
     * @param target The entity to chase.
     */
    public SpawnDecoysTask(Entity target) {
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
            owner.getEntity().setEntityType("transformed");

            spawn();
            spawn++;
        }
    }

    /**
     * Spawns in enemies according to the classes variables
     */
    public void spawn() {
        spawned = true;
        for (int i = 0; i < 4; i++) {
            Entity elf = NPCFactory.createMeleeHellViking(target);
            ServiceLocator.getGameAreaService().incNum();
            Vector2 spawnPosition = owner.getEntity().getCenterPosition();
            switch (i % 4) {
                case 0:
                    spawnPosition.add(new Vector2(-1, 1));
                    break;
                case 1:
                    spawnPosition.add(new Vector2(1, 1));
                    break;
                case 2:
                    spawnPosition.add(new Vector2(-1, -1));
                    break;
                default:
                    spawnPosition.add(new Vector2(1, -1));
                    break;
            }
            gameArea.spawnEntityAt(elf, spawnPosition, true, true);
        }
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
        if (ratio < 0.5) {
            owner.getEntity().setEntityType("loki");
        }
        if (spawned) {
            return false;
        }

        if (ServiceLocator.getGameAreaService().getNumEnemy() == 0 && ratio < 0.5 && mapBound()) {
            return true;
        }

        return ratio < 0.75 && spawn < 1;
    }
}
