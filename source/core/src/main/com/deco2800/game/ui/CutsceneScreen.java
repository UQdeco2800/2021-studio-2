package com.deco2800.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.textbox.TextBox;
import com.deco2800.game.utils.BooleanObject;

import java.util.Timer;
import java.util.TimerTask;


public class CutsceneScreen extends UIComponent {

    /**
     * The priority of the UI component to be displayed on the screen.
     */
    private static final float Z_INDEX = 10f;

    /**
     * Instance of the Text Box that will be displayed to the screen.
     */
    private TextBox textBox;

    /**
     * Bars to indicate a cutscene is taking place.
     */
    private Image blackScreen;

    /**
     * Boolean to determine if the screen should be black or not.
     */
    private boolean open = false;

    /**
     * If the cutscene is currently opening.
     */
    private final BooleanObject opening = new BooleanObject(false);

    /**
     * If the cutscene is currently closing.
     */
    private final BooleanObject closing = new BooleanObject(false);

    @Override
    public void create() {
        super.create();
        textBox = entity.getComponent(TextBox.class);
        addActors();
    }

    /**
     * Adds the mainCharacterLabel actor to the screen which will be constantly updated to display changes.
     */
    private void addActors() {
        blackScreen = new Image(ServiceLocator.getResourceService()
                .getAsset("images/textBoxDisplay/black_bars.png", Texture.class));
        blackScreen.setWidth(ServiceLocator.getRenderService().getStage().getWidth());
        blackScreen.setHeight(ServiceLocator.getRenderService().getStage().getHeight());
        blackScreen.setY(ServiceLocator.getRenderService().getStage().getHeight());

        stage.addActor(blackScreen);
    }

    /**
     * Sets the black screen to be open.
     */
    public void setOpen() {
        this.open = true;
    }

    /**
     * Sets the black screen to be closed.
     */
    public void setClosed() {
        this.open = false;
    }

    /**
     * Displays the message to the screen if the text box is open, the sub message will be
     * displayed.
     *
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
        if (open) {
            if (!opening.getBoolean()) {
                closing.setFalse();
                opening.setTrue();
                openScreen();
            }
        } else {
            if (!closing.getBoolean()) {
                closing.setTrue();
                opening.setFalse();
                closeScreen();
            }
        }
    }

    /**
     * Makes the bars fade into the screen.
     */
    private void openScreen() {
        moveDown(blackScreen, opening);
    }

    /**
     * Makes the bars fade out of the screen.
     */
    private void closeScreen() {
        moveUp(blackScreen, closing);
    }

    /**
     * Moves the bars down relative to their original height.
     *
     * @param bar  the image that will change position
     * @param type the boolean type that will be checked to repeat
     */
    private void moveDown(Image bar, BooleanObject type) {
        float initialHeight = -ServiceLocator.getRenderService().getStage().getHeight();
        if (bar.getY() > initialHeight - ServiceLocator.getRenderService().getStage().getHeight()
                && type.getBoolean()) {
            bar.setY(bar.getY() - 6);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    moveDown(bar, type);
                    timer.cancel();
                }
            }, 15);
        }
    }

    /**
     * Moves the bars up relative to their original height.
     *
     * @param bar  the image that will change position
     * @param type the boolean type that will be checked to repeat
     */
    private void moveUp(Image bar, BooleanObject type) {
        float initialHeight = ServiceLocator.getRenderService().getStage().getHeight();
        if (bar.getY() < initialHeight + ServiceLocator.getRenderService().getStage().getHeight()
                && type.getBoolean()) {
            bar.setY(bar.getY() + 6);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    moveUp(bar, type);
                    timer.cancel();
                }
            }, 15);
        }
    }

    /**
     * Gets the priority of the text display.
     *
     * @return the priority of the text to be displayed to the screen.
     */
    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        super.dispose();
        blackScreen.remove();
    }

}
