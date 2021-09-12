package com.deco2800.game.components.death;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
            "lowHealthImages/testDeath1.png",
            "lowHealthImages/youdied.png",
            "lowHealthImages/testDeath1.png",
            "images/main_menu_background.png",
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

        Skin menuButtons = new Skin(Gdx.files.internal("mainMenuSkin/mainMenu.json"));

        Button restartForestBtn = new Button(menuButtons, "start");
        Button restartTestBtn = new Button(menuButtons, "start");
        Button exitBtn = new Button(menuButtons, "quit");

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
        table.add();
        table.add(dead).padBottom(200f);
        table.row();
        table.add(restartForestBtn).pad(50f).padTop(120f);
        table.add(restartTestBtn).pad(50f).padTop(120f);
        table.add(exitBtn).pad(50f).padTop(120f);
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
        stack.clear();
        this.unloadAssets();
    }
}
