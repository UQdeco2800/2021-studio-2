package com.deco2800.game.components.touch;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.components.ColliderComponent;

public class TouchTeleportComponent extends TouchComponent {

    public TouchTeleportComponent(short targetLayer, short myLayer) {
        super(targetLayer, myLayer);
    }

    @Override
    void onCollisionStart(Fixture me, Fixture other) {
        if (!this.checkEntities(me, other)) {
            return;
        }
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        if (target.getComponent(ColliderComponent.class) == null) {
            //Not a target
            return;
        }
        if ((targetLayer & target.getComponent(ColliderComponent.class).getLayer()) == 0) {
            //Not a target
            return;
        }
        if (this.getEntity().data.containsKey("teleportTarget") && (boolean) this.getEntity().data.get("teleportTarget")) {
            target.teleport((Vector2) this.getEntity().data.get("teleportLoc"));
        }
    }
}
