package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.death.DeathActions;
import com.deco2800.game.components.death.DeathDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Screen containing the death screen UI and functionality components
 * <p>
 * Note that this screen is not currently in use and the the death screen is simply displayed as an UI component
 */
public class DeathScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(DeathScreen.class);

    private final GdxGame game;
    private final Renderer renderer;
    private static final String[] deathScreenTextures = {"playerDeath/player_death.png"};
    private static final String[] deathScreenAtlases = {"playerDeath/player_death.atlas"};

    public DeathScreen(GdxGame game) {
        this.game = game;

        logger.debug("Initialising Death screen services");
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerTimeSource(new GameTime());

        renderer = RenderFactory.createRenderer();
        renderer.getCamera().getEntity().setPosition(5f, 5f);

        loadAssets();
        createUI();
    }

    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void dispose() {
        logger.debug("Disposing death screen");
        renderer.dispose();

        unloadAssets();

        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();
        ServiceLocator.clear();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextureAtlases(deathScreenAtlases);
        resourceService.loadTextures(deathScreenTextures);
        ServiceLocator.getResourceService().loadAll();
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(deathScreenTextures);
        resourceService.unloadAssets(deathScreenAtlases);
    }

    /**
     * Creates the setting screen's ui including components for rendering ui elements to the screen
     * and capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();

        AnimationRenderComponent animator = new AnimationRenderComponent(ServiceLocator.getResourceService().getAsset(
                "playerDeath/player_death.atlas", TextureAtlas.class));
        animator.addAnimation("death_animation", 0.20f, Animation.PlayMode.NORMAL);

        Entity ui = new Entity();
        ui.addComponent(new DeathDisplay())
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new DeathActions(game))
                .addComponent(animator);
        ServiceLocator.getEntityService().register(ui);
        ui.setScale(5, 5);
        ui.setPosition(2.7f, -0.5f);
        animator.startAnimation("death_animation");
    }
}
