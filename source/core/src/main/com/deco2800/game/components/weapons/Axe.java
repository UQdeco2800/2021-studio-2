package com.deco2800.game.components.weapons;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Represents the axe used by entities. Main difference to its superclass is that
 * Axe uses axe-related assets, and also has a strong attack which
 * uses an AOE (area of effect) attack.
 */
public class Axe extends MeleeWeapon {
    /** Sound that plays every axe swing */
    private final Sound attackSound;
    /** Sound that plays when axe hits enemy */
    private final Sound impactSound;

    /** AOE / Strong attack size */
    private final Vector2 strongAttackSize;
    /** Determines whether the axe has used its strong attack */
    private boolean hasStrongAttacked;

    public Axe(short targetLayer, int attackPower, float knockback, Vector2 weaponSize) {
        super(targetLayer, attackPower, knockback, weaponSize);
        attackSound = ServiceLocator.getResourceService().
                getAsset("sounds/swish.ogg", Sound.class);
        impactSound = ServiceLocator.getResourceService()
                .getAsset("sounds/impact.ogg", Sound.class);
        strongAttackSize = new Vector2(2f, 2f); // default size
        hasStrongAttacked = false;
    }

    /**
     * Attacks, but also plays animation.
     * @see MeleeWeapon
     */
    @Override
    public void attack(int attackDirection) {
        super.attack(attackDirection);
        AnimationRenderComponent animator =  entity.getComponent(AnimationRenderComponent.class);
        if (animator == null) {
            return;
        }
        /*
        switch (attackDirection) {
            case UP:
                animator.startAnimation("back_axe_attack");
                break;
            case DOWN:
                animator.startAnimation("front_axe_attack");
                break;
            case LEFT:
                animator.startAnimation("left_axe_attack");
                break;
            case RIGHT:
                animator.startAnimation("right_axe_attack");
                break;
        } */

        switch (attackDirection) {
            case UP:
                animator.startAnimation("up_attack");
                break;
            case DOWN:
                animator.startAnimation("down_attack");
                break;
            case LEFT:
                animator.startAnimation("left_attack");
                break;
            case RIGHT:
                animator.startAnimation("right_attack");
                break;
        }
    }

    /**
     * Attacks using an AOE (meleeWeapon.CENTER) direction. The attack will
     * connect with any enemies immediately around the entity.
     */
    public void strongAttack() {
        if (timeAtAttack != 0 || hasStrongAttacked) {
            return;
        }
        hasStrongAttacked = true;
        super.attack(MeleeWeapon.CENTER);
        AnimationRenderComponent animator =  entity.getComponent(AnimationRenderComponent.class);
        if (animator == null) {
            return;
        }
        animator.startAnimation("aoe_attack");
    }

    /**
     * Implements functionality for strong attacks, also plays attack sound
     * during attack frame (for both light and strong).
     * @see MeleeWeapon
     */
    @Override
    protected void triggerAttackStage(long timeSinceAttack) {
        if (timeSinceAttack > frameDuration && timeSinceAttack < 2 * frameDuration) {
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