package com.deco2800.game.utils.math;

import com.badlogic.gdx.math.Vector2;

/**
 * Contains additional utility constants and functions for common Vector2 operations.
 */
public class Vector2Utils {
  public static final Vector2 LEFT = new Vector2(-1f, 0f);
  public static final Vector2 RIGHT = new Vector2(1f, 0f);
  public static final Vector2 UP = new Vector2(0f, 1f);
  public static final Vector2 DOWN = new Vector2(0f, -1f);
  public static final Vector2 CENTER = new Vector2(0f, 0f);

  public static final Vector2 ONE = new Vector2(1f, 1f);
  public static final Vector2 MAX = new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
  public static final Vector2 MIN = new Vector2(Float.MIN_VALUE, Float.MIN_VALUE);

  /**
   * Calculate the direction (out of 4 directions) from a given vecotr.
   *
   * @param vector The vector relative to the origin
   * @return Vector representing direction (either up, down, left or right)
   */
  public static Vector2 toDirection(Vector2 vector) {
    Vector2 direction = CENTER;
    if (Math.abs(vector.x) > Math.abs(vector.y)) {
      if (vector.x < 0) {
        direction = LEFT;
      } else if (vector.x > 0) {
        direction = RIGHT;
      }
    } else if (Math.abs(vector.y) > Math.abs(vector.x)) {
      if (vector.y < 0) {
        direction = UP;
      } else if (vector.y > 0) {
        direction = DOWN;
      }
    }
    return direction;
  }


  /**
   * Calculate the angle in degrees of a vector.
   *
   * @param vector The vector relative to the origin
   * @return Angle in degrees from -180 to 180
   */
  public static double angleTo(Vector2 vector) {
    return Math.toDegrees(Math.atan2(vector.y, vector.x));
  }

  /**
   * Calculate the angle in degrees between two vectors
   *
   * @param from The vector from which angle is measured
   * @param to The vector to which angle is measured
   * @return Angle in degrees from -180 to 180
   */
  public static double angleFromTo(Vector2 from, Vector2 to) {
    return Math.toDegrees(Math.atan2(to.y - from.y, to.x - from.x));
  }

  /**
   * Swaps the elements (x & y) of a vector.
   *
   * @param vector The vector to be swapped
   * @return The vector that's been swapped
   */
  public static Vector2 swapAxis(Vector2 vector) {
    vector.set(vector.y, vector.x);
    return vector;
  }

  private Vector2Utils() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
