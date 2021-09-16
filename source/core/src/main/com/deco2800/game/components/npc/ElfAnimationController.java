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

        entity.getEvents().addListener("rangedLeftShoot", this::animateRangerLeft);
        entity.getEvents().addListener("rangedRightShoot", this::animateRangerRight);
        entity.getEvents().addListener("rangedUpShoot", this::animateRangerUp);
        entity.getEvents().addListener("rangedDownShoot", this::animateRangerDown);

        entity.getEvents().addListener("assassinLeftShoot", this::animateAssassinLeft);
        entity.getEvents().addListener("assassinRightShoot", this::animateAssassinRight);
        entity.getEvents().addListener("assassinUpShoot", this::animateAssassinUp);
        entity.getEvents().addListener("assassinDownShoot", this::animateAssassinDown);
    }

    public void setDeath() {
        death = true;
    }

    public void animateLeft() {
        if (!death) {
            switch (entity.getEntityType()) {
                case "assassin":
                    animator.startAnimation("assassinMoveLeft");
                    break;
                case "ranged":
                    animator.startAnimation("rangerMoveLeft");
                    break;
                default:
                    animator.startAnimation("moveLeft");
                    break;
            }
        } else {
            if (entity.getEntityType().equals("melee")) {
                animator.getEntity().setScale(animator.getEntity().getScale().x * 2.5f, animator.getEntity().getScale().y);
                animator.startAnimation("leftDeath");
            } else if (entity.getEntityType().equals("elfBoss")) {
                animator.startAnimation("leftBossDeath");
            }
        }
    }

    public void animateRight() {
        if (!death) {
            switch (entity.getEntityType()) {
                case "assassin":
                    animator.startAnimation("assassinMoveRight");
                    break;
                case "ranged":
                    animator.startAnimation("rangerMoveRight");
                    break;
                default:
                    animator.startAnimation("moveRight");
                    break;
            }
        } else {
            if (entity.getEntityType().equals("melee")) {
                animator.getEntity().setScale(animator.getEntity().getScale().x * 2.5f, animator.getEntity().getScale().y);
                animator.startAnimation("rightDeath");
            } else if (entity.getEntityType().equals("elfBoss")) {
                animator.startAnimation("rightBossDeath");
            }
        }
    }

    public void animateUp() {
        if (!death) {
            switch (entity.getEntityType()) {
                case "assassin":
                    animator.startAnimation("assassinMoveUp");
                    break;
                case "ranged":
                    animator.startAnimation("rangerMoveUp");
                    break;
                default:
                    animator.startAnimation("moveUp");
                    break;
            }
        } else {
            if (entity.getEntityType().equals("melee")) {
                animator.getEntity().setScale(animator.getEntity().getScale().x * 2.5f, animator.getEntity().getScale().y);
                animator.startAnimation("upDeath");
            } else if (entity.getEntityType().equals("elfBoss")) {
                animator.startAnimation("frontBossDeath");
            }
        }
    }

    public void animateDown() {
        if (!death) {
            switch (entity.getEntityType()) {
                case "assassin":
                    animator.startAnimation("assassinMoveDown");
                    break;
                case "ranged":
                    animator.startAnimation("rangerMoveDown");
                    break;
                default:
                    animator.startAnimation("moveDown");
                    break;
            }
        } else {
            if (entity.getEntityType().equals("melee")) {
                animator.getEntity().setScale(animator.getEntity().getScale().x * 2.5f, animator.getEntity().getScale().y);
                animator.startAnimation("backDeath");
            } else if (entity.getEntityType().equals("elfBoss")) {
                animator.startAnimation("backBossDeath");
            }
        }
    }

    public void animateRangerLeft() {
        animator.startAnimation("rangerLeft");
    }

    public void animateRangerRight() {
        animator.startAnimation("rangerRight");
    }

    public void animateRangerUp() {
        animator.startAnimation("rangerUp");
    }

    public void animateRangerDown() {
        animator.startAnimation("rangerDown");
    }

    public void animateAssassinLeft() {
        animator.startAnimation("assassinLeft");
    }

    public void animateAssassinRight() {
        animator.startAnimation("assassinRight");
    }

    public void animateAssassinUp() {
        animator.startAnimation("assassinUp");
    }

    public void animateAssassinDown() {
        animator.startAnimation("assassinDown");
    }

}
