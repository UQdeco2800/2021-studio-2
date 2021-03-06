package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.player.KeyboardPlayerInputComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
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
    private boolean damageLocked;

    public CombatStatsComponent(int health, int baseAttack) {
        this.health = health;
        setMaxHealth(health);
        setBaseAttack(baseAttack);
        damageLocked = false;
        //if entities can heal trigger this even
    }

    @Override
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
     * @param health the new max health of the player to set
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
        if (damageLocked) {
            return;
        }

        if (health > maxHealth) {
            this.health = maxHealth; //cannot get more health than his set max health
        } else if (health > 0) {
            this.health = health;
        } else {
            this.health = 0;
            if (this.entity != null) {
                if (getEntity().getComponent(KeyboardPlayerInputComponent.class) != null) {
                    getEntity().getComponent(KeyboardPlayerInputComponent.class).lockPlayer();
                    // Deliberately chose this because other entities that have animations may have death animations
                    // it is not possible for a class with no component to animate their death.
                } else if (this.entity.getComponent(AnimationRenderComponent.class) == null) {
                    this.entity.prepareDispose();
                }
            }
        }
        if (this.entity != null) {
            entity.getEvents().trigger("updateHealth", this.health);
            entity.getEvents().trigger("updateBossHealth", this.health);
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

    private void stunEnemy() {
        if (getEntity().getComponent(HitboxComponent.class) == null) {
            return;
        }
        if (getEntity().getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.NPC) {
            if (this.getEntity().getComponent(PhysicsMovementComponent.class) == null) {
                return; // ignore if the physics component is null
            }
            if (this.getEntity().getComponent(
                    PhysicsMovementComponent.class).getTarget() == null) {
                return; // ignore if the physics component is null
            }
            getEntity().getComponent(PhysicsMovementComponent.class).setMoving(false);
            Vector2 direction = this.getEntity().getComponent(
                    PhysicsMovementComponent.class).getDirection();

            if (Math.abs(direction.x) > Math.abs(direction.y)) {
                if (direction.x < 0) {
                    this.getEntity().getEvents().trigger("stunLeft");
                } else {
                    this.getEntity().getEvents().trigger("stunRight");
                }
            } else {
                if (direction.y < 0) {
                    this.getEntity().getEvents().trigger("stunDown");
                } else {
                    this.getEntity().getEvents().trigger("stunUp");
                }
            }
        }
    }

    /**
     * called when an entity is attack
     * if this entity has a transform component or hit animations they will be called.
     * if combatStatComponent is disabled this method will not do anything.
     *
     * @param attacker the CombatStatComponent of the attacker
     */
    public void hit(CombatStatsComponent attacker) {
        if (damageLocked) {
            return;
        }
        if (this.enabled) {
            int newHealth = getHealth() - attacker.getBaseAttack();
            //check for hit animations
            checkHitAnimations();
            //if entity has Transform Component and is about to die we don't want to update hp
            // here since it will dispose. Instead we want to disable this component and perform
            // our transformation.
            if (!checkTransformation(newHealth)) {
                setHealth(newHealth);
                stunEnemy();
            }
        }
    }

    /**
     * Entity receives a hit, but also adds a weapon attack.
     *
     * @param attacker          the attacking entity with the weapon
     * @param weaponAttackPower the weapon damage.
     */
    public void hit(CombatStatsComponent attacker, int weaponAttackPower) {
        if (this.enabled) {
            int newHealth = getHealth() - attacker.getBaseAttack() - weaponAttackPower;
            //check for hit animations
            checkHitAnimations();
            this.entity.getEvents().trigger("enemyHit");
            this.entity.getEvents().trigger("enableWalk");
            //if entity has Transform Component and is about to die we don't want to update hp
            // here since it will dispose. Instead we want to disable this component and perform
            // our transformation.
            if (!checkTransformation(newHealth)) {
                setHealth(newHealth);
                stunEnemy();
            }
        }
    }

    /**
     * Hits purely by weapon attack, ignoring base combat stats of attacking entity.
     *
     * @param weaponAttackPower the weapon damage
     */
    public void weaponHit(int weaponAttackPower) {
        if (this.enabled) {
            int newHealth = getHealth() - weaponAttackPower;
            //check for hit animations
            checkHitAnimations();
            //if entity has Transform Component and is about to die we don't want to update hp
            // here since it will dispose. Instead we want to disable this component and perform
            // our transformation.
            if (!checkTransformation(newHealth)) {
                setHealth(newHealth);
                stunEnemy();
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
     * will only transform the entity if its hp is less than or equal to 0 and will disable CombatStatsComponent
     *
     * @param health the current health of the entity
     * @return true if entity has a TransformComponent otherwise false
     */
    public boolean checkTransformation(int health) {
        //'transformEntity' event is added in TransformEntityComponent
        if (health <= 0 && entity.getEvents().hasEvent("transformEntity")) {
            entity.getEvents().trigger("transformEntity");
            return true;
        }
        return false;
    }

    public void setDamageLocked(boolean lock) {
        this.damageLocked = lock;
    }
}
