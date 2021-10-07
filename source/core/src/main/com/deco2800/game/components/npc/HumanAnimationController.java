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

        animator.startAnimation("default");
    }

    /**
     * Sets the death of the entity to be true.
     */
    public void setDeath() {
        death = true;
    }

    /**
     * Animates the entity in the left direction, if the player is dead, then it will animate the death.
     */
    public void animateLeft() {
        if (entity.getEntityType().equals("transformed")) {
            if (!attack && !left) {
                animator.startAnimation("transformedMoveLeft");
                enableWalk();
                left = true;
            }
        } else if (!death) {
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

    /**
     * Animates the entity in the right direction, if the player is dead, then it will animate the death.
     */
    public void animateRight() {
        if (entity.getEntityType().equals("transformed")) {
            if (!attack && !right) {
                animator.startAnimation("transformedMoveRight");
                enableWalk();
                right = true;
            }
        } else if (!death) {
            if (!attack && !right) {
                animator.startAnimation("moveRight");
                enableWalk();
                right = true;
            } else if (((System.currentTimeMillis() - this.start) / 1000.0) > 0.15) {
                attack = false;
            }
        } else {
            animator.startAnimation("rightDeath");
        }
    }

    /**
     * Animates the entity in the up direction, if the player is dead, then it will animate the death.
     */
    public void animateUp() {
        if (entity.getEntityType().equals("transformed")) {
            if (!attack && !up) {
                animator.startAnimation("transformedMoveUp");
                enableWalk();
                up = true;
            }
        } else if (!death) {
            if (!attack && !up) {
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

    /**
     * Animates the entity in the down direction, if the player is dead, then it will animate the death.
     */
    public void animateDown() {
        if (entity.getEntityType().equals("transformed")) {
            System.out.println("here");
            if (!attack && !down) {
                animator.startAnimation("transformedMoveDown");
                System.out.println("here2");
                enableWalk();
                down = true;
            }
        } else if (!death) {
            if (!attack && !down) {
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

    /**
     * Animates the entity to attack in the down direction, this will prevent animations from overriding
     * for the duration of the attack animation.
     */
    public void animateAttackDown() {
        if (entity.getEntityType().equals("transformed")) {
            animateDown();
        }
        animator.startAnimation("EnemyAttackDown");
        attack = true;
        this.start = System.currentTimeMillis();
        enableWalk();
    }

    /**
     * Animates the entity to attack in the up direction, this will prevent animations from overriding
     * for the duration of the attack animation.
     */
    public void animateAttackUp() {
        if (entity.getEntityType().equals("transformed")) {
            animateUp();
        }
        animator.startAnimation("EnemyAttackUp");
        attack = true;
        this.start = System.currentTimeMillis();
        enableWalk();
    }

    /**
     * Animates the entity to attack in the left direction, this will prevent animations from overriding
     * for the duration of the attack animation.
     */
    public void animateAttackLeft() {
        if (entity.getEntityType().equals("transformed")) {
            animateLeft();
        }
        animator.startAnimation("EnemyAttackLeft");
        attack = true;
        this.start = System.currentTimeMillis();
        enableWalk();
    }

    /**
     * Animates the entity to attack in the right direction, this will prevent animations from overriding
     * for the duration of the attack animation.
     */
    public void animateAttackRight() {
        if (entity.getEntityType().equals("transformed")) {
            animateRight();
        }
        animator.startAnimation("EnemyAttackRight");
        attack = true;
        this.start = System.currentTimeMillis();
        enableWalk();
    }

    /**
     * Clears any animations that the entity is currently moving in so it can start a new animation
     * depending on the direction the entity is walking in.
     */
    private void enableWalk() {
        up = false;
        down = false;
        right = false;
        left = false;
    }
}
