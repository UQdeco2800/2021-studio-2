package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;

import java.util.Scanner;

public class TeleportComponent extends TouchComponent{
    private CombatStatsComponent combatStats;

    private Scanner scanner = new Scanner(System.in);
    private long start = 0;
    private Entity player;

    /**
     * Create a component which attacks entities on collision, without knockback.
     * @param targetLayer The physics layer of the target's collider.
     */
    public TeleportComponent(short targetLayer, Entity player) {
        super(targetLayer);
        this.player = player;
    }

    @Override
    public void create() {
        super.create();
        combatStats = entity.getComponent(CombatStatsComponent.class);
    }

    /**
     * action apply when the hitbox component collide
     * @param me the owner of the hitbox
     * @param other the target of the hitbox
     */
    @Override
    protected void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            //do nothing
            return;
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }

        // Try to teleport player
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
        if (targetStats != null && ((System.currentTimeMillis() - start) / 1000.) > 0.5) {
            //System.out.println((System.currentTimeMillis() - start) / 1000.0 + " seconds");
            //target.setPosition(3f,4f);
            System.out.println("x= " + target.getPosition().x + "  y= " + target.getPosition().y);
            target.setScale(new Vector2(5,5));
            //target.setPosition(new Vector2(15,8));
            //this.player.setPosition(new Vector2(15,8));
            //this.player.setScale(new Vector2(5,5));
        }
    }
}
