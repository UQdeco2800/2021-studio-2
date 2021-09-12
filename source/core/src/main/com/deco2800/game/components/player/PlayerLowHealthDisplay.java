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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UI component to display the blooded view when health reaches a certain
 * threshold.
 */
public class PlayerLowHealthDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(PlayerLowHealthDisplay.class);
    private Image bloodImage;
    private Stack stack;
    private Sound heartBeat;
    private boolean heartBeatOn = false; //tracker to prevent duplicate sounds being played
    private long heartID;
    private boolean displayOn = false; //need tracker so logging info isn't spammed

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
                ".mp3", Sound.class);
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
     */
    public void displayBloodyViewOn(float alpha) {
        if (!displayOn) {
            playHeartBeat(alpha);
            logger.info("bloody view on");
            stack.setVisible(true);
            displayOn = true;
        }
        bloodImage.setColor(1, 1, 1, alpha); //opacity of image changes depending on hp %

    }

    /**
     * turn the stack visibility off.
     * this is called when the event "bloodyViewOff" is triggered
     */
    public void displayBloodyViewOff() {
        if (displayOn) {
            logger.info("bloody view off");
            stopHeartBeat();
            stack.setVisible(false);
            displayOn = false;
        }
    }

    /**
     * plays the heart beat sound only if the tracker is currently false.
     * Speeds up the sound depending on hp%.
     *
     * @param modify the float value that will modify the speed of the sound
     */
    public void playHeartBeat(float modify) {
        if (!heartBeatOn) {
            logger.info("play heart beat sound successfully");
            heartID = heartBeat.loop();
            heartBeatOn = true;
        }
        float pitch = modify * 2f;
        heartBeat.setPitch(heartID, Math.min(pitch, 1.35f)); //speeds up the sound, capped at 1.35f
    }

    /**
     * stops the playing of heart beat sound and sets the tracker to false
     */
    public void stopHeartBeat() {
        if (heartBeatOn) {
            logger.info("play heart beat sound successfully");
            heartBeat.stop();
            heartBeatOn = false;
        }
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
