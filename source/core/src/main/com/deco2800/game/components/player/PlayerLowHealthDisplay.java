package com.deco2800.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Scaling;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

/**
 * UI component to display the blooded view when health reaches a certain
 * threshold.
 */
public class PlayerLowHealthDisplay extends UIComponent {
    private Image bloodImage;
    Stack stack;
    Sound heartBeat;
    boolean heartBeatTracker = false; //tracker to prevent duplicate sounds being played
    /**
     * Add all actors to the stage here and event listener
     */
    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("bloodyViewOn", this::displayBloodyViewOn);
        entity.getEvents().addListener("bloodyViewOff", this::displayBloodyViewOff);
    }

    /**
     * Create actors and add them to a Layout Widget.
     * This is also where the image for the bloodied view is added
     */
    private void addActors() {
        //sound
        heartBeat = ServiceLocator.getResourceService().getAsset("sounds/heartBeat_placeholder" +
                ".wav", Sound.class);
        //setup layout widgets and loading images
        loadImages();
        stackSetup();

        //add to stage
        stage.addActor(stack);
    }

    /**
     * initialisation of the stack and adding images to layout
     */
    public void stackSetup() {
        //initialise layout widget - stack
        stack = new Stack();
        stack.setFillParent(true);
        stack.setTouchable(Touchable.disabled); //disable touch inputs so its click through
        stack.setVisible(false);
        stack.add(bloodImage);
    }

    /**
     * retrieves images from service locator to use for the low health UI
     */
    public void loadImages() {
        //load and scale blood view
        bloodImage = new Image(ServiceLocator.getResourceService().getAsset("lowHealthImages" +
                "/BloodScreenDarkRepositioned.png", Texture.class));
        bloodImage.setScaling(Scaling.stretch);
    }

    /**
     * Turn the stack visibility on.
     * This is called when the event "bloodyViewOn" is triggered.
     *
     * @param alpha the opacity to set the bloody view to.
     * @param play if true the heart beat sound will play otherwise nothing else will occur
     */
    public void displayBloodyViewOn(float alpha, boolean play) {
        if (play) {
            playHeartBeat();
        }
        bloodImage.setColor(1,0,0,alpha); //opacity of image changes depending on hp %
        stack.setVisible(true);
    }
    /**
     * turn the stack visibility off.
     * this is called when the event "bloodyViewOff" is triggered
     */
    public void displayBloodyViewOff() {
        stopHeartBeat();
        stack.setVisible(false);
    }

    /**
     * plays the heart beat sound only if the tracker is currently false
     */
    public void playHeartBeat() {
        if (!heartBeatTracker) {
            heartBeat.loop();
            heartBeatTracker = true;
        }
    }

    /**
     * stops the playing of heart beat sound and sets the tracker to false
     */
    public void stopHeartBeat() {
        heartBeat.stop();
        heartBeatTracker = false;
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    /**
     * dispose all widgets and added images from addActor
     */
    @Override
    public void dispose() {
        super.dispose();
        bloodImage.remove();
        heartBeat.dispose();

    }
}