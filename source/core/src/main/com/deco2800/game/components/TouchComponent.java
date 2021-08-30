package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.physics.components.HitboxComponent;

abstract class TouchComponent extends Component {

    protected short targetLayer;
    protected HitboxComponent hitboxComponent;

    /**
     * Create a component which attacks entities on collision, without knockback.
     * @param targetLayer The physics layer of the target's collider.
     */
    public TouchComponent(short targetLayer) {
        this.targetLayer = targetLayer;
    }

    @Override
    public void create()  {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    abstract void onCollisionStart(Fixture me, Fixture other);
}
