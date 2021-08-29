package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.player.KeyboardPlayerInputComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.textbox.Dialogue;
import com.deco2800.game.ui.textbox.TextBox;

/**
 * When this entity touches a valid enemy's hitbox, deal damage to them and apply a knockback.
 *
 * <p>Requires CombatStatsComponent, HitboxComponent on this entity.
 *
 * <p>Damage is only applied if target entity has a CombatStatsComponent. Knockback is only applied
 * if target entity has a PhysicsComponent.
 */
public class TouchCutsceneComponent extends TouchComponent {

    /** Text box that can be used to change the set of messages being displayed. */
    private TextBox textBox;

    /**
     * Create a component which attacks entities on collision, without knockback.
     *
     * @param targetLayer The physics layer of the target's collider.
     */
    public TouchCutsceneComponent(short targetLayer) {
        super(targetLayer);
    }

    @Override
    public void create() {
        super.create();
        hitboxComponent = entity.getComponent(HitboxComponent.class);
        textBox = ServiceLocator.getEntityService().getUIEntity().getComponent(TextBox.class);
    }

    @Override
    protected void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }
        textBox.setDialogue(Dialogue.BOX_BOY);

        Entity player = ((BodyUserData) other.getBody().getUserData()).entity;
        PlayerActions actions = player.getComponent(PlayerActions.class);
        KeyboardPlayerInputComponent input = player.getComponent(KeyboardPlayerInputComponent.class);
        //Checks if the entity that has collided is the player
        if (actions == null) {
            return;
        }
        actions.stopWalking();
        input.stopWalking();
    }
}