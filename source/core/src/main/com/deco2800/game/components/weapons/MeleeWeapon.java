package com.deco2800.game.components.weapons;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.WeaponHitboxComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Melee weapon superclass from which all melee weapons will inherit from.
 * Enables entities to attack using a weapon.
 */
public class MeleeWeapon extends Component {
    protected final long frameDuration; // animation frame duration measured in milliseconds
    protected boolean hasAttacked; // determines whether entity has attacked.

    private long timeAtAttack; // time when the entity last attacked
                               // 0 if entity is not attacking.

    protected int attackPower; // damage
    protected short targetLayer; // what physics layer the weapon can damage
    protected float knockback; // physical knockback of weapon
    protected CombatStatsComponent combatStats; // base stats of entity

    /* Weapon attack width & range in terms of x & y, relative to entity size */
    private final Vector2 weaponSize;
    /* Hit box used by this melee weapon. NOTE: Multiple melee weapons equipped on the same
    entity can re-use the same weapon hit box instance, provided they are never setting/destroying
    it simultaneously. */
    private WeaponHitboxComponent weaponHitbox;

    private int attackDirection; // attack direction (see below for constants)
    public static final int CENTER = 0; // used for AOE (area of effect) attacks
    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;

    public MeleeWeapon(short targetLayer, int attackPower, float knockback,
                       Vector2 weaponSize) {
        this.targetLayer = targetLayer;
        this.attackPower = attackPower;
        this.knockback = knockback;
        this.weaponSize= weaponSize;
        timeAtAttack = 0L;
        frameDuration = 100L; // default frame duration is set at 100 milliseconds.
        hasAttacked = false;
    }

    @Override
    public void create() {
        combatStats = entity.getComponent(CombatStatsComponent.class);
        weaponHitbox = entity.getComponent(WeaponHitboxComponent.class);
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
    }

    /**
     * Update uses timer to synchronise attack with animation.
     * Assumes that animation has only 3 frames.
     */
    @Override
    public void update() {
        if (timeAtAttack != 0) {
            // calculate elapsed time since attack started
            long currentTime = ServiceLocator.getTimeSource().getTime();
            long timeSinceAttacked = currentTime - timeAtAttack;
            triggerAttackStage(timeSinceAttacked);
        }
    }

    /**
     * Weapon attack, called by owner entity.
     * Sets flags used in update(), to sync the attack with the animation.
     * Actual functionality for attacking is implemented inside update().
     * @param attackDirection direction of attack (see above for defined constants).
     */
    public void attack(int attackDirection) {
        this.attackDirection = attackDirection;
        timeAtAttack = ServiceLocator.getTimeSource().getTime();
        entity.getComponent(AnimationRenderComponent.class).startAnimation("");
        entity.getEvents().trigger("lockMovement", 3 * frameDuration);
        hasAttacked = true;
    }

    /**
     * Used by update() to sync weapon hit box with animation.
     * @param timeSinceAttack the time elapsed since the entity last attacked
     *                        0 - if the entity is not attacking.
     */
    protected void triggerAttackStage(long timeSinceAttack) {
        // Set hit box during attack frame (2nd frame).
        if (hasAttacked && timeSinceAttack > frameDuration && timeSinceAttack < 2 * frameDuration) {
            weaponHitbox.set(weaponSize.cpy(),  attackDirection);
            hasAttacked = false;
        // Destroy hit box as soon as attack frame ends (3rd frame).
        } else if (timeSinceAttack >= 3 * frameDuration) {
            timeAtAttack = 0;
            weaponHitbox.destroy();
        }
    }

    /**
     * Returns the total time the attack will take. Assumes the attack animation only has
     * 3 frames.
     * @return total time of attack
     */
    public long getTotalAttackTime() {
        return 3 * frameDuration; // number of frames * frame duration.
    }

    /**
     * Collision method used by weapon to detect collisions with enemy entities,
     * defined by the target layer.
     * @see com.deco2800.game.components.TouchAttackComponent
     * @return true - if weapon collided with enemy target
     *         false - otherwise.
     */
    protected boolean onCollisionStart(Fixture me, Fixture other) {

        if (weaponHitbox == null || weaponHitbox.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return false;
        }

        if (!PhysicsLayer.contains(this.targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return false;
        }

        // Try to attack target.
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
        if (targetStats != null) {
            targetStats.hit(combatStats);
        }

        // Apply knockback
        PhysicsComponent physicsComponent = target.getComponent(PhysicsComponent.class);
        if (physicsComponent != null && knockback > 0f) {
            Body targetBody = physicsComponent.getBody();
            Vector2 direction = target.getCenterPosition().sub(entity.getCenterPosition());
            Vector2 impulse = direction.setLength(knockback);
            targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
        }
        return true; // successfully collided with target.
    }
}






