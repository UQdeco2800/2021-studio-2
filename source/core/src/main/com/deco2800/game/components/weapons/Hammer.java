package com.deco2800.game.components.weapons;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.WeaponFactory;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Hammer: Similar to Axe but implements AOE lightning attack functionality.
 */
public class Hammer extends MeleeWeapon {

    /**
     * Sound that plays every axe swing
     */
    private final Sound attackSound;
    /**
     * Sound that plays when axe hits enemy
     */
    private final Sound impactSound;

    /**
     * AOE / Strong attack size
     */
    private final Vector2 strongAttackSize;
    /**
     * Determines whether the axe has used its strong attack
     */
    private boolean hasStrongAttacked;

    private boolean hasRangeAttacked;

    private HammerProjectile projectile;

    public Hammer(short targetLayer, int attackPower, float knockback, Vector2 weaponSize) {

        super(targetLayer, attackPower, knockback, weaponSize);
        attackSound = ServiceLocator.getResourceService().
                getAsset("sounds/swish.ogg", Sound.class);
        impactSound = ServiceLocator.getResourceService()
                .getAsset("sounds/impact.ogg", Sound.class);
        strongAttackSize = new Vector2(2f, 2f); // default size
        hasStrongAttacked = false;
        hasRangeAttacked = false;
    }

    /**
     * Attacks, but also plays animation.
     *
     * @see MeleeWeapon
     */
    @Override
    public void attack(int attackDirection) {
        if (timeAtAttack != 0 || hasAttacked
                || hasStrongAttacked) return;
        super.attack(attackDirection);
        AnimationRenderComponent animator = entity.getComponent(AnimationRenderComponent.class);
        if (animator == null) {
            return;
        }

        switch (attackDirection) {
            case UP:
                animator.startAnimation("up_hammer_attack");
                break;
            case DOWN:
                animator.startAnimation("down_hammer_attack");
                break;
            case LEFT:
                animator.startAnimation("left_hammer_attack");
                break;
            case RIGHT:
                animator.startAnimation("right_hammer_attack");
                break;
        }
    }

    /**
     * Attacks using an AOE (meleeWeapon.CENTER) direction. The attack will
     * connect with any enemies immediately around the entity.
     *
     * @param attackDirection - direction of attack, ignored for the time being.
     */
    @Override
    public void strongAttack(int attackDirection) {
        if (timeAtAttack != 0 || hasStrongAttacked) {
            return;
        }
        hasStrongAttacked = true;
        super.attack(MeleeWeapon.CENTER);
        AnimationRenderComponent animator = entity.getComponent(AnimationRenderComponent.class);
        if (animator == null) {
            return;
        }
        animator.startAnimation("hammer_aoe");
    }

    @Override
    public void rangedAttack(int attackDirection) {
        if (hasRangeAttacked) {
            projectile.recall();
            return;
        }
        Vector2 target = entity.getPosition();
        float range = 6f;
        switch (attackDirection) {
            case UP:
                target.y += range;
                break;
            case DOWN:
                target.y -= range;
                break;
            case LEFT:
                target.x -= range;
                break;
            case RIGHT:
                target.x += range;
                break;
        }
        // Spawn projectile
        Entity rangedMjolnir = WeaponFactory.createMjolnir(this.targetLayer, target, this);
        this.projectile = rangedMjolnir.getComponent(HammerProjectile.class);
        ServiceLocator.getGameAreaService().spawnEntityAt(
                rangedMjolnir, entity.getPosition(), true, true);
        attackSound.play();
        hasRangeAttacked = true;
    }

    public void destroyProjectile() {
        projectile = null;
        hasRangeAttacked = false;
    }

    public boolean isEquipped() {
        return hasRangeAttacked;
    }

    /**
     * Implements functionality for strong attacks, also plays attack sound
     * during attack frame (for both light and strong).
     *
     * @see MeleeWeapon
     */
    @Override
    protected void triggerAttackStage(long timeSinceAttack) {
        if (timeSinceAttack > attackFrameDuration * attackFrameIndex &&
                timeSinceAttack <= (attackFrameIndex + 1) * attackFrameDuration) {
            if (hasStrongAttacked) {
                attackSound.play();
                weaponHitbox.set(strongAttackSize.cpy(), MeleeWeapon.CENTER);
                hasStrongAttacked = false;
                hasAttacked = false; // strong attack overrides light attack.

            } else if (hasAttacked) {
                attackSound.play();
            }
        }
        super.triggerAttackStage(timeSinceAttack);
    }

    /**
     * Plays impact sound if weapon successfully collides with enemy.
     *
     * @see MeleeWeapon
     */
    @Override
    protected boolean onCollisionStart(Fixture me, Fixture other) {
        // if weapon collides with enemy, play impact sound
        if (super.onCollisionStart(me, other)) {
            impactSound.play();
            return true;
        }
        return false;
    }
}
