package com.deco2800.game.components.tasks;


import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.WeaponFactory;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.RandomUtils;

import java.util.concurrent.TimeUnit;


/**
 * Spawn the vortex and teleport the enemy
 */
public class TeleportationTask extends DefaultTask implements PriorityTask {

    /**
     * target entity - player
     */
    private final Entity target;
    /**
     * cooldown before skill can cast again
     */
    private final long cooldown;
    /**
     * time of the last skill cast
     */
    private long lastFired;
    /**
     * get game area
     */
    private final GameArea gameArea;
    /**
     * check if the vortex is spawning
     */
    private boolean spawn = false;
    /**
     * set initial health = 100
     */
    private int health = 1000;
    /**
     * random spawning position
     */
    private Vector2 pos2;

    /**
     * @param target   The entity to chase.
     * @param cooldown how long to wait in MS before casting teleport again
     */
    public TeleportationTask(Entity target, int cooldown) {
        this.target = target;
        this.cooldown = cooldown;
        this.gameArea = ServiceLocator.getGameAreaService();
    }

    /**
     * Set the time last skill cast to 0
     */
    @Override
    public void start() {
        lastFired = 0;
    }

    /**
     * check for spawning the vortex and teleport the enemy
     */
    @Override
    public void update() {
        if (canTeleport()) {
            health = owner.getEntity().getComponent(CombatStatsComponent.class).getHealth();
            owner.getEntity().getComponent(PhysicsMovementComponent.class).setMoving(false);
            teleport();
        }

        if (spawn && TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFired >= 800) {
            owner.getEntity().setPosition(pos2);
        }
    }

    /**
     * spawns the vortex and teleport the boss (random teleport)
     */
    public void teleport() {
        if (lastFired == 0) {
            lastFired = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
        }
        spawn = true;
        Entity entity = new Entity();
        entity.setPosition(owner.getEntity().getPosition());
        entity.setScale(owner.getEntity().getScale());

        Entity vortex = WeaponFactory.createVortexEnter(entity,
                getDirectionOfTarget(), false);

        Vector2 minPos =
                new Vector2(90, 55);
        Vector2 maxPos = new Vector2(95, 60);
        pos2 = RandomUtils.random(minPos, maxPos);
        Entity entity2 = new Entity();
        entity2.setPosition(pos2);
        entity2.setScale(owner.getEntity().getScale());
        gameArea.spawnEntityAt(vortex, owner.getEntity().getCenterPosition(),
                true, true);
        Entity vortex2 = WeaponFactory.createVortexExit(entity2, getDirectionOfTarget(),
                false);

        vortex.data.put("teleportLoc", pos2);

        gameArea.spawnEntityAt(vortex2, pos2, true, true);
    }

    /**
     * return the priority of arrow task
     *
     * @return highest priority, can teleport, else -1
     */
    @Override
    public int getPriority() {
        if (canTeleport() || spawn) {
            if (spawn && TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFired >= 2000) {
                spawn = false;
            }
            return 25;
        }
        return -1;
    }

    /**
     * return the distance of the entity to the target
     *
     * @return return the distance to target
     */
    private float getDistanceToTarget() {
        return owner.getEntity().getCenterPosition().dst(target.getPosition());
    }

    /**
     * return the position of the target and return the angle from the entity (owner) to the target
     *
     * @return float angle from owner to target
     */
    private float getDirectionOfTarget() {
        Vector2 v1 = owner.getEntity().getCenterPosition().cpy();
        Vector2 v2 = target.getCenterPosition().cpy();
        Vector2 v3 = v1.cpy().sub(v2);
        return (v3.angleDeg());
    }

    /**
     * Check if there are any object between the entity and the target
     *
     * @return true if no object, false otherwise
     */
    private boolean isTargetVisible() {
        return owner.getEntity().canSeeEntity(target);
    }

    /**
     * check if the entity health is less than 50%
     * check if the time on last cast is more than 2 seconds and is target visible and
     * is entity within target attack distance and is entity health decrease
     *
     * @return true can teleport, false otherwise
     */
    private boolean canTeleport() {
        int currentHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getHealth();
        int maxHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getMaxHealth();

        if ((float) currentHealth / maxHealth < 0.75f && TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFired >= cooldown
                && isTargetVisible()
                && getDistanceToTarget() < owner.getEntity().getAttackRange()) {
            return currentHealth < health;
        }
        return false;
    }
}
