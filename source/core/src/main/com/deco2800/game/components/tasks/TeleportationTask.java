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

    /**
     * target entity - player
     */
    private final Entity target;
    /**
     * object - physics engine
     */
    private final PhysicsEngine physics;
    /**
     * debug mode
     */
    private final DebugRenderer debugRenderer;
    /**
     * ray cast on debug mode
     */
    private final RaycastHit hit = new RaycastHit();
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
    private int health = 100;
    /**
     * random spawning position
     */
    private Vector2 pos2;
    /**
     * check if the enemy still outside the map area
     */
    private int count = 0;


    /**
     * @param target   The entity to chase.
     * @param cooldown how long to wait in MS before casting teleport again
     */
    public TeleportationTask(Entity target, int cooldown) {
        this.target = target;
        this.cooldown = cooldown;
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
        if (ServiceLocator.getGameAreaService().getNumEnemy() > 0 && mapBound()) {
            owner.getEntity().getComponent(PhysicsMovementComponent.class).setMoving(false);
            return;
        }
        if (ServiceLocator.getGameAreaService().getNumEnemy() == 0 && mapBound()) {
            owner.getEntity().getComponent(PhysicsMovementComponent.class).setMoving(false);
            teleport(new Vector2(2f, 2f));
            count++;
        }
        if (canTeleport()) {
            health = owner.getEntity().getComponent(CombatStatsComponent.class).getHealth();
            owner.getEntity().getComponent(PhysicsMovementComponent.class).setMoving(false);
            teleport();
        }

        if (spawn) {
            if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFired >= 800) {
                owner.getEntity().setPosition(pos2);
            }
        }

    }

    /**
     * method overloading - teleport to a given position
     *
     * @param position position to teleport to
     */
    public void teleport(Vector2 position) {
        Entity vortex = WeaponFactory.createVortex(getDirectionOfTarget(), false);

        gameArea.spawnEntityAt(vortex, owner.getEntity().getPosition(), true, true);
        Entity vortex2 = WeaponFactory.createVortex(getDirectionOfTarget(), false);

        gameArea.spawnEntityAt(vortex2, position, true, true);
        owner.getEntity().setPosition(position);
    }

    /**
     * spawns the vortex and teleport the boss (random teleport)
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
     * @return highest priority, can teleport, else -1
     */
    @Override
    public int getPriority() {
        if ((ServiceLocator.getGameAreaService().getNumEnemy() == 0 && count == 0)
                || (ServiceLocator.getGameAreaService().getNumEnemy() != 0 && mapBound())) {
            return 100;
        }
        if (canTeleport() || spawn) {
            if (spawn && TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFired >= 2200) {
                spawn = false;
            }
            return 30;
        }
        return -1;
    }

    /**
     * check if not inside the boundary of the map
     *
     * @return true if not inside the map, false otherwise
     */
    public boolean mapBound() {
        return (owner.getEntity().getPosition().x < 0
                && owner.getEntity().getPosition().y < 0)
                || (owner.getEntity().getPosition().x > 30
                && owner.getEntity().getPosition().y > 30);
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
     *
     * @return true can teleport, false otherwise
     */
    private boolean canTeleport() {
        int currentHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getHealth();
        int maxHealth = owner.getEntity().getComponent(CombatStatsComponent.class).getMaxHealth();

        if ((float) currentHealth / maxHealth < 0.5f) {
            if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFired >= cooldown
                    && isTargetVisible()
                    && getDistanceToTarget() < owner.getEntity().getAttackRange()) {
                return currentHealth < health;
            }
        }
        return false;
    }
}
