package com.deco2800.game.components.weapons;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.physics.components.HitboxComponent;

/*
Enables attack using a weapon
 */
public class MeleeWeapon extends Weapon {
    private int attackPower;
    private final int attackRange;
    private final int attackWidth;
    private CombatStatsComponent combatStats; // get information about entity's base attack stats


    public MeleeWeapon(short targetLayer, float knockback, int attackPower, int range, int attackWidth) {
        super(targetLayer, attackPower, knockback);
        this.attackRange = range;
        this.attackWidth = attackWidth;
    }
    @Override
    public void create(){
        // get combat stats from owner
        combatStats = entity.getComponent(CombatStatsComponent.class);
        // listen for collisions
        entity.getEvents().addListener("Weapon collison", this::weaponCollision);

    }

    @Override
    public void attack(){
        // START animation goes here.
        // ATTACK animation goes here.
            // create a new hitbox component & attach it to owner entity, its size is defined in terms of attack width & range
            /** HitboxComponent weaponHitBox = new HitboxComponent();
            entity.addComponent(weaponHitBox.setAsBox(new Vector2(attackWidth, attackRange))); **/
            // Use to detect collisions, & call onCollisionStart (handled in events)
            // Delete the hitbox component.

        // FINISH animation goes here.
    }

    private void weaponCollision(Fixture me, Fixture other) {

    }

}





