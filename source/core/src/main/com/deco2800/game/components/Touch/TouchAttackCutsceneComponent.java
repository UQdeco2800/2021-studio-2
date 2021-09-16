package com.deco2800.game.components.Touch;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.player.KeyboardPlayerInputComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.textbox.TextBox;

import java.util.Timer;
import java.util.TimerTask;

public class TouchAttackCutsceneComponent extends TouchComponent {

    private final int repeats;

    private final int lastKeyPressed;

    /**
     * Create a component which attacks entities on collision, without knockback.
     *
     * @param targetLayer    The physics layer of the target's collider.
     * @param repeats        the number of repeated attacks
     * @param lastKeyPressed the key direction the player will attack in
     */
    public TouchAttackCutsceneComponent(short targetLayer, int repeats, int lastKeyPressed) {
        super(targetLayer);
        this.repeats = repeats;
        this.lastKeyPressed = lastKeyPressed;
    }

    /**
     * The method that is called once a collision event is triggered. The method
     * will check that the correct entities are in the collision, if they are not
     * then the method will terminate prematurely.
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

        Entity collidedEntity = ((BodyUserData) other.getBody().getUserData()).entity;
        PlayerActions actions = collidedEntity.getComponent(PlayerActions.class);
        KeyboardPlayerInputComponent input = collidedEntity.getComponent(KeyboardPlayerInputComponent.class);
        //Checks if the entity that has collided is the player
        if (actions == null) {
            return;
        }
        actions.stopWalking();
        input.stopWalking();
        openCutsceneBars();
        input.setLastKeyPressed(lastKeyPressed);
        repeatAttacks(input, 0);
    }

    /**
     * The player will repeatedly attack in the direction specified.
     *
     * @param input KeyboardInputComponent use to control the main player
     * @param count number of attacks that will be performed
     */
    private void repeatAttacks(KeyboardPlayerInputComponent input, int count) {
        input.setLastKeyPressed(lastKeyPressed);
        input.keyDown(Input.Keys.SPACE);
        if (count < repeats) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    repeatAttacks(input, count + 1);
                    timer.cancel();
                }
            }, 500);
        } else {
            input.unlockPlayer();
            TextBox textBox = ServiceLocator.getEntityService()
                    .getUIEntity().getComponent(TextBox.class);
            textBox.setClosed();
            textBox.hideBars();
        }
    }
}
