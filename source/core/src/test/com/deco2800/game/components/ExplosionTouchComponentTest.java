package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.Touch.ExplosionTouchComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
public class ExplosionTouchComponentTest {
    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    @Test
    void explosiveDealContinuousDamage() {
        short targetLayer = (1 << 3);
        short myLayer = (1 << 9);
        Entity player = createTarget(targetLayer);
        Entity boss = createBoss(targetLayer);

        ExplosionTouchComponent component = new ExplosionTouchComponent(targetLayer, myLayer, 2f);
        boss.addComponent(component);

        boss.create();

        Fixture entityFixture = boss.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = player.getComponent(HitboxComponent.class).getFixture();
        boss.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        assertEquals(0, player.getComponent(CombatStatsComponent.class).getHealth());

    }

    private Entity createBoss(short targetLayer) {
        Entity entity =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new CombatStatsComponent(100, 10))
                        .addComponent(new HitboxComponent())
                        .addComponent(new TouchAttackComponent(targetLayer));
        return entity;
    }

    private Entity createTarget(short layer) {
        Entity target =
                new Entity()
                        .addComponent(new CombatStatsComponent(10, 0))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new HitboxComponent().setLayer(layer));
        target.create();
        return target;
    }
}