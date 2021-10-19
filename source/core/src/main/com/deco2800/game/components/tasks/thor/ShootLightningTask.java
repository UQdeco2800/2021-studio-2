package com.deco2800.game.components.tasks.thor;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.WeaponFactory;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class ShootLightningTask extends DefaultTask implements PriorityTask {

    /**
     * Required to spawn the entity which will damage the player.
     */
    private final GameArea gameArea;

    /**
     * The target entity the enemy will attempt to attack.
     */
    private final Entity target;

    /**
     * THe delay between each attacks for this task.
     */
    private final long cooldownMS;

    /**
     * Last time that the attack was created.
     */
    private long lastFiredTime;

    /**
     * Time of the last shoot animation played.
     */
    private long lastShootAnimation;

    /**
     * Duration of the shoot animation time in Milliseconds.
     */
    private final long shootAnimationTimeMS;

    /**
     * List of previous positions of the player.
     */
    private final LinkedList<Vector2> lastPositions;

    private static final Logger logger = LoggerFactory.getLogger(ShootLightningTask.class);

    private final Random randomBooleanGenerator;

    /**
     * Constructor for the AI task that will allow the enemy to create fire pillars.
     *
     * @param target     The entity to chase.
     * @param cooldownMS how long to wait in MS before shooting again
     */
    public ShootLightningTask(Entity target, long cooldownMS, long shootAnimationTimeMS) {
        this.target = target;
        this.cooldownMS = cooldownMS;
        this.shootAnimationTimeMS = shootAnimationTimeMS;
        this.gameArea = ServiceLocator.getGameAreaService();
        lastPositions = new LinkedList<>();
        lastShootAnimation = 0;
        lastFiredTime = 0;
        this.randomBooleanGenerator = new SecureRandom();
    }

    @Override
    public void start() {
        if (canAttack()) {
            logger.debug("Boss is able to attack and will spawn a fire pillar");
            owner.getEntity().getComponent(PhysicsMovementComponent.class).setMoving(false);
            if (randomBooleanGenerator.nextBoolean()) {
                spawnLighning();
            } else {
                spawnAOE();
            }
        }
    }

    /**
     * Returns whether the enemy can attack.
     *
     * @return True if the enemy can attack, false if not
     */
    private boolean canAttack() {

        logger.debug("Checking if the boss can attack");

        return (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastFiredTime >= cooldownMS
                && getDistanceToTarget() < owner.getEntity().getAttackRange());
    }

    /**
     * Creates a pillar at the last location of the player, recorded at a delay.
     */
    private void spawnLighning() {
        lastPositions.add(target.getPosition().cpy());
        lastFiredTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
        if (lastPositions.size() < 2) {
            return;
        }

        shootAnimation();

        logger.debug("Boss Spawning Lightning");

        Entity pillar =
                WeaponFactory.createLightning(PhysicsLayer.PLAYER, LightningProjectile.FOLLOW_PLAYER);
        Vector2 lastPosition = lastPositions.remove(0);
        Vector2 position = new Vector2(lastPosition.x + target.getPosition().x,
                lastPosition.y + target.getPosition().y);

        gameArea.spawnEntityAt(pillar, new Vector2(position.x / 2, position.y / 2), true, true);
    }

    /**
     * Creates a pillar on top of boss, recorded at a delay.
     */
    private void spawnAOE() {
        lastPositions.add(target.getPosition().cpy());
        lastFiredTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
        if (lastPositions.size() < 2) {
            return;
        }

        shootAnimation();

        logger.debug("Boss Spawning Lightning");

        Entity pillar =
                WeaponFactory.createLightning(PhysicsLayer.PLAYER, LightningProjectile.FOLLOW_PLAYER);

        gameArea.spawnEntityAt(pillar, owner.getEntity().getPosition(), true, true);
    }

    /**
     * Returns the distance of the entity to the target
     *
     * @return Returns the distance as a float between the player and the enemy
     */
    private float getDistanceToTarget() {
        return owner.getEntity().getCenterPosition().dst(target.getPosition());
    }

    /**
     * Plays the animation in the direction of the attack.
     */
    private void shootAnimation() {
        lastShootAnimation = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
        owner.getEntity().getComponent(AnimationRenderComponent.class).startAnimation("hammer_aoe");
    }

    /**
     * return the priority of arrow task
     *
     * @return highest priority if can shoot, else -1
     */
    @Override
    public int getPriority() {
        if (canAttack() || TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastShootAnimation < shootAnimationTimeMS) {
            return 20;
        }
        return -1;
    }
}
