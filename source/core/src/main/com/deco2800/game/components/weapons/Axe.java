package com.deco2800.game.components.weapons;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Represents the axe used by entities. Main difference to its superclass is that
 * Axe uses axe-related assets. In future development this will have different attack functionality,
 * including combos, strong-attacks, etc.
 */
public class Axe extends MeleeWeapon {
    //private final Sound attackSound;
    //private final Sound impactSound;


    public Axe(short targetLayer, int attackPower, float knockback, Vector2 weaponSize) {
        super(targetLayer, attackPower, knockback, weaponSize);
        //attackSound = ServiceLocator.getResourceService().getAsset("sounds/swish.ogg", Sound.class);
        //impactSound = ServiceLocator.getResourceService().getAsset("sounds/impact.ogg", Sound.class);
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
        switch (attackDirection) {
            case UP:
                animator.startAnimation("BackAxe_Attack");
                break;
            case DOWN:
                animator.startAnimation("FrontAxe_Attack");
                break;
            case LEFT:
                animator.startAnimation("LeftAxe_Attack");
                break;
            case RIGHT:
                animator.startAnimation("RightAxe_Attack");
                break;
        }
    }

    /**
     * Plays attack sound during attack frame.
     * @see MeleeWeapon
     */
    @Override
    protected void triggerAttackStage(long timeSinceAttack) {
        if (hasAttacked && timeSinceAttack > frameDuration & timeSinceAttack < 2 * frameDuration) {
            //attackSound.play();
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
            //impactSound.play();
            return true;
        }
        return false;
    }
}
