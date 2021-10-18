package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndScreenDisplay extends UIComponent {
    private static final float Z_INDEX = 2f; //higher z index means placed to the front?
    private static final Logger logger = LoggerFactory.getLogger(EndScreenDisplay.class);
    private Table table;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    protected void addActors() {
        table = new Table();
        table.setFillParent(true);
        buttonSetup();


    }

    private void buttonSetup() {
        //create new skin file for buttons
        Skin endButton = new Skin(Gdx.files.internal("deathScreenSkin/deathScreen.json")); //NEEDTO MAKE OWN SKIN FILE
        TextButton exitBtn = new TextButton("GGEZ GGEZ GGEZ GO NEXT", endButton);
        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("mainMenu");
                    }
                });

        table.add(exitBtn);
        stage.addActor(table);
        table.setVisible(false);
    }

    public void displayButtons() {
        table.setVisible(true);
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    protected void draw(SpriteBatch batch) {
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
