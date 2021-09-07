package com.deco2800.game.components.npc;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class ArrowAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        // create the image of arrow - rotate so that it face toward the character
        Image arrow = new Image(new Texture("images/arrow_normal.png"));
        arrow.addAction(Actions.parallel(Actions.moveTo(1, 1, 10), Actions.rotateBy(20)));


        animator = this.entity.getComponent(AnimationRenderComponent.class);
    }

    public float setXPosition(float x) {
        return x;
    }

    public float setYPosition(float y) {
        return y;
    }
}
