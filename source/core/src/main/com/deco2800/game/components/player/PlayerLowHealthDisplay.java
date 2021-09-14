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

import java.util.Timer;
import java.util.TimerTask;

/**
 * UI component to display the blooded view when health reaches a certain
 * threshold.
 */
public class PlayerLowHealthDisplay extends UIComponent {
    private Image bloodImage;

    private Image blackScreen;
    Stack stack;
    Sound heartBeat;
    boolean heartBeatTracker = false; //tracker to prevent duplicate sounds being played

    boolean dead = false;

    long heartID;

    /**
     * Add all actors to the stage here and event listener
     */

    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("bloodyViewOn", this::displayBloodyViewOn);
        entity.getEvents().addListener("bloodyViewOff", this::displayBloodyViewOff);
        entity.getEvents().addListener("deathFade", this::displayDeath);
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
        blackScreenSetup();

        //add to stage
        stage.addActor(stack);
        stage.addActor(blackScreen);
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
     * Initialises the settings for the death screen to be displayed.
     */
    public void blackScreenSetup() {
        blackScreen.setScaling(Scaling.stretch);
        blackScreen.setVisible(false);
        blackScreen.setFillParent(true);
        blackScreen.setSize(1920, 1080);
        blackScreen.setColor(0, 0, 0, 0);
        blackScreen.setTouchable(Touchable.disabled);
    }

    /**
     * retrieves images from service locator to use for the low health UI
     */
    public void loadImages() {
        //load and scale blood view
        bloodImage = new Image(ServiceLocator.getResourceService().getAsset("lowHealthImages" +
                "/BloodScreenDarkRepositioned.png", Texture.class));
        bloodImage.setScaling(Scaling.stretch);
        blackScreen = new Image(ServiceLocator.getResourceService().getAsset("images/textBoxDisplay" +
                "/black_bars.png", Texture.class));
    }

    /**
     * Turn the stack visibility on.
     * This is called when the event "bloodyViewOn" is triggered.
     *
     * @param alpha the opacity to set the bloody view to.
     * @param play  if true the heart beat sound will play otherwise nothing else will occur
     */
    public void displayBloodyViewOn(float alpha, boolean play) {
        if (play) {
            playHeartBeat(alpha);
        }
        bloodImage.setColor(1, 0, 0, alpha); //opacity of image changes depending on hp %
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
     * Triggered once the entity has died, the screen will slowly fade to black.
     */
    public void displayDeath() {
        if (!dead) {
            blackScreen.setVisible(true);
            fadeScreen(0);
            dead = true;
        }
    }

    /**
     * Causes the screen to fade to black after the player has died. This method is called recursively
     * at a delay, with the base case being if the screen is already pitch black.
     *
     * @param opacity the opacity of the screen which tends to 1 (complete black)
     */
    private void fadeScreen(float opacity) {
        if (opacity < 1) {
            blackScreen.setColor(0, 0, 0, opacity);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    fadeScreen(opacity + 0.05f);
                    timer.cancel();
                }
            }, 30);
        } else {
            blackScreen.setColor(0, 0, 0, 1);
        }
    }

    /**
     * plays the heart beat sound only if the tracker is currently false.
     * Speeds up the sound depending on hp%.
     *
     * @param modify the float value that will modify the speed of the sound
     */
    public void playHeartBeat(float modify) {
        if (!heartBeatTracker) {
            heartID = heartBeat.loop();
            heartBeatTracker = true;
        }
        float pitch = modify * 2f;
        heartBeat.setPitch(heartID, Math.min(pitch, 1.35f)); //speeds up the sound, capped at 1.35f
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
        blackScreen.remove();
    }
}