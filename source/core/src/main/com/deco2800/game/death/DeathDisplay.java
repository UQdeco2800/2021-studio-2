package com.deco2800.game.death;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeathDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(DeathDisplay.class);
    private static final float Z_INDEX = 2f;
    private Image background;
    private Image dead;
    private Stack stack;
    private Table table;
    private String[] deathScreenTextures = new String[]{"lowHealthImages/testDeath1.png", "lowHealthImages/youdied.png"};

    @Override
    public void create() {
        super.create();
        loadAssets();
        addActors();
    }

    private void addActors() {
        stack = new Stack();
        stack.setFillParent(true);
        stack.setDebug(true);
        stack.setTouchable(Touchable.disabled); //disable touch inputs so its clickthrough
        background = new Image(ServiceLocator.getResourceService().getAsset("lowHealthImages/testDeath1.png",
                Texture.class));
        background.setScaling(Scaling.stretch);
        stack.add(background);
        table = new Table();
        table.setFillParent(true);

        TextButton startBtn = new TextButton("Restart", skin);
        TextButton exitBtn = new TextButton("Exit", skin);

        // Triggers an event when the button is pressed
        startBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Start button clicked");
                        entity.getEvents().trigger("start");
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


        dead = new Image(ServiceLocator.getResourceService().getAsset("lowHealthImages/youdied.png",
                Texture.class));

        table.add(dead);
        table.row();
        table.add(startBtn).padTop(30f);
        table.row();
        table.add(exitBtn).padTop(15f);
        table.row();

        stage.addActor(stack);
        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        ServiceLocator.getResourceService().loadTextures(deathScreenTextures);
        while (!resourceService.loadForMillis(10)) {
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
        stack.clear();
        table.clear();
        super.dispose();
        this.unloadAssets();
    }
}
