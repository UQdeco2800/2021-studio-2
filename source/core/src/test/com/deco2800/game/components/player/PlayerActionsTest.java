package com.deco2800.game.components.player;

import com.badlogic.gdx.Input;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(GameExtension.class)
public class PlayerActionsTest {

    @Test
    void shouldMoveUp() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.W);
        assertEquals(0f, keyboardInput.getWalkDirection().x);
        assertEquals(1f, keyboardInput.getWalkDirection().y);
    }

    @Test
    void shouldMoveLeft() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.A);
        assertEquals(-1f, keyboardInput.getWalkDirection().x);
        assertEquals(0f, keyboardInput.getWalkDirection().y);
    }

    @Test
    void shouldMoveRight() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.D);
        assertEquals(1f, keyboardInput.getWalkDirection().x);
        assertEquals(0f, keyboardInput.getWalkDirection().y);
    }

    @Test
    void shouldMoveDown() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.S);
        assertEquals(0f, keyboardInput.getWalkDirection().x);
        assertEquals(-1f, keyboardInput.getWalkDirection().y);
    }

    @Test
    void shouldMoveUpRight() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.W);
        keyboardInput.keyDown(Input.Keys.D);
        assertEquals(0.7071f, keyboardInput.getWalkDirection().x);
        assertEquals(0.7071f, keyboardInput.getWalkDirection().y);
    }

    @Test
    void shouldMoveUpLeft() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.W);
        keyboardInput.keyDown(Input.Keys.A);
        assertEquals(-0.7071f, keyboardInput.getWalkDirection().x);
        assertEquals(0.7071f, keyboardInput.getWalkDirection().y);
    }

    @Test
    void shouldSprintUp() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.W);
        keyboardInput.keyDown(Input.Keys.SHIFT_LEFT);
        assertEquals(0f, keyboardInput.getWalkDirection().x);
        assertEquals(1.4f, keyboardInput.getWalkDirection().y);
    }

    @Test
    void shouldSprintDown() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.S);
        keyboardInput.keyDown(Input.Keys.SHIFT_LEFT);
        assertEquals(0f, keyboardInput.getWalkDirection().x);
        assertEquals(-1.4f, keyboardInput.getWalkDirection().y);
    }

    @Test
    void shouldSprintLeft() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.A);
        keyboardInput.keyDown(Input.Keys.SHIFT_LEFT);
        assertEquals(-1.4f, keyboardInput.getWalkDirection().x);
        assertEquals(0f, keyboardInput.getWalkDirection().y);
    }

    @Test
    void shouldSprintRight() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.D);
        keyboardInput.keyDown(Input.Keys.SHIFT_LEFT);
        assertEquals(1.4f, keyboardInput.getWalkDirection().x);
        assertEquals(0f, keyboardInput.getWalkDirection().y);
    }

    @Test
    void shouldWalkUpRight() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.W);
        keyboardInput.keyDown(Input.Keys.D);
        assertEquals(0.7071f, keyboardInput.getWalkDirection().x);
        assertEquals(0.7071f, keyboardInput.getWalkDirection().y);
    }

    @Test
    void shouldWalkDownRight() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.S);
        keyboardInput.keyDown(Input.Keys.D);
        assertEquals(0.7071f, keyboardInput.getWalkDirection().x);
        assertEquals(-0.7071f, keyboardInput.getWalkDirection().y);
    }

    @Test
    void shouldWalkUpLeft() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.W);
        keyboardInput.keyDown(Input.Keys.A);
        assertEquals(-0.7071f, keyboardInput.getWalkDirection().x);
        assertEquals(0.7071f, keyboardInput.getWalkDirection().y);
    }

    @Test
    void shouldWalkDownLeft() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.A);
        keyboardInput.keyDown(Input.Keys.S);
        assertEquals(-0.7071f, keyboardInput.getWalkDirection().x);
        assertEquals(-0.7071f, keyboardInput.getWalkDirection().y);
    }

    @Test
    void shouldSprintDownLeft() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.A);
        keyboardInput.keyDown(Input.Keys.S);
        keyboardInput.keyDown(Input.Keys.SHIFT_LEFT);

        assertTrue((-0.98994 - keyboardInput.getWalkDirection().x) < 0.001);
        assertTrue((-0.98994 - keyboardInput.getWalkDirection().y) < 0.001);
    }

    @Test
    void shouldSprintUpRight() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.W);
        keyboardInput.keyDown(Input.Keys.D);
        keyboardInput.keyDown(Input.Keys.SHIFT_LEFT);

        assertTrue((0.98994 - keyboardInput.getWalkDirection().x) < 0.001);
        assertTrue((0.98994 - keyboardInput.getWalkDirection().y) < 0.001);
    }

    @Test
    void shouldSprintDownRight() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.A);
        keyboardInput.keyDown(Input.Keys.S);
        keyboardInput.keyDown(Input.Keys.SHIFT_LEFT);

        assertTrue((-0.98994 - keyboardInput.getWalkDirection().x) < 0.001);
        assertTrue((-0.98994 - keyboardInput.getWalkDirection().y) < 0.001);
    }

    @Test
    void shouldSprintUpLeft() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.W);
        keyboardInput.keyDown(Input.Keys.D);
        keyboardInput.keyDown(Input.Keys.SHIFT_LEFT);

        assertTrue((-0.98994 - keyboardInput.getWalkDirection().x) < 0.001);
        assertTrue((0.98994 - keyboardInput.getWalkDirection().y) < 0.001);
    }

    @Test
    void shouldStandStill() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());

        assertEquals(0, keyboardInput.getWalkDirection().x);
        assertEquals(0, keyboardInput.getWalkDirection().y);
    }

    @Test
    void shouldStandStillAfterUp() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.W);
        keyboardInput.keyUp(Input.Keys.W);

        assertEquals(0, keyboardInput.getWalkDirection().x);
        assertEquals(0, keyboardInput.getWalkDirection().y);
    }

    @Test
    void shouldStandStillAfterDown() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.S);
        keyboardInput.keyUp(Input.Keys.S);

        assertEquals(0, keyboardInput.getWalkDirection().x);
        assertEquals(0, keyboardInput.getWalkDirection().y);
    }

    @Test
    void shouldStandStillAfterLeft() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.A);
        keyboardInput.keyUp(Input.Keys.A);

        assertEquals(0, keyboardInput.getWalkDirection().x);
        assertEquals(0, keyboardInput.getWalkDirection().y);
    }

    @Test
    void shouldStandStillAfterRight() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.D);
        keyboardInput.keyUp(Input.Keys.D);

        assertEquals(0, keyboardInput.getWalkDirection().x);
        assertEquals(0, keyboardInput.getWalkDirection().y);
    }

    @Test
    void shouldMoveLeftAfterUpLeft() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.W);
        keyboardInput.keyDown(Input.Keys.A);
        keyboardInput.keyUp(Input.Keys.W);

        assertEquals(-1f, keyboardInput.getWalkDirection().x);
        assertEquals(0, keyboardInput.getWalkDirection().y);
    }

    @Test
    void shouldDashUp() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.W);
        keyboardInput.keyDown(Input.Keys.CAPS_LOCK);

        assertEquals(0f, keyboardInput.getWalkDirection().x);
        assertEquals(2.5, keyboardInput.getWalkDirection().y);
    }

    @Test
    void shouldDashUpLeft() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.W);
        keyboardInput.keyDown(Input.Keys.A);
        keyboardInput.keyDown(Input.Keys.CAPS_LOCK);

        assertTrue((-1.7678 - keyboardInput.getWalkDirection().x) < 0.001);
        assertTrue((1.7678 - keyboardInput.getWalkDirection().y) < 0.001);
    }

    @Test
    void shouldDashDownRight() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.S);
        keyboardInput.keyDown(Input.Keys.D);
        keyboardInput.keyDown(Input.Keys.CAPS_LOCK);

        assertTrue((1.7678 - keyboardInput.getWalkDirection().x) < 0.001);
        assertTrue((-1.7678 - keyboardInput.getWalkDirection().y) < 0.001);
    }

    @Test
    void shouldNotMoveWhileDashing() {
        KeyboardPlayerInputComponent keyboardInput = new KeyboardPlayerInputComponent();
        keyboardInput.setEntity(new Entity());
        keyboardInput.keyDown(Input.Keys.S);
        keyboardInput.keyDown(Input.Keys.D);
        keyboardInput.keyDown(Input.Keys.CAPS_LOCK);
        keyboardInput.keyUp(Input.Keys.S);
        keyboardInput.keyUp(Input.Keys.D);

        assertTrue((1.7678 - keyboardInput.getWalkDirection().x) < 0.001);
        assertTrue((-1.7678 - keyboardInput.getWalkDirection().y) < 0.001);
    }
}
 
