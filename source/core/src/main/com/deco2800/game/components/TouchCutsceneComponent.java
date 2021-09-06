package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.player.KeyboardPlayerInputComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.textbox.Dialogue;
import com.deco2800.game.ui.textbox.RandomDialogueSet;
import com.deco2800.game.ui.textbox.TextBox;

import java.util.Timer;
import java.util.TimerTask;

/**
 * When this entity touches a valid enemy's hitbox, deal damage to them and apply a knockback.
 *
 * <p>Requires CombatStatsComponent, HitboxComponent on this entity.
 *
 * <p>Damage is only applied if target entity has a CombatStatsComponent. Knockback is only applied
 * if target entity has a PhysicsComponent.
 */
public class TouchCutsceneComponent extends TouchComponent {

    private Entity collidedEntity;
    private PlayerActions actions;
    private RandomDialogueSet dialogueSet;

    /**
     * Create a component which attacks entities on collision, without knockback.
     *
     * @param targetLayer The physics layer of the target's collider.
     */
    public TouchCutsceneComponent(short targetLayer, RandomDialogueSet dialogueSet) {
        super(targetLayer);
        this.dialogueSet = dialogueSet;
    }

    /**
     * Creates the component by adding the text box so the dialogue can be changed
     * once the entity has been collided with.
     */
    @Override
    public void create() {
        super.create();
    }

    /**
     * The method that is called once a collision event is triggered. The method
     * will check that the correct entities are in the collision, if they are not
     * then the method will terminate prematurely.
     *
     * If the entities are correct, a text box will be displayed and the player will
     * stop moving.
     *
     * @param me the entity that will trigger the event
     * @param other the entity that is required to trigger the event on collision
     */
    @Override
    protected void onCollisionStart(Fixture me, Fixture other) {
        if (!this.checkEntities(me, other)) {
            return;
        }

        TextBox textBox = ServiceLocator.getEntityService()
                .getUIEntity().getComponent(TextBox.class);
        textBox.setOrderedDialogue(dialogueSet);

        collidedEntity = ((BodyUserData) other.getBody().getUserData()).entity;
        actions = collidedEntity.getComponent(PlayerActions.class);
        KeyboardPlayerInputComponent input =  collidedEntity.getComponent(KeyboardPlayerInputComponent.class);
        //Checks if the entity that has collided is the player
        if (actions == null) {
            return;
        }
        actions.stopWalking();
        input.stopWalking();
    }

}