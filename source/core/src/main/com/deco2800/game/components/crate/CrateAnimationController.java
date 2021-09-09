package com.deco2800.game.components.crate;

import com.deco2800.game.components.Component;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * Plays animation based on the event listeners when the barrel is interacted with.
 */
public class CrateAnimationController extends Component {
    AnimationRenderComponent crateAnimation;

    @Override
    public void create() {
        super.create();
        crateAnimation = this.entity.getComponent(AnimationRenderComponent.class);
        EventHandler crateHandler = this.entity.getEvents();
        crateHandler.addListener("hit", this::hitBarrel);
        crateHandler.addListener("death", this::breakBarrel);
    }

    public void hitBarrel() {
        crateAnimation.startAnimation("hit");
    }

    public void breakBarrel() {
        crateAnimation.startAnimation("break");
    }

    public void spawnHealth() {

    }
}
