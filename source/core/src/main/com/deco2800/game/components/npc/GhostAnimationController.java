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

  //should be able to get rid of these but will test later
  public void animateWander() {
    animator.startAnimation("floatUp");
  }

  void animateChase() {
    animator.startAnimation("floatDown");
  }
}
 
