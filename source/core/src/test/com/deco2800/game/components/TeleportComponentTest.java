package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;



@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class TeleportComponentTest {
    @BeforeEach
    void beforeEach() {

        ServiceLocator.registerPhysicsService(new PhysicsService());

    }

    @Test
    void saveTheHealthAfterTeleport() {
        short layer = (1 << 6);
        Entity entity = createPlayer(layer);
        Entity target = createTelport(layer);

        Fixture entityFixture = entity.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        assertEquals(entity.getComponent(CombatStatsComponent.class).getHealth(), 10);
        entity.getComponent(CombatStatsComponent.class).setHealth(5);
        entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);
        assertEquals(entity.getComponent(CombatStatsComponent.class).getHealth(), 5);
    }

    Entity createPlayer(short targetLayer) {
        Entity entity =
                new Entity()
                        .addComponent(new TouchAttackComponent(targetLayer))
                        .addComponent(new CombatStatsComponent(10, 10))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent());
        entity.create();
        return entity;
    }

    Entity createTelport(short layer) {
        Entity target =
                new Entity()
                        .addComponent(new CombatStatsComponent(0, 0))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(layer))
                        .addComponent(new TeleportComponent(layer));
        target.create();
        return target;
    }
}
