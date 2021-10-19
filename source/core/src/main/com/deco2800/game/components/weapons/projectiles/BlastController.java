package com.deco2800.game.components.weapons.projectiles;

import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Component that is the main controller of the projectile entity, "Blast", shot from Scepter
 */
public class BlastController extends ProjectileController {
    /**
     * The blast disappears on a hit
     */
    @Override
    protected void onHit() {
        entity.getComponent(AnimationRenderComponent.class).startAnimation("hit");
        entity.getComponent(PhysicsMovementComponent.class).setMoving(false);
        this.hit = true;
    }
}
