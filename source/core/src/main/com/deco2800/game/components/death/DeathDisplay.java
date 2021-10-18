package com.deco2800.game.components.death;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
            "lowHealthImages/youdied.png",
            "images/player.png"
    };

    private final String[] deathScreenTextureAtlases = new String[]{
            "images/player.atlas"
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
        Image title = new Image(ServiceLocator.getResourceService().getAsset(
                "lowHealthImages/youdied.png", Texture.class));

        Skin menuButtons = new Skin(Gdx.files.internal("deathScreenSkin/deathScreen.json"));

        TextButton restartTutorialBtn = new TextButton("Restart Tutorial", menuButtons);
        TextButton exitBtn = new TextButton("Exit MainMenu", menuButtons);

        // Triggers an event when the button is pressed
        restartTutorialBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Start button clicked");
                        //Needs to check which one it is in
                        entity.getEvents().trigger("startTutorial");
                    }
                });
        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("mainMenu");
                    }
                });


        table.clear();
        table.add(title).padTop(-250f);
        table.row();
        table.add(restartTutorialBtn).padTop(30f);
        table.row();
        table.add(exitBtn).padTop(30f);
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
        super.dispose();
        this.unloadAssets();
    }
}
