package com.deco2800.game.components.tasks;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.touch.TouchAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.LineEntity;
import com.deco2800.game.entities.configs.WeaponConfigs;
import com.deco2800.game.entities.factories.WeaponFactory;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.files.UserSettings;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.physics.raycast.RaycastHit;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * Spawns an arrow to shoot at a target
 */
public class ShootProjectileTask extends DefaultTask implements PriorityTask {
    /**
     * entity represent the target - player
     */
    private final Entity target;
    /**
     * control the game flow
     */
    private final PhysicsEngine physics;
    /**
     * debug screen
     */
    private final DebugRenderer debugRenderer;
    /**
     * cast a line from owner to target
     */
    private final RaycastHit hit = new RaycastHit();
    /**
     * time before can shoot again
     */
    private long cooldownMS;
    /**
     * time when begin to shoot arrow
     */
    private long lastFired;
    /**
     * time when begin to shoot fireball
     */
    private long lastCreatedFireball;
    /**
     * Game area to spawn arrow
     */
    private final GameArea gameArea;
    /**
     * the location to spawn the arrow
     */
    private Vector2 tragectoryLocation = null;
    /**
     * chance to shoot multiple arrow
     */
    private double multishotChance = 0.00;
    /**
     * the type of arrow
     */
    private projectileTypes projectileType = projectileTypes.normalArrow;
    /**
     * if the power up is trigger
     */
    private boolean poweringUp = false;
    /**
     * the line to aim for fast arrow
     */
    private LineEntity aimingLine = null;
    /**
     * time to play shoot animation - projectile shoot
     */
    private long shootAnimationTimeMS;
    /**
     * time of the first animation
     */
    private long shootAnimationStart = 0;
    /**
     * count so that the boss only rampage once
     */
    private int count = 0;
    /**
     * logger for boss turning rampage
     */
    private static final Logger logger = LoggerFactory.getLogger(ShootProjectileTask.class);

    /**
     * time when the boss start rampage
     */
    private long rampageStart = 0;

