package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {

  private Table table;
  private Image heartImage;
  private Image healthBarLeft;
  private Image healthBarMiddle;
  private Image healthBarRight;
  private Image frameLeft;
  private Image frameMiddle;
  private Image frameRight;
  private Label healthLabel;
  private Image dash;

  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();

    entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    table = new Table();
    // Done using a private method because this will be called repeatedly
    createTable();

    // Health Bar Frame
    Table healthText = new Table();
    healthText.top().left();
    healthText.setFillParent(true);
    healthText.setZIndex(10);
    healthText.padLeft(220f).padTop(40f);

    healthLabel = new Label("100%", skin, "health");

    healthText.add(healthLabel);

    // Heart image
    heartImage =
            new Image(ServiceLocator.getResourceService().getAsset("images/hp_icon.png", Texture.class));

    // Health image
    healthBarLeft =
            new Image(ServiceLocator.getResourceService().getAsset("images/health_left.png", Texture.class));
    healthBarMiddle =
            new Image(ServiceLocator.getResourceService().getAsset("images/health_middle.png", Texture.class));
    healthBarRight =
            new Image(ServiceLocator.getResourceService().getAsset("images/health_right.png", Texture.class));

    // Dash image
    dash = new
            Image(ServiceLocator.getResourceService().getAsset("images/dash_icon.png", Texture.class));

    // Health Bar Frame
    Table tableFrame = new Table();
    tableFrame.top().left();
    tableFrame.setFillParent(true);
    frameLeft =
            new Image(ServiceLocator.getResourceService().getAsset("images/health_frame_left.png", Texture.class));
    frameMiddle =
            new Image(ServiceLocator.getResourceService().getAsset("images/health_frame_middle.png", Texture.class));
    frameRight =
            new Image(ServiceLocator.getResourceService().getAsset("images/health_frame_right.png", Texture.class));
    tableFrame.padLeft(94f).padTop(36.5f);
    tableFrame.add(frameLeft).height(40f).width(20f);
    tableFrame.add(frameMiddle).height(40f).width(280f);
    tableFrame.add(frameRight).height(40f).width(20f);

    updatePlayerHealthUI(100);
    stage.addActor(tableFrame);
    stage.addActor(table);
    stage.addActor(healthText);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    // draw is handled by the stage
  }

  /**
   * Updates the player's health on the ui.
   * Also checks for when to display the low health changes
   * @param health player health
   */
  public void updatePlayerHealthUI(int health) {
    updateLowHealthUI();
    CharSequence text = String.format("%d", health);
    healthLabel.setText(text + "%");
    table.reset();
    createTable();
    table.add(healthBarLeft).height(40f).width(20f);
    table.add(healthBarMiddle).height(40f).width(3 * health - 20);
    table.add(healthBarRight).height(40f).width(20f);
    table.row();
    table.add(dash).size(64f).pad(5);
  }

  /** Sets the properties of the table to add the health bar items. */
  private void createTable() {
    table.top().left();
    table.setFillParent(true);
    table.padTop(20f).padLeft(20f);
    table.add(heartImage).size(64f).pad(5);
  }

  /**
   * Updates the PlayerLowHealthDisplay components by triggering the respective events
   * also checks if the player is dead.
   */
  public void updateLowHealthUI() {
    float currentHealth = entity.getComponent(CombatStatsComponent.class).getHealth();
    float maxHealth = entity.getComponent(CombatStatsComponent.class).getMaxHealth();
    boolean isDead = entity.getComponent(CombatStatsComponent.class).isDead();
    float lowHealthThreshold = 0.33f * maxHealth; //change float value to change the threshold

    //checks if player is dead, turns off bloody view
    if (isDead) {
      entity.getEvents().trigger("bloodyViewOff");
      entity.getEvents().trigger("deathScreen");
    } else if (currentHealth <=  lowHealthThreshold) {
      //call the event trigger for bloodyViewOn when hp reaches below threshold
      entity.getEvents().trigger("bloodyViewOn");
    } else {
      //turn off blood view when above low health threshold
      entity.getEvents().trigger("bloodyViewOff");
    }
  }

  @Override
  public void dispose() {
    super.dispose();
    heartImage.remove();
    healthBarLeft.remove();
    healthBarRight.remove();
    healthBarMiddle.remove();
    frameLeft.remove();
    frameRight.remove();
    frameMiddle.remove();
    healthLabel.remove();
    dash.remove();
  }
}
