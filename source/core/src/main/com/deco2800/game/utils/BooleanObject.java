package com.deco2800.game.utils;

public class BooleanObject {

    private boolean bool;

    public BooleanObject(boolean value) {
        this.bool = value;
    }

    public boolean getBoolean() {
        return this.bool;
    }

    public void setFalse() {
        this.bool = false;
    }

    public void setTrue() {
        this.bool = true;
    }
}