    public enum projectileTypes {
        normalArrow,
        trackingArrow,
        fastArrow,
        fireBall
    }


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
        lastCreatedFireball = 0;
    }

    /**
     * set how long to pause and play animation after firing
     *
     * @param shootAnimationTimeMS time in MS to pause when shooting
     */
    public void setShootAnimationTimeMS(long shootAnimationTimeMS) {
        this.shootAnimationTimeMS = shootAnimationTimeMS;
    }

    /**
     * set how long to wait before firing again
     *
     * @param cooldownMS time in MS to pause after shooting
     */
    public void setCooldownMS(long cooldownMS) {
        this.cooldownMS = cooldownMS;
    }

    /**
     * Set the time of the last arrow fired to 0 and generate new fireballs;
     */
    @Override
    public void start() {
        lastFired = 0;
    }

    /**
     * create fireballs when needed
     *
     * @return true if fireballs are present
     */
    private boolean checkFireBalls() {
        //Stops the fireballs from being created until ready.
        //Specifically so the boss doesnt create them before he teleports
        if (projectileType.equals(projectileTypes.fireBall)) {
            if (owner.getEntity().data.get("createFireBall").equals(true)) {
                if (!owner.getEntity().data.containsKey("fireBalls")) {
                    //create fireball list
                    Entity[] entities = new Entity[]{
                            null,
                            WeaponFactory.createFireBall(target, owner.getEntity(), new Vector2(0, 1)),
                            null
                    };
                    gameArea.spawnEntityAt(entities[1], owner.getEntity().getCenterPosition(),
                            true, true);
                    owner.getEntity().data.put("fireBalls", entities);
                    lastCreatedFireball = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
                    return (true);
                } else if (projectileType.equals(projectileTypes.fireBall) && TimeUnit.NANOSECONDS.toMillis(
                        System.nanoTime()) - lastCreatedFireball >= cooldownMS * 2.5) {
                    //Add new fireball
                    int index = 0;
                    Entity[] entities = (Entity[]) owner.getEntity().data.get("fireBalls");
                    for (Entity fireball : entities) {
                        if (!ServiceLocator.getEntityService().getEntities().contains(
                                fireball, true)) {
                            entities[index] = WeaponFactory.createFireBall(target,
                                    owner.getEntity(), new Vector2(index - 1, 1));
                            gameArea.spawnEntityAt(entities[index],
                                    owner.getEntity().getCenterPosition(),
                                    true, true);
                            lastCreatedFireball = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
                            return (true);
                        }
                        index++;
                    }
                } else if (projectileType.equals(projectileTypes.fireBall)) {
                    //Check for fireball but don't make one
                    Entity[] entities = (Entity[]) owner.getEntity().data.get("fireBalls");
                    for (Entity fireball : entities) {
                        if (ServiceLocator.getEntityService().getEntities().contains(
                                fireball, true)) {
                            if (fireball.data.get("fireBallMovement").equals(false)) {
                                return (true);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * gets the next fireball assuming it exists
     *
     * @return next fireball to cast
     */
    private Entity getNextFireBall() {
        Entity[] entities = (Entity[]) owner.getEntity().data.get("fireBalls");
        for (Entity fireball : entities) {
            if (ServiceLocator.getEntityService().getEntities().contains(fireball, true)) {
                if (fireball.data.get("fireBallMovement").equals(false)) {
                    return (fireball);
                }
            }
        }
        return (null);
    }

    /**
     * update the arrow - check whether the entity can shoot the arrow or not
     */
    @Override
    public void update() {
        if (canShoot() || poweringUp) {
            owner.getEntity().getComponent(PhysicsMovementComponent.class).setMoving(false);
            //trigger shoot projectile animations here
            shoot();
        }
        checkFireBalls();
    }

    private void shootAnimation() {
        shootAnimationStart = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
        float targetDir = (getDirectionOfTarget() + 360 - 45) % 360; //shift axis
//        if (owner.getEntity().getEntityType() != null && this.owner.getEntity().getEntityType().equals("assassin")) {
//            if (targetDir > 0 && targetDir < 90) { //if arrow of the angle is between 0 and 90 degrees use left shoot animation
//                owner.getEntity().getEvents().trigger("assassinDownShoot");
//            } else if (targetDir > 90 && targetDir < 180) {
//                owner.getEntity().getEvents().trigger("assassinRightShoot");
//            } else if (targetDir > 180 && targetDir < 270) {
//                owner.getEntity().getEvents().trigger("assassinUpShoot");
//            } else if (targetDir > 270 && targetDir < 360) {
//                owner.getEntity().getEvents().trigger("assassinLeftShoot");
//            }
//        } else if (owner.getEntity().getEntityType() != null && this.owner.getEntity().getEntityType().equals("ranged")) {
//            if (targetDir > 0 && targetDir < 90) { //if arrow of the angle is between 0 and 90 degrees use left shoot animation
//                owner.getEntity().getEvents().trigger("rangedDownShoot");
//            } else if (targetDir > 90 && targetDir < 180) {
//                owner.getEntity().getEvents().trigger("rangedRightShoot");
//            } else if (targetDir > 180 && targetDir < 270) {
//                owner.getEntity().getEvents().trigger("rangedUpShoot");
//            } else if (targetDir > 270 && targetDir < 360) {
//                owner.getEntity().getEvents().trigger("rangedLeftShoot");
//            }
//        } else {
//            if (targetDir > 0 && targetDir < 90) { //if arrow of the angle is between 0 and 90 degrees use left shoot animation
//                owner.getEntity().getEvents().trigger("attackDown");
//            } else if (targetDir > 90 && targetDir < 180) {
//                owner.getEntity().getEvents().trigger("attackRight");
//            } else if (targetDir > 180 && targetDir < 270) {
//                owner.getEntity().getEvents().trigger("attackUp");
//            } else if (targetDir > 270 && targetDir < 360) {
//                owner.getEntity().getEvents().trigger("attackLeft");
//            }
//        }
    }

    /**
     * Spawns in a projectile according to the class' variables
     */
    public void shoot() {
        if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - shootAnimationStart >= shootAnimationTimeMS) {
            if (!poweringUp) {
                lastFired = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
            }
            switch (projectileType) {
                case normalArrow: {
                    shootNormalArrow();
                    break;
                }
                case trackingArrow: {
                    shootTrackingArrow();
                    break;
                }
                case fastArrow: {
                    powerupFastArrow();
                    break;
                }
                case fireBall: {
                    shootFireball();
                    break;
                }
            }
        }
    }

    private void shootNormalArrow() {
        Random rand = new SecureRandom();
        Vector2 relativeLoc = target.getPosition().cpy().sub(owner.getEntity().getPosition());
        relativeLoc.scl(30);
        relativeLoc.add(owner.getEntity().getPosition());
        Entity arrow = WeaponFactory.createNormalArrow(relativeLoc, getDirectionOfTarget());
        gameArea.spawnEntityAt(arrow, owner.getEntity().getCenterPosition(), true, true);
        int multiplier = 0;
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
        shootAnimation();
    }

    private void shootTrackingArrow() {
        Random rand = new SecureRandom();
        //Spawns arrows in a different location on a circle around the entity
        Vector2 offset = owner.getEntity().getCenterPosition().cpy().sub(owner.getEntity().getPosition());
        offset.setAngleDeg(getDirectionOfTarget());
        //creates a nice ring effect at multishots above 8
        float angle = (float) (360 / (Math.max(8, Math.floor(multishotChance)) * 2 + 1));

        Entity arrow = WeaponFactory.createTrackingArrow(target, getDirectionOfTarget());
        gameArea.spawnEntityAt(arrow, owner.getEntity().getPosition().cpy().sub(offset), true, true);
        int multiplier = 0;
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
        shootAnimation();
    }

    private void powerupFastArrow() {
        float AOE = 1f;
        if (!poweringUp) {
            poweringUp = true;
        }
        if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFired >= cooldownMS) {
            poweringUp = false;
        }
        if (tragectoryLocation == null) {
            tragectoryLocation = target.getCenterPosition();
        }

        Vector2 relativeLocationTarget = tragectoryLocation.cpy()
                .sub(owner.getEntity().getCenterPosition());

        //Rotate archer
        float targetDir = (relativeLocationTarget.angleDeg() + 180 - 45) % 360; //shift axis
//        if (targetDir > 0 && targetDir < 90) { //if arrow of the angle is between 0 and 90 degrees use left shoot animation
//            owner.getEntity().getEvents().trigger("DownStart");
//        } else if (targetDir > 90 && targetDir < 180) {
//            owner.getEntity().getEvents().trigger("RightStart");
//        } else if (targetDir > 180 && targetDir < 270) {
//            owner.getEntity().getEvents().trigger("UpStart");
//        } else if (targetDir > 270 && targetDir < 360) {
//            owner.getEntity().getEvents().trigger("LeftStart");
//        }

        updateTrajectory(AOE);
        float fade = ((float) TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFired) / cooldownMS;
        Color newColor = new Color(Color.YELLOW);
        newColor.a = 0.5f;
        newColor.g = fade;
        newColor = new Color(2.0f * fade, 2.0f * (1 - fade), 0f, 0.5f);
        aimingLine.getComponent(TextureRenderComponent.class).getSprite()
                .setColor(newColor);
        shootFastArrow(AOE);
    }

    private void updateTrajectory(float AOE) {
        float turningAngle = 30f / UserSettings.get().fps;
        Vector2 relativeLocationTarget = tragectoryLocation.cpy()
                .sub(owner.getEntity().getCenterPosition());
        Vector2 relativeLocationEntity = target.getCenterPosition().cpy()
                .sub(owner.getEntity().getCenterPosition());

        if (relativeLocationTarget.angleDeg(relativeLocationEntity) > turningAngle
                && relativeLocationEntity.angleDeg(relativeLocationTarget) > turningAngle) {
            if (relativeLocationTarget.angleDeg(relativeLocationEntity)
                    > relativeLocationEntity.angleDeg(relativeLocationTarget)) {
                //left
                relativeLocationTarget.rotateAroundDeg(new Vector2(0, 0), turningAngle);
            } else {
                //right
                relativeLocationTarget.rotateAroundDeg(new Vector2(0, 0), -turningAngle);
            }
            //If obstacle is blocking the way
            if (physics.raycast(owner.getEntity().getCenterPosition(), tragectoryLocation, PhysicsLayer.OBSTACLE, hit)) {
                if (tragectoryLocation.dst(target.getCenterPosition()) < AOE) {
                    //add 0.1f to make sure it still collides
                    relativeLocationTarget.setLength(Math.min(owner.getEntity().getCenterPosition().dst(hit.point) + 0.1f, relativeLocationEntity.len()));
                } else {
                    //add 0.1f to make sure it still collides
                    relativeLocationTarget.setLength(Math.min(owner.getEntity().getCenterPosition().dst(hit.point) + 0.1f, owner.getEntity().getAttackRange()));
                }
            } else {
                if (tragectoryLocation.dst(target.getCenterPosition()) < AOE) {
                    relativeLocationTarget.setLength(Math.min(owner.getEntity().getAttackRange(), relativeLocationEntity.len()));
                } else {
                    relativeLocationTarget.setLength(owner.getEntity().getAttackRange());
                }
            }
            relativeLocationTarget.add(owner.getEntity().getCenterPosition());
            this.tragectoryLocation = relativeLocationTarget;
        } else {
            this.tragectoryLocation = relativeLocationEntity
                    .setLength(Math.min(owner.getEntity().getAttackRange(), relativeLocationEntity.len()))
                    .add(owner.getEntity().getCenterPosition());
        }
        if (aimingLine != null) {
            aimingLine.setTarget(tragectoryLocation, owner.getEntity().getCenterPosition());
        } else {
            aimingLine = WeaponFactory.aimingLine(cooldownMS);
            gameArea.spawnEntityAt(aimingLine, aimingLine.setTarget(tragectoryLocation, owner.getEntity().getCenterPosition()), true, true);
        }
        showTrajectory();
    }

    private void shootFastArrow(float AOE) {
        if (!poweringUp) {
            Entity arrow = WeaponFactory.createFastArrow(
                    tragectoryLocation.cpy().sub(owner.getEntity().getCenterPosition())
                            .scl(30).add(owner.getEntity().getCenterPosition())
                    , getDirectionOfTarget());
            gameArea.spawnEntityAt(arrow, owner.getEntity().getCenterPosition(), true, true);
            //Check if hit
            if (isTargetVisible() && tragectoryLocation.dst(target.getCenterPosition()) < AOE) {
                int damage = FileLoader.readClass(WeaponConfigs.class, "configs/Weapons.json").fastArrow.baseAttack;
                target.getComponent(CombatStatsComponent.class).addHealth(-damage);
            } else {
                arrow.data.put("dealDamage", false);
            }
            tragectoryLocation = null;
            aimingLine.prepareDispose();
            aimingLine = null;
            shootAnimation();
        }
    }

    private void shootFireball() {
        if (checkFireBalls()) {
            //TrackingArrowConfig config = new TrackingArrowConfig();
            Entity fireBall = getNextFireBall();
            if (fireBall != null) {
                //Change behaviour
                fireBall.setAngle(getDirectionOfTarget());
                fireBall.getComponent(HitboxComponent.class).setLayer(PhysicsLayer.PROJECTILEWEAPON);
                fireBall.data.put("fireBallMovement", true);
                fireBall.getComponent(TouchAttackComponent.class).setTargetLayer(
                        (short) (PhysicsLayer.OBSTACLE | PhysicsLayer.PLAYER));
                //add flying animation.
                AnimationRenderComponent animator = fireBall.getComponent(AnimationRenderComponent.class);
                animator.startAnimation("flying");
            }
            shootAnimation();
        }
    }

    /**
     * Show trajectory before shooting on debug screen
     */
    public void showTrajectory() {
        // If there is an obstacle in the path to the player, not visible.
        if (physics.raycast(owner.getEntity().getCenterPosition(), tragectoryLocation, PhysicsLayer.OBSTACLE, hit)) {
            debugRenderer.drawLine(owner.getEntity().getCenterPosition(), hit.point, Color.YELLOW, 1);
        } else {
            debugRenderer.drawLine(owner.getEntity().getCenterPosition(), tragectoryLocation, Color.YELLOW, 1);
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
    public void setProjectileType(projectileTypes projectileType) {
        this.projectileType = projectileType;
    }

    /**
     * return the priority of arrow task
     *
     * @return highest priority if can shoot, else -1
     */
    @Override
    public int getPriority() {
        if (owner.getEntity().getEntityType().equals("elfBoss")) {
            int health = owner.getEntity().getComponent(CombatStatsComponent.class).getHealth();
            int max = owner.getEntity().getComponent(CombatStatsComponent.class).getMaxHealth();
            if ((float) health / max <= 0.5f) {
                if (count == 0) {
                    logger.info("Berserk mode: Attack Speed x 4");
                    logger.info("Berserk mode: Deal true damage 20% player health");
                    setCooldownMS(500);
                    owner.getEntity().getComponent(CombatStatsComponent.class).setBaseAttack(
                            target.getComponent(CombatStatsComponent.class).getMaxHealth() / 5);
                    rampageStart = System.currentTimeMillis();
                    count++;
                }
                if (ServiceLocator.getGameAreaService().getNumEnemy() != 0) {
                    if ((float) health / max <= 0.25) {
                        logger.info("You can't kill a boss when his minions are alive");
                        owner.getEntity().getComponent(CombatStatsComponent.class).setHealth(max);
                    }
                }
                if (count == 1) {
                    if (System.currentTimeMillis() - rampageStart >= 30000) {
                        logger.info("Berserk off");
                        setCooldownMS(2000);
                        owner.getEntity().getComponent(CombatStatsComponent.class).setHealth(max);
                        owner.getEntity().getComponent(CombatStatsComponent.class).setBaseAttack(0);
                        count++;
                    }
                }
            }
        }
        checkFireBalls();
        if (canShoot() || poweringUp || TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - shootAnimationStart < shootAnimationTimeMS) {
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
     * Check if there are any object between the entity and the target
     *
     * @return true if no object, false otherwise
     */
    private boolean isTargetVisible() {
        return owner.getEntity().canSeeEntity(target);
    }

    /**
     * check if target can shoot based on given cooldown of the shooting and target is visible
     *
     * @return true if can shoot, false otherwise
     */
    private boolean canShoot() {
        if (projectileType.equals(projectileTypes.fireBall) && checkFireBalls()) {
            return (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFired >= cooldownMS
                    && isTargetVisible() && getDistanceToTarget() < owner.getEntity().getAttackRange());
        } else if (!projectileType.equals(projectileTypes.fireBall)) {
            return (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFired >= cooldownMS
                    && isTargetVisible() && getDistanceToTarget() < owner.getEntity().getAttackRange());
        } else {
            return (false);
        }
    }
}
