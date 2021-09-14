package com.deco2800.game.components.weapons;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.tasks.MovementTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.PlayerConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.*;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.MarshalledObject;

public class Blast extends Component {
    private Vector2 start;
    private final float distance = 6f;
    private HitboxComponent hitbox;
    private int attackPower;
    private int knockback;
    private short targetLayer;
    protected CombatStatsComponent combatStats;

    private static final Logger logger = LoggerFactory.getLogger(MovementTask.class);

    private final long gameTime;
    protected Vector2 target;


    public Blast() {
        this.gameTime = ServiceLocator.getTimeSource().getTime();
    }

    @Override
    public void create() {
        super.create();
        this.start = entity.getCenterPosition();
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        this.hitbox = entity.getComponent(HitboxComponent.class);
        this.attackPower = 50;
        this.knockback = 100;
        this.targetLayer = PhysicsLayer.NPC;
        this.combatStats = entity.getComponent(CombatStatsComponent.class);
        //AnimationRenderComponent animator =  entity.getComponent(AnimationRenderComponent.class);
        //animator.startAnimation("orb");
    }

    @Override
    public void update() {
        super.update();
        Vector2 position = entity.getCenterPosition();
        if (Math.abs(position.x - start.x) > distance || Math.abs(position.y - start.y) > distance) {
            //AnimationRenderComponent animator = entity.getComponent(AnimationRenderComponent.class);
            //animator.startAnimation("explode");
            entity.prepareDispose();
        }
        if ((ServiceLocator.getTimeSource().getTime() - gameTime) > 1000L) {
            entity.prepareDispose();
        }
    }

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
            Vector2 direction = target.getCenterPosition().sub(entity.getCenterPosition());
            Vector2 impulse = direction.setLength(knockback);
            targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
        }
        entity.prepareDispose();
        return true; // successfully collided with target.
    }
}
