package com.deco2800.game.components.Touch;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;

public class TouchWin extends TouchComponent {
    /**
     * Create a component which allows entities to interact with each other once they are within the vicinity.
     *
     * @param targetLayer The physics layer of the target's collider.
     */
    public TouchWin(short targetLayer) {
        super(targetLayer);
    }


    void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            //do nothing
            return;
        }

        if (PhysicsLayer.notContains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }
        Entity player = ((BodyUserData) other.getBody().getUserData()).entity;
        player.getEvents().trigger("gameWin");
    }

}
