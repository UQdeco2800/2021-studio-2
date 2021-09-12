package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.textbox.DialogueSet;
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
    private final RandomDialogueSet dialogueSet;
    private final DialogueSet type;

    /**
     * Create a component which attacks entities on collision, without knockback.
     *
     * @param targetLayer The physics layer of the target's collider.
     * @param dialogueSet The set of dialogue which a dialogue will be chosen from to display
     * @param type The type of randomness for the selection of dialogue
     */
    public TouchCutsceneComponent(short targetLayer, RandomDialogueSet dialogueSet, DialogueSet type) {
        super(targetLayer);
        this.dialogueSet = dialogueSet;
        this.type = type;
    }

    /**
     * The method that is called once a collision event is triggered. The method
     * will check that the correct entities are in the collision, if they are not
     * then the method will terminate prematurely.
     * <p>
     * If the entities are correct, a text box will be displayed and the player will
     * stop moving.
     *
     * @param me    the entity that will trigger the event
     * @param other the entity that is required to trigger the event on collision
     */
    @Override
    protected void onCollisionStart(Fixture me, Fixture other) {
        if (this.checkEntities(me, other)) {
            return;
        }

        TextBox textBox = ServiceLocator.getEntityService()
                .getUIEntity().getComponent(TextBox.class);
        textBox.showBars();
        switch (type) {
            case FIRST_ENCOUNTER:
                textBox.setRandomFirstEncounter(dialogueSet);
                break;
            case BOSS_DEFEATED_BEFORE:
                textBox.setRandomDefeatDialogueSet(dialogueSet);
                break;
            case PLAYER_DEFEATED_BEFORE:
                textBox.setRandomBeatenDialogueSet(dialogueSet);
                break;
            case ORDERED:
                textBox.setOrderedDialogue(dialogueSet);
                break;
        }
    }
}
