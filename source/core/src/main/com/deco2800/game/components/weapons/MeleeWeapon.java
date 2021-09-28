package com.deco2800.game.components.weapons;

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
import com.deco2800.game.services.ServiceLocator;

/**
 * Melee weapon superclass from which all melee weapons will inherit from.
 * Enables entities to attack using a weapon.
 */
public class MeleeWeapon extends Component {
    /**
     * animation frame duration measured in milliseconds
     */
    protected long attackFrameDuration;

    /** num of light attack frames in animation */
    protected int numOfAttackFrames;

    /** the index of the frame where the attack lands */
    protected int attackFrameIndex;

    /**
     * determines whether entity has attacked.
     */
    protected boolean hasAttacked;

    /**
     * Time when the entity last attacked, 0 if entity is not attacking.
     */
    protected long timeAtAttack;

    /**
     * Weapon Damage
     */
    protected final int attackPower;
    /**
     * The physics layer the weapon can damage
     */
    protected final short targetLayer;
    /**
     * Physical Knockback of weapon
     */
    protected final float knockback;

    /**
     * Base stats of owner entity
     */
    protected CombatStatsComponent combatStats;

    /**
     * Weapon attack width and range in terms of x and y, relative to entity size
     */
    protected Vector2 weaponSize;
    /**
     * Hit box used by this melee weapon. NOTE: Multiple melee weapons equipped on the same
     * entity can re-use the same weapon hit box instance, provided they aren't setting/destroying
     * it simultaneously.
     */
    protected WeaponHitboxComponent weaponHitbox;

    /**
     * attack direction (see below for constants)
     */
    private int attackDirection;
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
        this.weaponSize = weaponSize;
        timeAtAttack = 0L;
        // default attack frame infomration
        setAttackFrames(100L, 3, 1);
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
        if (timeAtAttack != 0L) {
            // calculate elapsed time since attack started
            long currentTime = ServiceLocator.getTimeSource().getTime();
            long timeSinceAttacked = currentTime - timeAtAttack;
            triggerAttackStage(timeSinceAttacked);
        }
    }

    /**
     * Weapon attack, called by owner entity.
     * Sets flags used in update(), to sync the attack with the animation.
     * Actual functionality for attacking is implemented inside triggerAttackStage()
     *
     * @param attackDirection direction of attack (see above for defined constants).
     */
    public void attack(int attackDirection) {
        if (timeAtAttack != 0 || hasAttacked) return;
        this.attackDirection = attackDirection;
        timeAtAttack = ServiceLocator.getTimeSource().getTime();
        hasAttacked = true;
    }

    /**
     * The weapon's strong / alternative attack. This is to be implemented
     * in weapon sub-classes.
     */
    public void strongAttack(int attackDirection) {
        // to be implemented in sub-class
    }

    /**
     * Used by update() to sync weapon hit box with animation.
     *
     * @param timeSinceAttack the time elapsed since the entity last attacked
     *                        0 - if the entity is not attacking.
     */
    protected void triggerAttackStage(long timeSinceAttack) {
        // Set hit box during attack frame
        if (hasAttacked && timeSinceAttack > (attackFrameDuration * attackFrameIndex)
                && timeSinceAttack < (attackFrameIndex + 1) * attackFrameDuration) {
            weaponHitbox.set(weaponSize.cpy(), attackDirection);
            hasAttacked = false; // use flag to ensure weapon is only set once.
            // Destroy hit box as soon as attack frame ends.
        } else if (timeSinceAttack >= (attackFrameIndex + 1) * attackFrameDuration) {
            timeAtAttack = 0;
            weaponHitbox.destroy();
        }
    }

    /**
     * Sets frame duration, which determines how long an attack lasts.
     *
     * @param frameDuration how long each attack animation frame takes.
     */
    public void setAttackFrameDuration(long frameDuration) {
        this.attackFrameDuration = frameDuration;
    }

    /**
     * Sets all frame information, which determines when an attack lands,
     * and how long the attack is.
     * @param frameDuration how long each attack animation frame takes in milliseconds
     * @param numOfFrames number of frames in the attack animation
     * @param attackIndex the index of the frame where the attack lands. Attack lasts for
     *                    1 frame duration.
     */
    public void setAttackFrames(long frameDuration, int numOfFrames, int attackIndex) {
        this.attackFrameDuration = frameDuration;
        this.numOfAttackFrames = numOfFrames;
        this.attackFrameIndex = attackIndex;
    }

    /**
     * Returns the total time the attack will take.
     *
     * @return total time of attack
     */
    public long getTotalAttackTime() {
        return numOfAttackFrames * attackFrameDuration; // number of frames * frame duration.
    }

    /**
     * Collision method used by weapon to detect collisions with enemy entities,
     * defined by the target layer.
     *
     * @param me    - the fixture used to represent this weapon
     * @param other - the fixture that our weapon is colliding with.
     * @return true - if weapon collided with enemy target
     * false - otherwise.
     * @see com.deco2800.game.components.TouchAttackComponent
     */
    protected boolean onCollisionStart(Fixture me, Fixture other) {

        if (weaponHitbox == null || weaponHitbox.getFixture() != me) {
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
            Vector2 direction = target.getCenterPosition().sub(entity.getCenterPosition());
            Vector2 impulse = direction.setLength(knockback);
            targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
        }
        return true; // successfully collided with target.
    }
}
