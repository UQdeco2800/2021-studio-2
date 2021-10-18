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

    private long timeSinceRangeAttack;
    private final long RANGE_COOLDOWN = 500L;

    private GameArea gameArea;
    private final float range = 6f;

    public Scepter(short targetLayer, int attackPower, float knockback, Vector2 weaponSize) {
        super(targetLayer, attackPower, knockback, weaponSize);
        attackSound = ServiceLocator.getResourceService().
                getAsset("sounds/swish.ogg", Sound.class);
        impactSound = ServiceLocator.getResourceService()
                .getAsset("sounds/impact.ogg", Sound.class);
        this.gameArea = ServiceLocator.getGameAreaService();
        timeSinceRangeAttack = 0L;
    }

    @Override
    public void update() {
        super.update();
        long currentTime = ServiceLocator.getTimeSource().getTime();
        if (timeSinceRangeAttack != 0L && currentTime - timeSinceRangeAttack > RANGE_COOLDOWN) {
            timeSinceRangeAttack = 0L;
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
            case DOWN:
                animator.startAnimation("down_attack");
                break;
            case LEFT:
                animator.startAnimation("left_scepter_attack");
                break;
            case RIGHT:
                animator.startAnimation("right_scepter_attack");
                break;
        }
    }

    /**
     * Attacks using an AOE (meleeWeapon.CENTER) direction. The attack will
     * connect with any enemies immediately around the entity.
     */
    @Override
    public void rangedAttack(int attackDirection) {
        if (timeSinceRangeAttack != 0) return; // cooldown hasn't expired yet
        super.rangedAttack(attackDirection);
        Vector2 target = entity.getCenterPosition();
        switch (attackDirection) {
            case UP:
                target.y += this.range;
                break;
            case DOWN:
                target.y -= this.range;
                break;
            case LEFT:
                target.x -= this.range;
                break;
            case RIGHT:
                target.x += this.range;
                break;
        }
        Entity blast = WeaponFactory.createBlast(target);
        gameArea.spawnEntityAt(blast, entity.getCenterPosition(), true, true);
        timeSinceRangeAttack = ServiceLocator.getTimeSource().getTime();
    }

    /**
     * Implements functionality for strong attacks, also plays attack sound
     * during attack frame (for both light and strong).
     *
     * @see MeleeWeapon
     */
    @Override
    protected void triggerAttackStage(long timeSinceAttack) {
        if (timeSinceAttack > attackFrameDuration && timeSinceAttack < 3 * attackFrameDuration) {
            if (hasAttacked) {
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
