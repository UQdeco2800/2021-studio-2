package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.screens.MainGameScreen;

import java.util.Scanner;

public class TeleportComponent extends TouchComponent {
    private CombatStatsComponent combatStats;

    private Scanner scanner = new Scanner(System.in);
    private long start = 0;
    private Entity player;
    private boolean boss;

    public TeleportComponent(short targetLayer, Entity player) {
        super(targetLayer);
        this.player = player;

        boss = true;
    }

    @Override
    public void create() {
        super.create();
        combatStats = entity.getComponent(CombatStatsComponent.class);
    }

    /**
     * action apply when the hitbox component collide
     *
     * @param me    the owner of the hitbox
     * @param other the target of the hitbox
     */
    @Override
    protected void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            //do nothing
            return;
        }

        // Try to teleport player
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
        if (boss) {
            if (targetStats != null && ((System.currentTimeMillis() - start) / 1000.) > 0.5) {
                //System.out.println("here");
                //target.getComponent(CombatStatsComponent.class).setHealth(50);
                MainGameScreen.levelChange();
            }
        }
    }
}
