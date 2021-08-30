package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultMultiTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wander around by moving a random position within a range of the starting position. Wait a little
 * bit between movements. Requires an entity with a PhysicsMovementComponent.
 */
public class WanderTask extends DefaultMultiTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(WanderTask.class);

  private final Vector2 wanderRange;
  private final float waitTime;
  private Vector2 startPos;
  protected MovementTask movementTask;
  protected WaitTask waitTask;
  protected PhysicsMovementComponent PhysicsMovementComponent;//getDirection;

  /**
   * @param wanderRange Distance in X and Y the entity can move from its position when start() is
   *     called.
   * @param waitTime How long in seconds to wait between wandering.
   */
  public WanderTask(Vector2 wanderRange, float waitTime) {
    this.wanderRange = wanderRange;
    this.waitTime = waitTime;

  }


  @Override
  public int getPriority() {
    return 1; // Low priority task
  }

  @Override
  public void start() {
    super.start();
    startPos = owner.getEntity().getPosition();

    waitTask = new WaitTask(waitTime);
    waitTask.create(owner);
    movementTask = new MovementTask(getRandomPosInRange());
    movementTask.create(owner);

    movementTask.start();
    currentTask = movementTask;

    this.owner.getEntity().getEvents().trigger("wanderStart");
  }

  @Override
  public void update() {
    if (currentTask.getStatus() != Status.ACTIVE) {
      if (currentTask == movementTask) {
        startWaiting();
      } else {
        startMoving();
      }

    }
    currentTask.update();
  }

  private void startWaiting() {
    logger.debug("Starting waiting");
    swapTask(waitTask);
  }

  /**public void */



  private void startMoving() {

    logger.debug("Starting moving");
    movementTask.setTarget(getRandomPosInRange());



//    this.

//    System.out.println(Strings
//            .format(getRandomPosInRange().x, getRandomPosInRange().y);


//    if (getRandomPosInRange().x >= 0 && getRandomPosInRange().y ) {
//
//    }

    /*System.out.println(getRandomPosInRange());*/

    float x;
    x=getRandomPosInRange().x;
    /*System.out.println(x);*/


    /**System.out.println("getRandomPosInRange():");
    System.out.println(getRandomPosInRange());*/
    /**
    if (getRandomPosInRange().x < 0){
      System.out.println("x < 0");

    }**/



    /**System.out.println(getRandomPosInRange().x);*/



    /**getRandomPosInRange().getx;

    String[] pos=getRandomPosInRange();*/








    /** my logic assumptions for code:
     * if the x value for random position > 0, character travelling right - change to right sprite
     * if the x value for random position < 0, character travelling left - change to left sprite
     * if the y value for random position >0, character travelling up - change to back sprite
     * if the y value for random position < 0, character travelling forward - change to front sprite
     *
     *absolute vaue x and y, negative or positive, up down, left, right
     * event
     *
     * Animation trigger - probably use code similar to this: animator.addAnimation("walk", 0.1f); - replace "walk with left"
     *-- view Wiki (Animations page)
     * (note: need to register animation into texture render - search how to do this)
     *
     *Need an index in atlas (https://gamedev.stackexchange.com/questions/47/what-is-a-texture-atlas)
     * - index is included in ghost.atlas - but not sure how it recognises where each sprite is
     *
     *
     *
     *
     *
     */
    swapTask(movementTask);


  }

  private Vector2 getRandomPosInRange() {
    Vector2 halfRange = wanderRange.cpy().scl(0.5f);
    Vector2 min = startPos.cpy().sub(halfRange);
    //System.out.println("startPos.cpy()"+startPos.cpy());
    Vector2 max = startPos.cpy().add(halfRange);
    /**System.out.println("Vector2 min:");
    System.out.println(min);*/
    return RandomUtils.random(min, max);
  }
}
