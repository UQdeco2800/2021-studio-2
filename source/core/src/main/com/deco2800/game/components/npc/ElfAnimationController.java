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

        entity.getEvents().addListener("attackLeft", this::animateLeftAttack);
        entity.getEvents().addListener("attackRight", this::animateRightAttack);
        entity.getEvents().addListener("attackUp", this::animateUpAttack);
        entity.getEvents().addListener("attackDown", this::animateDownAttack);

        entity.getEvents().addListener("stunLeft", this::animateLeftStun);
        entity.getEvents().addListener("stunRight", this::animateRightStun);
        entity.getEvents().addListener("stunUp", this::animateUpStun);
        entity.getEvents().addListener("stunDown", this::animateDownStun);

        //entity.getEvents().addListener("rangedLeftShoot", this::animateRangerLeft);
//        entity.getEvents().addListener("rangedRightShoot", this::animateRangerRight);
//        entity.getEvents().addListener("rangedUpShoot", this::animateRangerUp);
//        entity.getEvents().addListener("rangedDownShoot", this::animateRangerDown);

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
                    animator.startAnimation("assassinLeft");
                    break;
                case "ranged":
                    //System.out.println("rangerLeft");
                    animator.startAnimation("rangerLeft");
                    break;
                default:
                    animator.startAnimation("moveLeft");
                    break;
            }
        } else {
            if (!entity.getEntityType().equals("elfBoss")) {
                if (entity.getEntityType().equals("melee")) {
                    animator.getEntity().setScale(animator.getEntity().getScale().x * 2.5f,
                            animator.getEntity().getScale().y);
                } else {
                    animator.getEntity().setScale(animator.getEntity().getScale().x * 2f,
                            animator.getEntity().getScale().y);
                }

                if (entity.getEntityType().equals("assassin")) {
                    animator.startAnimation("assassinLeftDeath");
                } else {
                    animator.startAnimation("leftDeath");
                }
            } else {
                animator.startAnimation("leftBossDeath");
            }
            //Add death animations for Assassin, guard, boss
        }
    }

    public void animateRight() {
        if (!death) {
            switch (entity.getEntityType()) {
                case "assassin":
                    animator.startAnimation("assassinRight");
                    break;
                case "ranged":
                    //System.out.println("rangerRight");
                    animator.startAnimation("rangerRight");
                    break;
                default:
                    animator.startAnimation("moveRight");
                    break;
            }
        } else {
            if (!entity.getEntityType().equals("elfBoss")) {
                if (entity.getEntityType().equals("melee")) {
                    animator.getEntity().setScale(animator.getEntity().getScale().x * 2.5f,
                            animator.getEntity().getScale().y);
                } else {
                    animator.getEntity().setScale(animator.getEntity().getScale().x * 2f,
                            animator.getEntity().getScale().y);
                }
                if (entity.getEntityType().equals("assassin")) {
                    //animator.startAnimation("assassinRightDeath");
                    animator.startAnimation("assassinLeftDeath");
                } else {
                    //animator.startAnimation("rightDeath");
                    animator.startAnimation("leftDeath");
                }
            } else {
                animator.startAnimation("rightBossDeath");
            }
        }
    }

    public void animateUp() {
        if (!death) {
            switch (entity.getEntityType()) {
                case "assassin":
                    animator.startAnimation("assassinUp");
                    break;
                case "ranged":
                    //System.out.println("rangerUp");
                    animator.startAnimation("rangerUp");
                    break;
                default:
                    animator.startAnimation("moveUp");
                    break;
            }
        } else {
            if (!entity.getEntityType().equals("elfBoss")) {
                if (entity.getEntityType().equals("melee")) {
                    animator.getEntity().setScale(animator.getEntity().getScale().x * 2.5f,
                            animator.getEntity().getScale().y);
                } else {
                    animator.getEntity().setScale(animator.getEntity().getScale().x * 2f,
                            animator.getEntity().getScale().y);
                }
                if (entity.getEntityType().equals("assassin")) {
                    animator.startAnimation("assassinLeftDeath");
                    //animator.startAnimation("assassinFrontDeath");
                } else {
                    //animator.startAnimation("frontDeath");
                    animator.startAnimation("leftDeath");
                }
            } else {
                animator.startAnimation("frontBossDeath");
            }
        }
    }

    public void animateDown() {
        if (!death) {
            switch (entity.getEntityType()) {
                case "assassin":
                    animator.startAnimation("assassinDown");
                    break;
                case "ranged":
                    //System.out.println("rangerDown");
                    animator.startAnimation("rangerDown");
                    break;
                default:
                    animator.startAnimation("moveDown");
                    break;
            }
        } else {
            if (!entity.getEntityType().equals("elfBoss")) {
                if (entity.getEntityType().equals("melee")) {
                    animator.getEntity().setScale(animator.getEntity().getScale().x * 2.5f,
                            animator.getEntity().getScale().y);
                } else {
                    animator.getEntity().setScale(animator.getEntity().getScale().x * 2f,
                            animator.getEntity().getScale().y);
                }
                if (entity.getEntityType().equals("assassin")) {
                    //animator.startAnimation("assassinBackDeath");
                    animator.startAnimation("assassinLeftDeath");
                } else {
                    //animator.startAnimation("backDeath");
                    animator.startAnimation("leftDeath");
                }
            } else {
                animator.startAnimation("backBossDeath");
            }
        }
    }

    //public void animateRangerLeft() {
