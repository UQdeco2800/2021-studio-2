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
    private boolean opening = false;

    /**
     * If the cutscene is currently closing.
     */
    private boolean closing = false;

    @Override
    public void create() {
        super.create();
        textBox = entity.getComponent(TextBox.class);
        addActors();
        openScreenInstant();
    }

    /**
     * Adds the mainCharacterLabel actor to the screen which will be constantly updated to display changes.
     */
    private void addActors() {
        blackScreen = new Image(ServiceLocator.getResourceService()
                .getAsset("images/textBoxDisplay/black_bars.png", Texture.class));
        blackScreen.setWidth(ServiceLocator.getRenderService().getStage().getWidth());
        blackScreen.setHeight(ServiceLocator.getRenderService().getStage().getHeight() * 2f);
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
     * Checks if the black screen should be rendered into the game.
     *
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
        if (open) {
            if (!opening) {
                closing = false;
                opening = true;
                openScreen();
            }
        } else {
            if (!closing) {
                closing = true;
                opening = false;
                closeScreen();
            }
        }
    }

    /**
     * Shows the black screen.
     */
    private void openScreen() {
        moveDown();
    }

    /**
     * Removes the black screen.
     */
    private void closeScreen() {
        moveUp();
    }

    /**
     * Shows the black screen with no fade in time.
     */
    private void openScreenInstant() {
        blackScreen.setY(0);
    }

    /**
     * Removes the black screen with no fade out time.
     */
    private void closeScreenInstant() {
        blackScreen.setY(ServiceLocator.getRenderService().getStage().getHeight());
    }

    /**
     * Moves the screens down relative to their original height.
     */
    private void moveDown() {
        float initialHeight = ServiceLocator.getRenderService().getStage().getHeight();
        if (blackScreen.getY() > initialHeight - ServiceLocator.getRenderService().getStage().getHeight()
                && opening) {
            blackScreen.setY(blackScreen.getY() - 10);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    moveDown();
                    timer.cancel();
                }
            }, 15);
        }
    }

    /**
     * Moves the screens up relative to their original height.
     */
    private void moveUp() {
        if (blackScreen.getY() < ServiceLocator.getRenderService().getStage().getHeight()
                && closing) {
            blackScreen.setY(blackScreen.getY() + 10);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    moveUp();
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
