package com.deco2800.game.components.weapons;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
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
}
