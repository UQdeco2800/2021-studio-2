package com.deco2800.game.components.touch;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;

public class ExplosionTouchComponent extends TouchComponent {
    private final float knockbackForce;
    private long start = 0;
    private boolean disable = false;

    public ExplosionTouchComponent(short targetLayer, short myLayer, float knockback) {
        super(targetLayer, myLayer);
        this.knockbackForce = knockback;
    }

    @Override
    void onCollisionStart(Fixture me, Fixture other) {
        if (disable) {
            return;
        }
        if (!this.checkEntities(me, other)) {
            return;
        }
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        if (target.getComponent(ColliderComponent.class) == null) {
            //Not a target
            return;
        }
        if ((targetLayer & target.getComponent(ColliderComponent.class).getLayer()) == 0) {
            //Not a target
            return;
        }

        // Apply Initial knockback
        PhysicsComponent physicsComponent = target.getComponent(PhysicsComponent.class);
        if (physicsComponent != null && (knockbackForce > 0f) || (hitboxComponent.getFixture() != me)) {
            assert physicsComponent != null;
            Body targetBody = physicsComponent.getBody();
            Vector2 direction = target.getCenterPosition().sub(entity.getCenterPosition());
            Vector2 impulse = direction.setLength(knockbackForce);
            targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
        }
        applyContinuousDamage(me, other);
    }

    /**
     * ensures collision is still being calculated until not colliding
     */
    @Override
    public void update() {
        if (disable) {
            return;
        }
        if (inCollision) {
            for (Fixture fixture : collidingFixtures) {
                applyContinuousDamage(hitboxComponent.getFixture(), fixture);
            }
        }
    }

    @Override
    public void dispose() {
        disable = true;
    }

    /**
     * actions to apply when the hitbox component is colliding
     *
     * @param me    the owner of the hitbox
     * @param other the target of the hitbox
     */
    protected void applyContinuousDamage(Fixture me, Fixture other) {
        if (disable) {
            return;
        }
        if (!this.checkEntities(me, other)) {
            return;
        }
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        if (target.getComponent(ColliderComponent.class) == null) {
            //Not a target
            return;
        }
        if ((targetLayer & target.getComponent(ColliderComponent.class).getLayer()) == 0) {
            //Not a target
            return;
        }
        //Explosion damage
        CombatStatsComponent combatStats = new CombatStatsComponent(1, 100);

        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);

        // Try to attack target.
        if (targetStats != null && ((System.currentTimeMillis() - start) / 1000.0) > 0.5) {
            targetStats.hit(combatStats);
            start = System.currentTimeMillis();
        }

        // Apply continuous knockback
        PhysicsComponent physicsComponent = target.getComponent(PhysicsComponent.class);
        if (physicsComponent != null && (knockbackForce > 0f) || (hitboxComponent.getFixture() != me)) {
            assert physicsComponent != null;
            Body targetBody = physicsComponent.getBody();
            Vector2 direction = target.getCenterPosition().sub(entity.getCenterPosition());
            Vector2 impulse = direction.setLength(0.5f);
            targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
            targetBody.setLinearVelocity(targetBody.getLinearVelocity().clamp(-knockbackForce * 10, knockbackForce * 10));
        }
    }
}

