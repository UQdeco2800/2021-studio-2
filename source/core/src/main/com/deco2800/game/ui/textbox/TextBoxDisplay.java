package com.deco2800.game.ui.textbox;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
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

    /**
     * The name to be displayed on the text box for the main character.
     */
    private Label mainCharacterName;

    /**
     * The mainCharacterLabel that will be added to the screen to display the message.
     */
    private Label mainCharacterLabel;

    /**
     * Stores the image that will surround the text message.
     */
    private Image mainCharacterBox;

    /**
     * Stores the main character image that will surround the text message.
     */
    private Image mainCharacterImage;

    /**
     * The name to be displayed on the text box for the NPC.
     */
    private Label enemyName;

    /**
     * The mainCharacterLabel that will be added to the screen to display the message.
     */
    private Label enemyLabel;

    /**
     * Stores the image that will surround the text message.
     */
    private Image enemyBox;

    /**
     * Stores the enemy character image that will surround the text message.
     */
    private Image enemyImage;

    /**
     * Bars to indicate a cutscene is taking place.
     */
    private Image topBar;
    private Image botBar;

    /**
     * If the cutscene is currently opening.
     */
    private boolean opening = false;

    /**
     * If the cutscene is currently closing.
     */
    private boolean closing = false;

    private final float TEXT_BOX_HEIGHT = 400f;

    private final float TEXT_BOX_WIDTH = 800f;

    private final float DISPLAY_Y_POS = 115f;

    private final float TEXT_Y_POS = 250f;

    private final float CHARACTER_IMAGE_Y_POS = 250f;

    private final float CHARACTER_SIZE = 384f;

    private final float MAIN_CHARACTER_DISPLAY_X = 70f;

    private final float NAME_Y = 325f;

    private final float ENEMY_DISPLAY_X =
            ServiceLocator.getRenderService().getStage().getWidth() - TEXT_BOX_WIDTH - MAIN_CHARACTER_DISPLAY_X;

    private final float ENEMY_TEXT_X =
            ServiceLocator.getRenderService().getStage().getWidth() - TEXT_BOX_WIDTH + 60f;

    private final float ENEMY_CHARACTER_X =
            ServiceLocator.getRenderService().getStage().getWidth() - CHARACTER_SIZE - MAIN_CHARACTER_DISPLAY_X;

    private final float ENEMY_NAME_X = ServiceLocator.getRenderService().getStage().getWidth() - 705;

    private final float BAR_HEIGHT = 120f;

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
        float MAIN_CHARACTER_NAME_X = 550f;
        float MAIN_CHARACTER_TEXT_X = 180f;
        float MAIN_CHARACTER_X_POS = 100f;

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
        mainCharacterName = new Label("WARRIOR", skin);
        mainCharacterName.setAlignment(Align.center);
        mainCharacterName.setPosition(MAIN_CHARACTER_NAME_X, NAME_Y);
        mainCharacterLabel = new Label("", skin);
        mainCharacterLabel.setPosition(MAIN_CHARACTER_TEXT_X, TEXT_Y_POS);
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
        stage.addActor(mainCharacterName);

        setEnemyTextBox();
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
                setNPCVisible(false);
                setMainCharacterVisible(true);
                mainCharacterLabel.setText(textBox.getSubMessage());
            } else {
                setMainCharacterVisible(false);
                setNPCVisible(true);
                setEnemyTextBox();
                enemyLabel.setText(textBox.getSubMessage());
            }
        } else {
            setMainCharacterVisible(false);
            setNPCVisible(false);
            if (!closing) {
                closing = true;
                opening = false;
                closeBars();
            }
        }
    }

    /**
     * Sets all of the display for the main character to the boolean value of visible
     *
     * @param visible boolean which is true if the items should be displayed, false otherwise
     */
    private void setMainCharacterVisible(boolean visible) {
        mainCharacterName.setVisible(visible);
        mainCharacterImage.setVisible(visible);
        mainCharacterLabel.setVisible(visible);
        mainCharacterBox.setVisible(visible);
    }

    /**
     * Sets all of the display for the NPC to the boolean value of visible
     *
     * @param visible boolean which is true if the items should be displayed, false otherwise
     */
    private void setNPCVisible(boolean visible) {
        enemyName.setVisible(visible);
        enemyImage.setVisible(visible);
        enemyLabel.setVisible(visible);
        enemyBox.setVisible(visible);
    }


    /**
     * Makes the bars fade into the screen.
     */
    private void openBars() {
        moveDown(topBar, opening);
        moveUp(botBar, opening);
    }

    /**
     * Makes the bars fade out of the screen.
     */
    private void closeBars() {
        moveDown(botBar, closing);
        moveUp(topBar, closing);
    }

    /**
     * Moves the bars down relative to their original height.
     *
     * @param bar  the image that will change position
     * @param type the boolean type that will be checked to repeat
     */
    private void moveDown(Image bar, boolean type) {
        float initialHeight = -120;
        if (bar == topBar) {
            initialHeight = ServiceLocator.getRenderService().getStage().getHeight();
        }
        if (bar.getY() > initialHeight - BAR_HEIGHT && type) {
            bar.setY(bar.getY() - 2);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    moveDown(bar, true);
                    timer.cancel();
                }
            }, 5);
        }
    }

    /**
     * Moves the bars up relative to their original height.
     *
     * @param bar  the image that will change position
     * @param type the boolean type that will be checked to repeat
     */
    private void moveUp(Image bar, boolean type) {
        float initialHeight = -120;
        if (bar == topBar) {
            initialHeight = ServiceLocator.getRenderService().getStage().getHeight();
        }
        if (bar.getY() < initialHeight + BAR_HEIGHT && type) {
            bar.setY(bar.getY() + 2);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    moveUp(bar, false);
                    timer.cancel();
                }
            }, 5);
        }
    }

    /**
     * Sets the display of the text box to match the NPC speaking.
     */
    private void setEnemyTextBox() {
        RandomDialogueSet set = textBox.getRandomDialogueSet();
        if (set == null) {
            return;
        }
        if (enemyName != null) {
            enemyName.remove();
        }
        if (enemyImage != null) {
            enemyImage.remove();
        }
        if (enemyBox != null) {
            enemyBox.remove();
        }
        if (enemyLabel != null) {
            enemyLabel.remove();
        }

        switch (set) {
            case TUTORIAL:
                enemyName = new Label("PRISONER", skin);
                enemyImage = new Image(ServiceLocator.getResourceService()
                        .getAsset("images/textBoxDisplay/prisoner_image.png", Texture.class));
                enemyBox = new Image(ServiceLocator.getResourceService()
                        .getAsset("images/textBoxDisplay/prison_text_box.png", Texture.class));
                break;
            case LOKI_OPENING:
                enemyName = new Label("    LOKI", skin);
                enemyImage = new Image(ServiceLocator.getResourceService()
                        .getAsset("images/textBoxDisplay/loki_image.png", Texture.class));
                enemyBox = new Image(ServiceLocator.getResourceService()
                        .getAsset("images/textBoxDisplay/loki_text_box.png", Texture.class));
                break;
            case GARMR:
                enemyName.setText("GARMR");
                break;
        }
        setNPCPosition();
    }

    /**
     * Sets the position of all the items to be displayed for the updated NPC.
     */
    private void setNPCPosition() {
        enemyLabel = new Label("", skin);
        enemyLabel.setPosition(ENEMY_TEXT_X, TEXT_Y_POS);

        enemyName.setAlignment(Align.center);
        enemyName.setPosition(ENEMY_NAME_X, NAME_Y);

        enemyImage.setPosition(ENEMY_CHARACTER_X, CHARACTER_IMAGE_Y_POS);
        enemyImage.setWidth(CHARACTER_SIZE);
        enemyImage.setHeight(CHARACTER_SIZE);

        enemyBox.setPosition(ENEMY_DISPLAY_X, DISPLAY_Y_POS);
        enemyBox.setWidth(TEXT_BOX_WIDTH);
        enemyBox.setHeight(TEXT_BOX_HEIGHT);

        stage.addActor(enemyImage);
        stage.addActor(enemyBox);
        stage.addActor(enemyName);
        stage.addActor(enemyLabel);
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
        mainCharacterName.remove();
        mainCharacterImage.remove();
        mainCharacterLabel.remove();
        mainCharacterBox.remove();
        enemyName.remove();
        enemyImage.remove();
        enemyLabel.remove();
        enemyBox.remove();
        topBar.remove();
        botBar.remove();
    }
}
