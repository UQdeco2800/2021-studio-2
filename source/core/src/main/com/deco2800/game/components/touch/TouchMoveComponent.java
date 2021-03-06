package com.deco2800.game.components.touch;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.player.KeyboardPlayerInputComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TouchMoveComponent extends TouchComponent {

    private static final Logger logger = LoggerFactory.getLogger(TouchMoveComponent.class);

    /**
     * The direction the character will move in
     */
    private final Vector2 direction;

    /**
     * Checks if the component has been triggered before
     */
    private boolean triggered = false;

    /**
     * Used to see if the move component should be repeated or not
     */
    private boolean repeatable = true;

    /**
     * Maximum number of times the cutscene can be repeated
     */
    private int maxRepeats = Integer.MAX_VALUE;

    /**
     * Current number of times the cutscene has been triggered
     */
    private int numRepeats = 0;

    /**
     * Create a component which attacks entities on collision, without knockback.
     *
     * @param targetLayer The physics layer of the target's collider.
     * @param direction   direction the player will attack in
     * @param repeatable  if the trigger can be repeated
     */
    public TouchMoveComponent(short targetLayer, Vector2 direction, boolean repeatable) {
        super(targetLayer);
        this.direction = direction;
        this.repeatable = repeatable;
    }

    /**
     * Create a component which attacks entities on collision, without knockback.
     *
     * @param targetLayer The physics layer of the target's collider.
     * @param direction   direction the player will attack in
     * @param maxRepeats  maximum number of times this will be repeated
     */
    public TouchMoveComponent(short targetLayer, Vector2 direction, int maxRepeats) {
        super(targetLayer);
        this.direction = direction;
        this.maxRepeats = maxRepeats;
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
        if ((triggered && !repeatable) || (numRepeats >= maxRepeats)) {
            return;
        }
        if (hitboxComponent.getFixture() != me || PhysicsLayer.notContains(targetLayer, other.getFilterData().categoryBits)) {
            return;
        }
        triggered = true;
        numRepeats++;

        Entity collidedEntity = ((BodyUserData) other.getBody().getUserData()).entity;
        PlayerActions actions = collidedEntity.getComponent(PlayerActions.class);
        KeyboardPlayerInputComponent input = collidedEntity.getComponent(KeyboardPlayerInputComponent.class);
        CombatStatsComponent combat = collidedEntity.getComponent(CombatStatsComponent.class);
        //Checks if the entity that has collided is the player
        if (actions == null) {
            return;
        }
        actions.stopWalking();
        input.stopWalking();
        openCutsceneBars();
        movePlayer(actions, input, combat);
    }

    /**
     * Moves the character in the direction specified in the constructor,
     * the character is the entity that will be moved and changing the actions of that entity.
     *
     * @param actions the actions of the entity moving
     * @param input   the input controller of the player
     * @param combat  the health stats of the player component
     */
    private void movePlayer(PlayerActions actions, KeyboardPlayerInputComponent input, CombatStatsComponent combat) {
        if (direction.x != 0 || direction.y != 0) {

            logger.debug("The player will be moved in the direction {} due to collision", direction);

            input.lockPlayer();
            combat.setDamageLocked(true);
            actions.walk(direction);
        } else {
            input.unlockPlayer();
            combat.setDamageLocked(false);

        }
    }
}
