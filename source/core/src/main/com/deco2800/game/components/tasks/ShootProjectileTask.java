package com.deco2800.game.components.tasks;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.WeaponConfigs;
import com.deco2800.game.entities.factories.WeaponFactory;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.physics.raycast.RaycastHit;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.services.ServiceLocator;

import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * Spawns an arrow to shoot at a target
 */
public class ShootProjectileTask extends DefaultTask implements PriorityTask {

    private final Entity target;
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();
    private final long cooldownMS;
    private long lastFired;
    private final GameArea gameArea;
    //Below are currently unused and will be used for future arrows
    private Vector2 tragectoryLocation = null;
    private double multishotChance = 0.00;
    private String projectileType = "normalArrow";
    private boolean poweringUp = false;

    /**
     * @param target     The entity to chase.
     * @param cooldownMS how long to wait in MS before shooting again
     */
    public ShootProjectileTask(Entity target, long cooldownMS) {
        this.target = target;
        this.cooldownMS = cooldownMS;
        this.gameArea = ServiceLocator.getGameAreaService();
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
    }

    /**
     * Set the time of the last arrow fire to 0;
     */

    @Override
    public void start() {
        lastFired = 0;
    }

    /**
     * update the arrow - check whether the entity can shoot the arrow or not
     */
    @Override
    public void update() {
        if (canShoot() || poweringUp) {
            owner.getEntity().getComponent(PhysicsMovementComponent.class).setMoving(false);
            shoot();
        }
    }

