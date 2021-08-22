package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.death.DeathDisplay;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table table;
  private Image heartImage;
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
    table.top().left();
    table.setFillParent(true);
    table.padTop(45f).padLeft(5f);
    table.setDebug(true); //to see the outlines

    // Heart image
    float heartSideLength = 30f;
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));

    // Health text
    int health = entity.getComponent(CombatStatsComponent.class).getHealth();
    CharSequence healthText = String.format("Health: %d", health);
    healthLabel = new Label(healthText, skin, "large");

    table.add(heartImage).size(heartSideLength).pad(5);
    table.add(healthLabel);
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
    CharSequence text = String.format("Health: %d", health);
    healthLabel.setText(text);
  }

  /**
   * Updates the PlayerLowHealthDisplay components by triggering the respective events
   * also checks if the player is dead.
   */
  public void updateLowHealthUI() {
    float currentHealth =  entity.getComponent(CombatStatsComponent.class).getHealth();
    float maxHealth =  entity.getComponent(CombatStatsComponent.class).getMaxHealth();
    boolean isDead = entity.getComponent(CombatStatsComponent.class).isDead();
    float lowHealthThreshold = 0.33f * maxHealth;

    //checks if player is dead, turns off bloody view
    if (isDead) {
      entity.getEvents().trigger("bloodyViewOff");
      entity.getEvents().trigger("deathScreen");
      //triggerDeath(); //somehow trigger the  death screen?
    } else if (currentHealth <=  lowHealthThreshold) {
      //call the event trigger for bloodyViewOn when hp reaches below threshold
      System.out.println("Blood View on");
      entity.getEvents().trigger("bloodyViewOn");
    } else {
      //turn off blood view when above low health threshold
      System.out.println("Blood View off");
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
    healthLabel.remove();
  }
}
