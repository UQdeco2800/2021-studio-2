package com.deco2800.game.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BooleanObjectTest {

    @Test
    void shouldInstantiateFalse() {
        BooleanObject bool = new BooleanObject(false);
        assertFalse(bool.getBoolean());
    }

    @Test
    void shouldInstantiateTrue() {
        BooleanObject bool = new BooleanObject(true);
        assertTrue(bool.getBoolean());
    }

    @Test
    void shouldChangeFalse() {
        BooleanObject bool = new BooleanObject(true);
        bool.setFalse();
        assertFalse(bool.getBoolean());
    }

    @Test
    void shouldChangeTrue() {
        BooleanObject bool = new BooleanObject(false);
        bool.setTrue();
        assertTrue(bool.getBoolean());
    }
}
