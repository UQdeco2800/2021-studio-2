package com.deco2800.game.components.pause;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.components.mainmenu.MainMenuDisplay;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PauseMenuDisplay extends MainMenuDisplay {
    private static final Logger logger = LoggerFactory.getLogger(PauseMenuDisplay.class);
    private static final String[] pauseMenuTextures = {"images/pauseMenu.png"};
    private Image menuBackground;
    private Stack stack;
    private Table pauseTable;


    @Override
    public void create() {
        loadAssets();
        super.create();
        addActors();

        entity.getEvents().addListener("toggleMenu", this::toggleMenu);
    }

    protected void addActors() {
        stack = new Stack();
        menuBackground = new Image(ServiceLocator.getResourceService().getAsset("images/pauseMenu.png",
                Texture.class));
        pauseTable = new Table();

        Skin menuButtons = new Skin(Gdx.files.internal("pauseMenuSkin/pause.json"));

        TextButton contBtn = new TextButton("Resume", menuButtons);
        TextButton restartForestBtn = new TextButton("Restart", menuButtons);
        //TextButton restartTutorialBtn = new TextButton("Restart Tutorial", menuButtons);
        TextButton mainMenuBtn = new TextButton("Exit", menuButtons);
        //TextButton exitBtn = new TextButton("Exit to Desktop", menuButtons);


        // Triggers an event when the button is pressed
        contBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Start button clicked");
                        //Needs to check which one it is in
                        entity.getEvents().trigger("togglePause");
                    }
                });
        restartForestBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Start button clicked");
                        //Needs to check which one it is in
                        entity.getEvents().trigger("startForest");
                    }
                });
        mainMenuBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("Main Menu button clicked");
                        entity.getEvents().trigger("mainMenu");
                    }
                });
//        restartTutorialBtn.addListener(
//                new ChangeListener() {
//                    @Override
//                    public void changed(ChangeEvent changeEvent, Actor actor) {
//                        logger.debug("Start button clicked");
//                        //Needs to check which one it is in
//                        entity.getEvents().trigger("startTutorial");
//                    }
//                });
//
//        exitBtn.addListener(
//                new ChangeListener() {
//                    @Override
//                    public void changed(ChangeEvent changeEvent, Actor actor) {
//
//                        logger.debug("Exit button clicked");
//                        entity.getEvents().trigger("exit");
//                    }
//                });

        pauseTable.add(contBtn).padTop(150f);
        pauseTable.row();
        pauseTable.add(restartForestBtn).padTop(30f);
        pauseTable.row();
        pauseTable.add(mainMenuBtn).padTop(30f);
//        pauseTable.row();
//        pauseTable.add(restartTutorialBtn).padTop(30f);
//        pauseTable.row();
//        pauseTable.add(exitBtn).padTop(30f);

        stack.add(menuBackground);
        stack.add(pauseTable);


        stack.setSize(400f, 600f);
        stack.setPosition(stage.getWidth() / 2 , stage.getHeight() / 2 , Align.center);
        stack.setVisible(false);
        stage.addActor(stack);
    }

    private void resizeMenu() {

        stack.setPosition(stage.getWidth() / 2 , stage.getHeight() / 2 , Align.center);
    }

    public void toggleMenu() {
        stack.setVisible(ServiceLocator.getTimeSource().isPaused());
    }

    private void loadAssets() {
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(pauseMenuTextures);
        ServiceLocator.getResourceService().loadAll();
    }

    public void unloadAssets() {
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(pauseMenuTextures);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // do the resizing here
        resizeMenu();
    }

    @Override
    public void dispose() {
        unloadAssets();
    }
}
