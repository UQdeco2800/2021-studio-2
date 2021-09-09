package com.deco2800.game.components.player;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.input.InputComponent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler uses keyboard input and mouse input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
    /**
     * Current walk direction of player
     */
    private final Vector2 walkDirection = Vector2.Zero.cpy();

    /**
     * The last key the player pressed, used to determine attack direction
     */
    private int lastKeyPressed;

    /**
     * Distance scale for moving in a diagonal direction.
     */
    public static final float DIAGONAL_DISTANCE = 0.7071f;

    /**
     * Multiplier difference for the dash ability.
     */
    public static final float DASH_MULTIPLIER = 2.5f;

    /**
     * When the player is pressing W, up is 1, else, up is 0.
     */
    private byte up = 0;

    /**
     * When the player is pressing A, left is 1, else, left is 0.
     */
    private byte left = 0;

    /**
     * When the player is pressing S, down is 1, else, down is 0.
     */
    private byte down = 0;

    /**
     * When the player is pressing D, right is 1, else, right is 0.
     */
    private byte right = 0;

    /**
     * While the player is in their dash, it is 1, else it is 0.
     */
    private boolean dashing = false;

    /**
     * Used to change the speed of the player quickly.
     */
    private float speedMultiplier = 1;

    /**
     * Locks the player from inputting any controls.
     */
    private boolean locked = false;

    /**
     * Stores the last system time since the dash ability was pressed.
     */
    //Used to check cool down of the dash ability: private long lastDash = 0L;
    public KeyboardPlayerInputComponent() {
        super(5);
    }

    /**
     * Triggers player events on specific keycodes.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyDown(int)
     */
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.W:
                lastKeyPressed = Keys.W;
                this.up = 1;
                triggerWalkEvent();
                return true;
            case Keys.A:
                lastKeyPressed = Keys.A;
                this.left = 1;
                triggerWalkEvent();
                return true;
            case Keys.S:
                lastKeyPressed = Keys.S;
                this.down = 1;
                triggerWalkEvent();
                return true;
            case Keys.D:
                lastKeyPressed = Keys.D;
                this.right = 1;
                triggerWalkEvent();
                return true;
            case Keys.SPACE:
                entity.getEvents().trigger("attack", lastKeyPressed);
                return true;
            case Keys.SHIFT_LEFT:
                this.speedMultiplier = 1.4f;
                triggerWalkEvent();
                return true;
            case Keys.CAPS_LOCK:
                dashing = true;
                triggerDashEvent();
                update();

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        dashing = false;
                        triggerWalkEvent();
                        timer.cancel();
                    }
                }, 150);
                return true;

            default:
                return false;
        }
    }

    /**
     * Triggers player events on specific keycodes.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyUp(int)
     */
    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.W:
                this.up = 0;
                triggerWalkEvent();
                return true;
            case Keys.A:
                this.left = 0;
                triggerWalkEvent();
                return true;
            case Keys.S:
                this.down = 0;
                triggerWalkEvent();
                return true;
            case Keys.D:
                this.right = 0;
                triggerWalkEvent();
                return true;
            case Keys.SHIFT_LEFT:
                this.speedMultiplier = 1;
                triggerWalkEvent();
                return true;
            default:
                return false;
        }
    }

    /**
     * Disables all of the inputs that are currently being pressed.
     */
    public void stopWalking() {
        this.left = 0;
        this.right = 0;
        this.down = 0;
        this.up = 0;
        this.speedMultiplier = 1;
        entity.getEvents().trigger("walkStop");
    }

    /**
     * Triggers player events on a mouse click. Direction is determined by
     * mouse click coordinates (screenX, screenY).
     *
     * @return whether the mouse input was processed.
     * @see InputProcessor#touchDown(int, int, int, int)
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Buttons.LEFT) { // Attack using left mouse button
            entity.getEvents().trigger("mouseAttack",
                    new Vector2(screenX, screenY));
            return true;
        }
        // other mouse buttons go here (TBD).
        return false;
    }

    /**
     * Triggers the walk event for the player character. The method will handle
     * diagonal movement to ensure that walk speed is consistent and check if
     * the player is no longer moving.
     */
    private void triggerWalkEvent() {
        if (dashing || locked) {
            return;
        }
        calculateDistance(speedMultiplier);
        if (walkDirection.x == 0 && walkDirection.y == 0) {
            entity.getEvents().trigger("walkStop");
        } else {
            calculateDistance(speedMultiplier);
            entity.getEvents().trigger("walk", walkDirection);
        }
    }

    /**
     * Triggers the dash event for the player character. The method will
     * call a movement at a certain speed.
     */
    private void triggerDashEvent() {
        calculateDistance(DASH_MULTIPLIER);
        entity.getEvents().trigger("walk", walkDirection);
    }

    /**
     * Helper function to set the X and Y coordinate movement distances.
     * The method will calculate the distance and check if the player is moving
     * in a diagonal movement and change the distances accordingly.
     *
     * @param multiplier multiplies the distance moved by the character
     */
    private void calculateDistance(float multiplier) {
        float x = (float) this.right - this.left;
        float y = (float) this.up - this.down;
        if (x != 0 && y != 0) {
            x = (this.right - this.left) * DIAGONAL_DISTANCE;
            y = (this.up - this.down) * DIAGONAL_DISTANCE;
        }
        walkDirection.x = x * multiplier;
        walkDirection.y = y * multiplier;
    }

    /**
     * Sets the last key pressed attribute to the integer that has been passed in.
     * @param key direction of the attack of the player
     */
    public void setLastKeyPressed(int key) {
        this.lastKeyPressed = key;
    }

    /**
     * This method has been created to test that the correct direction is
     * to be walked in.
     *
     * @return walk direction.
     */
    public Vector2 getWalkDirection() {
        return this.walkDirection;
    }

    public void lockPlayer() {
        this.locked = true;
    }

    public void unlockPlayer() {
        this.locked = false;
    }
} 
