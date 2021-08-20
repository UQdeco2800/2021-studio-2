package com.deco2800.game.ui.textbox;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.deco2800.game.ui.UIComponent;

public class TextBoxDisplay extends UIComponent {

    private static final float Z_INDEX = 10f;

    private TextBox textBox;
    private Label label;

    @Override
    public void create() {
        super.create();
        addActors();
        textBox = entity.getComponent(TextBox.class);
    }

    private void addActors() {
        label = new Label("", skin);
        label.setPosition(200f, 200f);
        stage.addActor(label);
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (textBox.isOpen()) {
            label.setVisible(true);
            String message = "Hello Team-1, TODO: Make an enum/class to store the dialogue " +
                    "that's \ncurrently planned to be added. Also might need to dissect the messages by" +
                    "\ntheir character length for different lines.";
            label.setText(message);
        } else {
            label.setVisible(false);
        }
    }

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
