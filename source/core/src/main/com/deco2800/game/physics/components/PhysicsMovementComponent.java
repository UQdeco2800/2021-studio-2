package com.deco2800.game.physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.ai.movement.MovementController;
import com.deco2800.game.components.Component;
import com.deco2800.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Movement controller for a physics-based entity.
 */
public class PhysicsMovementComponent extends Component implements MovementController {
    private static final Logger logger = LoggerFactory.getLogger(PhysicsMovementComponent.class);

    public PhysicsComponent physicsComponent;
    private Vector2 targetPosition;
    private boolean movementEnabled = true;
    private Vector2 maxSpeed = Vector2Utils.ONE;

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
    }

    @Override
    public void update() {
        if (movementEnabled && targetPosition != null) {
            Body body = physicsComponent.getBody();
            updateDirection(body);
        }
    }

    @Override
    public boolean getMoving() {
        return movementEnabled;
    }

    /**
     * Enable/disable movement for the controller. Disabling will immediately set velocity to 0.
     *
     * @param movementEnabled true to enable movement, false otherwise
     */
    @Override
    public void setMoving(boolean movementEnabled) {
        this.movementEnabled = movementEnabled;
        if (!movementEnabled) {
            Body body = physicsComponent.getBody();
            setToVelocity(body, Vector2.Zero);
        }
    }

    /**
     * @return Target position in the world
     */
    @Override
    public Vector2 getTarget() {
        return targetPosition;
    }

    /**
     * Set a target to move towards. The entity will be steered towards it in a straight line, not
     * using pathfinding or avoiding other entities.
     *
     * @param target target position
     */
    @Override
    public void setTarget(Vector2 target) {
        logger.trace("Setting target to {}", target);
        this.targetPosition = target;
    }

    public void setMaxSpeed(Vector2 maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void DirectionAnimation (){
        // ranged ghosts have ID of 20 and 21 - they change animation based on arrow shooting angle
        // ideally wouldn't use ID
        // will need to figure out later how to specify entities are not ranged ghosts using a label of some kind

        if (this.getEntity().getId()!=20 && this.getEntity().getId()!=21) {
            if (this.getDirection().x > this.getDirection().y) {
                if (this.getDirection().x < 0) {
                    this.getEntity().getEvents().trigger("LeftStart");
                }
                 else {
                    this.getEntity().getEvents().trigger("RightStart");
                }
            }
            else{
                if (this.getDirection().y < 0) {
                    this.getEntity().getEvents().trigger("DownStart");
                } else {
                    this.getEntity().getEvents().trigger("UpStart");
                }
            }
        }
    }
    private void updateDirection(Body body) {
        Vector2 desiredVelocity = getDirection().scl(maxSpeed);
        setToVelocity(body, desiredVelocity);
        DirectionAnimation();
        }

    private void setToVelocity(Body body, Vector2 desiredVelocity) {
        // impulse force = (desired velocity - current velocity) * mass
        Vector2 velocity = body.getLinearVelocity();
        Vector2 impulse = desiredVelocity.cpy().sub(velocity).scl(body.getMass());
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }

    public Vector2 getDirection() {
        return targetPosition.cpy().sub(entity.getPosition()).nor();
    }
}
