package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to an entity's state and plays the animation when one
 * of the events is triggered.
 */
public class HumanAnimationController extends Component {
    AnimationRenderComponent animator;
    private boolean death;

    private boolean up = false;

    private boolean down = false;

    private boolean left = false;

    private boolean right = false;

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

        entity.getEvents().addListener("attackLeft", this::animateAttackLeft);
        entity.getEvents().addListener("attackRight", this::animateAttackRight);
        entity.getEvents().addListener("attackUp", this::animateAttackUp);
        entity.getEvents().addListener("attackDown", this::animateAttackDown);

        entity.getEvents().addListener("attackLeft", this::animateAttackLeft);
        entity.getEvents().addListener("attackRight", this::animateAttackRight);
        entity.getEvents().addListener("attackUp", this::animateAttackUp);
        entity.getEvents().addListener("attackDown", this::animateAttackDown);

        animateDown();
    }

    public void setDeath() {
        death = true;
    }

    public void animateLeft() {
        if (!death) {
            if (!left) {
                animator.startAnimation("moveLeft");
                left = true;
            }
        } else {
            animator.startAnimation("deathLeft");
        }
    }

    public void animateRight() {
        if (!death) {
            if (!right) {
                animator.startAnimation("moveRight");
                right = true;
            }
        } else {
            animator.startAnimation("deathRight");
        }
    }

    public void animateUp() {
        if (!death) {
            if (!up) {
                animator.startAnimation("moveUp");
                up = true;
            }
        } else {
            animator.startAnimation("deathUp");
        }
    }

    public void animateDown() {
        if (!death) {
            if (!down) {
                animator.startAnimation("moveDown");
                down = true;
            }
        } else {
            animator.startAnimation("deathDown");
        }
    }

    public void animateAttackLeft() {
        left = false;
        animator.startAnimation("attackLeft");
    }

    public void animateAttackRight() {
        right = false;
        animator.startAnimation("attackRight");
    }

    public void animateAttackUp() {
        up = false;
        animator.startAnimation("attackUp");
    }

    public void animateAttackDown() {
        down = false;
        animator.startAnimation("attackDown");
    }
}
