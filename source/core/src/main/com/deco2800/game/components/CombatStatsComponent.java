package com.deco2800.game.components;

import com.deco2800.game.components.player.KeyboardPlayerInputComponent;
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
        entity.getEvents().addListener("healEntity", this::addHealth);
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
        if (health >= 0) {
            this.health = health;
        } else {
            this.health = 0;
            if (getEntity() != null) {
                if (getEntity().getComponent(KeyboardPlayerInputComponent.class) == null) {
                    getEntity().prepareDispose();
                }
            }
        }
        if (entity != null) {
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

    /**
     * called when an entity is attack
     * if this entity has a transform component or hit animations they will be called.
     * if combatStatComponent is disabled this method will not do anything.
     * @param attacker the CombatStatComponent of the attacker
     */
    public void hit(CombatStatsComponent attacker) {
        if (this.enabled) {
            int newHealth = getHealth() - attacker.getBaseAttack();
            //check for hit animations
            checkHitAnimations();
            //if entity has Transform Component and is about to die we don't want to update hp
            // here since it will dispose. Instead we want to disable this component and perform
            // our transformation.
            if (!checkTransformation(newHealth)) {
                setHealth(newHealth);
            }
        }
    }

    /**
     * plays hit animation if the entity has one in its animation controller and
     * atlas file. The event name registered for starting the hit animations must be called
     * 'hit'. The animation names don't have to be 'hit' though.
     */
    private void checkHitAnimations() {
        if (entity.getEvents().hasEvent("hit")) {
            entity.getEvents().trigger("hit");
        }
    }

    /**
     * if the entity has a Transform Component it will execute its transformation
     * will only transform the entity if its hp <= 0 and will disable CombatStatsComponent
     * @param health the current health of the entity
     * @return true if entity has a TransformComponent otherwise false
     */
    private boolean checkTransformation(int health) {
        //transform is added in TransformEntityComponent
        if (entity.getEvents().hasEvent("transformEntity")) {
            if (health <= 0) {
                entity.getEvents().trigger("transformEntity");
                return true;
            }
        }
        return false;
    }
}
