package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.touch.TouchMoveComponent;
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
class TouchMoveComponentTest {

    @Mock
    TextBox textBox;

    @Mock
    KeyboardPlayerInputComponent inputComponent;

    @Mock
    PlayerActions actions;

    Vector2 moveLeft = new Vector2(-1f, 0f);

    Vector2 noMovement = new Vector2(0f, 0f);

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
    void shouldOpenBarsOnCollision() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 3);
        Entity trigger = createTrigger(triggerLayer, moveLeft);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(textBox).showBars();
    }

    @Test
    void shouldNotOpenBarsOnCollision() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 4);
        Entity trigger = createTrigger(triggerLayer, moveLeft);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(textBox, never()).showBars();
        verify(textBox, never()).setClosed();
    }

    @Test
    void shouldStopPlayingMovement() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 3);
        Entity trigger = createTrigger(triggerLayer, moveLeft);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(inputComponent).stopWalking();
        verify(actions).stopWalking();
    }

    @Test
    void shouldLockPlayerInput() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 3);
        Entity trigger = createTrigger(triggerLayer, moveLeft);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(inputComponent).lockPlayer();
    }

    @Test
    void shouldForceMovement() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 3);
        Entity trigger = createTrigger(triggerLayer, moveLeft);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(actions).walk(moveLeft);
    }

    @Test
    void shouldNotForceMovement() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 4);
        Entity trigger = createTrigger(triggerLayer, moveLeft);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(actions, never()).walk(moveLeft);
    }

    @Test
    void shouldNotMovePlayer() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 3);
        Entity trigger = createTrigger(triggerLayer, noMovement);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(actions, never()).walk(noMovement);
    }

    @Test
    void shouldNotLockPlayer() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 3);
        Entity trigger = createTrigger(triggerLayer, noMovement);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(inputComponent, never()).lockPlayer();
    }

    @Test
    void shouldNotLockPlayerDifferentTarget() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 4);
        Entity trigger = createTrigger(triggerLayer, noMovement);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(inputComponent, never()).lockPlayer();
    }

    @Test
    void shouldNotStopWalkingDifferentTarget() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 4);
        Entity trigger = createTrigger(triggerLayer, noMovement);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(inputComponent, never()).stopWalking();
        verify(actions, never()).stopWalking();
    }

    @Test
    void shouldNotRepeat() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 3);
        Entity trigger = createTrigger(triggerLayer, noMovement);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(inputComponent, times(1)).stopWalking();
        verify(actions, times(1)).stopWalking();
    }

    @Test
    void shouldRepeat() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 3);
        Entity trigger = createTriggerRepeat(triggerLayer, noMovement);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(inputComponent, times(2)).stopWalking();
        verify(actions, times(2)).stopWalking();
    }

    Entity createTrigger(short triggerLayer, Vector2 direction) {
        Entity entity =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new TouchMoveComponent(triggerLayer,
                                direction, false));
        entity.create();
        return entity;
    }

    Entity createTriggerRepeat(short triggerLayer, Vector2 direction) {
        Entity entity =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new TouchMoveComponent(triggerLayer,
                                direction, true));
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
