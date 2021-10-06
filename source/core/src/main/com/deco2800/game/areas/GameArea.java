package com.deco2800.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.areas.terrain.Map;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Represents an area in the game, such as a level, indoor area, etc. An area has a terrain and
 * other entities to spawn on that terrain.
 *
 * <p>Support for enabling/disabling game areas could be added by making this a Component instead.
 */
public abstract class GameArea implements Disposable {

    protected TerrainComponent terrain;
    protected List<Entity> areaEntities;
    protected Entity player;
    protected int numEnemy = 0;
    protected int numBoss = 0;
    protected Map map;

    protected GameArea() {
        areaEntities = new ArrayList<>();
    }

    /**
     * Create the game area in the world.
     */
    public void create() {
        ServiceLocator.registerGameArea(this);
    }

    /**
     * Dispose of all internal entities in the area
     */
    public void dispose() {
        for (Entity entity : areaEntities) {
            entity.dispose();
        }
    }

    /**
     * Use for teleport, track the current map player in
     */
    public int getLevel() {
        return 0;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * increase number of enemy on the map (keep track) - when the enemy is create and spawn
     */
    public void incNum() {
        numEnemy++;
    }

    /**
     * decrease number of enemy on the map (keep track) - when the enemy died
     */
    public void decNum() {
        numEnemy--;
    }

    /**
     * increase the number of boss
     */
    public void incBossNum() {
        numBoss++;
    }

    /**
     * decrease number of boss - spawn the teleport portal to another map
     */
    public void decBossNum() {
        numBoss--;
        if (numBoss == 0) {
            if (getLevel() == 9) {
                //tutorial level
                Entity teleport = ObstacleFactory.createTeleport();
                HashMap<String, Float> teleportPos = map.getTeleportObjects()[0];
                GridPoint2 fixedPos = new GridPoint2(teleportPos.get("x").intValue(), (map.getDimensions().get("n_tiles_heihgt") - teleportPos.get("y").intValue()));
                this.spawnEntityAt(teleport, fixedPos, true, true);
            } else if (getLevel() == 0) {
                //gama area 0
                Entity teleport = ObstacleFactory.createTeleport();
                HashMap<String, Float>[] teleportPos = map.getTeleportObjects();
                GridPoint2 fixedPos = new GridPoint2(teleportPos[0].get("x").intValue(), (map.getDimensions().get("n_tiles_height") - teleportPos[0].get("y").intValue() - 1));
                this.spawnEntityAt(teleport, fixedPos, true, true);

            } else if (getLevel() == 1) {
                //gama area 1
                Entity teleport = ObstacleFactory.createTeleport();
                HashMap<String, Float>[] teleportPos = map.getTeleportObjects();
                GridPoint2 fixedPos = new GridPoint2(teleportPos[0].get("x").intValue(), (map.getDimensions().get("n_tiles_height") - teleportPos[0].get("y").intValue() - 1));
                this.spawnEntityAt(teleport, fixedPos, true, true);

            } else if (getLevel() == 2) {
                //gama area 2
                Entity teleport = ObstacleFactory.createTeleport();
                HashMap<String, Float>[] teleportPos = map.getTeleportObjects();
                GridPoint2 fixedPos = new GridPoint2(teleportPos[0].get("x").intValue(), (map.getDimensions().get("n_tiles_height") - teleportPos[0].get("y").intValue() - 1));
                this.spawnEntityAt(teleport, fixedPos, true, true);

            } else if (getLevel() == 3) {
                //gama area 3
                Entity teleport = ObstacleFactory.createTeleport();
                HashMap<String, Float>[] teleportPos = map.getTeleportObjects();
                GridPoint2 fixedPos = new GridPoint2(teleportPos[0].get("x").intValue(), (map.getDimensions().get("n_tiles_height") - teleportPos[0].get("y").intValue() - 1));
                this.spawnEntityAt(teleport, fixedPos, true, true);

            } else if (getLevel() == 4) {
                //gama area 4
                Entity teleport = ObstacleFactory.createTeleport();
                HashMap<String, Float>[] teleportPos = map.getTeleportObjects();
                GridPoint2 fixedPos = new GridPoint2(teleportPos[0].get("x").intValue(), (map.getDimensions().get("n_tiles_height") - teleportPos[0].get("y").intValue() - 1));
                this.spawnEntityAt(teleport, fixedPos, true, true);

            }

        }
    }

    /**
     * get the number of enemy on the map
     *
     * @return int number of enemy
     */
    public int getNumEnemy() {
        return numEnemy;
    }

    /**
     * Returns the player entity that is created.
     *
     * @return player entity - main character being controlled
     */
    public Entity getPlayer() {
        return player;
    }


    /**
     * Spawn entity at its current position
     *
     * @param entity Entity (not yet registered)
     */
    protected void spawnEntity(Entity entity) {
        areaEntities.add(entity);
        ServiceLocator.getEntityService().register(entity);
    }

    /**
     * Spawn entity on a given tile. Requires the terrain to be set first.
     *
     * @param entity  Entity (not yet registered)
     * @param tilePos tile position to spawn at
     * @param centerX true to center entity X on the tile, false to align the bottom left corner
     * @param centerY true to center entity Y on the tile, false to align the bottom left corner
     */
    public void spawnEntityAt(
            Entity entity, GridPoint2 tilePos, boolean centerX, boolean centerY) {
        Vector2 worldPos = terrain.tileToWorldPosition(tilePos);
        float tileSize = terrain.getTileSize();

        if (centerX) {
            worldPos.x += (tileSize / 2) - entity.getCenterPosition().x;
        }
        if (centerY) {
            worldPos.y += (tileSize / 2) - entity.getCenterPosition().y;
        }

        entity.setPosition(worldPos);
        spawnEntity(entity);
    }

    /**
     * Spawn entity on a given tile. Requires the terrain to be set first.
     *
     * @param entity    Entity (not yet registered)
     * @param entityPos world position to spawn at
     * @param centerX   true to center entity X on the tile, false to align the bottom left corner
     * @param centerY   true to center entity Y on the tile, false to align the bottom left corner
     */
    public void spawnEntityAt(
            Entity entity, Vector2 entityPos, boolean centerX, boolean centerY) {
        float tileSize = terrain.getTileSize();

        if (centerX) {
            entityPos.x += (tileSize / 2) - entity.getCenterPosition().x;
        }
        if (centerY) {
            entityPos.y += (tileSize / 2) - entity.getCenterPosition().y;
        }

        entity.setPosition(entityPos);
        spawnEntity(entity);
    }

}
