package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.textbox.TextBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
class TouchCutsceneComponentTest {
//
//    TextBox textBox;
//
//    @BeforeEach
//    void beforeEach() {
//        ServiceLocator.registerPhysicsService(new PhysicsService());
//        ServiceLocator.registerInputService(new InputService());
//        Entity ui = new Entity();
//        ui.addComponent(new TextBox());
//
//        ServiceLocator.registerEntityService(new EntityService());
//        ServiceLocator.getEntityService().registerUI(ui);
//        textBox = ServiceLocator.getEntityService().getUIEntity().getComponent(TextBox.class);
//    }
//
//    @Test
//    void shouldStartMessage() {
//        short targetLayer = (1 << 3);
//        short triggerLayer = (1 << 3);
//        Entity entity = createTrigger(triggerLayer);
//        Entity target = createTarget(targetLayer);
//
//        Fixture entityFixture = entity.getComponent(HitboxComponent.class).getFixture();
//        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
//        entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);
//
//        assertEquals("Ragnarok is nearing warrior.", textBox.getMessage());
//    }
//
//    @Test
//    void shouldNotStartMessage() {
//        short targetLayer = (1 << 3);
//        short triggerLayer = (1 << 4);
//        Entity entity = createTrigger(triggerLayer);
//        Entity target = createTarget(targetLayer);
//
//        Fixture entityFixture = entity.getComponent(HitboxComponent.class).getFixture();
//        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
//        entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);
//
//        assertEquals("You're running out of time... \n" +
//                "Quickly! Press any key once you're ready \n" +
//                "to see the next message.", textBox.getMessage());
//    }
//
//    Entity createTrigger(short targetLayer) {
//        Entity entity =
//                new Entity()
//                        .addComponent(new TouchCutsceneComponent(targetLayer))
//                        .addComponent(new PhysicsComponent())
//                        .addComponent(new HitboxComponent())
//                        .addComponent(new ColliderComponent());
//
//        entity.create();
//        return entity;
//    }
//
//    Entity createTarget(short layer) {
//        Entity target =
//                new Entity()
//                        .addComponent(new CombatStatsComponent(10, 0))
//                        .addComponent(new PhysicsComponent())
//                        .addComponent(new HitboxComponent().setLayer(layer));
//        target.create();
//        return target;
//    }
}
