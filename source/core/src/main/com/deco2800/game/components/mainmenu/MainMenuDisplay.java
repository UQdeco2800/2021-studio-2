package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ui component for displaying the Main menu.
 */
public class MainMenuDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
    private static final float Z_INDEX = 2f;
    protected Stack stack;
    protected Table table;
    private Pixmap mouseCursor;

    @Override
    public void create() {
        super.create();
        addActors();
    }


    protected void addActors() {
        mouseCursor = new Pixmap(Gdx.files.internal("images/swordcursor.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(mouseCursor, 0, 0));

        stack = new Stack();
        stack.setFillParent(true);
        stack.setTouchable(Touchable.disabled); //disable touch inputs so its clickthrough

        table = new Table();
        table.setFillParent(true);

        Skin menuButtons = new Skin(Gdx.files.internal("mainMenuSkin/mainMenu.json"));

        Button startForestBtn = new Button(menuButtons, "start");
        Button startTutorialBtn = new Button(menuButtons, "start");
        Button settingsBtn = new Button(menuButtons, "settings");
        Button exitBtn = new Button(menuButtons, "quit");

        // Triggers an event when the button is pressed
        startForestBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Start button clicked");
                        entity.getEvents().trigger("startForest");
                    }
                });

        startTutorialBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Start button clicked");
                        entity.getEvents().trigger("startTutorial");
                    }
                });

        settingsBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Settings button clicked");
                        entity.getEvents().trigger("settings");
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
        table.add(startForestBtn).padTop(30f);
        table.row();
        table.add(startTutorialBtn).padTop(30f);
        table.row();
        table.add(settingsBtn).padTop(30f);
        table.row();
        table.add(exitBtn).padTop(30f);

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

    @Override
    public void dispose() {
        table.clear();
        mouseCursor.dispose();
        super.dispose();
    }
}
