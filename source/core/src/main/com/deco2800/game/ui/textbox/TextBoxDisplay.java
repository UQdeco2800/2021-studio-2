package com.deco2800.game.ui.textbox;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.graphics.Texture;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

public class TextBoxDisplay extends UIComponent {

    /** The priority of the UI component to be displayed on the screen. */
    private static final float Z_INDEX = 10f;

    /** Instance of the Text Box that will be displayed to the screen. */
    private TextBox textBox;

    /** The mainCharacterLabel that will be added to the screen to display the message. */
    private Label mainCharacterLabel;

    /** Stores the image that will surround the text message. */
    private Image mainCharacterImage;

    /** The mainCharacterLabel that will be added to the screen to display the message. */
    private Label enemyLabel;

    /** Stores the image that will surround the text message. */
    private Image enemyImage;

    private final float TEXT_BOX_HEIGHT = 570f;

    private final float TEXT_BOX_WIDTH = 1050;

    private final float DISPLAY_Y_POS = 50f;

    private final float TEXT_Y_POS = 200f;

    private final float MAIN_CHARACTER_DISPLAY_X = 100f;

    private final float MAIN_CHARACTER_TEXT_X = 200f;

    private final float ENEMY_DISPLAY_X =
            ServiceLocator.getRenderService().getStage().getWidth() - TEXT_BOX_WIDTH - MAIN_CHARACTER_DISPLAY_X;

    private final float ENEMY_TEXT_X =
            ServiceLocator.getRenderService().getStage().getWidth() - TEXT_BOX_WIDTH + 100f;


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
        //Text box for the main character set up
        mainCharacterLabel = new Label("", skin);
        mainCharacterLabel.setPosition(MAIN_CHARACTER_TEXT_X, TEXT_Y_POS);
        mainCharacterLabel.setFontScale(1.5f);
        mainCharacterImage = new Image(ServiceLocator.getResourceService()
                .getAsset("images/text_box.png", Texture.class));
        mainCharacterImage.setPosition(MAIN_CHARACTER_DISPLAY_X, DISPLAY_Y_POS);
        mainCharacterImage.setWidth(TEXT_BOX_WIDTH);
        mainCharacterImage.setHeight(TEXT_BOX_HEIGHT);

        stage.addActor(mainCharacterImage);
        stage.addActor(mainCharacterLabel);

        //Text box for any other NPC set up

        enemyLabel = new Label("", skin);
        enemyLabel.setPosition(ENEMY_TEXT_X, TEXT_Y_POS);
        enemyLabel.setFontScale(1.5f);
        enemyImage = new Image(ServiceLocator.getResourceService()
                .getAsset("images/text_box_enemy.png", Texture.class));
        enemyImage.setPosition(ENEMY_DISPLAY_X, DISPLAY_Y_POS);
        enemyImage.setWidth(TEXT_BOX_WIDTH);
        enemyImage.setHeight(TEXT_BOX_HEIGHT);

        stage.addActor(enemyImage);
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
            if (textBox.isMainCharacterShowing()) {
                enemyLabel.setVisible(false);
                enemyImage.setVisible(false);
                mainCharacterLabel.setVisible(true);
                mainCharacterImage.setVisible(true);
                mainCharacterLabel.setText(textBox.getSubMessage());
            } else {
                mainCharacterLabel.setVisible(false);
                mainCharacterImage.setVisible(false);
                enemyLabel.setVisible(true);
                enemyImage.setVisible(true);
                enemyLabel.setText(textBox.getSubMessage());
            }
        } else {
            mainCharacterLabel.setVisible(false);
            mainCharacterImage.setVisible(false);
            enemyLabel.setVisible(false);
            enemyImage.setVisible(false);
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
        mainCharacterLabel.remove();
        mainCharacterImage.remove();
    }
}
