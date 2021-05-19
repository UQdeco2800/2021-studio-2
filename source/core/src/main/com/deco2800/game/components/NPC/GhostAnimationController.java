package com.deco2800.game.components.NPC;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class GhostAnimationController  extends Component {
  AnimationRenderComponent animator;


  @Override
  public void create() {
    super.create();
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("wanderStart", this::animateWander);
    entity.getEvents().addListener("chaseStart", this::animateChase);
  }

  void animateWander() {
    animator.startAnimation("float");
  }

  void animateChase() {
    animator.startAnimation("angry_float");
  }
}
