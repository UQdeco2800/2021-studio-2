package com.deco2800.game.components.weapons.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Component that is the main controller of the projectile entity, "Blast", shot from Scepter
 */
public abstract class ProjectileController extends Component {
    protected HitboxComponent hitbox;
    protected short targetLayer;
    protected CombatStatsComponent combatStats;
    protected long gameTime;
    protected boolean hit;

    /**
     * Component that is the main controller of the projectile entity, "Blast", shot from Scepter
     */
    protected ProjectileController() {
        this.gameTime = ServiceLocator.getTimeSource().getTime();
    }

    /**
     * Starts variables and listeners
     */
    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        this.hitbox = entity.getComponent(HitboxComponent.class);
        this.targetLayer = PhysicsLayer.NPC;
        this.combatStats = entity.getComponent(CombatStatsComponent.class);
        this.hit = false;
    }

    /**
     * Destroys the blast if it's at the end of its lifespan
     */
    @Override
    public void update() {
        if (hit) {
            entity.prepareDispose();
        }
        if ((ServiceLocator.getTimeSource().getTime() - gameTime) > BlastStats.PROJECTILE_LIFESPAN) {
            this.onHit();
        }
    }

    /**
     * Collision method used to detect whether projectile has collided with enemy
     *
     * @param me    fixture of this projectile
     * @param other fixture of colliding entity.
     */
    protected void onCollisionStart(Fixture me, Fixture other) {

        if (hitbox == null || hitbox.getFixture() != me) {
            // Not triggered by weapon hit box, ignore
            return;
        }

        if (PhysicsLayer.notContains(this.targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }

        // Try to attack target.
        Entity targetEntity = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = targetEntity.getComponent(CombatStatsComponent.class);
        if (targetStats != null) {
            targetStats.weaponHit(BlastStats.ATTACK_POWER);
        }

        // Apply knockback
        PhysicsComponent physicsComponent = targetEntity.getComponent(PhysicsComponent.class);
        if (physicsComponent != null && BlastStats.KNOCKBACK > 0f) {
            Body targetBody = physicsComponent.getBody();
            Vector2 direction = targetEntity.getCenterPosition().sub(entity.getCenterPosition());
            Vector2 impulse = direction.setLength(BlastStats.KNOCKBACK);
            targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
        }
        this.onHit();
    }

    /**
     * Abstract function that reacts to a collision with enemies, varies depending on weapon
     */
    protected abstract void onHit();
}
