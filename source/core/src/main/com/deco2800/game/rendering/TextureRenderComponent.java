package com.deco2800.game.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.services.ServiceLocator;

/**
 * Render a static texture.
 */
public class TextureRenderComponent extends RenderComponent {
    private final Texture texture;
    private final Sprite sprite;

    /**
     * @param texturePath internal path of static teture to render. Will be scaled to the entity's
     *                    scale.
     */
    public TextureRenderComponent(String texturePath) {
        this(ServiceLocator.getResourceService().getAsset(texturePath, Texture.class));
    }

    /**
     * @param texture Static texture to render. Will be scaled to the entity's scale.
     */
    public TextureRenderComponent(Texture texture) {
        this.texture = texture;
        this.sprite = null;
    }

    /**
     * @param sprite Sprite to render,  Will be scaled to the entity's scale.
     */
    public TextureRenderComponent(Sprite sprite) {
        this.texture = null;
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }

    /**
     * Scale the entity to a width of 1 and a height matching the texture's ratio
     */
    public void scaleEntity() {
        entity.setScale(1f, (float) texture.getHeight() / texture.getWidth());
    }

    /**
     * draw the image
     *
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
        Vector2 position = entity.getPosition();
        Vector2 positionCenter = entity.getCenterPosition();
        Vector2 scale = entity.getScale();
        float angle = entity.getAngle();

        if (texture != null) {
            batch.draw(texture, position.x, position.y, scale.x, scale.y);
        } else if (sprite != null) {
            //Prepare sprite to be drawn
            sprite.setScale(entity.getScale().x / sprite.getWidth(),
                    entity.getScale().y / sprite.getHeight());
            sprite.setRotation(angle);
            sprite.setCenter(positionCenter.x, positionCenter.y);
            sprite.draw(batch);
        }
    }
}
