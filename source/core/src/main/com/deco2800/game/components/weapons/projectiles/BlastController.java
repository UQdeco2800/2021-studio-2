package com.deco2800.game.components.weapons.projectiles;

/**
 * Component that is the main controller of the projectile entity, "Blast", shot from Scepter
 */
public class BlastController extends ProjectileController {
    /**
     * The blast disappears on a hit
     */
    @Override
    protected void onHit() {
        entity.prepareDispose();
    }
}
