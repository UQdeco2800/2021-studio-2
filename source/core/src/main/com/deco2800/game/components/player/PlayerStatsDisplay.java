package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
  private float maxHealth;

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
    createTable();

    // Heart image
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/hp_icon.png", Texture.class));

    // Health image
    healthBarLeft = new Image(ServiceLocator.getResourceService().getAsset("images/health_left.png", Texture.class));
    healthBarMiddle = new Image(ServiceLocator.getResourceService().getAsset("images/health_middle.png", Texture.class));
    healthBarRight = new Image(ServiceLocator.getResourceService().getAsset("images/health_right.png", Texture.class));

    maxHealth =  entity.getComponent(CombatStatsComponent.class).getMaxHealth();
    updatePlayerHealthUI((int)maxHealth); //initialise hp bar size
    stage.addActor(table);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    // draw is handled by the stage
  }

  /**
   * Updates the player's health on the ui.
   * Also checks for when to display the low health changes and death screen
   * @param health player health
   */
  public void updatePlayerHealthUI(int health) {
    updateLowHealthUI();
    deathScreen();
    table.reset();
    createTable();
    table.add(healthBarLeft).height(30f);
    float scale = (health / maxHealth * 100) * 2; //hp bar, same size even if increasing max hp
    table.add(healthBarMiddle).height(30f).width(scale);
    table.add(healthBarRight).height(30f);
  }

  /** Sets the properties of the table to add the health bar items. */
  private void createTable() {
    table.top().left();
    table.setFillParent(true);
    table.padTop(20f);
    table.add(heartImage).size(50f).pad(10);
  }

  /**
   * Updates the PlayerLowHealthDisplay components by triggering the respective events
   */
  public void updateLowHealthUI() {
    float currentHealth =  entity.getComponent(CombatStatsComponent.class).getHealth();
    float lowHealthThreshold = 0.30f * maxHealth; //change float value to change the threshold
    float alpha = 1.2f - (currentHealth/lowHealthThreshold); //changing opacity of bloody view
    boolean play = currentHealth < (maxHealth * 0.30f); //heart beat sound plays at 30% max health

    if (currentHealth <=  lowHealthThreshold) {
      //call the event trigger for bloodyViewOn when hp reaches below threshold
      entity.getEvents().trigger("bloodyViewOn", alpha, play);
    } else {
      //turn off blood view when above low health threshold
      entity.getEvents().trigger("bloodyViewOff");
    }
  }

  /**
   * checks if player is dead if so trigger the death screen.
   * uses event triggers to turn off the bloody view and display
   * the death screen.
   */
  public void deathScreen() {
    boolean isDead = entity.getComponent(CombatStatsComponent.class).isDead();
    if (isDead) {
      entity.getEvents().trigger("bloodyViewOff");
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      entity.getEvents().trigger("deathScreen");
    }
  }

  @Override
  public void dispose() {
    super.dispose();
    heartImage.remove();
    healthBarLeft.remove();
    healthBarRight.remove();
    healthBarMiddle.remove();
  }
}
