package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.textbox.TextBox;

import java.util.ArrayList;

abstract class TouchComponent extends Component {

    protected short targetLayer;
    protected HitboxComponent hitboxComponent;
    protected boolean inCollision = false;
    protected ArrayList<Fixture> collidingFixtures = new ArrayList<>();

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
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
        collidingFixtures.trimToSize();
    }

    void onCollisionStart(Fixture me, Fixture other) {
        if (!this.checkEntities(me, other)) {
            inCollision = true;
            collidingFixtures.add(other);
        }
    }

    void onCollisionEnd(Fixture me, Fixture other) {
        if (!this.checkEntities(me, other)) {
            inCollision = false;
            collidingFixtures.remove(other);
        }
    }

    protected boolean checkEntities(Fixture me, Fixture other) {
        // Not triggered by hitbox, ignore
        return (hitboxComponent.getFixture() != me
                // Doesn't match our target layer, ignore
                || PhysicsLayer.notContains(targetLayer, other.getFilterData().categoryBits));
    }

    /**
     * Enables the cutscene bars to appear.
     */
    protected void openCutsceneBars() {
        TextBox textBox = ServiceLocator.getEntityService()
                .getUIEntity().getComponent(TextBox.class);
        textBox.showBars();
    }

    public void setTargetLayer(short targetLayer) {
        this.targetLayer = targetLayer;
    }
}
