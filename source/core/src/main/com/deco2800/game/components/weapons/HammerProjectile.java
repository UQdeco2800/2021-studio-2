package com.deco2800.game.components.weapons;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.tasks.MovementTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component that is the main controller of the projectile entity.
 */
public class HammerProjectile extends Component {

    /** Sound that plays when hammer hits enemy */
    private final Sound impactSound;

    /** Start position of projectile */
    private Vector2 start;
    /** Hitbox used by projectile */
    private HitboxComponent hitbox;
    /** Movement component used by projectile */
    private PhysicsMovementComponent movingComponent;

    /** Damage the projectile does */
    private final int attackPower;
    /** Knockback applied when projectile hits target */
    private final int knockback;
    /** The target layer that the projectile will damage */
    private final short targetLayer;
    /** CombatStats of owner entity */
    protected CombatStatsComponent combatStats;

    /** Current status of Mjolnir. See below for constants */
    private int status;
    int THROWING = 0;
    int RECALLING = 1;
    int STATIC = 2;

    /** Game time since last notable event: e.g. since creation or since recall was called */
    private long gameTime;
    /** The target that the projectile will move towards */
    protected Vector2 target;
    /** The hammer component that controls the projectile */
    private final Hammer owner;

    /**
     * Component that is the main controller of the projectile entity.
     */
    public HammerProjectile(short targetLayer, Hammer owner) {
        this.targetLayer = targetLayer;
        impactSound = ServiceLocator.getResourceService()
                .getAsset("sounds/impact.ogg", Sound.class);
        this.status = THROWING;
        this.attackPower = 5;
        this.knockback = 25;
        this.owner = owner;
        this.gameTime = ServiceLocator.getTimeSource().getTime();
    }

    /**
     * Starts variables and listeners
     */
    @Override
    public void create() {
        super.create();

        this.start = entity.getPosition();
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        this.hitbox = entity.getComponent(HitboxComponent.class);
        this.movingComponent = entity.getComponent(PhysicsMovementComponent.class);
        this.combatStats = entity.getComponent(CombatStatsComponent.class);
    }

    /**
     * Destroys the blast if it's at the end of its lifespan
     */
    @Override
    public void update() {
        super.update();
        triggerAttackStage();
    }

    /**
     * Sets direction and velocity of projectile depending on current stage of
     * projectile.
     */
    public void triggerAttackStage() {
        Vector2 position = entity.getPosition();

        if (this.status == THROWING) {
            float distance = 6f;
            // Check if projectile has reached target, or has timed out (2 seconds)
            if ((Math.abs(position.x - start.x) > distance || Math.abs(position.y - start.y) > distance) ||
                    (ServiceLocator.getTimeSource().getTime() - gameTime) > 2000L) {
                // Make projectile static.
                this.status = STATIC;
                movingComponent.setMoving(false);
                hitbox.setEnabled(false);
            }
        // Do nothing if static (changed by recall function
        } else if (this.status == RECALLING) {
            // Update target as being position of owner entity.
            movingComponent.setTarget(owner.getEntity().getPosition());
            // Distance is current distance between projectile and owner entity
            float distance = entity.getPosition().dst(owner.getEntity().getPosition());
            // Terminate when target reached (< 0.25f away from owner)
            if (distance < 0.25f ||
                    (ServiceLocator.getTimeSource().getTime() - gameTime) > 1000L) {
                owner.destroyProjectile();
                entity.prepareDispose();
            }
        }
    }

    /**
     * Sets projectile to recall (fly back to owner entity). Actual target is set
     * by triggerAttackStage().
     */
    public void recall() {
        movingComponent.setMoving(true);
        hitbox.setEnabled(true);
        this.gameTime = ServiceLocator.getTimeSource().getTime();
        this.status = RECALLING;
    }

    /**
     * Collision method used to detect whether projectile has collided with enemy
     * @param me fixture of this projectile
     * @param other fixture of colliding entity.
     * @return if successful collision
     */
    protected boolean onCollisionStart(Fixture me, Fixture other) {

        if (hitbox == null || hitbox.getFixture() != me) {
            // Not triggered by weapon hit box, ignore
            return false;
        }

        if (PhysicsLayer.notContains(this.targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return false;
        }

        // Try to attack target.
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
        if (targetStats != null) {
            impactSound.play();
            // add entity's base attack to attack, if they exist.
            if (combatStats == null) {
                targetStats.weaponHit(attackPower);
            } else {
                targetStats.hit(combatStats, attackPower);
            }
        }

        // Apply knockback
        PhysicsComponent physicsComponent = target.getComponent(PhysicsComponent.class);
        if (physicsComponent != null && knockback > 0f) {
            Body targetBody = physicsComponent.getBody();
            Vector2 direction = target.getPosition().sub(entity.getPosition());
            Vector2 impulse = direction.setLength(knockback);
            targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
        }
        return true; // successfully collided with target.
    }
}
