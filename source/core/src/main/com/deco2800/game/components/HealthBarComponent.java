package com.deco2800.game.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.RenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
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
        TextureRegion defaultTexture = this.atlas.findRegion("default");
        entity.setScale(scale, (float) defaultTexture.getRegionHeight() / defaultTexture.getRegionWidth());
    }

    public boolean addAnimation(String name, float frameDuration, Animation.PlayMode playMode) {
        Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions(name);
        if (regions == null || regions.size == 0) {
            logger.warn("Animation {} not found in texture atlas", name);
            return false;
        } else if (animations.containsKey(name)) {
            logger.warn(
                    "Animation {} already added in texture atlas. Animations should only be added once.",
                    name);
            return false;
        }

        Animation<TextureRegion> animation = new Animation<>(frameDuration, regions, playMode);
        animations.put(name, animation);
        logger.debug("Adding animation {}", name);
        return true;
    }

    public boolean removeAnimation(String name) {
        logger.debug("Removing animation {}", name);
        return animations.remove(name) != null;
    }

    public boolean hasAnimation(String name) {
        return animations.containsKey(name);
    }

    public void startAnimation(String name) {
        Animation<TextureRegion> animation = animations.getOrDefault(name, null);
        if (animation == null) {
            logger.error(
                    "Attempted to play unknown animation {}. Ensure animation is added before playback.",
                    name);
            return;
        }

        currentAnimation = animation;
        currentAnimationName = name;
        animationPlayTime = 0f;
        logger.debug("Starting animation {}", name);
    }

    public String getCurrentAnimation() {
        return currentAnimationName;
    }

    public boolean isFinished() {
        return currentAnimation != null && currentAnimation.isAnimationFinished(animationPlayTime);
    }

    public boolean stopAnimation() {
        if (currentAnimation == null) {
            return false;
        }

        logger.debug("Stopping animation {}", currentAnimationName);
        currentAnimation = null;
        currentAnimationName = null;
        animationPlayTime = 0f;
        return true;
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
        float ratioOfHealth = (float) (currentHealth / MaxHealth);

    }

    @Override
    public void dispose() {
        ServiceLocator.getRenderService().unregister(this);
    }

}
