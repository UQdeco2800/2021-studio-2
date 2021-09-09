package com.deco2800.game.components;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class TouchCutsceneMoveComponentTest {

/*    TextBox textBox;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerInputService(new InputService());
        Entity ui = new Entity();
        ui.addComponent(new TextBox());

        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.getEntityService().registerUI(ui);
        textBox = ServiceLocator.getEntityService().getUIEntity().getComponent(TextBox.class);
    }

    @Test
    void inputMovementParameters() {
        short targetLayer = (1 << 3);
        short triggerLayer = (1 << 3);
        Entity entity = createTrigger(triggerLayer);
        Entity target = createTarget(targetLayer);
    }

    @ExtendWith(GameExtension.class)
    @ExtendWith(MockitoExtension.class)
    class TextureRenderComponentTest {
        @Mock
        Texture texture;
        @Mock
        SpriteBatch spriteBatch;
        @Mock Entity entity;

        @Test
        void shouldDrawTexture() {
            when(entity.getPosition()).thenReturn(new Vector2(2f, 2f));
            when(entity.getScale()).thenReturn(new Vector2(1f, 1f));
            TextureRenderComponent component = new TextureRenderComponent(texture);
            component.setEntity(entity);
            component.render(spriteBatch);

            verify(spriteBatch).draw(texture, 2f, 2f, 1f, 1f);
        }
    }

    Entity createTrigger(short triggerLayer) {
        Entity entity =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new TouchMoveComponent(triggerLayer,
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
    }*/
}