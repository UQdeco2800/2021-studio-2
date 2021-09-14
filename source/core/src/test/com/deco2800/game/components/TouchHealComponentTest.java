package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
public class TouchHealComponentTest {
    Entity entity;
    Entity health;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        entity = player();
        health = healthItem();
        entity.create();
        health.create();
    }

    @AfterEach
    void afterEach() {
        ServiceLocator.clear();
    }

    @Test
    void touchHealComponentGivesPlayerHealthOnCollision() {
        Fixture player = entity.getComponent(HitboxComponent.class).getFixture();
        Fixture health2 = health.getComponent(HitboxComponent.class).getFixture();

        assertEquals(100, entity.getComponent(CombatStatsComponent.class).getHealth());
        entity.getComponent(CombatStatsComponent.class).setHealth(10);
        assertEquals(10, entity.getComponent(CombatStatsComponent.class).getHealth());

        health.getEvents().trigger("collisionStart", health2, player);
        assertEquals(100, entity.getComponent(CombatStatsComponent.class).getHealth(), "player " +
                "should've gotten health back");
    }

    Entity player() {
        return new Entity()
                .addComponent(new CombatStatsComponent(100, 20))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));
    }

    Entity healthItem() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setSensor(true))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new TouchHealComponent(PhysicsLayer.PLAYER));
    }

}
