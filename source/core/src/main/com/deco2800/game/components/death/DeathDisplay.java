package com.deco2800.game.components.death;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import com.deco2800.game.components.mainmenu.MainMenuDisplay;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UI component for displaying the death screen
 */
public class DeathDisplay extends MainMenuDisplay {
    private static final Logger logger = LoggerFactory.getLogger(DeathDisplay.class);
    private final String[] deathScreenTextures = new String[]{
      "lowHealthImages/testDeath1.png",
      "lowHealthImages/youdied.png",
      "lowHealthImages/testDeath1.png",
      "images/main_menu_background.png"
    };


    @Override
    public void create() {
        loadAssets();
        super.create();
    }

    /**
     * Adds the death screen visual components to the class
     */
    @Override
    protected void addActors() {
        super.addActors();

        TextButton restartForestBtn = new TextButton("Restart Forest", skin);
        TextButton restartTestBtn = new TextButton("Restart Test", skin);
        TextButton exitBtn = new TextButton("Exit", skin);
        Image background = new Image(ServiceLocator.getResourceService().getAsset("lowHealthImages/testDeath1.png",
                Texture.class));
        background.setScaling(Scaling.stretch);
        stack.add(background);

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
        Image dead = new Image(ServiceLocator.getResourceService().getAsset("lowHealthImages/youdied.png",
                Texture.class));
        table.clear();
        table.add(dead);
        table.row();
        table.add(restartForestBtn).padTop(30f);
        table.row();
        table.add(restartTestBtn).padTop(30f);
        table.row();
        table.add(exitBtn).padTop(30f);
        table.row();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        ServiceLocator.getResourceService().loadTextures(deathScreenTextures);
        while (resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(deathScreenTextures);
    }

    @Override
    public void dispose() {
        super.dispose();
        stack.clear();
        this.unloadAssets();
    }
}
 
