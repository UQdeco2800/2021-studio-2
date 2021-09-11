package com.deco2800.game.components.tasks;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.WeaponFactory;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.physics.raycast.RaycastHit;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.RandomUtils;


import java.util.concurrent.TimeUnit;


/**
 * Spawn the vortex and teleport the enemy
 */
public class TeleportationTask extends DefaultTask implements PriorityTask {

    private final Entity target;
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();
    private final long cooldownMS;
    private long lastFired;
    private final GameArea gameArea;
    private boolean spawn = false;
    private int maxHealth;
    private int health = 100;
    private Vector2 pos2;


    /**
     * @param target The entity to chase.
     * @param cooldownMS how long to wait in MS before shooting again
     */
    public TeleportationTask(Entity target, int cooldownMS) {
        this.target = target;
        this.cooldownMS = cooldownMS;
        this.gameArea = ServiceLocator.getGameAreaService();
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
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
        if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFired >= 800) {
            owner.getEntity().setPosition(pos2);
        }
        health = owner.getEntity().getComponent(CombatStatsComponent.class).getHealth();

    }

    /**
     * spawns the vortex and teleport the boss
     */
    public void teleport() {
        if (lastFired == 0) {
            lastFired = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
        }
        spawn = true;
        Entity vortex = WeaponFactory.createVortex(getDirectionOfTarget(), false);

        gameArea.spawnEntityAt(vortex, owner.getEntity().getPosition(), true, true);
        Entity vortex2 = WeaponFactory.createVortex(getDirectionOfTarget(), false);

        Vector2 minPos =
                new Vector2(0, 0);
        Vector2 maxPos = new Vector2(10, 10);
        pos2 = RandomUtils.random(minPos, maxPos);

        gameArea.spawnEntityAt(vortex2, pos2, true, true);

    }

    /**
     * return the priority of arrow task
     *
     * @return highest priority if can shoot, else -1
     */
    @Override
    public int getPriority() {
        if (canTeleport() || spawn) {
            if (spawn && TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFired >= 2200) {
                spawn = false;
            }
            return 30;
        }
        return -1;
    }

    /**
     * return the distance of the entity to the target
     *
     * @return return the d
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
     * check if target is block by any object
     *
     * @return true if it not block, false otherwise
     */
    private boolean isTargetVisible() {
        Vector2 from = owner.getEntity().getCenterPosition();
        Vector2 to = target.getCenterPosition();

        // If there is an obstacle in the path to the player, not visible.
        if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
            debugRenderer.drawLine(from, hit.point, Color.RED, 1);
            return false;
        }
        Vector2 from2 = owner.getEntity().getPosition();
        Vector2 to2 = target.getPosition();

        // If there is an obstacle in the path to the player, not visible.
        if (physics.raycast(from2, to2, PhysicsLayer.OBSTACLE, hit)) {
            debugRenderer.drawLine(from2, hit.point, Color.RED, 1);
            return false;
        }
        debugRenderer.drawLine(from, to);
        return true;
    }

    /**
     * check if the entity health is less than 50%
     * check if the time on last cast is more than 2 seconds and is target visible and
     * is entity within target attack distance and is entity health decrease
     * @return true can teleport, false otherwise
     */
    private boolean canTeleport() {
        int currentHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getHealth();
        maxHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getMaxHealth();

        if ((float) currentHealth / maxHealth < 0.5f) {
            if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFired >= cooldownMS
                    && isTargetVisible()
                    && getDistanceToTarget() < owner.getEntity().getAttackRange()) {
                return currentHealth < health;
            }
        }
        return false;
    }
}
