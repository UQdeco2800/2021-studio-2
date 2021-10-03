package com.deco2800.game.components.weapons;

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
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component that is the main controller of the projectile entity.
 */
public class HammerProjectile extends Component {
    private Vector2 start;
    private HitboxComponent hitbox;
    private PhysicsMovementComponent movingComponent;
    private final int attackPower;
    private final int knockback;
    private final short targetLayer;
    protected CombatStatsComponent combatStats;

    private int status;
    int THROWING = 0;
    int RECALLING = 1;
    int STATIC = 2;

    private long gameTime;
    protected Vector2 target;

    private Hammer owner;

    /**
     * Component that is the main controller of the projectile entity.
     */
    public HammerProjectile(short targetLayer) {
        this.targetLayer = targetLayer;
        this.status = THROWING;
        this.attackPower = 5;
        this.knockback = 25;
        this.gameTime = ServiceLocator.getTimeSource().getTime();
    }

    /**
     * Starts variables and listeners
     */
    @Override
    public void create() {
        super.create();
        this.start = entity.getPosition();
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        this.hitbox = entity.getComponent(HitboxComponent.class);
        this.movingComponent = entity.getComponent(PhysicsMovementComponent.class);
        this.combatStats = entity.getComponent(CombatStatsComponent.class);
    }

    /**
     * Destroys the blast if it's at the end of its lifespan
     */
    @Override
    public void update() {
        super.update();
        // Set next stage if at target
        triggerAttackStage();
    }

    /**
     * Sets direction and velocity of projectile depending on current stage of
     * projectile.
     */
    public void triggerAttackStage() {
        Vector2 position = entity.getPosition();
        if (this.status == THROWING) {
            float distance = 6f;
            if ((Math.abs(position.x - start.x) > distance || Math.abs(position.y - start.y) > distance) ||
                    (ServiceLocator.getTimeSource().getTime() - gameTime) > 1000L) {
                this.status = STATIC;
                movingComponent.setMoving(false);
                hitbox.setEnabled(false);
            }
            // Do nothing if static
        } else if (this.status == RECALLING) {
            // update target & distance
            movingComponent.setTarget(owner.getEntity().getPosition());
            float distance = entity.getPosition().dst(owner.getEntity().getPosition());
            if (distance < 0.25f ||
                    (ServiceLocator.getTimeSource().getTime() - gameTime) > 1000L) {
                owner.destroyProjectile();
                entity.prepareDispose();
            }
        }
    }

    /**
     * Sets
     */
    public void recall() {
        System.out.println("recalling!");
        movingComponent.setMoving(true);
        hitbox.setEnabled(true);
        this.gameTime = ServiceLocator.getTimeSource().getTime();
        this.status = RECALLING;
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
                targetStats.weaponHit(attackPower);
            } else {
                targetStats.hit(combatStats, attackPower);
            }
        }

        // Apply knockback
        PhysicsComponent physicsComponent = target.getComponent(PhysicsComponent.class);
        if (physicsComponent != null && knockback > 0f) {
            Body targetBody = physicsComponent.getBody();
            Vector2 direction = target.getPosition().sub(entity.getPosition());
            Vector2 impulse = direction.setLength(knockback);
            targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
        }
        return true; // successfully collided with target.
    }

    public HammerProjectile setOwner(Hammer owner) {
        this.owner = owner;
        return this;
    }
}
