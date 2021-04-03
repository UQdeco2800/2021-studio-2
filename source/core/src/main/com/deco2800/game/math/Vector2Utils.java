package com.deco2800.game.math;

import com.badlogic.gdx.math.Vector2;

public class Vector2Utils {
  public static Vector2 Left = new Vector2(-1f, 0f);
  public static Vector2 Right = new Vector2(1f, 0f);
  public static Vector2 Up = new Vector2(0f, 1f);
  public static Vector2 Down = new Vector2(0f, -1f);
  public static Vector2 One = new Vector2(1f, 1f);

  /**
   * Calculate the angle in degrees of a vector.
   * @param vector The vector relative to the origin
   * @return Angle in degrees from -180 to 180
   */
  public static double angleTo(Vector2 vector) {
    return Math.toDegrees(Math.atan2(vector.y, vector.x));
  }

  /**
   * Calculate the angle in degrees between two vectors
   * @param from The vector from which angle is measured
   * @param to The vector to which angle is measured
   * @return Angle in degrees from -180 to 180
   */
  public static double angleFromTo(Vector2 from, Vector2 to) {
    return Math.toDegrees(Math.atan2(to.y - from.y, to.x - from.x));
  }
}
