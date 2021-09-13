package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class GhostAnimationController extends Component {
    AnimationRenderComponent animator;
    private boolean death;

    /**
     * Create the animation
     * add listener on entity with wander and chase task
     * play animation on corresponding atlas
     */
    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        death = false;

        entity.getEvents().addListener("LeftStart", this::animateLeft);
        entity.getEvents().addListener("RightStart", this::animateRight);
        entity.getEvents().addListener("UpStart", this::animateUp);
        entity.getEvents().addListener("DownStart", this::animateDown);
    }

    public void setDeath() {
        death = true;
    }

    public void animateLeft() {
        if (!death) {
            animator.startAnimation("floatLeft");
        } else {

            animator.startAnimation("leftDeath");
        }
    }

    public void animateRight() {
        if (!death) {
            animator.startAnimation("floatRight");
        } else {
            animator.startAnimation("rightDeath");
        }
    }

    public void animateUp() {
        if (!death) {
            animator.startAnimation("floatUp");
        } else {
            animator.startAnimation("frontDeath");
        }
    }

    public void animateDown() {
        if (!death) {
            animator.startAnimation("floatDown");
        } else {
            animator.startAnimation("frontDeath");
        }
    }
}
