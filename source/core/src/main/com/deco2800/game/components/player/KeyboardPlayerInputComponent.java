package com.deco2800.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.utils.math.Vector2Utils;

//This class has been imported to allow for a short delay for dash abilities
import java.util.Timer;
import java.util.TimerTask;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
  private final Vector2 walkDirection = Vector2.Zero.cpy();

  /** Distance scale for moving in a diagonal direction. */
  public final float DIAGONAL_DISTANCE = 0.7071f;

  /** When the player is pressing W, up is 1, else, up is 0. */
  private float up = 0f;

  /** When the player is pressing A, up is 1, else, up is 0. */
  private float left = 0f;

  /** When the player is pressing S, up is 1, else, up is 0. */
  private float down = 0f;

  /** When the player is pressing D, up is 1, else, up is 0. */
  private float right = 0f;

  /** Used to change the speed of the player quickly. */
  private float speedMultiplier = 1;

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
        this.up = 1;
        triggerWalkEvent();
        return true;
      case Keys.A:
        this.left = 1;
        triggerWalkEvent();
        return true;
      case Keys.S:
        this.down = 1;
        triggerWalkEvent();
        return true;
      case Keys.D:
        this.right = 1;
        triggerWalkEvent();
        return true;
      case Keys.SPACE:
        entity.getEvents().trigger("attack");
        return true;
      case Keys.SHIFT_LEFT:
        this.speedMultiplier = 1.4f;
        triggerWalkEvent();
        return true;
      case Keys.Q:
        this.speedMultiplier = 2.5f;
        triggerWalkEvent();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
          @Override
          public void run() {
            speedMultiplier = 1f;
            triggerWalkEvent();
          }
        }, 150);
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

  private void triggerWalkEvent() {
    //Checks to see if the player should be static or is currently moving.
    if ((this.up - this.down) == 0 && (this.right - this.left) == 0) {
      entity.getEvents().trigger("walkStop");
    } else {
      float x = this.right - this.left;
      float y = this.up - this.down;
      if (x != 0 && y != 0) {
        x = (this.right - this.left) * DIAGONAL_DISTANCE;
        y = (this.up - this.down) * DIAGONAL_DISTANCE;
      }
      walkDirection.x = x * this.speedMultiplier;
      walkDirection.y = y * this.speedMultiplier;
      entity.getEvents().trigger("walk", walkDirection);

    }
  }
}


// Original Code
///**
// * Input handler for the player for keyboard and touch (mouse) input.
// * This input handler only uses keyboard input.
// */
//public class KeyboardPlayerInputComponent extends InputComponent {
//  private final Vector2 walkDirection = Vector2.Zero.cpy();
//
//  public KeyboardPlayerInputComponent() {
//    super(5);
//  }
//
//  /**
//   * Triggers player events on specific keycodes.
//   *
//   * @return whether the input was processed
//   * @see InputProcessor#keyDown(int)
//   */
//  @Override
//  public boolean keyDown(int keycode) {
//    switch (keycode) {
//      case Keys.W:
//        walkDirection.add(Vector2Utils.UP);
//        triggerWalkEvent();
//        return true;
//      case Keys.A:
//        walkDirection.add(Vector2Utils.LEFT);
//        triggerWalkEvent();
//        return true;
//      case Keys.S:
//        walkDirection.add(Vector2Utils.DOWN);
//        triggerWalkEvent();
//        return true;
//      case Keys.D:
//        walkDirection.add(Vector2Utils.RIGHT);
//        triggerWalkEvent();
//        return true;
//      case Keys.SPACE:
//        entity.getEvents().trigger("attack");
//        return true;
//      default:
//        return false;
//    }
//  }
//
//  /**
//   * Triggers player events on specific keycodes.
//   *
//   * @return whether the input was processed
//   * @see InputProcessor#keyUp(int)
//   */
//  @Override
//  public boolean keyUp(int keycode) {
//    switch (keycode) {
//      case Keys.W:
//        walkDirection.sub(Vector2Utils.UP);
//        triggerWalkEvent();
//        return true;
//      case Keys.A:
//        walkDirection.sub(Vector2Utils.LEFT);
//        triggerWalkEvent();
//        return true;
//      case Keys.S:
//        walkDirection.sub(Vector2Utils.DOWN);
//        triggerWalkEvent();
//        return true;
//      case Keys.D:
//        walkDirection.sub(Vector2Utils.RIGHT);
//        triggerWalkEvent();
//        return true;
//      default:
//        return false;
//    }
//  }
//
//  private void triggerWalkEvent() {
//    if (walkDirection.epsilonEquals(Vector2.Zero)) {
//      entity.getEvents().trigger("walkStop");
//    } else {
//      entity.getEvents().trigger("walk", walkDirection);
//    }
//  }
//}
