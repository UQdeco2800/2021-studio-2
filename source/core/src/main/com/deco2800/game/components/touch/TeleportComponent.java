package com.deco2800.game.components.touch;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.screens.MainGameScreen;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.CutsceneScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

public class TeleportComponent extends TouchComponent {

    private static final Logger logger = LoggerFactory.getLogger(TeleportComponent.class);

    private long start = 0;

    public TeleportComponent(short targetLayer) {
        super(targetLayer);
    }

    /**
     * action apply when the hitbox component collide
     *
     * @param me    the owner of the hitbox
     * @param other the target of the hitbox
     */
    @Override
    protected void onCollisionStart(Fixture me, Fixture other) {

        logger.debug("A TeleportComponent entity has been collided with");

        if (this.checkEntities(me, other)) {
            return;
        }

        // Try to teleport player
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;

        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
        if (targetStats != null && ((System.currentTimeMillis() - start) / 1000.) > 0.5) {
            CutsceneScreen screen = ServiceLocator.getEntityService()
                    .getUIEntity().getComponent(CutsceneScreen.class);
            screen.setOpen();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    logger.debug("Level is being changed after the collision");
                    MainGameScreen.levelChange();
                    timer.cancel();
                }
            }, 1000);
        }
    }
}