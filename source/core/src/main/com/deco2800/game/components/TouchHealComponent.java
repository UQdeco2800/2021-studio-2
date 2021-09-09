package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;

public class TouchHealComponent extends TouchComponent {
    /**
     * Create a component which allows Player entity to interact with the Health item and give itself health
     *
     * @param targetLayer The physics layer of the target's collider.
     */
    public TouchHealComponent(short targetLayer) {
        super(targetLayer);
    }

    @Override
    public void create() {
        super.create();
    }

    /**
     * On collision with the potion entity the player will gain health based on the
     * potion entities base attack
     * @param me potion entity
     * @param other player entity
     */
    @Override
    void onCollisionStart(Fixture me, Fixture other) {

        if (hitboxComponent.getFixture() != me) {
            //do nothing
            return;
        }

        if (PhysicsLayer.notContains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }

        //get the potions combat stats so we know how much to heal the player
        CombatStatsComponent healthCombatStats = entity.getComponent(CombatStatsComponent.class);

        Entity player = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent playerStats = player.getComponent(CombatStatsComponent.class);

        playerStats.addHealth(healthCombatStats.getBaseAttack()); //heal the player based on the potion entity attack

        //dispose health potion after giving player hp
        ((BodyUserData) me.getBody().getUserData()).entity.prepareDispose();

    }
}
