package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.touch.TeleportComponent;
import com.deco2800.game.components.touch.TouchAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.CutsceneScreen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class TeleportComponentTest {

    @Mock
    CutsceneScreen cutsceneScreen;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerInputService(new InputService());
        Entity ui = new Entity();
        ui.addComponent(mock(CutsceneScreen.class));

        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.getEntityService().registerUI(ui);

        cutsceneScreen = ServiceLocator.getEntityService().getUIEntity().getComponent(CutsceneScreen.class);
    }

    @Test
    void shouldNotTeleport() {
        short targetLayer = (1 << 4);
        short triggerLayer = (1 << 3);
        Entity trigger = createTrigger(triggerLayer);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(cutsceneScreen, never()).openScreen();
    }

    @Test
    void shouldTeleport() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 3);
        Entity trigger = createTrigger(triggerLayer);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(cutsceneScreen).setOpen();
    }

    Entity createTrigger(short triggerLayer) {
        Entity entity =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new TeleportComponent(triggerLayer))
                        .addComponent(new CombatStatsComponent(20, 0));
        entity.create();
        return entity;
    }

    @Test
    void saveTheHealthAfterTeleport() {
        short layer = (1 << 6);
        Entity entity = createPlayer(layer);
        Entity target = createTelport(layer);

        Fixture entityFixture = entity.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        Assertions.assertEquals(10, entity.getComponent(CombatStatsComponent.class).getHealth());
        entity.getComponent(CombatStatsComponent.class).setHealth(5);
        entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);
        Assertions.assertEquals(5, entity.getComponent(CombatStatsComponent.class).getHealth());
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


    Entity createTarget(short layer) {
        Entity target =
                new Entity()
                        .addComponent(new CombatStatsComponent(10, 0))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(layer));
        target.create();
        return target;
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
