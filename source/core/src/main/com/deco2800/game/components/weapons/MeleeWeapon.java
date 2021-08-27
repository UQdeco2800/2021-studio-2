package com.deco2800.game.components.weapons;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.WeaponHitboxComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Enables entities to attack using a weapon
 */
public class MeleeWeapon extends Weapon {
    private boolean hasAttacked; // determines whether entity is currently attacking
    private long timeAtAttack; // time when the entity last attacked
    private Sound attackSound;

    /* weapon attack width & range in terms of x & y, relative to entity size */
    private final Vector2 weaponSize;
    private WeaponHitboxComponent weaponHitbox;

    private int attackDirection; // attack direction (see below for constants)
    public static final int CENTER = 0; // used for AOE (area of effect) attacks.
    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;

    public MeleeWeapon(short targetLayer, int attackPower, float knockback,
                       Vector2 weaponSize, long attackDuration) {
        super(targetLayer, attackPower, knockback);
        this.weaponSize= weaponSize;
        this.hasAttacked = false;
        timeAtAttack = 0L;
    }

    @Override
    public void create() {
        super.create();
        weaponHitbox = entity.getComponent(WeaponHitboxComponent.class);
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        this.attackSound = ServiceLocator.getResourceService().getAsset("sounds/Impact4.ogg", Sound.class);
        knockback += 10f;
    }

    /**
     * Used to synchronise attack with animation using timed update.
     */
    @Override
    public void update() {
        if (timeAtAttack != 0) {
            // calculate elapsed time since attack started
            long currentTime = ServiceLocator.getTimeSource().getTime();
            long timeSinceAttacked = currentTime - timeAtAttack;

            if (hasAttacked && (timeSinceAttacked > 200L && timeSinceAttacked < 400L)) {
                weaponHitbox.set(weaponSize,  attackDirection);
                attackSound.play();
                hasAttacked = false;
            } else if (timeSinceAttacked >= 400L) {
                timeAtAttack = 0;
                weaponHitbox.destroy();
            }
        }
    }

    /**
     * Weapon attack, called by owner entity.
     * Sets flags used in update(), to sync the attack with the animation.
     * Actual functionality for attacking is implemented inside update().
     * @param attackDirection direction of attack.
     */
    @Override
    public void attack(int attackDirection) {
        this.attackDirection = attackDirection;
        timeAtAttack = ServiceLocator.getTimeSource().getTime();
        entity.getComponent(AnimationRenderComponent.class).startAnimation("sprite");
        entity.getEvents().trigger("lockMovement", 600L);
        hasAttacked = true;
    }

    /**
     * TRIGGERED BY WEAPON COLLISION IN ATTACK()
     */
    private void onCollisionStart(Fixture me, Fixture other) {

        if (weaponHitbox == null || weaponHitbox.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }

        if (!PhysicsLayer.contains(this.targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
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
    }
}






