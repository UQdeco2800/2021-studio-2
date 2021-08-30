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

    /** The label that will be added to the screen to display the message. */
    private Label label;

    /** Stores the image that will surround the text message. */
    private Image image;

    @Override
    public void create() {
        super.create();
        addActors();
        textBox = entity.getComponent(TextBox.class);
    }

    /**
     * Adds the label actor to the screen which will be constantly updated to display changes.
     */
    private void addActors() {
        label = new Label("", skin);
        label.setPosition(200f, 200f);
        label.setFontScale(1.5f);
        image = new Image(ServiceLocator.getResourceService()
                .getAsset("images/test_box.png", Texture.class));
        stage.addActor(label);
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
            label.setVisible(true);
            label.setText(textBox.getSubMessage());
        } else {
            label.setVisible(false);
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
        label.remove();
    }
}
