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
        entity.getEvents().addListener("stopForward", this::stopForwardWalk);
        entity.getEvents().addListener("stopRight", this::stopRightWalk);
        entity.getEvents().addListener("stopLeft", this::stopLeftWalk);
        entity.getEvents().addListener("stopBackward", this::stopBackwardWalk);
        stopForwardWalk();
    }

    /**
     * Once triggered, it changes the animation of the sprite to walk forward.
     */
    void animateForwardWalk() {
        animator.startAnimation("walk_forward");
    }

    /**
     * Once triggered, it changes the animation of the sprite to walk to the right.
     */
    void animateRightWalk() {
        animator.startAnimation("walk_right");
    }

    /**
     * Once triggered, it changes the animation of the sprite to walk to the left.
     */
    void animateLeftWalk() {
        animator.startAnimation("walk_left");
    }

    /**
     * Once triggered, it changes the animation of the sprite to walk backwards.
     */
    void animateBackwardWalk() {
        animator.startAnimation("walk_backward");
    }

    /**
     * Once triggered, it changes the animation of the sprite to be shown in the default frame.
     */
    void stopForwardWalk() {
        animator.startAnimation("default");
    }

    /**
     * Once triggered, it changes the animation of the sprite to default frame facing backwards.
     */
    void stopBackwardWalk() {
        animator.startAnimation("default_backward");
    }

    /**
     * Once triggered, it changes the animation of the sprite to the default frame facing to the left.
     */
    void stopLeftWalk() {
        animator.startAnimation("default_left");
    }

    /**
     * Once triggered, it changes the animation of the sprite to the default frame facing to the right.
     */
    void stopRightWalk() {
        animator.startAnimation("default_right");
    }
}
 
