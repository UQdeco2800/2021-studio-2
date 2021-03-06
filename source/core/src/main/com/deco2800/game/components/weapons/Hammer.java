package com.deco2800.game.components.weapons;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.weapons.projectiles.HammerProjectile;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.WeaponFactory;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Hammer: Similar to Axe but implements AOE lightning attack functionality.
 */
public class Hammer extends MeleeWeapon {

    /**
     * Sound that plays every hammer swing
     */
    private final Sound attackSound;
    /**
     * Sound that plays when hammer hits enemy
     */
    private final Sound impactSound;
    /**
     * Sound that plays when hammer hits enemy
     */
    private final Sound aoeSound;

    /**
     * AOE / Strong attack size
     */
    private final Vector2 strongAttackSize;
    /**
     * Determines whether the axe has used its strong attack
     */
    private boolean hasStrongAttacked;
    private long timeSinceStrongAttack;
    private static final long STRONG_COOLDOWN = 3000L;

    private boolean hasRangeAttacked;
    private long timeSinceRangeAttack;
    private static final long RANGE_COOLDOWN = 2000L;

    private HammerProjectile projectile;

    private AnimationRenderComponent animator;

    public Hammer(short targetLayer, int attackPower, float knockback, Vector2 weaponSize) {

        super(targetLayer, attackPower, knockback, weaponSize);
        attackSound = ServiceLocator.getResourceService().
                getAsset("sounds/swish.ogg", Sound.class);
        impactSound = ServiceLocator.getResourceService()
                .getAsset("sounds/clank.mp3", Sound.class);
        aoeSound = ServiceLocator.getResourceService()
                .getAsset("sounds/lightning.mp3", Sound.class);
        strongAttackSize = new Vector2(2f, 2f); // default size
        hasStrongAttacked = false;
        hasRangeAttacked = false;
        timeSinceStrongAttack = 0L;
        timeSinceRangeAttack = 0L;
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
        if (timeSinceRangeAttack != 0L && currentTime - timeSinceRangeAttack > RANGE_COOLDOWN) {
            timeSinceRangeAttack = 0L;
        }
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
        if (isAttacking()) return;
        super.attack(attackDirection);
        if (animator == null) {
            return;
        }
        switch (attackDirection) {
            case UP:
                animator.startAnimation("up_hammer_attack");
                break;
            case LEFT:
                animator.startAnimation("left_hammer_attack");
                break;
            case RIGHT:
                animator.startAnimation("right_hammer_attack");
                break;
            default:
                animator.startAnimation("down_hammer_attack");
                break;
        }
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
        aoeSound.play(0.2f);
        if (animator == null) {
            return;
        }
        animator.startAnimation("hammer_aoe");
        timeSinceStrongAttack = ServiceLocator.getTimeSource().getTime();
    }

    @Override
    public void rangedAttack(int attackDirection) {
        if (hasRangeAttacked) {
            projectile.recall();
            animator.startAnimation("hammer_recall");
            return;
            // if already attacking, or cool-down hasn't expired yet, do nothing.
        } else if (isAttacking() || timeSinceRangeAttack != 0) {
            return;
        }
        Vector2 target = entity.getPosition();
        float range = 6f;
        switch (attackDirection) {
            case UP:
                target.y += range;
                animator.startAnimation("up_throw");
                break;
            case LEFT:
                target.x -= range;
                animator.startAnimation("left_throw");
                break;
            case RIGHT:
                target.x += range;
                animator.startAnimation("right_throw");
                break;
            default:
                target.y -= range;
                animator.startAnimation("down_throw");
                break;
        }
        // Spawn projectile
        Entity rangedMjolnir = WeaponFactory.createMjolnir(this.targetLayer, target, this);
        this.projectile = rangedMjolnir.getComponent(HammerProjectile.class);
        ServiceLocator.getGameAreaService().spawnEntityAt(
                rangedMjolnir, entity.getPosition(), true, true);
        attackSound.play();
        hasRangeAttacked = true;
        timeSinceRangeAttack = ServiceLocator.getTimeSource().getTime();
    }

    public void destroyProjectile() {
        projectile.getEntity().prepareDispose();
        animator.startAnimation("hammer_catch");
        projectile = null;
        hasRangeAttacked = false;
    }

    public boolean isEquipped() {
        return !hasRangeAttacked;
    }

    private boolean isAttacking() {
        return timeAtAttack != 0 || hasAttacked || hasStrongAttacked || hasRangeAttacked;
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
            impactSound.play(0.2f, 0.4f, 0f);
            return true;
        }
        return false;
    }
}
