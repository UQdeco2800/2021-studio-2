package com.deco2800.game.components.weapons.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.tasks.MovementTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component that is the main controller of the projectile entity, "Blast", shot from Scepter
 */
public abstract class ProjectileController extends Component {
    protected BlastStats stats;
    protected HitboxComponent hitbox;
    protected short targetLayer;
    protected CombatStatsComponent combatStats;
    protected final long gameTime;
    protected Vector2 target;

    /**
     * Component that is the main controller of the projectile entity, "Blast", shot from Scepter
     */
    public ProjectileController() {
        this.gameTime = ServiceLocator.getTimeSource().getTime();
    }

    /**
     * Starts variables and listeners
     */
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        this.hitbox = entity.getComponent(HitboxComponent.class);
        this.targetLayer = PhysicsLayer.NPC;
        this.combatStats = entity.getComponent(CombatStatsComponent.class);
    }

    /**
     * Destroys the blast if it's at the end of its lifespan
     */
    @Override
    public void update() {
        super.update();
        if ((ServiceLocator.getTimeSource().getTime() - gameTime) > this.stats.projectileLifespan) {
            entity.prepareDispose();
        }
    }

    /**
     * @param me
     * @param other
     * @return if successful collision
     */
    protected boolean onCollisionStart(Fixture me, Fixture other) {

        if (hitbox == null || hitbox.getFixture() != me) {
            // Not triggered by weapon hit box, ignore
            return false;
        }

        if (PhysicsLayer.notContains(this.targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return false;
        }

        // Try to attack target.
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
        if (targetStats != null) {
            // add entity's base attack to attack, if they exist.
            if (hitbox == null) {
                targetStats.weaponHit(this.stats.attackPower);
            } else {
                targetStats.hit(combatStats, this.stats.attackPower);
            }
        }

        // Apply knockback
        PhysicsComponent physicsComponent = target.getComponent(PhysicsComponent.class);
        if (physicsComponent != null && this.stats.knockback > 0f) {
            Body targetBody = physicsComponent.getBody();
            Vector2 direction = target.getCenterPosition().sub(entity.getCenterPosition());
            Vector2 impulse = direction.setLength(this.stats.knockback);
            targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
        }
        this.onHit();
        return true; // successfully collided with target.
    }

    /**
     * Abstract function that reacts to a collision with enemies, varies depending on weapon
     */
    protected abstract void onHit();
}
