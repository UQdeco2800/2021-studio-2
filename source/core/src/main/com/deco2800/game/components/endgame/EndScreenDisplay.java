package com.deco2800.game.components.endgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndScreenDisplay extends UIComponent {
    private static final float Z_INDEX = 2f; //higher z index means placed to the front?
    private static final Logger logger = LoggerFactory.getLogger(EndScreenDisplay.class);
    private Image endBackground;
    private Stack stack;
    private Table table;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    protected void addActors() {
        stack = new Stack();
        table = new Table();
        table.setFillParent(true);
        addBackground();
        buttonSetup();


    }

    private void addBackground() {
        endBackground =
                new Image(ServiceLocator.getResourceService().getAsset("end/huh.png",
                        Texture.class));
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
        stack.setFillParent(true);
        stack.setTouchable(Touchable.disabled);
        stack.add(endBackground);
        table.add(exitBtn);
        stage.addActor(stack);
        stage.addActor(table);
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    protected void draw(SpriteBatch batch) {
        //pass
    }
}
