package com.deco2800.game.ui.textbox;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

import java.util.Timer;
import java.util.TimerTask;

public class TextBoxDisplay extends UIComponent {

    /**
     * The priority of the UI component to be displayed on the screen.
     */
    private static final float Z_INDEX = 10f;

    /**
     * Instance of the Text Box that will be displayed to the screen.
     */
    private TextBox textBox;

    /** The mainCharacterLabel that will be added to the screen to display the message. */
    private Label mainCharacterLabel;

    /** Stores the image that will surround the text message. */
    private Image mainCharacterBox;

    /** Stores the main character image that will surround the text message. */
    private Image mainCharacterImage;

    /** The mainCharacterLabel that will be added to the screen to display the message. */
    private Label enemyLabel;

    /** Stores the image that will surround the text message. */
    private Image enemyBox;

    /** Stores the enemy character image that will surround the text message. */
    private Image enemyImage;

    /** Bars to indicate a cutscene is taking place. */
    private Image topBar;
    private Image botBar;

    /** If the cutscene is currently opening. */
    private boolean opening;

    /** If the cutscene is currently closing. */
    private boolean closing;

    private final float TEXT_BOX_HEIGHT = 400f;

    private final float TEXT_BOX_WIDTH = 800f;

    private final float DISPLAY_Y_POS = 115f;

    private final float TEXT_Y_POS = 250f;

    private final float CHARACTER_IMAGE_Y_POS = 250f;

    private final float CHARACTER_SIZE = 384f;

    private final float MAIN_CHARACTER_X_POS = 100f;

    private final float MAIN_CHARACTER_DISPLAY_X = 70f;

    private final float MAIN_CHARACTER_TEXT_X = 180f;

    private final float ENEMY_DISPLAY_X =
            ServiceLocator.getRenderService().getStage().getWidth() - TEXT_BOX_WIDTH - MAIN_CHARACTER_DISPLAY_X;

    private final float ENEMY_TEXT_X =
            ServiceLocator.getRenderService().getStage().getWidth() - TEXT_BOX_WIDTH + 60f;

    private final float ENEMY_CHARACTER_X =
            ServiceLocator.getRenderService().getStage().getWidth() - CHARACTER_SIZE - MAIN_CHARACTER_DISPLAY_X;

    private final float BAR_HEIGHT = 120f;

    @Override
    public void create() {
        super.create();
        addActors();
        textBox = entity.getComponent(TextBox.class);
    }

    /**
     * Adds the mainCharacterLabel actor to the screen which will be constantly updated to display changes.
     */
    private void addActors() {

        topBar = new Image(ServiceLocator.getResourceService()
                .getAsset("images/textBoxDisplay/black_bars.png", Texture.class));
        botBar = new Image(ServiceLocator.getResourceService()
                .getAsset("images/textBoxDisplay/black_bars.png", Texture.class));
        topBar.setWidth(ServiceLocator.getRenderService().getStage().getWidth());
        topBar.setHeight(BAR_HEIGHT);
        topBar.setY(ServiceLocator.getRenderService().getStage().getHeight());
        botBar.setHeight(BAR_HEIGHT);
        botBar.setWidth(ServiceLocator.getRenderService().getStage().getWidth());
        botBar.setY(-BAR_HEIGHT);

        stage.addActor(topBar);
        stage.addActor(botBar);

        //Text box for the main character set up
        mainCharacterLabel = new Label("", skin);
        mainCharacterLabel.setPosition(MAIN_CHARACTER_TEXT_X, TEXT_Y_POS);
        mainCharacterLabel.setFontScale(1f);
        mainCharacterBox = new Image(ServiceLocator.getResourceService()
                .getAsset("images/textBoxDisplay/default_text_box.png", Texture.class));
        mainCharacterBox.setPosition(MAIN_CHARACTER_DISPLAY_X, DISPLAY_Y_POS);
        mainCharacterBox.setWidth(TEXT_BOX_WIDTH);
        mainCharacterBox.setHeight(TEXT_BOX_HEIGHT);
        mainCharacterImage = new Image(ServiceLocator.getResourceService()
                .getAsset("images/textBoxDisplay/main_character_image.png", Texture.class));
        mainCharacterImage.setPosition(MAIN_CHARACTER_X_POS, CHARACTER_IMAGE_Y_POS);
        mainCharacterImage.setWidth(CHARACTER_SIZE);
        mainCharacterImage.setHeight(CHARACTER_SIZE);

        stage.addActor(mainCharacterImage);
        stage.addActor(mainCharacterBox);
        stage.addActor(mainCharacterLabel);

        //Text box for any other NPC set up

        enemyLabel = new Label("", skin);
        enemyLabel.setPosition(ENEMY_TEXT_X, TEXT_Y_POS);
        enemyLabel.setFontScale(1f);
        enemyBox = new Image(ServiceLocator.getResourceService()
                .getAsset("images/textBoxDisplay/default_text_box.png", Texture.class));
        enemyBox.setPosition(ENEMY_DISPLAY_X, DISPLAY_Y_POS);
        enemyBox.setWidth(TEXT_BOX_WIDTH);
        enemyBox.setHeight(TEXT_BOX_HEIGHT);
        enemyImage = new Image(ServiceLocator.getResourceService()
                .getAsset("images/textBoxDisplay/prisoner_image.png", Texture.class));
        enemyImage.setPosition(ENEMY_CHARACTER_X, CHARACTER_IMAGE_Y_POS);
        enemyImage.setWidth(CHARACTER_SIZE);
        enemyImage.setHeight(CHARACTER_SIZE);

        stage.addActor(enemyImage);
        stage.addActor(enemyBox);
        stage.addActor(enemyLabel);
    }