    /**
     * Spawns in an arrow according to the classes variables
     */
    public void shoot() {
        if (!poweringUp) {
            lastFired = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
        }
        switch (projectileType) {
            case "normalArrow": {
                Vector2 relativeLoc = target.getPosition().cpy().sub(owner.getEntity().getPosition());
                relativeLoc.scl(30);
                relativeLoc.add(owner.getEntity().getPosition());
                Entity arrow = WeaponFactory.createNormalArrow(relativeLoc, getDirectionOfTarget());
                gameArea.spawnEntityAt(arrow, owner.getEntity().getCenterPosition(), true, true);
                int multiplier = 0;
                Random rand = new Random();
                double chance = rand.nextDouble();
                double multishotChanceTemp = multishotChance;
                while (multishotChanceTemp >= chance) {
                    multiplier++;
                    Entity arrowLeft = WeaponFactory.createNormalArrow(getMultishotVector(-1, multiplier), getMultishotDirection(-1, multiplier));
                    gameArea.spawnEntityAt(arrowLeft, owner.getEntity().getCenterPosition(), true, true);
                    Entity arrowRight = WeaponFactory.createNormalArrow(getMultishotVector(1, multiplier), getMultishotDirection(1, multiplier));
                    gameArea.spawnEntityAt(arrowRight, owner.getEntity().getCenterPosition(), true, true);
                    chance = rand.nextDouble();
                    multishotChanceTemp -= 1;
                }
                break;
            }
            case "trackingArrow": {
                //Spawns arrows in a different location on a circle around the entity
                Vector2 offset = owner.getEntity().getCenterPosition().cpy().sub(owner.getEntity().getPosition());
                offset.setAngleDeg(getDirectionOfTarget());
                //creates a nice ring effect at multishots above 8
                float angle = (float) (360 / (Math.max(8, Math.floor(multishotChance)) * 2 + 1));

                Entity arrow = WeaponFactory.createTrackingArrow(target, getDirectionOfTarget());
                gameArea.spawnEntityAt(arrow, owner.getEntity().getPosition().cpy().sub(offset), true, true);
                int multiplier = 0;
                Random rand = new Random();
                double chance = rand.nextDouble();
                double multishotChanceTemp = multishotChance;
                while (multishotChanceTemp >= chance) {
                    multiplier++;
                    arrow = WeaponFactory.createTrackingArrow(target, getDirectionOfTarget());
                    offset.setAngleDeg(getDirectionOfTarget() + angle * multiplier);
                    gameArea.spawnEntityAt(arrow, owner.getEntity().getPosition().cpy().sub(offset), true, true);
                    arrow = WeaponFactory.createTrackingArrow(target, getDirectionOfTarget());
                    offset.setAngleDeg(getDirectionOfTarget() - angle * multiplier);
                    gameArea.spawnEntityAt(arrow, owner.getEntity().getPosition().cpy().sub(offset), true, true);
                    chance = rand.nextDouble();
                    multishotChanceTemp -= 1;
                }
                break;
            }
            case "fastArrow":
                float AOE = 1f;
                if (!poweringUp) {
                    poweringUp = true;
                    //Trigger powering up animation otherwise entity will not be rendered correctly
                }
                if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFired >= cooldownMS) {
                    poweringUp = false;
                }
                if (tragectoryLocation == null) {
                    tragectoryLocation = target.getCenterPosition();
                }
                float turningAngle = 0.1f;//UserSettings.get().fps;

                Vector2 relativeLocationTarget = tragectoryLocation.cpy().sub(owner.getEntity().getCenterPosition());
                Vector2 relativeLocationEntity = target.getCenterPosition().cpy().sub(owner.getEntity().getCenterPosition());
                if (relativeLocationTarget.angleDeg(relativeLocationEntity) > turningAngle && relativeLocationEntity.angleDeg(relativeLocationTarget) > turningAngle) {
                    physics.raycast(owner.getEntity().getCenterPosition(), tragectoryLocation.scl(30), PhysicsLayer.OBSTACLE, hit);
                    if (relativeLocationTarget.angleDeg(relativeLocationEntity) > relativeLocationEntity.angleDeg(relativeLocationTarget)) {
                        //left
                        relativeLocationTarget.rotateAroundDeg(new Vector2(0, 0), turningAngle)
                                .setLength(owner.getEntity().getCenterPosition().dst(hit.point))
                                .add(owner.getEntity().getCenterPosition());
                        this.tragectoryLocation = relativeLocationTarget;
                    } else {
                        //right
                        relativeLocationTarget.rotateAroundDeg(new Vector2(0, 0), -turningAngle)
                                .setLength(owner.getEntity().getCenterPosition().dst(hit.point))
                                .add(owner.getEntity().getCenterPosition());
                        this.tragectoryLocation = relativeLocationTarget;
                    }
                } else {
                    this.tragectoryLocation = target.getCenterPosition();
                }
                //Currently only works in debug mode
                //In the future an aiming line sprite will be drawn
                showTrajectory(tragectoryLocation);
                //Draw shot sprite
                if (!poweringUp) {
                    Entity arrow = WeaponFactory.createFastArrow(tragectoryLocation, getDirectionOfTarget());
                    gameArea.spawnEntityAt(arrow, owner.getEntity().getCenterPosition(), true, true);
                    //Check if hit
                    if (isTargetVisible() && tragectoryLocation.dst(target.getCenterPosition()) < AOE) {
                        int damage = FileLoader.readClass(WeaponConfigs.class, "configs/Weapons.json").fastArrow.baseAttack;
                        target.getComponent(CombatStatsComponent.class).addHealth(-damage);
                    }
                    tragectoryLocation = null;
                }
                break;
        }
    }

    /**
     * Show trajectory before shooting
     *
     * @param loc the location vector
     */
    public void showTrajectory(Vector2 loc) {
        // If there is an obstacle in the path to the player, not visible.
        if (physics.raycast(owner.getEntity().getCenterPosition(), loc, PhysicsLayer.OBSTACLE, hit)) {
            debugRenderer.drawLine(owner.getEntity().getCenterPosition(), hit.point, Color.YELLOW, 1);
        } else {
            debugRenderer.drawLine(owner.getEntity().getCenterPosition(), loc, Color.YELLOW, 1);
        }
    }

    /**
     * Set the chance that the entity shoot more than one arrow at a time
     *
     * @param multishotChance chance for the entity to shoot multiple arrows
     */
    public void setMultishotChance(double multishotChance) {
        this.multishotChance = multishotChance;
    }

    /**
     * Set the type of the projectile - whether it multishots, follow target or show trajectory
     *
     * @param projectileType type of arrow
     */
    public void setProjectileType(String projectileType) {
        this.projectileType = projectileType;
    }

    /**
     * return the priority of arrow task
     *
     * @return highest priority if can shoot, else -1
     */
    @Override
    public int getPriority() {
        if (canShoot() || poweringUp) {
            return 20;
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
     * @param direction  1 to calculate right arrow, -1 to calculate left arrow
     * @param multiplier how many arrows over to calculate
     * @return direction arrow should go
     */
    private float getMultishotDirection(int direction, int multiplier) {
        //creates a nice ring effect at multishots above 8
        float angle = (float) (360 / (Math.max(8, Math.floor(multishotChance)) * 2 + 1));
        return (getDirectionOfTarget() + ((-direction) * angle * multiplier));
    }

    /**
     * @param direction  1 to calculate right arrow, -1 to calculate left arrow
     * @param multiplier how many arrows over to calculate
     * @return direction arrow should go
     */
    private Vector2 getMultishotVector(int direction, int multiplier) {
        //creates a nice ring effect at multishots above 8
        float angle = (float) (360 / (Math.max(8, Math.floor(multishotChance)) * 2 + 1));
        Vector2 v1 = owner.getEntity().getCenterPosition().cpy();
        Vector2 v2 = target.getCenterPosition().cpy();
        Vector2 v3 = v2.cpy().sub(v1); //heading relative to entity
        v3.rotateAroundDeg(new Vector2(0, 0), ((-direction) * angle * multiplier));
        v3.scl(30);
        v3.add(v1);
        return (v3);
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
        //to.add(owner.getEntity().getCenterPosition().sub(owner.getEntity().getPosition()));
        debugRenderer.drawLine(from, to);
        return true;
    }

    /**
     * check if target can shoot based on given cooldown of the shooting and target is visible
     *
     * @return true if can shoot, false otherwise
     */
    private boolean canShoot() {
        return (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFired >= cooldownMS
                && isTargetVisible() && getDistanceToTarget() < owner.getEntity().getAttackRange());
    }
}
