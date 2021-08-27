package com.deco2800.game.components.weapons;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;

/**
 * Represents the overall weapon class for which all weapons will inherit from.
 */
public abstract class Weapon extends Component {
    protected int attackPower; // damage
    protected short targetLayer; // weapon target (what the weapon can damage)
    protected float knockback;
    protected CombatStatsComponent combatStats;

    public Weapon(short targetLayer, int attackPower, float knockback) {
        this.targetLayer = targetLayer;
        this.attackPower = attackPower;
        this.knockback = knockback;
    }

    @Override
    public void create() {
        combatStats = entity.getComponent(CombatStatsComponent.class);
    }

    public abstract void attack(int attackDirection);

}
