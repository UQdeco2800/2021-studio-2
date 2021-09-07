package com.deco2800.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.components.TouchCutsceneComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

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
    public static Entity createTrigger() {
        Entity tree =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/rock.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new TouchCutsceneComponent(PhysicsLayer.PLAYER));

        tree.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        tree.getComponent(TextureRenderComponent.class).scaleEntity();
        tree.scaleHeight(2.5f);
        PhysicsUtils.setScaledCollider(tree, 0f, 0f);
        return tree;
    }

    private CutsceneTriggerFactory () {
        throw new IllegalStateException("Instantiating static util class");
    }
}
