package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.CutsceneScreen;
import com.deco2800.game.ui.UIComponent;
import com.deco2800.game.ui.textbox.TextBox;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {

    /**
     * Table which holds the icon and changeable health bar.
     */
    private Table table;

    /**
     * Table to hold the frame of the health bar.
     */
    private Table tableFrame;

    /**
     * HP Icon
     */
    private Image heartImage;

    /**
     * Holds the left image of the health bar
     */
    private Image healthBarLeft;

    /**
     * Holds the middle image of the health bar that will change in width
     */
    private Image healthBarMiddle;

    /**
     * Holds the right image of the health bar
     */
    private Image healthBarRight;

    /**
     * The maximum health of the player.
     */
    private float maxHealth;

    /**
     * Holds the left image of the health bar frame
     */
    private Image frameLeft;

    /**
     * Holds the middle image of the health bar frame
     */
    private Image frameMiddle;

    /**
     * Holds the right image of the health bar frame
     */
    private Image frameRight;

    /**
     * Holds the health percentage label that will update with health changes
     */
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
     *
     * @see Table for positioning options
     */
    private void addActors() {
        maxHealth = entity.getComponent(CombatStatsComponent.class).getMaxHealth();
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
        heartImage = new Image(ServiceLocator.getResourceService()
                .getAsset("images/hp_icon.png", Texture.class));

        // Health image
        healthBarLeft = new Image(ServiceLocator.getResourceService()
                .getAsset("images/health_left.png", Texture.class));
        healthBarMiddle = new Image(ServiceLocator.getResourceService()
                .getAsset("images/health_middle.png", Texture.class));
        healthBarRight = new Image(ServiceLocator.getResourceService()
                .getAsset("images/health_right.png", Texture.class));

        // Health Bar Frame
        tableFrame = new Table();
        tableFrame.top().left();
        tableFrame.setFillParent(true);
        frameLeft = new Image(ServiceLocator.getResourceService()
                .getAsset("images/health_frame_left.png", Texture.class));
        frameMiddle = new Image(ServiceLocator.getResourceService()
                .getAsset("images/health_frame_middle.png", Texture.class));
        frameRight = new Image(ServiceLocator.getResourceService().
                getAsset("images/health_frame_right.png", Texture.class));
        tableFrame.padLeft(94f).padTop(36.5f);
        tableFrame.add(frameLeft).height(40f).width(20f);
        tableFrame.add(frameMiddle).height(40f).width(280f);
        tableFrame.add(frameRight).height(40f).width(20f);

        stage.addActor(tableFrame);
        updatePlayerHealthUI((int) maxHealth); //initialise hp bar size
        stage.addActor(table);
        stage.addActor(healthText);
    }

    @Override
    public void draw(SpriteBatch batch) {
        TextBox textBox = ServiceLocator.getEntityService()
                .getUIEntity().getComponent(TextBox.class);
        CutsceneScreen cutsceneScreen = ServiceLocator.getEntityService()
                .getUIEntity().getComponent(CutsceneScreen.class);
        if (textBox.shouldShowBars() || cutsceneScreen.isOpen()) {
            table.setVisible(false);
            healthLabel.setVisible(false);
            heartImage.setVisible(false);
            tableFrame.setVisible(false);
        } else {
            table.setVisible(true);
            healthLabel.setVisible(true);
            heartImage.setVisible(true);
            tableFrame.setVisible(true);
        }
    }

    /**
     * Updates the player's health on the ui.
     * Also checks for when to display the low health changes and death screen
     *
     * @param health player health
     */
    public void updatePlayerHealthUI(int health) {
        updateLowHealthUI();
        CharSequence text = String.format("%d", health * 100 / 300);
        healthLabel.setText(text + "%");
        table.reset();
        createTable();
        table.add(healthBarLeft).height(40f).width(20f);
        table.add(healthBarMiddle).height(40f).width(health - 20f);
        table.add(healthBarRight).height(40f).width(20f);
    }

    /**
     * Sets the properties of the table to add the health bar items.
     */
    private void createTable() {
        table.top().left();
        table.setFillParent(true);
        table.padTop(20f).padLeft(20f);
        table.add(heartImage).size(64f).pad(5);
    }

    /**
     * Updates the PlayerLowHealthDisplay components by triggering the respective events
     */
    public void updateLowHealthUI() {
        float currentHealth = entity.getComponent(CombatStatsComponent.class).getHealth();
        float lowHealthThreshold = 0.30f * maxHealth; //change float value to change the threshold
        float alpha = 1.2f - (currentHealth / lowHealthThreshold); //changing opacity of bloody view
        boolean play = currentHealth < (maxHealth * 0.30f); //heart beat sound plays at 30% max health

        if (currentHealth <= 0) {
            tableFrame.setVisible(false);
            entity.getEvents().trigger("deathFade");
        }
        if (currentHealth <= lowHealthThreshold) {
            //call the event trigger for bloodyViewOn when hp reaches below threshold
            entity.getEvents().trigger("bloodyViewOn", alpha, play);
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
    }
}
