package com.deco2800.game.components;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.rendering.RenderComponent;
import com.deco2800.game.services.ServiceLocator;

public class HealthBarComponent extends RenderComponent {
    private float ratioOfHealth = 1f;
    private float ratioOfHealthPrevious = 1f;
    private final Sprite health;
    private final Sprite healthBar;
    private final Sprite healthDecrease;
    private final float heightOfHealth;
    private final float xSize = 0.8f;
    private final float size;
    private float previousHealth;
    private boolean healthDecreaseCheck;
    private long start = 0;

    public HealthBarComponent(Sprite health, Sprite healthBar, Sprite healthDecrease) {
        super();
        this.health = health;
        this.healthBar = healthBar;
        this.healthDecrease = healthDecrease;
        this.heightOfHealth = 1f;
        this.size = 1f;
    }

    public HealthBarComponent(Sprite health, Sprite healthBar, Sprite healthDecrease, Float heightOfHealth) {
        super();
        this.health = health;
        this.healthBar = healthBar;
        this.healthDecrease = healthDecrease;
        this.heightOfHealth = heightOfHealth;
        this.size = 1f;
    }

    public HealthBarComponent(Sprite health, Sprite healthBar, Sprite healthDecrease, Float heightOfHealth, Float size) {
        super();
        this.health = health;
        this.healthBar = healthBar;
        this.healthDecrease = healthDecrease;
        this.heightOfHealth = heightOfHealth;
        this.size = size;
    }

    public void scaleHealth(float xScale) {
        float width = (xSize / health.getWidth()) * size;
        float height = (entity.getScale().y / health.getHeight()) * (xScale / 2) * size;
        health.setScale(height, width);
    }

    public void scaleHealthBar() {
        float width = (xSize / healthBar.getWidth()) * size;
        float height = (entity.getScale().y / healthBar.getHeight()) * 0.5f * size;
        healthBar.setScale(height, width);
    }

    public void scaleHealthDecrease(float xScale, float xScalePrevious, double ratioHealthDecrease) {
        float scale = xScale + (xScalePrevious - xScale) * (float) ratioHealthDecrease;
        float width = (xSize / healthBar.getWidth());
        float height = (entity.getScale().y / health.getHeight()) * (scale / 2);
        healthDecrease.setScale(height, width);
    }

    private static double round(double value) {
        int scale = (int) Math.pow(10, 1);
        return (double) Math.round(value * scale) / scale;
    }

    @Override
    public void create() {
        super.create();
        scaleHealthBar();
        healthDecreaseCheck = false;
        start = System.currentTimeMillis();
        previousHealth = getEntity().getComponent(CombatStatsComponent.class).getHealth();
    }

    @Override
    public void update() {
        float currentHealth = getEntity().getComponent(CombatStatsComponent.class).getHealth();
        float MaxHealth = getEntity().getComponent(CombatStatsComponent.class).getMaxHealth();
        if (currentHealth != previousHealth) {
            float saveHealth = previousHealth;
            healthDecreaseCheck = true;
            ratioOfHealthPrevious = saveHealth / MaxHealth;
            ratioOfHealthPrevious = (float) round(ratioOfHealthPrevious);
            start = System.currentTimeMillis();

        }
        previousHealth = currentHealth;

        ratioOfHealth = currentHealth / MaxHealth;
        ratioOfHealth = (float) round(ratioOfHealth);
        if (ratioOfHealth == 0f) {
            ratioOfHealth = 0.01f;
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
        if (healthDecreaseCheck && healthDecrease != null) {
            if ((System.currentTimeMillis() - start) / 1000 >= 1) {
                healthDecreaseCheck = false;
            } else {
                healthDecrease.setRotation(angle);
                healthDecrease.setCenter(positionCenter.x, positionCenter.y + heightOfHealth);
                double ratioOfDecrease = 1 - ((System.currentTimeMillis() - start) / 1000.0);
                scaleHealthDecrease(ratioOfHealth, ratioOfHealthPrevious, ratioOfDecrease);
                healthDecrease.draw(batch);
            }
        }

        if (health != null) {
            scaleHealth(ratioOfHealth);
            health.setRotation(angle);
            health.setCenter(positionCenter.x, positionCenter.y + heightOfHealth);
            health.draw(batch);
        }
    }

}
