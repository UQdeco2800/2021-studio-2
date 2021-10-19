package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.components.touch.TouchCutsceneComponent;
import com.deco2800.game.components.touch.TouchMoveComponent;
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
     * @param type        the type of selection of dialogue
     * @return entity that will create the trigger within the map
     */
    public static Entity createDialogueTrigger(RandomDialogueSet dialogueSet, DialogueSet type, int numRepeats) {
        Entity trigger =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new TouchMoveComponent(PhysicsLayer.PLAYER,
                                new Vector2(0f, 0f), false))
                        .addComponent(new TouchCutsceneComponent(PhysicsLayer.PLAYER, dialogueSet, type, numRepeats));

        trigger.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        PhysicsUtils.setScaledCollider(trigger, 0f, 0f);
        trigger.getComponent(ColliderComponent.class).setSensor(true);
        trigger.scaleHeight(1f);
        return trigger;
    }

    /**
     * Creates an entity that can trigger a cutscene to start.
     *
     * @param dialogueSet the dialogue set the entity will trigger
     * @param type        the type of selection of dialogue
     * @return entity that will create the trigger within the map
     */
    public static Entity createPrisonerCutscene(RandomDialogueSet dialogueSet, DialogueSet type, int numRepeats) {

        Entity trigger =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/textBoxDisplay/prisoner_image.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new TouchMoveComponent(PhysicsLayer.PLAYER,
                                new Vector2(0f, 0f), numRepeats))
                        .addComponent(new TouchCutsceneComponent(PhysicsLayer.PLAYER, dialogueSet, type, numRepeats));

        trigger.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        PhysicsUtils.setScaledCollider(trigger, 0.6f, 0.3f);
        trigger.getComponent(ColliderComponent.class).setSensor(true);
        trigger.scaleHeight(2f);
        return trigger;
    }

    /**
     * Creates an entity that can trigger the start of a movement cutscene that will force the player to move left
     *
     * @return an Entity that will force the player to move left when collided with
     */
    public static Entity createLeftMoveTrigger() {
        return createMoveTriggerBase()
                .addComponent(new TouchMoveComponent(PhysicsLayer.PLAYER, new Vector2(-1f, 0f), false));
    }

    /**
     * Creates an entity that can trigger the start of a movement cutscene that will force the player to move right
     *
     * @return an Entity that will force the player to move right when collided with
     */
    public static Entity createRightMoveTrigger() {
        return createMoveTriggerBase()
                .addComponent(new TouchMoveComponent(PhysicsLayer.PLAYER, new Vector2(1f, 0f), false));
    }

    /**
     * Creates an entity that can trigger the start of a movement cutscene that will force the player to move up
     *
     * @return an Entity that will force the player to move up when collided with
     */
    public static Entity createUpMoveTrigger() {
        return createMoveTriggerBase()
                .addComponent(new TouchMoveComponent(PhysicsLayer.PLAYER, new Vector2(0f, 1f), false));
    }

    /**
     * Creates an entity that can trigger the start of a movement cutscene that will force the player to move down
     *
     * @return an Entity that will force the player to move down when collided with
     */
    public static Entity createDownMoveTrigger() {
        return createMoveTriggerBase()
                .addComponent(new TouchMoveComponent(PhysicsLayer.PLAYER, new Vector2(0f, -1f), false));
    }

    /**
     * Creates a base for any triggers that requires a collider hitbox and physics component.
     * Some of the preset settings of this entity is that the component cannot be collided with and
     * the height of the trigger has been preset to be very small.
     *
     * @return A basic trigger entity with all required components
     */
    private static Entity createMoveTriggerBase() {
        Entity trigger =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC));
        trigger.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        trigger.getComponent(ColliderComponent.class).setSensor(true);
        PhysicsUtils.setScaledCollider(trigger, 0f, 0f);
        trigger.scaleHeight(0.1f);
        return trigger;
    }

}
