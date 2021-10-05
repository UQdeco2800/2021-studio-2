package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.endGame.EndScreenActions;
import com.deco2800.game.components.endGame.EndScreenDisplay;
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
 * the End screen to be played after Odin dies
 */
public class EndScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(EndScreen.class);

    private static final String[] endScreenTextures = {"end/winScreen.png", "end/winScreen2.png"};
    private static final String[] endScreenAtlas = {"end/winScreen.atlas"};
    private final GdxGame game;
    private final Renderer renderer;
    private AnimationRenderComponent endAnimator;

    public EndScreen(GdxGame game) {
        this.game = game;

        logger.debug("Initialising End Screen services");
        ServiceLocator.registerTimeSource(new GameTime());
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        renderer = RenderFactory.createRenderer();

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
        logger.debug("Disposing end game screen");
        //stop animations here?
        renderer.dispose();
        unloadAssets();

        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();
        //ServiceLocator.getResourceService().dispose();
        ServiceLocator.clear();
    }

    public void loadAssets() {
        logger.debug("loading end screen assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(endScreenTextures);
        resourceService.loadTextureAtlases(endScreenAtlas);
        ServiceLocator.getResourceService().loadAll();
    }

    public void unloadAssets() {
        logger.debug("Unloading end screen assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(endScreenTextures);
        resourceService.unloadAssets(endScreenAtlas);
    }

    public void createUI() {
        logger.debug("Creating ui for end game screen");
        Stage stage = ServiceLocator.getRenderService().getStage(); //if i use keyboard inputs

        this.endAnimator = new AnimationRenderComponent(ServiceLocator.getResourceService().getAsset(
                "end/winScreen.atlas", TextureAtlas.class));

        this.endAnimator.addAnimation("winScreen", 0.06f, Animation.PlayMode.NORMAL);

        //add components
        Entity endUi = new Entity()
                .addComponent(this.endAnimator)
                .addComponent(new EndScreenActions(this.game))
                .addComponent(new EndScreenDisplay())
                .addComponent(new InputDecorator(stage, 10));

        ServiceLocator.getEntityService().register(endUi);

        endUi.setScale(20.5f,12.5f);
        endUi.setPosition(-5.5f, -1.5f);

        this.endAnimator.startAnimation("winScreen");

        //display button after playing the win animations
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                endUi.getComponent(EndScreenDisplay.class).displayButtons();
            }
        }, 5f);

    }

}
