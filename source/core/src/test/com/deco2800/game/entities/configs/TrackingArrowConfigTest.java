package com.deco2800.game.entities.configs;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(GameExtension.class)
class TrackingArrowConfigTest {

    @Test
    void checkValueTest() {
        assertEquals(10000, TrackingArrowConfig.HEALTH);
        assertEquals(2f, TrackingArrowConfig.SPEED_X);
        assertEquals(2f, TrackingArrowConfig.SPEED_Y);
        assertEquals(60, TrackingArrowConfig.BASE_ATTACK);
    }

    @Test
    void intialisePrivateConstructor() {
        Constructor<TrackingArrowConfig> constructor = null;
        try {
            constructor = TrackingArrowConfig.class.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        assert constructor != null;
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
        } catch (InstantiationException e) {
            System.out.println("fail()");
        } catch (IllegalAccessException e) {
            System.out.println("fail()2");
        } catch (InvocationTargetException e) {
            System.out.println("fail()3");
        } catch (IllegalStateException e) {
            fail();
        }
    }
}
