package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.deco2800.game.ui.UIComponent;

/**
 * UI component to display the blooded view when health reaches a certain
 * threshold.
 */
public class PlayerLowHealthDisplay extends UIComponent {
    //add variables here

    /**
     * Add all actors to the stage here and event listener
     */
    @Override
    public void create() {

    }

    /**
     * Create actors and add them to a Layout Widget.
     * This is also where the image for the bloodied view is added
     */
    private void addActors() {

    }

    //add function to change the opacity of the image??

    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    /**
     * dispose all widgets and added images from addActor
     */
    @Override
    public void dispose() {

    }
}
