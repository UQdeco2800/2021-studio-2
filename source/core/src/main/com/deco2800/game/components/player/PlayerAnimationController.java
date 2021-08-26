package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class PlayerAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("walkForward", this::animateForwardWalk);
        entity.getEvents().addListener("walkRight", this::animateRightWalk);
        entity.getEvents().addListener("walkLeft", this::animateLeftWalk);
        entity.getEvents().addListener("walkBackward", this::animateBackwardWalk);
        entity.getEvents().addListener("stop", this::stopWalking);
        stopWalking();
    }

    void animateForwardWalk() {
        animator.startAnimation("walk_forward");
    }

    void animateRightWalk() {
        animator.startAnimation("walk_right");
    }

    void animateLeftWalk() {
        animator.startAnimation("walk_left");
    }

    void animateBackwardWalk() {
        animator.startAnimation("walk_backward");
    }

    void stopWalking() {
        animator.startAnimation("default");
    }
}
