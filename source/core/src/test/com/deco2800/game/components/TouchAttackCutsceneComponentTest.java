package com.deco2800.game.components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.Touch.TouchAttackCutsceneComponent;
import com.deco2800.game.components.player.KeyboardPlayerInputComponent;
import com.deco2800.game.components.player.PlayerActions;
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
import org.mockito.Mock;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class TouchAttackCutsceneComponentTest {

    @Mock
    TextBox textBox;

    @Mock
    KeyboardPlayerInputComponent inputComponent;

    @Mock
    PlayerActions actions;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerInputService(new InputService());
        Entity ui = new Entity();
        ui.addComponent(mock(TextBox.class));

        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.getEntityService().registerUI(ui);

        textBox = ServiceLocator.getEntityService().getUIEntity().getComponent(TextBox.class);
        inputComponent = mock(KeyboardPlayerInputComponent.class);
        actions = mock(PlayerActions.class);
    }

    @Test
    void shouldStopPlayingMovement() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 3);
        Entity trigger = createTrigger(triggerLayer);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(actions).stopWalking();
        verify(inputComponent).stopWalking();
    }

    @Test
    void shouldNotStopPlayingMovement() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 4);
        Entity trigger = createTrigger(triggerLayer);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(inputComponent, never()).stopWalking();
    }

    @Test
    void shouldNotSetLastKeyPressed() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 4);
        Entity trigger = createTrigger(triggerLayer);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(inputComponent, never()).setLastKeyPressed('W');
    }


    @Test
    void shouldForceAttack() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 3);
        Entity trigger = createTrigger(triggerLayer);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(inputComponent).keyDown(Input.Keys.SPACE);
    }

    @Test
    void shouldNotForceAttack() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 4);
        Entity trigger = createTrigger(triggerLayer);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(inputComponent, never()).keyDown(Input.Keys.SPACE);
    }

    Entity createTrigger(short triggerLayer) {
        Entity entity =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new TouchAttackCutsceneComponent(triggerLayer, 1, 'W'))
                        .addComponent(new CombatStatsComponent(20, 0));
        entity.create();
        return entity;
    }

    Entity createTarget(short layer) {
        Entity target =
                new Entity()
                        .addComponent(new CombatStatsComponent(10, 0))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(layer))
                        .addComponent(inputComponent)
                        .addComponent(actions);
        target.create();
        return target;
    }
}
