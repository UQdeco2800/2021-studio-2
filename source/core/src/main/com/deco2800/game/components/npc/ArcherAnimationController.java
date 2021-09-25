package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to an entity's state and plays the animation when one
 * of the events is triggered.
 */
public class ArcherAnimationController extends Component {
    AnimationRenderComponent animator;
    private boolean death;
    private boolean attack;
    private long start;
    private boolean left = false;
    private boolean up = false;
    private boolean down = false;
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
        attack = false;
        this.start = System.currentTimeMillis();
        animator = this.entity.getComponent(AnimationRenderComponent.class);

        entity.getEvents().addListener("LeftStart", this::animateLeft);
        entity.getEvents().addListener("RightStart", this::animateRight);
        entity.getEvents().addListener("UpStart", this::animateUp);
        entity.getEvents().addListener("DownStart", this::animateDown);

        entity.getEvents().addListener("attackUp", this::animateAttackUp);
        entity.getEvents().addListener("attackDown", this::animateAttackDown);
        entity.getEvents().addListener("attackLeft", this::animateAttackLeft);
        entity.getEvents().addListener("attackRight", this::animateAttackRight);
    }

    public void setDeath() {
        death = true;
    }

    public void animateLeft() {
        if (!death) {
            if (!attack && !left) {
                animator.startAnimation("moveLeft");
                enableWalk();
                left = true;
            } else if (((System.currentTimeMillis() - this.start) / 1000.0) > 0.15) {
                attack = false;
            }
        } else {
            animator.startAnimation("leftDeath");
        }
    }


    public void animateRight() {
        if (!death) {
            if (!attack && !right) {
                animator.startAnimation("moveRight");
                enableWalk();
                right = true;
            } else if (((System.currentTimeMillis() - this.start) / 1000.0) > 0.15) {
                attack = false; }
        } else {
            animator.startAnimation("rightDeath");
        }
    }

    public void animateUp() {
        if (!death) {
            if(!attack && !up) {
                animator.startAnimation("moveUp");
                enableWalk();
                up = true;
            } else if (((System.currentTimeMillis() - this.start) / 1000.0) > 0.15) {
                attack = false;
            }
        } else {
            animator.startAnimation("frontDeath");
        }
    }

    public void animateDown() {
        if (!death) {
            if(!attack && !down) {
                animator.startAnimation("moveDown");
                enableWalk();
                down = true;
            } else if (((System.currentTimeMillis() - this.start) / 1000.0) > 0.15) {
                attack = false;
            }
        } else {
            animator.startAnimation("backDeath");
        }
    }

    public void animateAttackDown() {
        animator.startAnimation("EnemyAttackDown");
        attack = true;
        this.start = System.currentTimeMillis();
        enableWalk();
    }
    public void animateAttackUp() {
        animator.startAnimation("EnemyAttackUp");
        attack = true;
        this.start = System.currentTimeMillis();
        enableWalk();
    }
    public void animateAttackLeft() {
        animator.startAnimation("EnemyAttackLeft");
        attack = true;
        this.start = System.currentTimeMillis();
        enableWalk();
    }
    public void animateAttackRight() {
        animator.startAnimation("EnemyAttackRight");
        attack = true;
        this.start = System.currentTimeMillis();
        enableWalk();
    }

    private void enableWalk() {
        up = false;
        down = false;
        right = false;
        left = false;
    }
}
