package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchAttackCutsceneComponent;
import com.deco2800.game.components.TouchCutsceneComponent;
import com.deco2800.game.components.TouchMoveComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.ui.textbox.DialogueSet;
import com.deco2800.game.ui.textbox.RandomDialogueSet;

/**
 * Factory to create obstacle entities.
 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */
public class CutsceneTriggerFactory {

    private CutsceneTriggerFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }

    /**
     * Creates an entity that can trigger a cutscene to start.
     *
     * @param dialogueSet the dialogue set the entity will trigger
     * @param type the type of selection of dialogue
     * @return entity that will create the trigger within the map
     */
    public static Entity createDialogueTrigger(RandomDialogueSet dialogueSet, DialogueSet type) {
        Entity trigger =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/prisoner.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new TouchMoveComponent(PhysicsLayer.PLAYER,
                                new Vector2(0f, 0f), 0, 0, true))
                        .addComponent(new TouchCutsceneComponent(PhysicsLayer.PLAYER, dialogueSet, type));

        trigger.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        trigger.getComponent(TextureRenderComponent.class).scaleEntity();
        PhysicsUtils.setScaledCollider(trigger, 0f, 0f);
        trigger.scaleHeight(1f);
        return trigger;
    }

    /**
     * Creates an entity that can trigger a cutscene to start.
     *
     * @param dialogueSet the dialogue set the entity will trigger
     * @param type the type of selection of dialogue
     * @return entity that will create the trigger within the map
     */
    public static Entity createLokiTrigger(RandomDialogueSet dialogueSet, DialogueSet type) {
        Entity trigger =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/textBoxDisplay/loki_image.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new TouchMoveComponent(PhysicsLayer.PLAYER,
                                new Vector2(0f, 0f), 0, 0, true))
                        .addComponent(new TouchCutsceneComponent(PhysicsLayer.PLAYER, dialogueSet, type));

        trigger.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        trigger.getComponent(TextureRenderComponent.class).scaleEntity();
        PhysicsUtils.setScaledCollider(trigger, 0f, 0f);
        trigger.scaleHeight(1f);
        return trigger;
    }

    /**
     * Creates an entity that can trigger a cutscene to start.
     *
     * @param lastKeyPressed the last key direction the player will attack in
     * @return entity that will create the trigger within the map
     */
    public static Entity createAttackTrigger(int repeats, int lastKeyPressed) {
        Entity trigger =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/textBoxDisplay/loki_image.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new TouchAttackCutsceneComponent(PhysicsLayer.PLAYER, repeats, lastKeyPressed))
                        .addComponent(new CombatStatsComponent(20, 0));

        trigger.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        trigger.getComponent(TextureRenderComponent.class).scaleEntity();
        PhysicsUtils.setScaledCollider(trigger, 0f, 0f);
        trigger.scaleHeight(1f);
        return trigger;
    }

    /**
     * Creates an entity that can trigger a cutscene to start.
     *
     * @param dialogueSet the dialogue set the entity will trigger
     * @param direction direction to move the player
     * @param x x position that player moves in
     * @param y y position that player moves in
     * @return entity that will create the trigger within the map
     */
    public static Entity createMoveDialogueTrigger(RandomDialogueSet dialogueSet, Vector2 direction, int x, int y) {
        Entity trigger =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new TouchMoveComponent(PhysicsLayer.PLAYER,
                                direction, x, y, false))
                        .addComponent(new TouchCutsceneComponent(PhysicsLayer.PLAYER, dialogueSet, DialogueSet.FIRST_ENCOUNTER));

        trigger.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        trigger.getComponent(ColliderComponent.class).setSensor(true);
        PhysicsUtils.setScaledCollider(trigger, 0f, 0f);
        trigger.scaleHeight(0f);
        return trigger;
    }

    /**
     * Creates an entity that can trigger a cutscene to start.
     *
     * @param direction direction to move the player
     * @param x x position that player moves in
     * @param y y position that player moves in
     * @return entity that will create the trigger within the map
     */
    public static Entity createMoveTrigger(Vector2 direction, int x, int y) {
        Entity trigger =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new TouchMoveComponent(PhysicsLayer.PLAYER,
                                direction, x, y, false));

        trigger.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        trigger.getComponent(ColliderComponent.class).setSensor(true);
        PhysicsUtils.setScaledCollider(trigger, 0f, 0f);
        trigger.scaleHeight(0.1f);
        return trigger;
    }
}
