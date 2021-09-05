package com.deco2800.game.entities.factories;

import com.deco2800.game.entities.Entity;

public class CrateFactory {

    public static Entity createCrate() {
        return null;
    }

    private CrateFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
