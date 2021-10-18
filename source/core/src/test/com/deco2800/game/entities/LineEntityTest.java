package com.deco2800.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
class LineEntityTest {

    private final String[] forestTextures = {
            "images/aiming_line.png"
    };

    @BeforeEach
    void beforeEach() {
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadTextures(forestTextures);
        resourceService.loadAll();
        RenderService renderService = new RenderService();
        ServiceLocator.registerRenderService(renderService);

    }

    @Test
    void shouldSetAndGetPosition() {
        Sprite sprite = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/aiming_line.png", Texture.class));
        LineEntity entity = new LineEntity(1000);
        entity.addComponent(new TextureRenderComponent(sprite));

        Vector2 v1 = new Vector2(0f, 0f);
        Vector2 v2 = new Vector2(5f, -5f);

        entity.setTarget(v2, v1);

        assertEquals(new Vector2(2, -3), entity.getPosition());

        entity.setPosition(0f, 0f);
        assertEquals(Vector2.Zero, entity.getPosition());
    }

    @Test
    void autoDispose() {
        Sprite sprite = new Sprite(ServiceLocator.getResourceService().getAsset(
                "images/aiming_line.png", Texture.class));
        LineEntity entity = new LineEntity(1000);
        entity.addComponent(new TextureRenderComponent(sprite));

        Vector2 v1 = new Vector2(0f, 0f);
        Vector2 v2 = new Vector2(5f, -5f);
        entity.setTarget(v2, v1);

        entity.create();

        EntityService entityService = mock(EntityService.class);
        ServiceLocator.registerEntityService(entityService);

        entity.update();
        // unable to pinpoint 1 second

        entity.dispose();

        // auto dispose after 1 seconds after create
        verify(entityService).unregister(entity);

    }
}
