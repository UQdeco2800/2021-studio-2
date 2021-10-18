package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.endgame.EndScreenActions;
import com.deco2800.game.components.endgame.EndScreenAnimationController;
import com.deco2800.game.components.endgame.EndScreenDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * the End screen to be played after Odin dies
 */
public class EndScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(EndScreen.class);

    private static final String[] endScreenTextures = {"end/huh.png"};
    private static final String[] endScreenAtlas = {};
    private final GdxGame game;
    private final Renderer renderer;
    private AnimationRenderComponent endAnimator;

    public EndScreen(GdxGame game) {
        this.game = game;

        logger.debug("Initialising End Screen services");
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        //ServiceLocator.registerTimeSource(new GameTime()); //dunno about this one

        renderer = RenderFactory.createRenderer();
        //renderer.getCamera().getEntity().setPosition(5f, 5f); //;do i really need this?

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
        ServiceLocator.getResourceService().dispose();
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
//        this.endAnimator = new AnimationRenderComponent(ServiceLocator.getResourceService().getAsset(
//                "end/endScreen.atlas", TextureAtlas.class));

        //add Animation frame name "startFrame","frame{#}", "ending",

        //start the first frame and loop until keyboard input -> do this feature last -> play it
        // all until the last frame.
        //link the last frame to the looping frame.
        //use a Timer.Schedule

        Entity endUi = new Entity()
                .addComponent(new EndScreenAnimationController())
                .addComponent(new EndScreenActions(this.game))
                .addComponent(new EndScreenDisplay())
                .addComponent(new InputDecorator(stage, 10));


        //make the Display put to the background -> only bring it out when on the last frame
        //addComponents
        //change frames based on player keyboard input

        ServiceLocator.getEntityService().register(endUi);

        //position UI entity here
        //Start first frame
    }

}
