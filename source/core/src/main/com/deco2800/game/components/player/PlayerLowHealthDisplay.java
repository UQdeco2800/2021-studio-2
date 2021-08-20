package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

/**
 * UI component to display the blooded view when health reaches a certain
 * threshold.
 */
public class PlayerLowHealthDisplay extends UIComponent {
    //add variables here
    private Image bloodImage;
    Table table;
    Stack stack;
    /**
     * Add all actors to the stage here and event listener
     */
    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("bloodyViewOn", this::displayBloodyViewOn);
        entity.getEvents().addListener("bloodyViewoff", this::displayBloodyViewOff);
    }

    /**
     * Create actors and add them to a Layout Widget.
     * This is also where the image for the bloodied view is added
     */
    private void addActors() {
        //initialise layout widget - stack
        stack = new Stack();
        stack.setFillParent(true);
        stack.setDebug(true);
        stack.setTouchable(Touchable.disabled); //disable touch inputs so its clickthrough
        stack.setVisible(false);

        //load and scale blood view
        bloodImage = new Image(ServiceLocator.getResourceService().getAsset("lowHealthImages" +
                "/testBlood2.png", Texture.class));
        bloodImage.setScaling(Scaling.stretch);

        //add to stage
        stack.add(bloodImage);
        stage.addActor(stack);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    /**
     * turn the stack visibility on.
     * this is called when the event "bloodyViewOn" is triggered
     */
    public void displayBloodyViewOn() {
        //change the opacity somehow of the image?
        stack.setVisible(true);
    }

    /**
     * turn the stack visibility off.
     * this is called when the event "bloodyViewOff" is triggered
     */
    public void displayBloodyViewOff() {
        //change the opacity somehow of the image?
        stack.setVisible(false);
    }
    /**
     * dispose all widgets and added images from addActor
     */
    @Override
    public void dispose() {
        super.dispose();
        bloodImage.remove();
    }
}
