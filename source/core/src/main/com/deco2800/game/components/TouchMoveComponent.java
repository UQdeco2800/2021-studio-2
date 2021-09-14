package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.player.KeyboardPlayerInputComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;

import java.util.Timer;
import java.util.TimerTask;

public class TouchMoveComponent extends TouchComponent {

    /**
     * The direction the character will move in
     */
    private final Vector2 direction;

    /**
     * The distance on the x axis the character will need to move.
     */
    private final float x;

    /**
     * The distance on the y axis the character will need to move.
     */
    private final float y;

    /**
     * Checks if the component has been triggered before
     */
    private boolean triggered = false;

    /**
     * Used to see if the move component should be repeated or not
     */
    private final boolean repeatable;

    /**
     * Create a component which attacks entities on collision, without knockback.
     *
     * @param targetLayer The physics layer of the target's collider.
     * @param direction   direction the player will attack in
     * @param x           the x position for the player to move
     * @param y           the y position for the player to move
     * @param repeatable  if the trigger can be repeated
     */
    public TouchMoveComponent(short targetLayer, Vector2 direction, float x, float y, boolean repeatable) {
        super(targetLayer);
        this.direction = direction;
        this.x = x;
        this.y = y;
        this.repeatable = repeatable;
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
        if (triggered && !repeatable) {
            return;
        }
        triggered = true;
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
        movePlayer(collidedEntity, actions, input);
    }

    /**
     * Moves the character in the direction specified in the constructor,
     * the character is the entity that will be moved and changing the actions of that entity.
     *
     * @param player  the entity that will be moved
     * @param actions the actions of the entity moving
     * @param input   the input controller of the player
     */
    private void movePlayer(Entity player, PlayerActions actions, KeyboardPlayerInputComponent input) {
        Vector2 position = player.getPosition();
        if (direction.x != 0 || direction.y != 0) {
            input.lockPlayer();
            actions.walk(direction);
            checkPosition(player, actions, position, input);
        }
    }

    /**
     * Repeatedly checks if the player has moved in the correct direction.
     *
     * @param player   the entity to be moved
     * @param actions  class to change the what the entity is doing
     * @param position start position of the entity
     */
    private void checkPosition(Entity player, PlayerActions actions, Vector2 position,
                               KeyboardPlayerInputComponent input) {
        float xDifference = player.getPosition().x - position.x > 0 ?
                player.getPosition().x - position.x : -1 * (player.getPosition().x - position.x);

        float yDifference = player.getPosition().y - position.y > 0 ?
                player.getPosition().y - position.y : -1 * (player.getPosition().y - position.y);
    }
}
