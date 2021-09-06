package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.TouchCutsceneComponent;
import com.deco2800.game.components.TouchMoveComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.ui.textbox.RandomDialogueSet;

import java.util.Random;

/**
 * Factory to create obstacle entities.
 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */
public class CutsceneTriggerFactory {

    /**
     * Creates an entity that can trigger a cutscene to start.
     * @return entity that will create the trigger within the map
     */
    public static Entity createTrigger(RandomDialogueSet dialogueSet) {
        Entity trigger =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/prisoner.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new TouchCutsceneComponent(PhysicsLayer.PLAYER, dialogueSet));
//                        .addComponent(new TouchMoveComponent(PhysicsLayer.PLAYER,
//                                new Vector2(0f, 1f), 0, 2));

        trigger.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        trigger.getComponent(TextureRenderComponent.class).scaleEntity();
        PhysicsUtils.setScaledCollider(trigger, 0f, 0f);
        trigger.scaleHeight(1f);
        return trigger;
    }

    private CutsceneTriggerFactory () {
        throw new IllegalStateException("Instantiating static util class");
    }
}
