package com.deco2800.game.components;

import com.deco2800.game.rendering.AnimationRenderComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage it combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
public class CombatStatsComponent extends Component {

    private static final Logger logger = LoggerFactory.getLogger(CombatStatsComponent.class);
    private int health;
    private int maxHealth; // if we want to change his max health use the setMaxHeatlh()
    private int baseAttack;

    public CombatStatsComponent(int health, int baseAttack) {
        this.health = health;
        setMaxHealth(health);
        setBaseAttack(baseAttack);
        //if entities can heal trigger this even
    }

    public void create() {
        entity.getEvents().addListener("heal", this::addHealth);
    }

    /**
    * Returns true if the entity's has 0 health, otherwise false.
    *
    * @return is player dead
    */
    public Boolean isDead() {
        return health == 0;
    }

    /**
     * Returns the entity's health.
     *
     * @return entity's health
     */
    public int getHealth() {
        return health;
    }


    /**
    * to change the max health of the player
    *
    * @param health  the new max health of the player to set
    */
    public void setMaxHealth(int health) {
        this.maxHealth = health;
    }

    /**
     * Return's entity's health maximum
     *
     * @return entity's maximum health
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Sets the entity's health. Health has a minimum bound of 0.
     *
     * @param health health
     */
    public void setHealth(int health) {
        if (health > maxHealth) {
            this.health = maxHealth; //cannot get more health than his set max health
        }
        else if (health >= 0) {
            this.health = health;
        } else {
            this.health = 0;
            if (this.entity != null) {
                this.entity.prepareDispose();
            }
        }
        if (this.entity != null) {
            entity.getEvents().trigger("updateHealth", this.health);
        }
    }

    /**
     * Adds to the player's health. The amount added can be negative.
     *
     * @param health health to add
     */
    public void addHealth(int health) {
        setHealth(this.health + health);
    }

    /**
     * Returns the entity's base attack damage.
     *
     * @return base attack damage
     */
    public int getBaseAttack() {
        return this.baseAttack;
    }

    /**
     * Sets the entity's attack damage. Attack damage has a minimum bound of 0.
     *
     * @param attack Attack damage
     */
    public void setBaseAttack(int attack) {
        if (attack >= 0) {
            this.baseAttack = attack;
        } else {
            logger.error("Can not set base attack to a negative attack value");
        }
    }

    public void hit(CombatStatsComponent attacker) {
    //check for hit animations
        // - must have a 'hit' event listener in the *animationController Component
        if (hasAnimation() && playHitAnimation()) {
            entity.getEvents().trigger("hit");
        }
        int newHealth = getHealth() - attacker.getBaseAttack();
        setHealth(newHealth);
    }

    public boolean playHitAnimation() {
        return this.entity.getComponent(AnimationRenderComponent.class).hasAnimation("hit");
    }

    public boolean hasAnimation() {
        AnimationRenderComponent animate = this.entity.getComponent(AnimationRenderComponent.class);
        return animate != null;
    }
}
