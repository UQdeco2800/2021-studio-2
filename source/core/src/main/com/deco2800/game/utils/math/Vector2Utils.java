package com.deco2800.game.utils.math;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.weapons.MeleeWeapon;

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

    private Vector2Utils() {
        throw new IllegalStateException("Instantiating static util class");
    }

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
     * Converts Vector2Utils direction to MeleeWeapon direction.
     *
     * @param vectorDirection defined in Vector2Utils
     * @return MeleeWeapon direction represented by an integer, or -1 if failed
     */
    public static int toWeaponDirection(Vector2 vectorDirection) {
        if (vectorDirection == Vector2Utils.UP) {
            return MeleeWeapon.UP;
        } else if (vectorDirection == Vector2Utils.DOWN) {
            return MeleeWeapon.DOWN;
        } else if (vectorDirection == Vector2Utils.LEFT) {
            return MeleeWeapon.LEFT;
        } else if (vectorDirection == Vector2Utils.RIGHT) {
            return MeleeWeapon.RIGHT;
        } else {
            return -1;
        }
    }

    /**
     * Swaps the elements / axis of a vector.
     *
     * @param vector The vector whose axis will be swapped
     * @return The vector that's been swapped
     */
    @SuppressWarnings("UnusedReturnValue")
    public static Vector2 swapAxis(Vector2 vector) {
        //noinspection SuspiciousNameCombination
        vector.set(vector.y, vector.x);
        return vector;
    }
}
