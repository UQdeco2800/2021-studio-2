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
        crateHandler.addListener("barrelHit", this::hitBarrel);
        crateHandler.addListener("barrelDeath", this::breakBarrel);
    }

    public void hitBarrel() {
        crateAnimation.startAnimation("barrelHit");
    }

    public void breakBarrel() {
        crateAnimation.startAnimation("barrelDeath");
    }
}
