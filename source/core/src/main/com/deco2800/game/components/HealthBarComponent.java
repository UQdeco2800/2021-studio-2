package com.deco2800.game.components;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.rendering.RenderComponent;
import com.deco2800.game.services.ServiceLocator;

public class HealthBarComponent extends RenderComponent {
    private float ratioOfHealth = 1f;
    private final Sprite health;
    private final Sprite healthBar;
    private float heightOfHealth;
    private float size;

    public HealthBarComponent(Sprite health, Sprite healthBar) {
        super();
        this.health = health;
        this.healthBar = healthBar;
        this.heightOfHealth = 1f;
        this.size = 1f;
    }

    public HealthBarComponent(Sprite health, Sprite healthBar, Float heightOfHealth) {
        super();
        this.health = health;
        this.healthBar = healthBar;
        this.heightOfHealth = heightOfHealth;
        this.size = 1f;
    }

    public HealthBarComponent(Sprite health, Sprite healthBar, Float heightOfHealth, Float size) {
        super();
        this.health = health;
        this.healthBar = healthBar;
        this.heightOfHealth = heightOfHealth;
        this.size = size;
    }

    public void scaleEntity(float xScale) {
        float width = (entity.getScale().x / health.getWidth()) * size;
        float height = (entity.getScale().y / health.getHeight()) * (xScale / 2) * size;
        health.setScale(height, width);
    }

    public void scaleHealthBar() {
        float width = (entity.getScale().x / healthBar.getWidth()) * size;
        float height = (entity.getScale().y / healthBar.getHeight()) * 0.5f * size;
        healthBar.setScale(height, width);
    }

    @Override
    public void create() {
        super.create();
        scaleHealthBar();
    }

    @Override
    public void update() {
        float currentHealth = getEntity().getComponent(CombatStatsComponent.class).getHealth();
        float MaxHealth = getEntity().getComponent(CombatStatsComponent.class).getMaxHealth();
        ratioOfHealth = currentHealth / MaxHealth;
        if (ratioOfHealth == 0f) {
            ratioOfHealth = 0.1f;
        }
    }
    @Override
    public void dispose() {
        ServiceLocator.getRenderService().unregister(this);
    }
    @Override
    protected void draw(SpriteBatch batch) {
        Vector2 position = entity.getPosition();
        Vector2 positionCenter = entity.getCenterPosition();
        float angle = entity.getAngle();

        if (healthBar != null) {
            healthBar.setRotation(angle);
            healthBar.setCenter(positionCenter.x, positionCenter.y + heightOfHealth);
            scaleHealthBar();
            healthBar.draw(batch);
        }

        if (health != null) {
            scaleEntity(ratioOfHealth);
            health.setRotation(angle);
            health.setCenter(positionCenter.x, positionCenter.y + heightOfHealth);
            health.draw(batch);
        }
    }

}