//        animator.startAnimation("rangerLeft");
//    }

//    public void animateRangerRight() {
//        animator.startAnimation("rangerRight");
//    }
//
//    public void animateRangerUp() {
//        animator.startAnimation("rangerUp");
//    }
//
//    public void animateRangerDown() {
//        animator.startAnimation("rangerDown");
//    }

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

    public void animateLeftAttack() {
        animator.startAnimation("attackLeft");
    }
    public void animateRightAttack() {
        animator.startAnimation("attackRight");
    }
    public void animateUpAttack() {
        animator.startAnimation("attackUp");
    }
    public void animateDownAttack() {
        animator.startAnimation("attackDown");
    }

    public void animateLeftStun() {
        switch (entity.getEntityType()) {
            case "assassin":
                //System.out.println("assassin Left stun");
                animator.startAnimation("assassinStunLeft");
                break;
            case "ranged":
                //System.out.println("rangerUp");
                animator.startAnimation("rangerStunLeft");
                break;
            default:
                //System.out.println("melee stun left");
                animator.startAnimation("stunLeft");
                break;
        }
    }

    public void animateRightStun() {
        switch (entity.getEntityType()) {
            case "assassin":
                //System.out.println("assassin Right stun");
                animator.startAnimation("assassinStunRight");
                break;
            case "ranged":
                //System.out.println("rangerUp");
                animator.startAnimation("rangerStunRight");
                break;
            default:
                //System.out.println("melee stun right");
                animator.startAnimation("stunRight");
                break;
        }
    }

    public void animateUpStun() {
        switch (entity.getEntityType()) {
            case "assassin":
                //System.out.println("assassin Up stun");
                animator.startAnimation("assassinStunUp");
                break;
            case "ranged":
                //System.out.println("rangerUp");
                animator.startAnimation("rangerStunUp");
                break;
            default:
                //System.out.println("melee stun up");
                animator.startAnimation("stunUp");
                break;
        }
    }

    public void animateDownStun() {
        switch (entity.getEntityType()) {
            case "assassin":
                //System.out.println("assassin Down stun");
                animator.startAnimation("assassinStunDown");
                break;
            case "ranged":
                //System.out.println("rangerDown");
                animator.startAnimation("rangerStunDown");
                break;
            default:
                //System.out.println("melee stun down");
                animator.startAnimation("stunDown");
                break;
        }
    }



//    public void animateRightStun(){
//        animator.startAnimation("stunRight");
//    }
//
//    public void animateUpStun(){
//        animator.startAnimation("stunUp");
//    }
//
//    public void animateDownStun(){
//        animator.startAnimation("stunDown");
//    }

}

