package com.deco2800.game.components.weapons;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;

public class Longsword extends MeleeWeapon {
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
    private long timeSinceStrongAttack;
    private final long STRONG_COOLDOWN = 2000L;

    private AnimationRenderComponent animator;

    public Longsword(short targetLayer, int attackPower, float knockback, Vector2 weaponSize) {
        super(targetLayer, attackPower, knockback, weaponSize);
        attackSound = ServiceLocator.getResourceService()
                .getAsset("sounds/swish.ogg", Sound.class);
        impactSound = ServiceLocator.getResourceService()
                .getAsset("sounds/clank.mp3", Sound.class);
        strongAttackSize = new Vector2(2f, 2f); // default size
        hasStrongAttacked = false;
        timeSinceStrongAttack = 0L;
    }

    @Override
    public void create() {
        super.create();
        animator = entity.getComponent(AnimationRenderComponent.class);
    }

    @Override
    public void update() {
        super.update();
        long currentTime = ServiceLocator.getTimeSource().getTime();
        if (timeSinceStrongAttack != 0L && currentTime - timeSinceStrongAttack > STRONG_COOLDOWN) {
            timeSinceStrongAttack = 0L;
        }
    }

    /**
     * Attacks, but also plays animation.
     *
     * @see MeleeWeapon
     */
    @Override
    public void attack(int attackDirection) {
        if (timeAtAttack != 0 || hasAttacked) return;
        super.attack(attackDirection);
        if (animator == null) {
            return;
        }
        switch (attackDirection) {
            case UP:
                animator.startAnimation("longsword_up");
                break;
            case LEFT:
                animator.startAnimation("longsword_left");
                break;
            case RIGHT:
                animator.startAnimation("longsword_right");
                break;
            default:
                animator.startAnimation("longsword_down");
                break;
        }
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
     * Attacks using an AOE (meleeWeapon.CENTER) direction. The attack will
     * connect with any enemies immediately around the entity.
     */
    @Override
    public void aoeAttack() {
        // if already attacking, or cooldown hasn't expired yet, do nothing.
        if (isAttacking() || timeSinceStrongAttack != 0) {
            return;
        }
        hasStrongAttacked = true;
        super.attack(MeleeWeapon.CENTER);
        if (animator == null) {
            return;
        }
        animator.startAnimation("longsword_aoe");
        timeSinceStrongAttack = ServiceLocator.getTimeSource().getTime();
    }

    private boolean isAttacking() {
        return timeAtAttack != 0 || hasAttacked || hasStrongAttacked;
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
            impactSound.play(0.2f, 0.4f, 0f);
            return true;
        }
        return false;
    }
}
