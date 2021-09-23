package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to an entity's state and plays the animation when one
 * of the events is triggered.
 */
public class VikingAnimationController extends Component {
    AnimationRenderComponent animator;
    private boolean death;
    private boolean attack;
    private long start;

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
            if (!attack) {
                animator.startAnimation("moveLeft");
            } else {
                System.out.println((System.currentTimeMillis() - this.start) / 1000.0);
                if (((System.currentTimeMillis() - this.start) / 1000.0) > 0.6) {
                    attack = false;
                }
            }
        } else {
            animator.startAnimation("leftDeath");
        }
    }


    public void animateRight() {
        if (!death) {
            if (!attack) {
                animator.startAnimation("moveRight");

            } else {
                System.out.println((System.currentTimeMillis() - this.start) / 1000.0);
                if (((System.currentTimeMillis() - this.start) / 1000.0) > 0.6) {
                    attack = false;
                }
            }
        } else {
            animator.startAnimation("rightDeath");
        }
    }

    public void animateUp() {
        if (!death) {
            if(!attack) {
                animator.startAnimation("moveUp");
            } else {
                System.out.println((System.currentTimeMillis() - this.start) / 1000.0);
                if (((System.currentTimeMillis() - this.start) / 1000.0) > 0.6) {
                    attack = false;
                }
            }
        } else {
            animator.startAnimation("frontDeath");
        }
    }

    public void animateDown() {
        if (!death) {
            if(!attack) {
                animator.startAnimation("moveDown");
            } else {
                System.out.println((System.currentTimeMillis() - this.start) / 1000.0);
                if (((System.currentTimeMillis() - this.start) / 1000.0) > 0.6) {
                    attack = false;
                }
            }
        } else {
            animator.startAnimation("backDeath");
        }
    }

    public void animateAttackDown() {
        System.out.println("bruh");
        animator.startAnimation("EnemyAttackDown");
        attack = true;
        this.start = System.currentTimeMillis();
    }
    public void animateAttackUp() {
        System.out.println("bruh");

        animator.startAnimation("EnemyAttackUp");
        attack = true;
        this.start = System.currentTimeMillis();
    }
    public void animateAttackLeft() {
        System.out.println("bruh");

        animator.startAnimation("EnemyAttackLeft");
        attack = true;
        this.start = System.currentTimeMillis();
    }
    public void animateAttackRight() {
        System.out.println("bruh");

        animator.startAnimation("EnemyAttackRight");
        attack = true;
        this.start = System.currentTimeMillis();
    }

}
