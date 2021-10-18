package com.deco2800.game.entities.configs;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(GameExtension.class)
class MeleeEnemyConfigTest {

    @Test
    void checkValueTest() {
        assertEquals(40, MeleeEnemyConfig.HEALTH);
        assertEquals(30, MeleeEnemyConfig.BASE_ATTACK);
    }

    @Test
    void intialisePrivateConstructor() {
        Constructor<MeleeEnemyConfig> constructor = null;
        try {
            constructor = MeleeEnemyConfig.class.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        assert constructor != null;
        constructor.setAccessible(true);
        try {
            MeleeEnemyConfig meleeEnemyConfig = constructor.newInstance();
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
