package com.deco2800.game.components.weapons.projectiles;

/**
 * Component that is the main controller of the projectile entity, "Blast", shot from Scepter
 */
public class BlastController extends ProjectileController {
    /**
     * the blast's unique stats are used
     */
    @Override
    public void create() {
        super.create();
    }

    /**
     * The blast disappears on a hit
     */
    @Override
    protected void onHit() {
        entity.prepareDispose();
    }
}
