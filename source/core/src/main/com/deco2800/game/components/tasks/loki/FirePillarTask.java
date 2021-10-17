package com.deco2800.game.components.tasks.loki;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.WeaponFactory;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class FirePillarTask extends DefaultTask implements PriorityTask {

    /** Required to spawn the entity which will damage the player. */
    private GameArea gameArea;

    /** Physics Engine to handle collisions between enemy and attack.  */
    private PhysicsEngine physics;

    /** Provides a view of the entities debug information. */
    private DebugRenderer debugRenderer;

    /** The target entity the enemy will attempt to attack. */
    private Entity target;

    /** THe delay between each attacks for this task. */
    private long cooldownMS;

    /** Last time that the attack was created. */
    private long lastFiredTime;

    /** Time of the last shoot animation played. */
    private long lastShootAnimation;

    /** Duration of the shoot animation time in Milliseconds. */
    private long shootAnimationTimeMS;

    /** List of previous positions of the player. */
    private LinkedList<Vector2> lastPositions;

    /** List of the previous Fire Pillar entities for disposal. */
    private LinkedList<Entity> firePillars;

    /** Count used to delay the ticks between pillars spawned. */
    private int count;

    private static final Logger logger = LoggerFactory.getLogger(FirePillarTask .class);

    /**
     * Constructor for the AI task that will allow the enemy to create fire pillars.
     *
     * @param target     The entity to chase.
     * @param cooldownMS how long to wait in MS before shooting again
     */
    public FirePillarTask(Entity target, long cooldownMS, long shootAnimationTimeMS) {
        this.target = target;
        this.cooldownMS = cooldownMS;
        this.shootAnimationTimeMS = shootAnimationTimeMS;
        this.gameArea = ServiceLocator.getGameAreaService();
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
        lastPositions = new LinkedList<>();
        firePillars = new LinkedList<>();

        lastShootAnimation = 0;
        lastFiredTime = 0;
        count = 0;
    }

    @Override
    public void start() {
        if (canAttack()) {
            logger.debug("Boss is able to attack and will spawn a fire pillar");
            owner.getEntity().getComponent(PhysicsMovementComponent.class).setMoving(false);
            spawnPillar();
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
                && isTargetVisible() && getDistanceToTarget() < owner.getEntity().getAttackRange());
    }

    /**
     * Creates a pillar at the last location of the player, recorded at a delay.
     */
    private void spawnPillar() {
        lastPositions.add(target.getPosition().cpy());
        lastFiredTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
        if (lastPositions.size() < 2) {
            return;
        }

        shootAnimation();

        logger.debug("Boss Spawning Fire Pillar");

        Entity pillar =
            WeaponFactory.createFirePillarBase();
        Vector2 lastPosition = lastPositions.remove(0);
        Vector2 position = new Vector2(lastPosition.x + target.getPosition().x,
                lastPosition.y + target.getPosition().y);

        gameArea.spawnEntityAt(pillar, new Vector2(position.x / 2, position.y / 2), true, true);
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
     * Return the position of the target and return the angle from the entity (owner) to the target
     *
     * @return Float angle from owner to target
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
     * Plays the animation in the direction of the attack.
     */
    private void shootAnimation() {
        lastShootAnimation = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
        float targetDir = (getDirectionOfTarget() + 360 - 45) % 360; //shift axis
        if (targetDir > 0 && targetDir < 90) { //if arrow of the angle is between 0 and 90 degrees use left shoot animation
            owner.getEntity().getEvents().trigger("attackDown");
        } else if (targetDir > 90 && targetDir < 180) {
            owner.getEntity().getEvents().trigger("attackRight");
        } else if (targetDir > 180 && targetDir < 270) {
            owner.getEntity().getEvents().trigger("attackUp");
        } else if (targetDir > 270 && targetDir < 360) {
            owner.getEntity().getEvents().trigger("attackLeft");
        }
    }

    /**
     * return the priority of arrow task
     *
     * @return highest priority if can shoot, else -1
     */
    @Override
    public int getPriority() {
        if (owner.getEntity().getEntityType().equals("transformed")) {
            return -1;
        }
        if (canAttack() || TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - lastShootAnimation < shootAnimationTimeMS) {
            return 20;
        }
        return -1;
    }
}
