package com.deco2800.game.components.weapons;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.WeaponFactory;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Represents the scepter used by entities. Main difference to its superclass is that
 * scepter uses scepter-related assets, and also has a strong attack which
 * shoots a projectile.
 */
public class Scepter extends MeleeWeapon {
    /**
     * Sound that plays every axe swing
     */
    private final Sound attackSound;
    /**
     * Sound that plays when axe hits enemy
     */
    private final Sound impactSound;

    /**
     * Time at the last range attack. Gets reset to 0 after it exceeds cool down.
     */
    private long timeAtRangeAttack;

    private final GameArea gameArea;

    public Scepter(short targetLayer, int attackPower, float knockback, Vector2 weaponSize) {
        super(targetLayer, attackPower, knockback, weaponSize);
        attackSound = ServiceLocator.getResourceService().
                getAsset("sounds/swish.ogg", Sound.class);
        impactSound = ServiceLocator.getResourceService()
                .getAsset("sounds/impact.ogg", Sound.class);
        this.gameArea = ServiceLocator.getGameAreaService();
        timeAtRangeAttack = 0L;
    }

    @Override
    public void update() {
        super.update();
        long currentTime = ServiceLocator.getTimeSource().getTime();
        long rangedCoolDown = 500L;
        if (timeAtRangeAttack != 0L && currentTime - timeAtRangeAttack > rangedCoolDown) {
            timeAtRangeAttack = 0L;
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
        AnimationRenderComponent animator = entity.getComponent(AnimationRenderComponent.class);
        if (animator == null) {
            return;
        }
        switch (attackDirection) {
            case UP:
                animator.startAnimation("up_attack");
                break;
            case LEFT:
                animator.startAnimation("left_scepter_attack");
                break;
            case RIGHT:
                animator.startAnimation("right_scepter_attack");
                break;
            default:
                animator.startAnimation("down_attack");
                break;
        }
    }

    /**
     * Attacks using an AOE (meleeWeapon.CENTER) direction. The attack will
     * connect with any enemies immediately around the entity.
     */
    @Override
    public void rangedAttack(int attackDirection) {
        if (timeAtRangeAttack != 0) return; // cooldown hasn't expired yet
        super.rangedAttack(attackDirection);
        Vector2 target = entity.getPosition(); // start at origin
        AnimationRenderComponent animator = entity.getComponent(AnimationRenderComponent.class);
        float range = 6f;
        float angle;
        switch (attackDirection) {
            case UP:
                target.y += range;
                angle = 270f;
                animator.startAnimation("up_range");
                break;
            case LEFT:
                target.x -= range;
                angle = 0f;
                animator.startAnimation("left_range");
                break;
            case RIGHT:
                target.x += range;
                angle = 180f;
                animator.startAnimation("right_range");
                break;
            default:
                target.y -= range;
                angle = 90f;
                animator.startAnimation("down_range");
                break;
        }
        Entity blast = WeaponFactory.createBlast(target, angle);
        gameArea.spawnEntityAt(blast, entity.getPosition(), true, true);
        timeAtRangeAttack = ServiceLocator.getTimeSource().getTime();
    }

    /**
     * Implements functionality for strong attacks, also plays attack sound
     * during attack frame (for both light and strong).
     *
     * @see MeleeWeapon
     */
    @Override
    protected void triggerAttackStage(long timeSinceAttack) {
        if (timeSinceAttack > attackFrameDuration && timeSinceAttack < 3 * attackFrameDuration && hasAttacked) {
            attackSound.play();
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
