package com.deco2800.game.rendering;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class AnimationRenderComponentTest {
    @Test
    void shouldAddRemoveAnimation() {
        TextureAtlas atlas = createMockAtlas();
        AnimationRenderComponent animator = new AnimationRenderComponent(atlas);

        assertTrue(animator.addAnimation("test_name", 0.1f));
        assertTrue(animator.removeAnimation("test_name"));
        assertFalse(animator.removeAnimation("test_name"));
    }

    @Test
    void shouldFailRemoveInvalidAnimation() {
        TextureAtlas atlas = mock(TextureAtlas.class);
        when(atlas.findRegions("test_name")).thenReturn(null);
        AnimationRenderComponent animator = new AnimationRenderComponent(atlas);

        assertFalse(animator.addAnimation("test_name", 0.1f));
        assertFalse(animator.removeAnimation("test_name"));
    }

    @Test
    void shouldFailDuplicateAddAnimation() {
        TextureAtlas atlas = createMockAtlas();
        AnimationRenderComponent animator = new AnimationRenderComponent(atlas);

        assertTrue(animator.addAnimation("test_name", 0.1f));
        assertFalse(animator.addAnimation("test_name", 0.2f));
    }

    @Test
    void shouldHaveAnimation() {
        TextureAtlas atlas = createMockAtlas();
        AnimationRenderComponent animator = new AnimationRenderComponent(atlas);

        animator.addAnimation("test_name", 0.1f);
        assertTrue(animator.hasAnimation("test_name"));
        animator.removeAnimation("test_name");
        assertFalse(animator.hasAnimation("test_name"));
    }

    @Test
    void shouldStopAnimation() {
        TextureAtlas atlas = createMockAtlas();
        AnimationRenderComponent animator = new AnimationRenderComponent(atlas);
        animator.addAnimation("test_name", 1f);
        assertFalse(animator.stopAnimation());

        animator.startAnimation("test_name");
        assertTrue(animator.stopAnimation());
        assertNull(animator.getCurrentAnimation());
    }

    static TextureAtlas createMockAtlas() {
        TextureAtlas atlas = mock(TextureAtlas.class);
        Array<AtlasRegion> regions = new Array<>(1);
        for (int i = 0; i < 1; i++) {
            regions.add(mock(AtlasRegion.class));
        }
        when(atlas.findRegions("test_name")).thenReturn(regions);
        return atlas;
    }
} 
