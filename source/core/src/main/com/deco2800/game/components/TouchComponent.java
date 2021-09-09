package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.textbox.TextBox;

abstract class TouchComponent extends Component {

    protected final short targetLayer;
    protected HitboxComponent hitboxComponent;

    /**
     * Create a component which allows entities to interact with each other once they are within the vicinity.
     *
     * @param targetLayer The physics layer of the target's collider.
     */
    public TouchComponent(short targetLayer) {
        this.targetLayer = targetLayer;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    abstract void onCollisionStart(Fixture me, Fixture other);

    protected boolean checkEntities(Fixture me, Fixture other) {
        // Not triggered by hitbox, ignore
        return (hitboxComponent.getFixture() != me
                // Doesn't match our target layer, ignore
                || PhysicsLayer.notContains(targetLayer, other.getFilterData().categoryBits));
    }

    /**
     * Enables the change in aspect ratio of the screen.
     */
    protected void showCutsceneBars() {
        TextBox textBox = ServiceLocator.getEntityService()
                .getUIEntity().getComponent(TextBox.class);
        textBox.showBars();
    }
}
