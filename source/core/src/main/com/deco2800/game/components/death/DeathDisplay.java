package com.deco2800.game.components.death;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import com.deco2800.game.components.mainmenu.MainMenuDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * UI component for displaying the death screen
 */
public class DeathDisplay extends MainMenuDisplay {
    private static final Logger logger = LoggerFactory.getLogger(DeathDisplay.class);
    protected List<Entity> areaEntities;
    private final String[] deathScreenTextures = new String[]{
            "lowHealthImages/youdied.png",
            "images/player.png"
    };

    private final String[] deathScreenTextureAtlases = new String[]{
            "images/player.atlas"
    };


    @Override
    public void create() {
        areaEntities = new ArrayList<>();
        loadAssets();
        super.create();
    }

    /**
     * Adds the death screen visual components to the class
     */
    @Override
    protected void addActors() {
        super.addActors();
        Image title = new Image(ServiceLocator.getResourceService().getAsset(
                "lowHealthImages/youdied.png", Texture.class));

        TextButton restartForestBtn = new TextButton("Restart Forest", skin);
        TextButton restartTestBtn = new TextButton("Restart Test", skin);
        TextButton exitBtn = new TextButton("Exit", skin);


        // Triggers an event when the button is pressed
        restartForestBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Start button clicked");
                        //Needs to check which one it is in
                        entity.getEvents().trigger("startForest");
                    }
                });
        // Triggers an event when the button is pressed
        restartTestBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Start button clicked");
                        //Needs to check which one it is in
                        entity.getEvents().trigger("startTest");
                    }
                });
        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });


        table.clear();
        table.add(title).padTop(-200f);
        table.row();
        table.add(restartForestBtn);
        table.row();
        table.add(restartTestBtn).padTop(20f);
        table.row();
        table.add(exitBtn).padTop(20f);

    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        ServiceLocator.getResourceService().loadTextures(deathScreenTextures);
        ServiceLocator.getResourceService().loadTextureAtlases(deathScreenTextureAtlases);
        while (resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(deathScreenTextures);
        resourceService.unloadAssets(deathScreenTextureAtlases);
    }

    @Override
    public void dispose() {
        for (Entity entity : areaEntities) {
            entity.dispose();
        }
        super.dispose();
        stack.clear();
        this.unloadAssets();
    }
}