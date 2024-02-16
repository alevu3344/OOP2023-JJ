package it.unibo.jetpackjoyride.core.entities.obstacle.impl;

import it.unibo.jetpackjoyride.core.entities.obstacle.api.AbstractObstacle;
import it.unibo.jetpackjoyride.core.hitbox.api.Hitbox;
import it.unibo.jetpackjoyride.core.movement.Movement;
import it.unibo.jetpackjoyride.utilities.GameInfo;

public final class Zapper extends AbstractObstacle {
    public Zapper(final Movement movement, final Hitbox hitbox) {
        super(ObstacleType.ZAPPER, movement, hitbox);
        this.entityStatus = EntityStatus.ACTIVE;
    }

    @Override
    protected void updateStatus(final boolean isSpaceBarPressed) {
        final Double outOfBoundsX = GameInfo.getInstance().getScreenWidth();
        if (this.movement.getCurrentPosition().get1() < -outOfBoundsX / 8) {
            this.entityStatus = EntityStatus.INACTIVE;
        }
    }
}