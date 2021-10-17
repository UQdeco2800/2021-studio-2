package com.deco2800.game.components.weapons.projectiles;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.weapons.Hammer;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Component that is the main controller of the mjolnir entity, 'shot' from hammer.
 */
public class HammerProjectile extends ProjectileController {

    /**
     * Sound that plays when hammer hits enemy
     */
    private final Sound impactSound;

    /**
     * Start position of projectile
     */
    private Vector2 start;
    /**
     * Movement component used by projectile
     */
    private PhysicsMovementComponent movingComponent;

    /**
     * Damage the projectile does
     */
    private final int attackPower;
    /**
     * Knockback applied when projectile hits target
     */
    private final int knockback;

    /**
     * Current status of Mjolnir. See below for constants
     */
    private int status;
    private final int THROWING = 0;
    private final int RECALLING = 1;

    /**
     * The hammer component that controls the projectile
     */
    private final Hammer owner;

    /**
     * Animator for the projectile
     */
    private AnimationRenderComponent animator;

    /**
     * Component that is the main controller of the projectile entity.
     */
    public HammerProjectile(short targetLayer, Hammer owner) {
        this.targetLayer = targetLayer;
        impactSound = ServiceLocator.getResourceService()
                .getAsset("sounds/impact.ogg", Sound.class);
        this.status = THROWING;
        this.attackPower = 5;
        this.knockback = 5;
        this.owner = owner;
        this.gameTime = ServiceLocator.getTimeSource().getTime();
    }

    /**
     * Starts variables and listeners
     */
    @Override
    public void create() {
        this.start = entity.getPosition();
        this.animator = entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        this.hitbox = entity.getComponent(HitboxComponent.class);
        this.movingComponent = entity.getComponent(PhysicsMovementComponent.class);
        this.combatStats = entity.getComponent(CombatStatsComponent.class);
        animator.startAnimation("hammer");
    }

    @Override
    public void update() {
        triggerAttackStage();
    }

    /**
     * Sets direction and velocity of projectile depending on current stage of
     * projectile.
     */
    public void triggerAttackStage() {
        Vector2 position = entity.getPosition();

        if (this.status == THROWING) {
            float distance = 6f; // default distance
            // Check if projectile has reached target, or has timed out (2 seconds)
            if ((Math.abs(position.x - start.x) > distance || Math.abs(position.y - start.y) > distance) ||
                    (ServiceLocator.getTimeSource().getTime() - gameTime) > 2000L) {
                // Make projectile static.
                this.status = 2;
                animator.stopAnimation();
                animator.startAnimation("default");
                movingComponent.setMoving(false);
                hitbox.setEnabled(false);
            }
            // Do nothing if static (changed by recall function)
        } else if (this.status == RECALLING) {
            // Update target as being position of owner entity.
            movingComponent.setTarget(owner.getEntity().getPosition());
            // Distance is current distance between projectile and owner entity
            float distance = entity.getPosition().dst(owner.getEntity().getPosition());
            // Terminate when target reached (< 0.25f away from owner)
            if (distance < 0.25f ||
                    (ServiceLocator.getTimeSource().getTime() - gameTime) > 2000L) {
                owner.destroyProjectile();
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
        animator.stopAnimation();
        animator.startAnimation("hammer");
    }

    /**
     * Collision method used to detect whether projectile has collided with enemy
     *
     * @param me    fixture of this projectile
     * @param other fixture of colliding entity.
     */
    protected void onCollisionStart(Fixture me, Fixture other) {

        if (hitbox == null || hitbox.getFixture() != me) {
            // Not triggered by weapon hit box, ignore
            return;
        }

        if (PhysicsLayer.notContains(this.targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }

        // Try to attack target.
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
        if (targetStats != null) {
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
        // successfully collided with target.
    }

    @Override
    protected void onHit() {
        impactSound.play();
    }
}
