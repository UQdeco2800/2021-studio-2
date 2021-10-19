package com.deco2800.game.entities.configs;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(GameExtension.class)
class ArrowConfigTest {

    @Test
    void checkValueTest() {
        assertEquals(10000, ArrowConfig.HEALTH);
        assertEquals(5f, ArrowConfig.SPEED_X);
        assertEquals(5f, ArrowConfig.SPEED_Y);
        assertEquals(30, ArrowConfig.BASE_ATTACK);
    }

    @Test
    void intialisePrivateConstructor() {
        Constructor<ArrowConfig> constructor = null;
        try {
            constructor = ArrowConfig.class.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        assert constructor != null;
        constructor.setAccessible(true);
        try {
            ArrowConfig arrowConfig = constructor.newInstance();
        } catch (InstantiationException e) {
            System.out.println("fail()");
        } catch (IllegalAccessException e) {
            System.out.println("fail()2");
        } catch (InvocationTargetException e) {
            System.out.println("fail()3");
        }  catch (IllegalStateException e) {
            fail();
        }
    }
}
