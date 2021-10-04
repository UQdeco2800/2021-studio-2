package com.deco2800.game.components.tasks;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;

import java.util.Scanner;

public class meleeStun extends TouchAttackComponent {

    public meleeStun(short targetLayer, Entity player) {
        super(targetLayer);
        //this.player = player;
    }

    @Override
    protected void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            //do nothing
            return;
        }


    }

}