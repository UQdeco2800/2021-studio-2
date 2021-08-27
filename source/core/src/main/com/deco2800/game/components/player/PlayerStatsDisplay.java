package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
  private Label healthLabel;

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
    //table.setDebug(true); //to see the outlines

    // Health text
    int health = entity.getComponent(CombatStatsComponent.class).getHealth();
    CharSequence healthText = "HP";
    healthLabel = new Label(healthText, skin, "large");

    // Heart image
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/hp_icon.png", Texture.class));

    // Health image
    healthBarLeft = new Image(ServiceLocator.getResourceService().getAsset("images/health_left.png", Texture.class));
    healthBarMiddle = new Image(ServiceLocator.getResourceService().getAsset("images/health_middle.png", Texture.class));
    healthBarRight = new Image(ServiceLocator.getResourceService().getAsset("images/health_right.png", Texture.class));

    updatePlayerHealthUI(100);
    stage.addActor(table);
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
    table.reset();
    createTable();
    table.add(healthBarLeft).height(30f);
    table.add(healthBarMiddle).height(30f).width(2 * health - 10);
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
   * also checks if the player is dead.
   */
  public void updateLowHealthUI() {
    float currentHealth =  entity.getComponent(CombatStatsComponent.class).getHealth();
    float maxHealth =  entity.getComponent(CombatStatsComponent.class).getMaxHealth();
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

//can't access another entities events find a diff way to trigger death screen
//  public void triggerDeath() {
//    System.out.println("Trigger Death");
//    entity.getEvents().trigger("deathScreen");
//  }

  @Override
  public void dispose() {
    super.dispose();
    heartImage.remove();
    healthBarLeft.remove();
    healthBarRight.remove();
    healthBarMiddle.remove();
    healthLabel.remove();
  }
}
