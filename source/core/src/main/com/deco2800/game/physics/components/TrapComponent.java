package com.deco2800.game.physics.components;

public class TrapComponent extends ColliderComponent {
    @Override
    public void create() {
        setSensor(true);
        super.create();
    }
}
