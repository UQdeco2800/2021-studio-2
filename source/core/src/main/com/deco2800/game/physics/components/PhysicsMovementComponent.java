package com.deco2800.game.physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.ai.movement.MovementController;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Movement controller for a physics-based entity.
 */
public class PhysicsMovementComponent extends Component implements MovementController {
    private static final Logger logger = LoggerFactory.getLogger(PhysicsMovementComponent.class);
    public NPCFactory npcFactory;
    public PhysicsComponent physicsComponent;
    private Vector2 targetPosition;
    private boolean movementEnabled = true;
    private Vector2 maxSpeed = Vector2Utils.ONE;

    public boolean animateAttack;
    public boolean leftStart;
    public boolean rightStart;
    public boolean upStart;
    public boolean downStart;


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
        if (this.targetPosition.x < 1) {
            //System.out.println("x target position <1");
        }
    }

    public void setMaxSpeed(Vector2 maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void DirectionAnimation() {
        if (animateAttack==true) {
            System.out.println("if attack animate animateAttack"+animateAttack);
            if (Math.abs(this.getDirection().x) > Math.abs(this.getDirection().y)) {
                if (this.getDirection().x < 0) {
                    if (leftStart == false) {
                        this.getEntity().getEvents().trigger("attackLeft");
                        //System.out.println("LeftAttack");
                        leftStart = true;
                    }
                    rightStart = false;
                    upStart = false;
                    downStart = false;
                } else {
                    if (rightStart == false) {
                        this.getEntity().getEvents().trigger("attackRight");
                        //System.out.println("RightAttack");
                        rightStart = true;
                    }
                    leftStart = false;
                    upStart = false;
                    downStart = false;
                }
            } else {
                if (this.getDirection().y < 0) {
                    if (downStart == false) {
                        this.getEntity().getEvents().trigger("attackDown");
                        //System.out.println("DownAttack");
                        downStart = true;
                    }
                    leftStart = false;
                    upStart = false;
                    rightStart = false;
                } else {
                    if (upStart == false) {
                        this.getEntity().getEvents().trigger("attackUp");
                        //System.out.println("UpAttack");
                        upStart = true;
                    }
                    leftStart = false;
                    downStart = false;
                    rightStart = false;
                }
            }
        }
        else{
            if (Math.abs(this.getDirection().x) > Math.abs(this.getDirection().y)) {
                if (this.getDirection().x < 0) {
                    if (leftStart == false) {
                        this.getEntity().getEvents().trigger("LeftStart");
                        leftStart=true;
                    }
                    rightStart = false;
                    upStart = false;
                    downStart = false;
                } else {
                    if (rightStart==false){
                        this.getEntity().getEvents().trigger("RightStart");
                        rightStart=true;
                    }
                    leftStart = false;
                    upStart = false;
                    downStart = false;
                }
            } else {
                if (this.getDirection().y < 0) {
                    if (downStart==false) {
                        this.getEntity().getEvents().trigger("DownStart");
                        downStart=true;
                    }
                    leftStart = false;
                    upStart = false;
                    rightStart = false;
                } else {
                    if (upStart==false) {
                        this.getEntity().getEvents().trigger("UpStart");
                        upStart=true;
                    }

                }
                leftStart = false;
                downStart = false;
                rightStart = false;
            }
        }
        System.out.println("this.getDirection().x"+this.getDirection().x+"this.getDirection().y"+this.getDirection().y);
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

    public void setAnimateAttack() {
        animateAttack = true;
    }

    public void stopAnimateAttack() {
        System.out.println("stopAnimateAttack, set to false");
        animateAttack = false;
    }
}
