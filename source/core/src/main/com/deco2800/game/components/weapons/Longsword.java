package com.deco2800.game.components.weapons;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
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

    public Longsword(short targetLayer, int attackPower, float knockback, Vector2 weaponSize) {
        super(targetLayer, attackPower, knockback, weaponSize);

        attackSound = ServiceLocator.getResourceService().
                getAsset("sounds/swish.ogg", Sound.class);
        impactSound = ServiceLocator.getResourceService()
                .getAsset("sounds/impact.ogg", Sound.class);
    }

    /**
     * Attacks, but also plays animation.
     *
     * @see MeleeWeapon
     */
    @Override
    public void attack(int attackDirection) {
        super.attack(attackDirection);
        AnimationRenderComponent animator = entity.getComponent(AnimationRenderComponent.class);
        if (animator == null) {
            return;
        }
        switch (attackDirection) {
            case UP:
                animator.startAnimation("longsword_up");
                break;
            case DOWN:
                animator.startAnimation("longsword_down");
                break;
            case LEFT:
                animator.startAnimation("longsword_left");
                break;
            case RIGHT:
                animator.startAnimation("longsword_right");
                break;
        }
    }
}
