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
import com.deco2800.game.ui.textbox.TextBox;

import java.util.Timer;
import java.util.TimerTask;

public class TouchCutsceneMoveComponent extends TouchCutsceneComponent{

    private Vector2 direction;
    private float x;
    private float y;

    /**
     * Create a component which attacks entities on collision, without knockback.
     *
     * @param targetLayer The physics layer of the target's collider.
     */
    public TouchCutsceneMoveComponent(short targetLayer, Vector2 direction, float x, float y) {
        super(targetLayer);
        this.direction = direction;
        this.x = x;
        this.y = y;

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
        super.onCollisionStart(me, other);
        movePlayer(collidedEntity, actions);
    }

    private void movePlayer(Entity player, PlayerActions actions) {
        Vector2 position = player.getPosition();

        //actions.walk(new Vector2(0, 1f));
        checkPosition(player, actions, position);

    }

    private void checkPosition(Entity player, PlayerActions actions, Vector2 position) {
        if (player.getPosition().x < position.x + x || player.getPosition().y < position.y + y) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    checkPosition(player, actions, position);
                    timer.cancel();
                }
            }, 50);
        } else {
            actions.stopWalking();
        }
    }
}
