package com.deco2800.game.components.weapons;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class Terminator extends Component {
    private Vector2 start;
    private final float distance = 6f;
    public Terminator() {

    }

    @Override
    public void create() {
        super.create();
        this.start = entity.getCenterPosition();
    }

    @Override
    public void update() {
        super.update();
        Vector2 position = entity.getCenterPosition();
        if (Math.abs(position.x - start.x) > distance || Math.abs(position.y - start.y) > distance) {
            //AnimationRenderComponent animator = entity.getComponent(AnimationRenderComponent.class);
            //animator.startAnimation("explode");
            entity.prepareDispose();
        }
    }
}
