package com.deco2800.game.components.weapons;

import com.deco2800.game.components.Component;

/**
 * Represents the overall weapon class for which all weapon-types will inherit from.
 */
public abstract class Weapon extends Component {
    private int attackPower; // damage
    private short targetLayer; // weapon target, determines what entities the weapon can inflict damage upon.
    private float knockback; // NOTE: optional


    public Weapon(short targetLayer, int attackPower, float knockback) {
        this.targetLayer = targetLayer;
        this.attackPower = attackPower;
        this.knockback = knockback;
    }

    public abstract void attack();

}
