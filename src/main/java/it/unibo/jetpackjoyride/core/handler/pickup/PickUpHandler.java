package it.unibo.jetpackjoyride.core.handler.pickup;

import java.util.ArrayList;
import java.util.List;

import it.unibo.jetpackjoyride.core.entities.entity.api.Entity.EntityStatus;
import it.unibo.jetpackjoyride.core.entities.entity.api.EntityGenerator;
import it.unibo.jetpackjoyride.core.entities.entity.impl.EntityGeneratorImpl;
import it.unibo.jetpackjoyride.core.entities.pickups.api.PickUp;
import it.unibo.jetpackjoyride.core.entities.pickups.api.PickUp.PickUpType;
import it.unibo.jetpackjoyride.core.handler.generic.GenericController;
import it.unibo.jetpackjoyride.core.hitbox.api.Hitbox;
import javafx.scene.Group;
import javafx.scene.Node;


public class PickUpHandler {
    private List<GenericController<PickUp,PickUpView>> listOfControllers;
    private EntityGenerator entityGenerator;

    public PickUpHandler() {
        this.listOfControllers = new ArrayList<>();
        entityGenerator = new EntityGeneratorImpl();
        this.spawnPickUp(PickUpType.VEHICLE);
    }

    public boolean update(final Group pickUpGroup, final Hitbox playerHitbox) {
        final var iterator = listOfControllers.iterator();
        boolean pickUpPickedUp = false;
        while (iterator.hasNext()) {
            var controller = iterator.next();

            controller.update(true);

            if (collisionChecker(controller.getEntityModel().getHitbox(), playerHitbox)
                        && controller.getEntityModel().getEntityStatus().equals(EntityStatus.ACTIVE)) {
                            pickUpPickedUp = true;
                    controller.getEntityModel().setEntityStatus(EntityStatus.DEACTIVATED);
                }

            if (!pickUpGroup.getChildren().contains((Node) controller.getImageView())) {
                pickUpGroup.getChildren().add((Node) controller.getImageView());
            }

            if (controller.getEntityModel().getEntityStatus().equals(EntityStatus.INACTIVE)) {
                pickUpGroup.getChildren().remove((Node) controller.getImageView());
                iterator.remove();
            }
        }
        return pickUpPickedUp;
    }

    private boolean collisionChecker(final Hitbox hitbox, final Hitbox playerHitbox) {
        for (final var vertex : playerHitbox.getHitboxVertex()) {
            if (hitbox.isTouching(vertex) || hitbox.isTouching(playerHitbox.getHitboxPosition())) {
                return true;
            }
        }
        for (final var vertex : hitbox.getHitboxVertex()) {
            if (playerHitbox.isTouching(vertex) || playerHitbox.isTouching(hitbox.getHitboxPosition())) {
                return true;
            }
        }
        return false;
    }

    private void spawnPickUp(final PickUpType pickUpType) {
        List<GenericController<PickUp, PickUpView>> pickUp = entityGenerator.generatePickUp(pickUpType);
        listOfControllers.addAll(pickUp);
    }

}