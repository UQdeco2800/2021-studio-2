package com.deco2800.game.physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Movement controller for a physics-based entity.
 */
public class PhysicsMovementComponent extends Component {
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

    public boolean getMoving() {
        return movementEnabled;
    }

    /**
     * Enable/disable movement for the controller. Disabling will immediately set velocity to 0.
     *
     * @param movementEnabled true to enable movement, false otherwise
     */
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
    public Vector2 getTarget() {
        return targetPosition;
    }

    /**
     * Set a target to move towards. The entity will be steered towards it in a straight line, not
     * using pathfinding or avoiding other entities.
     *
     * @param target target position
     */
    public void setTarget(Vector2 target) {
        logger.trace("Setting target to {}", target);
        this.targetPosition = target;
    }

    public void setMaxSpeed(Vector2 maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * enemy move down
     */
    public void downAnimation() {
        this.getEntity().getEvents().trigger("DownStart");
    }

    /**
     * enemy move up
     */
    public void upAnimation() {
        this.getEntity().getEvents().trigger("UpStart");
    }

    /**
     * enemy move left
     */
    public void leftAnimation() {
        this.getEntity().getEvents().trigger("LeftStart");
    }

    /**
     * enemy move right
     */
    public void rightAnimation() {
        this.getEntity().getEvents().trigger("RightStart");
    }

    /**
     *
     */
    public void deathAnimation() {
        if (Math.abs(this.getDirection().x) > Math.abs(this.getDirection().y)) { //x-axis movement
            if (this.getDirection().x < 0) { //left
                this.getEntity().getEvents().trigger("LeftStart");
            } else if (this.getDirection().x > 0) { //right
                this.getEntity().getEvents().trigger("RightStart");
            }
        } else if (Math.abs(this.getDirection().x) < Math.abs(this.getDirection().y)) { //y axis movement
            if (this.getDirection().y < 0) { //down
                this.getEntity().getEvents().trigger("DownStart");
            } else if (this.getDirection().y > 0) { //up
                this.getEntity().getEvents().trigger("UpStart");
            }
        }
    }

    /**
     *
     */
    public void directionAnimation() {
        if (Boolean.FALSE.equals(this.getEntity().getComponent(CombatStatsComponent.class).isDead())) {
            if (Math.abs(this.getDirection().x) > Math.abs(this.getDirection().y)) { //x-axis movement
                if (this.getDirection().x < 0) { //left
                    leftAnimation();
                } else if (this.getDirection().x > 0) { //right
                    rightAnimation();
                }
            } else { //y axis movement
                if (this.getDirection().y < 0) { //down
                    downAnimation();
                } else if (this.getDirection().y > 0) { //up
                    upAnimation();
                }
            }
        } else {
            deathAnimation();
        }
    }


    private void updateDirection(Body body) {
        Vector2 desiredVelocity = getDirection().scl(maxSpeed);
        setToVelocity(body, desiredVelocity);
        directionAnimation();
    }

    private void setToVelocity(Body body, Vector2 desiredVelocity) {
        Vector2 velocity = body.getLinearVelocity();
        Vector2 impulse = desiredVelocity.cpy().sub(velocity).scl(body.getMass());
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }

    public Vector2 getDirection() {
        return targetPosition.cpy().sub(entity.getPosition()).nor();
    }
}