    /**
     * Displays the message to the screen if the text box is open, the sub message will be
     * displayed.
     *
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
        if (textBox.isOpen()) {
            if (!opening) {
                closing = false;
                opening = true;
                openBars();
            }
            if (textBox.isMainCharacterShowing()) {
                enemyImage.setVisible(false);
                enemyLabel.setVisible(false);
                enemyBox.setVisible(false);
                mainCharacterImage.setVisible(true);
                mainCharacterLabel.setVisible(true);
                mainCharacterBox.setVisible(true);
                mainCharacterLabel.setText(textBox.getSubMessage());
            } else {
                mainCharacterImage.setVisible(false);
                mainCharacterLabel.setVisible(false);
                mainCharacterBox.setVisible(false);
                enemyImage.setVisible(true);
                enemyLabel.setVisible(true);
                enemyBox.setVisible(true);
                enemyLabel.setText(textBox.getSubMessage());
            }
        } else {
            mainCharacterImage.setVisible(false);
            mainCharacterLabel.setVisible(false);
            mainCharacterBox.setVisible(false);
            enemyImage.setVisible(false);
            enemyLabel.setVisible(false);
            enemyBox.setVisible(false);
            if (!closing) {
                opening = false;
                closeBars();
            }
            closing = true;
        }
    }

    /**
     * Makes the top bar of the cutscene slowly move into frame.
     */
    private void openBars() {
        moveDown(topBar);
        moveUp(botBar);
    }

    private void closeBars() {
        moveDown(botBar);
        moveUp(topBar);
    }

    private void moveDown(Image bar) {
        float initialHeight = -120;
        if (bar == topBar) {
            initialHeight = ServiceLocator.getRenderService().getStage().getHeight();
        }
        if (bar.getY() > initialHeight - BAR_HEIGHT) {
            bar.setY(bar.getY() - 2);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    moveDown(bar);
                    timer.cancel();
                }
            }, 5);
        }
    }

    private void moveUp(Image bar) {
        float initialHeight = - 120;
        if (bar == topBar) {
            initialHeight = ServiceLocator.getRenderService().getStage().getHeight();
        }
        if (bar.getY() < initialHeight + BAR_HEIGHT) {
            bar.setY(bar.getY() + 2);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    moveUp(bar);
                    timer.cancel();
                }
            }, 5);
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
        mainCharacterImage.remove();
        mainCharacterLabel.remove();
        mainCharacterBox.remove();
        enemyImage.setVisible(false);
        enemyLabel.remove();
        enemyBox.remove();
        topBar.remove();
        botBar.remove();
    }
}
