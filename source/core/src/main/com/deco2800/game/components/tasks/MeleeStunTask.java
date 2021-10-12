package com.deco2800.game.components.tasks;


import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.touch.TouchAttackComponent;
import com.deco2800.game.entities.Entity;

public class MeleeStunTask extends TouchAttackComponent {

    public MeleeStunTask(short targetLayer, Entity player) {
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
