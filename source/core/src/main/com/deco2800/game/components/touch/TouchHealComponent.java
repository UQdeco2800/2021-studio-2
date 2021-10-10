package com.deco2800.game.components.touch;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * component to allow entities to give health to entities on collision
 */
public class TouchHealComponent extends TouchComponent {

    private static final Logger logger = LoggerFactory.getLogger(TouchHealComponent.class);

    /**
     * Create a component which allows Player entity to interact with the Health item and give itself health
     *
     * @param targetLayer The physics layer of the target's collider.
     */
    public TouchHealComponent(short targetLayer) {
        super(targetLayer);
    }

    /**
     * On collision with the potion entity the player will gain health based on the
     * potion entities base attack
     *
     * @param me    potion entity
     * @param other player entity
     */
    @Override
    void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            //do nothing
            return;
        }

        if (PhysicsLayer.notContains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }
        if (enabled) {

            logger.debug("The player will be healed due to a collision with an entity with a TouchHealComponent");

            Entity player = ((BodyUserData) other.getBody().getUserData()).entity;
            player.getEvents().trigger("healEntity", 100);
            //dispose health potion after giving player hp
            ((BodyUserData) me.getBody().getUserData()).entity.prepareDispose();
        }
    }
}
