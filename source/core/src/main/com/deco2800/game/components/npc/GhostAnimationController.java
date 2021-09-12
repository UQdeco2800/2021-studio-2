package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class GhostAnimationController extends Component {
    AnimationRenderComponent animator;

    /**
     * Create the animation
     * add listener on entity with wander and chase task
     * play animation on corresponding atlas
     */
    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);

        entity.getEvents().addListener("LeftStart", this::animateLeft);
        entity.getEvents().addListener("RightStart", this::animateRight);
        entity.getEvents().addListener("UpStart", this::animateUp);
        entity.getEvents().addListener("DownStart", this::animateDown);

        entity.getEvents().addListener("Left_Shoot", this::animateLeftShoot);
        entity.getEvents().addListener("Right_Shoot", this::animateRightShoot);
        entity.getEvents().addListener("Down_Shoot", this::animateDownShoot);
        entity.getEvents().addListener("Up_Shoot", this::animateUpShoot);
    }

    public void animateLeft() {
        animator.startAnimation("floatLeft");
    }

    public void animateRight() {
        animator.startAnimation("floatRight");
    }

    public void animateUp() {
        animator.startAnimation("floatUp");
    }

    public void animateDown() {
        animator.startAnimation("floatDown");
    }

    public void animateLeftShoot() {
        animator.startAnimation("Left_Shoot");
    }

    public void animateRightShoot() {
        animator.startAnimation("Right_Shoot");
    }

    public void animateDownShoot() {
        animator.startAnimation("Down_Shoot");
    }

    public void animateUpShoot() {
        animator.startAnimation("Up_Shoot");
    }

}
