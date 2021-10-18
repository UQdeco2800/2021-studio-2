package com.deco2800.game.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.rendering.TextureRenderComponent;

import java.util.concurrent.TimeUnit;

/**
 * Creates an entity that represents a line.
 */
public class LineEntity extends Entity {

    private final long ttl;
    private final long timeCreated;

    /**
     * Creates a line entity
     *
     * @param ttl time to live in MS
     */
    public LineEntity(long ttl) {
        super();
        this.ttl = ttl;
        timeCreated = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    }


    /**
     * draws a line entity from origin to target
     *
     * @param target location of target
     * @param origin location of owner
     * @return Vector2 representation of the different in positions between the target and origin vectors
     */
    public Vector2 setTarget(Vector2 target, Vector2 origin) {
        Vector2 v1 = origin.cpy();
        Vector2 v2 = target.cpy();
        Vector2 v3 = v2.cpy().sub(v1);
        //update position
        Vector2 centerPoint = v3.cpy().setLength(v3.len() / 2).add(v1);
        Vector2 bodyOffset = this.getCenterPosition().cpy().sub(this.getPosition());
        Vector2 position = centerPoint.sub(bodyOffset);
        //update scale
        Sprite sprite = this.getComponent(TextureRenderComponent.class).getSprite();
        setScale(sprite.getWidth() / 40f * v3.len() / 4, sprite.getHeight() / 40f);
        this.setAngle(v3.angleDeg());
        this.setPosition(position);
        return (position);
    }

    @Override
    public void update() {
        super.update();
        if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) - timeCreated >= ttl) {
            this.prepareDispose();
        }
    }
}
