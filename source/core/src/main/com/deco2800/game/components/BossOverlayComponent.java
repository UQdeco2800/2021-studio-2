package com.deco2800.game.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.deco2800.game.ui.textbox.TextBox;

public class BossOverlayComponent extends UIComponent {

    /**
     * has the boss spawned
     */
    private boolean enemySpawn;

    /**
     * Table which holds the icon and changeable health bar.
     */
    private Table table;

    /**
     * Table to hold the frame of the health bar.
     */
    private Table tableFrame;

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
     * Name Of the boss
     */
    private String bossName = "Boss";

    private static final  float HP_BAR_SIZE = 500f;

    private static final float OFF_SET = 100f;


    /**
     * Creates actors and positions them on the stage using a table.
     *
     * @see Table for positioning options
     */
    private void addActors() {
        // Done using a private method because this will be called repeatedly
        createTable();

        // Health Bar Frame
        Table healthText = new Table();
        healthText.top().right();
        healthText.setFillParent(true);
        healthText.setZIndex(10);
        //position the boss name correctly in the center of hp bar
        healthText.padRight(((HP_BAR_SIZE / 2f) + OFF_SET) - (bossName.length() / 2f)).padTop(40f);

        healthLabel = new Label(bossName, skin, "health");
        healthText.add(healthLabel);

        // Health image
        healthBarLeft = new Image(ServiceLocator.getResourceService()
                .getAsset("images/health_left.png", Texture.class));
        healthBarMiddle = new Image(ServiceLocator.getResourceService()
                .getAsset("images/health_middle.png", Texture.class));
        healthBarRight = new Image(ServiceLocator.getResourceService()
                .getAsset("images/health_right.png", Texture.class));

        // Health Bar Frame
        tableFrame = new Table();
        tableFrame.top().right();
        tableFrame.setFillParent(true);
        frameLeft = new Image(ServiceLocator.getResourceService()
                .getAsset("images/boss_health_left.png", Texture.class));
        frameMiddle = new Image(ServiceLocator.getResourceService()
                .getAsset("images/boss_health_middle.png", Texture.class));
        frameRight = new Image(ServiceLocator.getResourceService().
                getAsset("images/boss_health_right.png", Texture.class));
        tableFrame.padRight(90f).padTop(26.5f);
        tableFrame.add(frameLeft).height(60f).width(30f);
        tableFrame.add(frameMiddle).height(55f).width(HP_BAR_SIZE);
        tableFrame.add(frameRight).height(60f).width(30f);

        stage.addActor(tableFrame);
        updateBossHealthUI((int) maxHealth); //initialise hp bar size
        stage.addActor(table);
        stage.addActor(healthText);
    }

    private void createTable() {
        table.top().right();
        table.setFillParent(true);
        table.padTop(36.5f).padRight(OFF_SET);
    }

    public void updateBossHealthUI(int health) {
        healthLabel.setText(bossName);
        table.reset();
        createTable();
        if (Boolean.FALSE.equals(entity.getComponent(CombatStatsComponent.class).isDead())) {
            table.add(healthBarLeft).height(40f).width(20f);
            table.add(healthBarMiddle).height(40f).width(health/maxHealth * HP_BAR_SIZE);
            table.add(healthBarRight).height(40f).width(20f);
        }
    }

    public void nameBoss(String boss) {
        this.bossName = boss;
    }

    @Override
    public void create() {
        super.create();
        maxHealth = entity.getComponent(CombatStatsComponent.class).getMaxHealth();
        table = new Table();
        enemySpawn = false;
        addActors();

        entity.getEvents().addListener("updateBossHealth", this::updateBossHealthUI);
    }

    public boolean hasEnemySpawned() {
        if (ServiceLocator.getGameAreaService().getNumEnemy() <= 0 && !enemySpawn) {
            enemySpawn = true;
        }
        return enemySpawn;
    }

    @Override
    protected void draw(SpriteBatch batch) {
        TextBox textBox = ServiceLocator.getEntityService()
                .getUIEntity().getComponent(TextBox.class);


        if (textBox.shouldShowBars() || !hasEnemySpawned()) {
            table.setVisible(false);
            healthLabel.setVisible(false);
            tableFrame.setVisible(false);
        } else {
            table.setVisible(true);
            healthLabel.setVisible(true);
            tableFrame.setVisible(true);
        }
    }


    @Override
    public void dispose() {
        super.dispose();
        healthBarLeft.remove();
        healthBarRight.remove();
        healthBarMiddle.remove();
        frameLeft.remove();
        frameRight.remove();
        frameMiddle.remove();
        healthLabel.remove();
    }
}
