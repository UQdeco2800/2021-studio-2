package com.deco2800.game.rendering;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimationRenderComponent extends RenderComponent {
  private static final Logger logger = LoggerFactory.getLogger(AnimationRenderComponent.class);
  private final GameTime timeSource;
  private final TextureAtlas atlas;
  private Map<String, Animation<TextureRegion>> animations;
  private Animation<TextureRegion> currentAnimation;
  private String currentAnimationName;
  private float animationPlayTime;

  public AnimationRenderComponent(TextureAtlas atlas) {
    this.atlas = atlas;
    this.animations = new HashMap<>(4);
    timeSource = ServiceLocator.getTimeSource();
  }

  public void addAnimation(String name, float frameDuration, PlayMode playMode) {
    Array<AtlasRegion> regions = atlas.findRegions(name);
    if (regions == null || regions.size == 0) {
      logger.warn("Animation {} not found in texture atlas", name);
      return;
    } else if (animations.containsKey(name)) {
      logger.warn(
        "Animation {} already added in texture atlas. Animations should only be added once.", name
      );
    }

    Animation<TextureRegion> animation = new Animation<>(frameDuration, regions, playMode);
    animations.put(name, animation);
    logger.debug("Adding animation {}", name);
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
        "Attempted to play unknown animation {}. Ensure animation is added before playback.", name
      );
      return;
    }

    currentAnimation = animation;
    currentAnimationName = name;
    animationPlayTime = 0f;
    logger.debug("Starting animation {}", name);
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

  public String getCurrentAnimation() {
    return currentAnimationName;
  }

  public boolean isFinished() {
    return currentAnimation != null && currentAnimation.isAnimationFinished(animationPlayTime);
  }

  @Override
  protected void draw(SpriteBatch batch) {
    if (currentAnimation == null) {
      return;
    }
    animationPlayTime += timeSource.getDeltaTime();
    TextureRegion region = currentAnimation.getKeyFrame(animationPlayTime);
    Vector2 pos = entity.getPosition();
    Vector2 scale = entity.getScale();
    batch.draw(region, pos.x, pos.y, scale.x, scale.y);
  }

  @Override
  public void dispose() {
    atlas.dispose();
    super.dispose();
  }
}
