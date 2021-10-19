package com.deco2800.game.entities.configs;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(GameExtension.class)
class ElfBossConfigTest {

    @Test
    void checkValueTest() {
        assertEquals(1000, ElfBossConfig.HEALTH);
        assertEquals(0, ElfBossConfig.BASE_ATTACK);
    }

    @Test
    void intialisePrivateConstructor() {
        Constructor<ElfBossConfig> constructor = null;
        try {
            constructor = ElfBossConfig.class.getDeclaredConstructor();
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
