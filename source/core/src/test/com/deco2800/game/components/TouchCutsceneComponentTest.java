package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
<<<<<<< HEAD
import com.deco2800.game.components.touch.TouchCutsceneComponent;
import com.deco2800.game.components.touch.TouchMoveComponent;
=======
import com.deco2800.game.components.Touch.TouchCutsceneComponent;
import com.deco2800.game.components.Touch.TouchMoveComponent;
import com.deco2800.game.components.player.KeyboardPlayerInputComponent;
>>>>>>> team-4
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.textbox.DialogueSet;
import com.deco2800.game.ui.textbox.RandomDialogueSet;
import com.deco2800.game.ui.textbox.TextBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class TouchCutsceneComponentTest {

    @Mock
    TextBox textBox;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerInputService(new InputService());
        Entity ui = new Entity();
        ui.addComponent(mock(TextBox.class));

        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.getEntityService().registerUI(ui);

        textBox = ServiceLocator.getEntityService().getUIEntity().getComponent(TextBox.class);
    }

    @Test
    void shouldOpenTextBoxOnCollision() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 3);
        Entity trigger = createTrigger(triggerLayer, DialogueSet.FIRST_ENCOUNTER);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(textBox).showBars();
    }

    @Test
    void shouldSetFirstEncounterText() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 3);
        Entity trigger = createTrigger(triggerLayer, DialogueSet.FIRST_ENCOUNTER);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(textBox).setRandomFirstEncounter(RandomDialogueSet.TEST);
    }

    @Test
    void shouldSetPlayerDefeatedBeforeText() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 3);
        Entity trigger = createTrigger(triggerLayer, DialogueSet.PLAYER_DEFEATED_BEFORE);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(textBox).setRandomBeatenDialogueSet(RandomDialogueSet.TEST);
    }

    @Test
    void shouldSetBossBeatenBeforeText() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 3);
        Entity trigger = createTrigger(triggerLayer, DialogueSet.BOSS_DEFEATED_BEFORE);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(textBox).setRandomDefeatDialogueSet(RandomDialogueSet.TEST);
    }

    @Test
    void shouldNotTrigger() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 4);
        Entity trigger = createTrigger(triggerLayer, DialogueSet.BOSS_DEFEATED_BEFORE);
        Entity target = createTarget(targetLayer);

        Fixture entityFixture = trigger.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        trigger.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        verify(textBox, never()).setClosed();
        verify(textBox, never()).showBars();
    }

    Entity createTrigger(short triggerLayer, DialogueSet dialogueSet) {
        Entity entity =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new TouchMoveComponent(triggerLayer,
                                new Vector2(1f, 0), false))
                        .addComponent(new TouchCutsceneComponent(triggerLayer, RandomDialogueSet.TEST,
                                dialogueSet, Integer.MAX_VALUE));
        entity.create();
        return entity;
    }

    Entity createTarget(short layer) {
        Entity target =
                new Entity()
                        .addComponent(new CombatStatsComponent(10, 0))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(layer))
                        .addComponent(new KeyboardPlayerInputComponent());
        target.create();
        return target;
    }
}
