package it.unibo.jetpackjoyride.core.handler.entity;

import it.unibo.jetpackjoyride.core.entities.barry.api.Barry;
import it.unibo.jetpackjoyride.core.entities.coin.impl.CoinGenerator;
import it.unibo.jetpackjoyride.core.entities.entity.api.Entity;
import it.unibo.jetpackjoyride.core.entities.entity.api.EntityModelGenerator;
import it.unibo.jetpackjoyride.core.entities.entity.api.Entity.EntityStatus;
import it.unibo.jetpackjoyride.core.entities.entity.impl.EntityModelGeneratorImpl;
import it.unibo.jetpackjoyride.core.entities.pickups.api.PickUp;
import it.unibo.jetpackjoyride.core.entities.pickups.impl.VehiclePickUp;
import it.unibo.jetpackjoyride.core.entities.powerup.api.PowerUp.PowerUpType;
import it.unibo.jetpackjoyride.core.handler.obstacle.ObstacleHandler;
import it.unibo.jetpackjoyride.core.handler.pickup.PickUpHandler;
import it.unibo.jetpackjoyride.core.handler.powerup.PowerUpHandler;
import it.unibo.jetpackjoyride.menu.shop.api.ShopController;
import it.unibo.jetpackjoyride.menu.shop.api.ShopController.Items;

import java.util.*;

import javafx.scene.Group;

public class EntityHandler {
    private ObstacleHandler obstacleHandler;
    private PowerUpHandler powerUpHandler;
    private PickUpHandler pickUpHandler;
    private Barry player;
    private CoinGenerator coinHandler;
    private EntityModelGenerator entityGenerator;
    private Set<Items> unlockedItems;

    private Set<Entity> listOfEntities;

    private boolean isUsingPowerUp;
    private boolean isCanvasAdded;

    public void initialize(final ShopController shopController) {
        this.obstacleHandler = new ObstacleHandler();
        this.powerUpHandler = new PowerUpHandler();
        this.pickUpHandler = new PickUpHandler();
        this.entityGenerator = new EntityModelGeneratorImpl();
        this.player = this.entityGenerator.generateBarry();
        this.coinHandler = new CoinGenerator(Optional.of(player.getHitbox()));
        this.listOfEntities = new HashSet<>();
        this.unlockedItems = shopController.getUnlocked();
        this.obstacleHandler.initialize();
        this.isUsingPowerUp = false;
    }

    public boolean update(final Group entityGroup, final boolean isSpaceBarPressed) {

        player.update(isSpaceBarPressed);
        if (!player.isAlive()) {
            coinHandler.setPlayerHitbox(Optional.empty());
            return false;
        }

        coinHandler.updatPosition();
        coinHandler.renderCoin();

        if (!isCanvasAdded) {
            coinHandler.addCoinsView(entityGroup);
            isCanvasAdded = true;
        }

        if (!this.isUsingPowerUp && !this.player.hasShield() && this.pickUpHandler.getAllPickUps().isEmpty() && !this.unlockedItems.isEmpty()) {
            this.spawnPickUp(this.unlockedItems);
        }

        final var obstacleHit = this.obstacleHandler
                .update(isUsingPowerUp ? Optional.of(this.powerUpHandler.getAllPowerUps().get(0).getHitbox())
                        : Optional.of(player.getHitbox()));

        if (obstacleHit.isPresent()) {
            if (this.isUsingPowerUp) {
                this.powerUpHandler.destroyAllPowerUps();
                this.isUsingPowerUp = false;
                this.player.setEntityStatus(EntityStatus.ACTIVE);
                this.coinHandler.setPlayerHitbox(Optional.of(this.player.getHitbox()));

            } else {
                this.player.hit(obstacleHit.get());
            }
        }

        this.powerUpHandler.update(isSpaceBarPressed);

        if (this.pickUpHandler.update(Optional.of(player.getHitbox()))) {

            final PickUp pickUpPickedUp = this.pickUpHandler.getAllPickUps().get(0);

            switch (pickUpPickedUp.getPickUpType()) {
                case VEHICLE:
                    player.setEntityStatus(EntityStatus.INACTIVE);
                    final VehiclePickUp vehiclePickUp = (VehiclePickUp) pickUpPickedUp;
                    this.spawnPowerUp(vehiclePickUp.getVehicleSpawn());
                    this.isUsingPowerUp = true;
                    this.obstacleHandler.deactivateAllObstacles();
                    this.coinHandler.setPlayerHitbox(
                            Optional.of(this.powerUpHandler.getAllPowerUps().get(0).getHitbox()));
                    break;
                case SHIELD:

                    this.player.setShieldOn();

                default:
                    break;
            }
        }
        this.listOfEntities.clear();
        this.listOfEntities.addAll(this.powerUpHandler.getAllPowerUps());
        this.listOfEntities.addAll(this.pickUpHandler.getAllPickUps());
        this.listOfEntities.addAll(this.obstacleHandler.getAllObstacles());
        if (!this.player.getEntityStatus().equals(EntityStatus.INACTIVE)) {
            this.listOfEntities.add(this.player);
        } else {
            this.listOfEntities.remove(player);
        }
        return true;
    }

    public Set<Entity> getAllEntities() {
        return this.listOfEntities;
    }

    private void spawnPickUp(final Set<Items> unlockedItems) {
        this.pickUpHandler.spawnPickUp(unlockedItems);
    }

    private void spawnPowerUp(final PowerUpType powerUpType) {
        this.powerUpHandler.spawnPowerUp(powerUpType);
    }

    public void stop() {
        this.obstacleHandler.over();
        this.coinHandler.stopGenerate();
    }

    public void start() {
        this.obstacleHandler.start();
        this.coinHandler.startGenerate();
    }

    public void reset() {
        this.obstacleHandler.deactivateAllObstacles();
        this.coinHandler.clean();
    }

}
