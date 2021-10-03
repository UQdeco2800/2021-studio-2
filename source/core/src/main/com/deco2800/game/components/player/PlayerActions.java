package com.deco2800.game.components.player;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.weapons.Axe;
import com.deco2800.game.components.weapons.Hammer;
import com.deco2800.game.components.weapons.MeleeWeapon;
import com.deco2800.game.components.weapons.Scepter;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {

    private static final Vector2 MAX_SPEED = new Vector2(3f, 3f); // Metres per second

    private PhysicsComponent physicsComponent;
    private Vector2 walkDirection = Vector2.Zero.cpy();
    private Vector2 lastDirection = Vector2.Zero.cpy();
    private long lockDuration;
    private long timeSinceStopped;
    private boolean moving = false;

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        entity.getEvents().addListener("walk", this::walk);
        entity.getEvents().addListener("walkStop", this::stopWalking);
        entity.getEvents().addListener("attack", this::attack);
        entity.getEvents().addListener("strongAttack", this::strongAttack);
        entity.getEvents().addListener("mouseAttack", this::mouseAttack);
        entity.getEvents().addListener("lockMovement", this::lockMovement);
        entity.getEvents().addListener("dash", this::dash);
        entity.getEvents().addListener("mouseStrongAttack", this::mouseStrongAttack);
        entity.getEvents().addListener("hit", this::hitAnimation);
    }

    /**
     * Updates speed of player if moving.
     * Also keeps track of lockMovement() duration.
     */
    @Override
    public void update() {
        if (moving) {
            updateSpeed();
            // lock movement if a duration is specified.
        } else if (lockDuration != 0) {
            long currentTime = ServiceLocator.getTimeSource().getTime();
            // determine whether lock duration has passed
            if ((currentTime - timeSinceStopped) >= lockDuration) {
                // unlock movement
                lockDuration = 0;
                moving = true;
            }
        }
    }

    /**
     * Updates the walking speed of the main character to allow for physics collision
     * calculations and change of position.
     */
    private void updateSpeed() {
        Body body = physicsComponent.getBody();
        Vector2 velocity = body.getLinearVelocity();
        Vector2 desiredVelocity = walkDirection.cpy().scl(MAX_SPEED);
        // impulse = (desiredVel - currentVel) * mass
        Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
        System.out.println(entity.getPosition()); //Used for debugging
    }

    /**
     * Moves the player towards a given direction.
     *
     * @param direction direction to move in
     */
    public void walk(Vector2 direction) {
        this.walkDirection = direction;
        this.lastDirection = walkDirection.cpy();
        triggerWalkAnimation();
        if (lockDuration == 0) {
            moving = true;
        }
    }

    /**
     * Stops the player from walking.
     */
    public void stopWalking() {
        this.walkDirection = Vector2.Zero.cpy();
        this.triggerStandAnimation();
        updateSpeed();
        moving = false;
    }

    /**
     * Moves the player towards a given direction.
     *
     * @param direction direction to move in
     */
    public void dash(Vector2 direction) {
        this.walkDirection = direction;
        if (lockDuration == 0) {
            moving = true;
        }
    }

    MeleeWeapon getEquippedWeapon() {
        MeleeWeapon weapon = entity.getComponent(Axe.class);
        if (weapon == null) {
            weapon = entity.getComponent(Hammer.class);
        }
        if (weapon == null) {
            weapon = entity.getComponent(Scepter.class);
        }
        return weapon;
    }

    /**
     * Makes the player attack. Player currently only uses an axe.
     *
     * @param keycode - the last pressed player key.
     */
    void attack(int keycode) {
        MeleeWeapon weapon = getEquippedWeapon();
        if (weapon == null) {
            return;
        }
        int attackDirection = 0;
        switch (keycode) {
            case Input.Keys.W:
                attackDirection = MeleeWeapon.UP;
                break;
            case Input.Keys.S:
                attackDirection = MeleeWeapon.DOWN;
                break;
            case Input.Keys.A:
                attackDirection = MeleeWeapon.LEFT;
                break;
            case Input.Keys.D:
                attackDirection = MeleeWeapon.RIGHT;
                break;
        }
        weapon.attack(attackDirection);
        lockMovement(weapon.getTotalAttackTime());
    }

    /**
     * Makes player use the strong attack associated with its equipped weapon.
     */
    void strongAttack(int keycode) {
        MeleeWeapon weapon = getEquippedWeapon();
        if (weapon == null) {
            return;
        }
        int attackDirection = 0;
        switch (keycode) {
            case Input.Keys.W:
                attackDirection = MeleeWeapon.UP;
                break;
            case Input.Keys.S:
                attackDirection = MeleeWeapon.DOWN;
                break;
            case Input.Keys.A:
                attackDirection = MeleeWeapon.LEFT;
                break;
            case Input.Keys.D:
                attackDirection = MeleeWeapon.RIGHT;
                break;
        }
        weapon.strongAttack(attackDirection);
    }

    /**
     * Makes the player attack using a mouse click.
     *
     * @param coordinates the mouse coordinates of the click
     */
    void mouseAttack(Vector2 coordinates) {
        MeleeWeapon weapon = getEquippedWeapon();
        if (weapon == null) {
            return;
        }
        Vector2 attackDirection = Vector2Utils.toDirection(new Vector2(
                coordinates.x - Gdx.graphics.getWidth() / 2f,
                coordinates.y - Gdx.graphics.getHeight() / 2f
        ));
        weapon.attack(Vector2Utils.toWeaponDirection(attackDirection));
        lockMovement(weapon.getTotalAttackTime());
    }

    /**
     * Locks player movement for a specified duration.
     *
     * @param duration - the time the movement lock will last, measured in milliseconds.
     */
    void lockMovement(long duration) {
        timeSinceStopped = ServiceLocator.getTimeSource().getTime();
        lockDuration = duration;
        moving = false;
    }

    /**
     * Triggers the animation to be played when the player gets hit.
     */
    public void hitAnimation() {
        if (lastDirection.y > 0) {
            entity.getEvents().trigger("damagedUp");
        } else if (lastDirection.y < 0) {
            entity.getEvents().trigger("damagedDown");
        } else if (lastDirection.x > 0) {
            entity.getEvents().trigger("damagedRight");
        } else if (lastDirection.x < 0) {
            entity.getEvents().trigger("damagedLeft");
        }
    }

    /**
     * Checks the direction that the player was last facing and changes the animation to match.
     */
    void triggerStandAnimation() {
        if (lastDirection.y > 0) {
            entity.getEvents().trigger("stopBackward");
        } else if (lastDirection.y < 0) {
            entity.getEvents().trigger("stopForward");
        } else if (lastDirection.x > 0) {
            entity.getEvents().trigger("stopRight");
        } else if (lastDirection.x < 0) {
            entity.getEvents().trigger("stopLeft");
        }
    }

    void mouseStrongAttack(Vector2 coordinates) {
        MeleeWeapon weapon = getEquippedWeapon();
        if (weapon == null) {
            return;
        }
        Vector2 attackDirection = Vector2Utils.toDirection(new Vector2(
                coordinates.x - Gdx.graphics.getWidth() / 2f,
                coordinates.y - Gdx.graphics.getHeight() / 2f
        ));
        weapon.strongAttack(Vector2Utils.toWeaponDirection(attackDirection));
        lockMovement(weapon.getTotalAttackTime());
    }

    /**
     * Checks the direction that the player is moving in and changes the animation to match.
     */
    void triggerWalkAnimation() {
        if (walkDirection.y > 0) {
            entity.getEvents().trigger("walkBackward");
        } else if (walkDirection.y < 0) {
            entity.getEvents().trigger("walkForward");
        } else if (walkDirection.x > 0) {
            entity.getEvents().trigger("walkRight");
        } else if (walkDirection.x < 0) {
            entity.getEvents().trigger("walkLeft");
        }
    }
}
