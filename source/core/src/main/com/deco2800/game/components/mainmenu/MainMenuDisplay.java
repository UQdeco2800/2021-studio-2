package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.deco2800.game.UI.UIComponent;

public class MainMenuDisplay extends UIComponent {
  private static final float Z_INDEX = 2f;
  private Label gameTitle;
  private Label instructions;

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    gameTitle = new Label("Box Boy and the Ghosts", skin, "title");
    instructions = new Label(
      "'Space' to play\n" +
          "'l' to load\n" +
          "'s' for settings\n" +
          "'e' to exit",
      skin
    );
    stage.addActor(gameTitle);
    stage.addActor(instructions);
  }

  @Override
  public void draw(SpriteBatch batch) {
    Vector2 titlePos = getCenteredPosition(gameTitle).add(0f, 50f);
    gameTitle.setPosition(titlePos.x, titlePos.y);

    Vector2 instructionsPos = getCenteredPosition(instructions).add(0f, -40f);
    instructions.setPosition(instructionsPos.x, instructionsPos.y);
  }

  @Override
  public float getZIndex() {
    return Z_INDEX;
  }

  @Override
  public void dispose() {
    super.dispose();
    gameTitle.remove();
    instructions.remove();
  }
}
