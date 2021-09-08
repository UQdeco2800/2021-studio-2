package com.deco2800.game.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.RenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HealthBarComponent extends RenderComponent {
    protected boolean enabled = true;
    private int currentHealth = 100;
    private float scale;
    private static final Logger logger = LoggerFactory.getLogger(AnimationRenderComponent.class);
    private final GameTime timeSource;
    private final TextureAtlas atlas;
    private final Map<String, Animation<TextureRegion>> animations;
    private Animation<TextureRegion> currentAnimation;
    private String currentAnimationName;
    private float animationPlayTime;

    public HealthBarComponent(TextureAtlas atlas, float scale) {
        this.atlas = atlas;
        this.scale = scale;
        this.animations = new HashMap<>(4);
        timeSource = ServiceLocator.getTimeSource();
    }

    public HealthBarComponent(TextureAtlas atlas) {
        this.atlas = atlas;
        this.scale = 1;
        this.animations = new HashMap<>(4);
        timeSource = ServiceLocator.getTimeSource();
    }

    public void setScale(float scale) {
        this.scale = scale;
        create();
    }

    @Override
    protected void draw(SpriteBatch batch) {
        draw(batch);
        TextureRegion region = currentAnimation.getKeyFrame(animationPlayTime);
        Vector2 pos = entity.getPosition();
        Vector2 scale = entity.getScale();
        batch.draw(region, pos.x, pos.y, scale.x, scale.y);
        animationPlayTime += timeSource.getDeltaTime();
    }

    @Override
    public void create() {

    }

    @Override
    public void update() {
        currentHealth = getEntity().getComponent(CombatStatsComponent.class).getHealth();
        float MaxHealth = getEntity().getComponent(CombatStatsComponent.class).getMaxHealth();
        float ratioOfHealth = (float)(currentHealth/MaxHealth);

    }

    @Override
    public void dispose() {
        ServiceLocator.getRenderService().unregister(this);
    }

}
