package com.deco2800.game.utils.math;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class RandomUtils {
    private RandomUtils() {
        throw new IllegalStateException("Instantiating static util class");
    }

    public static Vector2 random(Vector2 start, Vector2 end) {
        return new Vector2(MathUtils.random(start.x, end.x), MathUtils.random(start.y, end.y));
    }
}
