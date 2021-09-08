package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;


/**
 * When this entity touches a valid enemy's hitbox, deal damage to them and apply a knockback.
 *
 * <p>Requires CombatStatsComponent, HitboxComponent on this entity.
 *
 * <p>Damage is only applied if target entity has a CombatStatsComponent. Knockback is only applied
 * if target entity has a PhysicsComponent.
 */

public class TouchAttackComponent extends TouchComponent {
    private float knockbackForce = 0f;
    private CombatStatsComponent combatStats;
    private long start = 0;

    /**
     * Create a component which attacks entities on collision, without knockback.
     *
     * @param targetLayer The physics layer of the target's collider.
     */
    public TouchAttackComponent(short targetLayer) {
        super(targetLayer);
    }

    /**
     * Create a component which attacks entities on collision, with knockback.
     *
     * @param targetLayer The physics layer of the target's collider.
     * @param knockback   The magnitude of the knockback applied to the entity.
     */
    public TouchAttackComponent(short targetLayer, float knockback) {
        super(targetLayer);
        this.knockbackForce = knockback;
    }

    @Override
    public void create() {
        super.create();
        combatStats = entity.getComponent(CombatStatsComponent.class);
    }

    /**
     * action apply when the hitbox component collide
     *
     * @param me    the owner of the hitbox
     * @param other the target of the hitbox
     */
    @Override
    protected void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            //do nothing
            return;
        }

        if (PhysicsLayer.notContains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }

        // Try to attack target.
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
        if (targetStats != null && ((System.currentTimeMillis() - start) / 1000.0) > 0.5) {
            targetStats.hit(combatStats);
            start = System.currentTimeMillis();
        }

        // Apply knockback
        //Maybe this should be called during collision as sometimes hitboxes can overlap without onCollision being called in time
        PhysicsComponent physicsComponent = target.getComponent(PhysicsComponent.class);
        if ((physicsComponent != null && knockbackForce > 0f) || (hitboxComponent.getFixture() != me)) {
            if (physicsComponent != null) {
                Body targetBody = physicsComponent.getBody();
                Vector2 direction = target.getCenterPosition().sub(entity.getCenterPosition());
                Vector2 impulse = direction.setLength(knockbackForce);
                targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
            }
        }

        //Dissolve arrow attacks after hits
        if (getEntity().getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.PROJECTILEWEAPON) {
            //Remove later on to make arrows stick into walls and more
            getEntity().prepareDispose();
        }
    }
}
