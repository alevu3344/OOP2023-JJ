package it.unibo.jetpackjoyride.core.entities.obstacle.impl;

import it.unibo.jetpackjoyride.core.entities.entity.api.Entity;
import it.unibo.jetpackjoyride.core.entities.obstacle.api.AbstractObstacle;
import it.unibo.jetpackjoyride.core.entities.obstacle.api.Obstacle;
import it.unibo.jetpackjoyride.core.hitbox.api.Hitbox;
import it.unibo.jetpackjoyride.core.movement.Movement;
import it.unibo.jetpackjoyride.utilities.GameInfo;

/**
 * The {@link Zapper} class defines one of the obstacles implemented
 * in the game. Since it extends {@link AbstractObstacle}, it inherits all
 * methods and behaviours of {@link Entity} and {@link Obstacle}.
 * Zappers are obstacles which often come in group and challenge the player
 * by blocking different positions of the map at the same time.
 *
 * @author gabriel.stira@studio.unibo.it
 */
public final class Zapper extends AbstractObstacle {
    /**
     *  Defines the X coordinate at which the zapper status will be set to INACTIVE.
     */
    private static final Double OUTOFBOUNDSSX = -100.0;
    /*
     * Defines the X coordinate the zapper is generated by default.
     */
    private static final Double SPAWNINGXCOORDINATE = 1350.0;

    /**
     * Constructor used to create an instance of the class Zapper.
     * @param newMovement The movement characteristics of the zapper obstacle.
     * @param hitbox The collision characteristics of the zapper obstacle.
     */
    public Zapper(final Movement newMovement, final Hitbox hitbox) {
        super(ObstacleType.ZAPPER, newMovement, hitbox);
        this.setEntityStatus(EntityStatus.ACTIVE);

        final Double startingXSpeed = Double.valueOf(GameInfo.MOVE_SPEED.get());
        this.setEntityMovement(new Movement.Builder()
        .addNewPosition(SPAWNINGXCOORDINATE, this.getEntityMovement().getPosition().get2())
        .addNewSpeed(-startingXSpeed, this.getEntityMovement().getSpeed().get2())
        .addNewAcceleration(this.getEntityMovement().getAcceleration())
        .addNewRotation(this.getEntityMovement().getRotation())
        .addNewMovementChangers(this.getEntityMovement().getMovementChangers())
        .build());
    }

    /**
     * Updates the status of the zapper entity based on its position.
     * @param isSpaceBarPressed Is ignored by this entity.
     */
    @Override
    public void updateStatus(final boolean isSpaceBarPressed) {
        if (this.getEntityMovement().getPosition().get1() < OUTOFBOUNDSSX) {
            this.setEntityStatus(EntityStatus.INACTIVE);
        }
    }
}
