package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to an entity's state and plays the animation when one
 * of the events is triggered.
 */
public class ElfAnimationController extends Component {
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
        death = false;
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

    public void setDeath() {
        death = true;
    }

    public void animateLeft() {
        if (!death) {
            animator.startAnimation("moveLeft");
        } else {
            animator.startAnimation("leftDeath");
        }
    }

    public void animateRight() {
        if (!death) {
            animator.startAnimation("moveRight");
        } else {
            animator.startAnimation("rightDeath");
        }
    }

    public void animateUp() {
        if (!death) {
            animator.startAnimation("moveDown");
        } else {
            animator.startAnimation("frontDeath");
        }
    }

    public void animateDown() {
        if (!death) {
            animator.startAnimation("moveDown");
        } else {
            animator.startAnimation("frontDeath");
        }
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
