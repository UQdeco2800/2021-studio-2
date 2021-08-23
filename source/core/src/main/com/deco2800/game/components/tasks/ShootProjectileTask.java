package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.WeaponFactory;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.raycast.RaycastHit;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


/** Spawns an arrow to shoot at the player */
public class ShootProjectileTask extends DefaultTask implements PriorityTask {

    private final Entity target;
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();
    private long cooldownMS;
    private long lastFired;
    private GameArea gameArea;
    //Below are currently unused and will be used for future arrows
    private Vector2 moveSpeed = new Vector2(2,2);
    private boolean followTarget;
    private boolean showTrajectory;
    private double multishotChance;
    private String projectileType;

    /**
     * @param target The entity to chase.
     * @param cooldownMS how long to wait in MS before shooting again
     * @param gameArea used to spawn in the arrow
     */
    public ShootProjectileTask(Entity target, long cooldownMS, GameArea gameArea) {
        this.target = target;
        this.gameArea = gameArea;
        this.cooldownMS = cooldownMS;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
    }

    @Override
    public void start() {
        lastFired = 0;
    }

    @Override
    public void update() {
        if (canShoot()) {
            shoot();
        }
    }

    /**
     * Spawns in an arrow according to the classes variables
     */
    public void shoot() {
        lastFired = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
        Entity arrow = WeaponFactory.createNormalArrow(target.getCenterPosition());
        gameArea.spawnEntityAt(arrow, owner.getEntity().getCenterPosition(), true, true);
    }

    public void setFollowTarget(boolean followTarget) {
        this.followTarget = followTarget;
    }

    public void setShowTrajectory(boolean showTrajectory) {
        this.showTrajectory = showTrajectory;
    }

    public void setMultishotChance(double multishotChance) {
        this.multishotChance = multishotChance;
    }

    public void setProjectileType(String projectileType) {
        this.projectileType = projectileType;
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public int getPriority() {
        if (canShoot()) {
            return 20;
        }
        return -1;
    }

    private float getDistanceToTarget() {
        return owner.getEntity().getPosition().dst(target.getPosition());
    }

    private boolean isTargetVisible() {
        Vector2 from = owner.getEntity().getCenterPosition();
        Vector2 to;
        to = target.getCenterPosition();

        // If there is an obstacle in the path to the player, not visible.
        if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
            debugRenderer.drawLine(from, hit.point);
            return false;
        }
        debugRenderer.drawLine(from, to);
        return true;
    }

    private boolean canShoot() {
        if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFired >= cooldownMS) {
            LoggerFactory.getLogger(ShootProjectileTask.class).info("cooldown over!!!!!");
        }

        return (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFired >= cooldownMS
                && isTargetVisible() && getDistanceToTarget() < owner.getEntity().getAttackRange());
    }
}
