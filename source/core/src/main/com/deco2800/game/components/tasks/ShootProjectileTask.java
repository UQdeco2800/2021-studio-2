package com.deco2800.game.components.tasks;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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
import java.util.concurrent.TimeUnit;
import java.util.Random;


/** Spawns an arrow to shoot at a target */
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
    private boolean followTarget = false;
    private boolean showTrajectory = false;
    private double multishotChance = 0.00;
    private String projectileType = "normalArrow";

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


    private void shootingSound(){
        Sound arrowEffect = ServiceLocator.getResourceService().getAsset("sounds/arrow_shoot.mp3", Sound.class);
        arrowEffect.play();
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
        if (canShoot()) {
            shootingSound();
            shoot();
        }
    }

    /**
     * Spawns in an arrow according to the classes variables
     */
    public void shoot() {
        lastFired = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
        if (projectileType.equals("normalArrow")) {
            Entity arrow = WeaponFactory.createNormalArrow(target.getCenterPosition(), getDirectionOfTarget());
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
                multishotChanceTemp -=1;
            }
        }
    }

    /**
     * set the target to follow target
     * @param followTarget true if follow, false otherwise
     */
    public void setFollowTarget(boolean followTarget) {
        this.followTarget = followTarget;
    }

    /**
     * Set the arrow to show trajectory before shot
     * @param showTrajectory true if show, false otherwise
     */
    public void setShowTrajectory(boolean showTrajectory) {
        this.showTrajectory = showTrajectory;
    }

    /**
     * Set the chance that the entity shoot more than one arrow at a time
     * @param multishotChance chance for the entity to shoot multiple arrows
     */
    public void setMultishotChance(double multishotChance) {
        this.multishotChance = multishotChance;
    }

    /**
     * Set the type of the projectile - whether it multishots, follow target or show trajectory
     * @param projectileType type of arrow
     */
    public void setProjectileType(String projectileType) {
        this.projectileType = projectileType;
    }

    /**
     * Stop the arrow task
     */
    @Override
    public void stop() {
        super.stop();
    }

    /**
     * return the priority of arrow task
     * @return highest priority if can shoot, else -1
     */
    @Override
    public int getPriority() {
        if (canShoot()) {
            return 20;
        }
        return -1;
    }

    /**
     * return the distance of the entity to the target
     * @return return the d
     */
    private float getDistanceToTarget() {
        return owner.getEntity().getCenterPosition().dst(target.getPosition());
    }

    /**
     * return the position of the target and return the angle from the entity (owner) to the target
     * @return float angle from owner to target
     */
    private float getDirectionOfTarget() {
        Vector2 v1 = owner.getEntity().getCenterPosition().cpy();
        Vector2 v2 = target.getCenterPosition().cpy();
        Vector2 v3 = v1.cpy().sub(v2);
        return (v3.angleDeg());
    }

    /**
     *
     * @param direction 1 to calculate right arrow, -1 to calculate left arrow
     * @param multiplier how many arrows over to calculate
     * @return direction arrow should go
     */
    private float getMultishotDirection(int direction, int multiplier) {
        //creates a nice ring effect at multishots above 8
        float angle = (float) (360/(Math.max(8, Math.floor(multishotChance))*2+1));
        return (getDirectionOfTarget() + ((-direction) * angle * multiplier));
    }

    /**
     *
     * @param direction 1 to calculate right arrow, -1 to calculate left arrow
     * @param multiplier how many arrows over to calculate
     * @return direction arrow should go
     */
    private Vector2 getMultishotVector(int direction, int multiplier) {
        //creates a nice ring effect at multishots above 8
        float angle = (float) (360/(Math.max(8, Math.floor(multishotChance))*2+1));
        Vector2 v1 = owner.getEntity().getCenterPosition().cpy();
        Vector2 v2 = target.getCenterPosition().cpy();
        Vector2 v3 = v2.cpy().sub(v1); //heading relative to entity
        v3.rotateAroundDeg(new Vector2(0,0), ((-direction) * angle * multiplier));
        v3.add(v1);
        return (v3);
    }

    /**
     * check if target is block by any object
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
     * @return true if can shoot, false otherwise
     */
    private boolean canShoot() {
        if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFired >= cooldownMS) {
        }

        return (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFired >= cooldownMS
                && isTargetVisible() && getDistanceToTarget() < owner.getEntity().getAttackRange());
    }
}
