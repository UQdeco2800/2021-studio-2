package com.deco2800.game.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.textbox.TextBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class TouchCutsceneMoveComponentTest {

//    TextBox textBox;
//
//    @BeforeEach
//    void beforeEach() {
//        ServiceLocator.registerPhysicsService(new PhysicsService());
//        ServiceLocator.registerInputService(new InputService());
//        Entity ui = new Entity();
//        ui.addComponent(new TextBox());
//
//        ServiceLocator.registerEntityService(new EntityService());
//        ServiceLocator.getEntityService().registerUI(ui);
//        textBox = ServiceLocator.getEntityService().getUIEntity().getComponent(TextBox.class);
//    }
//
//    @Test
//    void inputMovementParameters() {
//        short targetLayer = (1 << 3);
//        short triggerLayer = (1 << 3);
//        Entity entity = createTrigger(triggerLayer);
//        Entity target = createTarget(targetLayer);
//    }


//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.math.Vector2;
//import com.deco2800.game.entities.Entity;
//import com.deco2800.game.extensions.GameExtension;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//    @ExtendWith(GameExtension.class)
//    @ExtendWith(MockitoExtension.class)
//    class TextureRenderComponentTest {
//        @Mock
//        Texture texture;
//        @Mock
//        SpriteBatch spriteBatch;
//        @Mock Entity entity;
//
//        @Test
//        void shouldDrawTexture() {
//            when(entity.getPosition()).thenReturn(new Vector2(2f, 2f));
//            when(entity.getScale()).thenReturn(new Vector2(1f, 1f));
//            TextureRenderComponent component = new TextureRenderComponent(texture);
//            component.setEntity(entity);
//            component.render(spriteBatch);
//
//            verify(spriteBatch).draw(texture, 2f, 2f, 1f, 1f);
//        }
//    }

    Entity createTrigger(short triggerLayer) {
        Entity entity =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new TouchCutsceneMoveComponent(triggerLayer,
                                new Vector2(1f, 0), 5f, 0f));
        entity.create();
        return entity;
    }

    Entity createTarget(short layer) {
        Entity target =
                new Entity()
                        .addComponent(new CombatStatsComponent(10, 0))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(layer));
        target.create();
        return target;
    }

}